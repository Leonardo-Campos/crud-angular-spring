package com.leo.crudspring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.crudspring.enums.Category;
import com.leo.crudspring.enums.validation.ValueOfEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CourseDTO(
        @JsonProperty("_id") Long id,
        @NotNull @Length(min = 5, max = 100) String name,
        @NotNull @Length(max = 10) @Enumerated(EnumType.STRING) String category,
        @NotNull @NotEmpty @Valid List<LessonDTO> lessons) {
}

