package com.leo.crudspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leo.crudspring.enums.Category;
import com.leo.crudspring.enums.converters.CategoryConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SQLDelete(sql = "UPDATE Course SET status = 'Inativo' WHERE id = ?")
@Where(clause = "status = 'Ativo'")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("_id")
    private Long id;

    @NotBlank
    @NotNull
    @Length(min = 4, max = 100)
    @Column(length = 100, nullable = false)
    private String name;

    //    @NotBlank
    @NotNull
//    @Length(max = 10)
//    @Pattern(regexp = "Back-End|Front-End")
    @Column(length = 200, nullable = false)
    @Enumerated(EnumType.STRING)
    @Convert(converter = CategoryConverter.class)
    private Category category;

    /**
     * TO-DO: ENUMS for category and Status
     * Classes 43 and 44 have bugs
     */

    @NotNull
    @Length(max = 10)
    @Pattern(regexp = "Ativo|Inativo")
    @Column(length = 10, nullable = false)
    private String status = "Ativo";

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

}
