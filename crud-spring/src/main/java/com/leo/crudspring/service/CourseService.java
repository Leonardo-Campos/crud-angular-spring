package com.leo.crudspring.service;

import com.leo.crudspring.dto.CourseDTO;
import com.leo.crudspring.dto.mapper.CourseMapper;
import com.leo.crudspring.enums.Category;
import com.leo.crudspring.exception.RecordNotFoundException;
import com.leo.crudspring.repository.CourseRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public List<CourseDTO> list() {
        return courseRepository.findAll()
                .stream().map(courseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO findById(@NotNull @Positive Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public CourseDTO create(@Valid @NotNull CourseDTO courseDTO) {
        return courseMapper.toDTO(courseRepository.save(courseMapper.toEntity(courseDTO)));
    }

    public CourseDTO update(@NotNull @Positive Long id, @Valid @NotNull CourseDTO courseDTO) {
        return courseRepository.findById(id)
                .map(record -> {
                    record.setName(courseDTO.name());

                    Category category = courseMapper.getCategoryByValue(courseDTO.category());
                    record.setCategory(category);

//                    record.setCategory(Category.valueOf(courseDTO.category()));
                    return courseRepository.save(record);
                }).map(courseMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        courseRepository.delete(courseRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

}
