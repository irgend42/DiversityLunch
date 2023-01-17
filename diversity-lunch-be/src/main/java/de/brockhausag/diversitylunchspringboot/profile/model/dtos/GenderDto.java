package de.brockhausag.diversitylunchspringboot.profile.model.dtos;

import de.brockhausag.diversitylunchspringboot.generics.defaultDimension.DefaultDimensionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenderDto implements DefaultDimensionDto {

    private Long id;

    @Schema(description = "the gender you identify with", example = "female")
    @Size(min = 2, max = 50, message = "gender descriptor must be between 2 and 50 chars long")
    @NotNull
    @NotBlank
    private String descriptor;

    @NotNull
    @Schema(description = "Default value for the given dimension")
    private boolean isDefault;
}
