package de.brockhausag.diversitylunchspringboot.integrationstests;

import de.brockhausag.diversitylunchspringboot.account.service.AccountService;
import de.brockhausag.diversitylunchspringboot.config.SecurityConfig;
import de.brockhausag.diversitylunchspringboot.data.ProfileTestdataFactory;
import de.brockhausag.diversitylunchspringboot.profile.model.ProfileEntity;
import de.brockhausag.diversitylunchspringboot.profile.service.ProfileService;
import de.brockhausag.diversitylunchspringboot.properties.DiversityLunchMailProperties;
import de.brockhausag.diversitylunchspringboot.security.DiversityLunchSecurityExpressionRoot;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("Test")
@Import(SecurityConfig.class)
public class EMailControllerIT {



    @MockBean
    private DiversityLunchMailProperties diversityLunchMailProperties;
    @MockBean
    private ProfileService profileService;
    @MockBean
    private DiversityLunchSecurityExpressionRoot diversityLunchSecurityExpressionRoot;
    @MockBean
    private AccountService accountService;


    private ProfileTestdataFactory profileTestdataFactory = new ProfileTestdataFactory();
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext appContext;

    @SneakyThrows
    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(appContext).apply(springSecurity()).build();



        // Nach jedem Test soll die Test Mail geloescht werden
        HttpUriRequest request = new HttpDelete( "http://localhost:8025/api/v1/messages");
        HttpClientBuilder.create().build().execute( request );
    }


    @SneakyThrows
    @Test
    public void testSendTestMail_expectedToNotThrowException() {
        when(this.diversityLunchMailProperties.getSender()).thenReturn("diversitylunchtest@brockhaus-ag.de");

        mockMvc.perform(
                post("/mailing/sendTestMail")
        ).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void testSendTestMail_expectedCorrectTestContentInMail() {
        //Send email
        when(this.diversityLunchMailProperties.getSender()).thenReturn("diversitylunchtest@brockhaus-ag.de");

        mockMvc.perform(
                post("/mailing/sendTestMail")
        );

        // Prepare request to Mailhog
        HttpUriRequest request = new HttpGet( "http://localhost:8025/api/v2/messages");

        //Send request to Mailhog
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        //Get values from response
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        JSONObject jsonResponse = new JSONObject(responseBody);
        String to = jsonResponse.getJSONArray("items").getJSONObject(0).getJSONObject("Content")
                .getJSONObject("Headers").getJSONArray("To").getString(0);
        String from = jsonResponse.getJSONArray("items").getJSONObject(0).getJSONObject("Content")
                .getJSONObject("Headers").getJSONArray("From").getString(0);
        String subject = jsonResponse.getJSONArray("items").getJSONObject(0).getJSONObject("Content")
                .getJSONObject("Headers").getJSONArray("Subject").getString(0);
        String body = jsonResponse.getJSONArray("items").getJSONObject(0).getJSONObject("MIME")
                .getJSONArray("Parts").getJSONObject(0).getJSONObject("MIME")
                .getJSONArray("Parts").getJSONObject(0).getJSONObject("MIME")
                .getJSONArray("Parts").getJSONObject(0).getString("Body");
        int amount = jsonResponse.getInt("total");

        //Assert
        Assertions.assertEquals("test@test.de",to);
        Assertions.assertEquals("diversitylunchtest@brockhaus-ag.de", from);
        Assertions.assertEquals("Testsubject",subject);
        Assertions.assertEquals("Hallo :)", body);
        Assertions.assertEquals(1, amount);
    }

    @SneakyThrows
    @Test
    public void testSendTestMailToUser_expectedToNotThrowException() {
        ProfileEntity tmpProfileEntity = profileTestdataFactory.createEntity();
        var accountEntity1 = accountService.getOrCreateAccount(tmpProfileEntity.getEmail());
        long id = 42;

        mockMvc.perform(
                post("/mailing/sendTestMailToUser?id=" + id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + profileTestdataFactory.getTokenStringFromId(accountEntity1.getUniqueName()))
        ).andExpect(status().isOk());
    }
}
