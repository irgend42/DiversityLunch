package de.brockhausag.diversitylunchspringboot.profile.controller;

import de.brockhausag.diversitylunchspringboot.dimensions.entities.model.BasicDimension;
import de.brockhausag.diversitylunchspringboot.dimensions.entities.model.BasicDimensionSelectableOption;
import de.brockhausag.diversitylunchspringboot.dimensions.services.model.BasicDimensionService;
import de.brockhausag.diversitylunchspringboot.profile.model.dtos.SocialBackgroundDiscriminationDto;
import de.brockhausag.diversitylunchspringboot.profile.generics.DimensionModelController;
import de.brockhausag.diversitylunchspringboot.profile.mapper.SocialBackgroundDiscriminationMapper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/social-background-discrimination")
@RestController
public class SocialBackgroundDiscriminationController extends DimensionModelController<
        SocialBackgroundDiscriminationDto,
        BasicDimensionSelectableOption,
        BasicDimension,
        BasicDimensionService,
        SocialBackgroundDiscriminationMapper> {
    public SocialBackgroundDiscriminationController(SocialBackgroundDiscriminationMapper mapper, BasicDimensionService service) {
        super(mapper, service, "Diskriminierung aufgrund sozialer Herkunft");
    }
}