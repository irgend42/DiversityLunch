package de.brockhausag.diversitylunchspringboot;

import de.brockhausag.diversitylunchspringboot.email.service.DiversityLunchEMailService;
import de.brockhausag.diversitylunchspringboot.properties.DiversityLunchMailProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor
public class ExperimentalIT {

    private DiversityLunchEMailService diversityLunchEMailService;

    @Autowired
    private JavaMailSender mailSender;

    @Mock
    private DiversityLunchMailProperties diversityLunchMailProperties;


    @BeforeEach
    void setUp() {
        this.diversityLunchEMailService = new DiversityLunchEMailService(this.mailSender, this.diversityLunchMailProperties);
    }

    @SneakyThrows
    @Test
    public void shouldSendMailToMailhog() {
        String emailAddress = "test@test.de";
        String subject = "subject";
        String bodyHtml = "subject";
        String bodyPlain = "subject";

        when(this.diversityLunchMailProperties.getSender()).thenReturn("test@test.de");

        this.diversityLunchEMailService.sendEmail(emailAddress, subject, bodyHtml, bodyPlain);
    }
}
