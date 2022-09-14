package de.brockhausag.diversitylunchspringboot.profile.utils;

import lombok.Builder;

public interface BaseEntity {
    Long getId();
    void setId(Long id);
    String getDescriptor();
    void setDescriptor(String descriptor);
}
