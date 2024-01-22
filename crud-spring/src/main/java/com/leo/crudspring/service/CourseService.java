package com.leo.crudspring.service;

import com.leo.crudspring.dto.CourseDTO;
import com.leo.crudspring.dto.CoursePageDTO;
import com.leo.crudspring.dto.mapper.CourseMapper;
import com.leo.crudspring.enums.Category;
import com.leo.crudspring.exception.RecordNotFoundException;
import com.leo.crudspring.model.Course;
import com.leo.crudspring.repository.CourseRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public CoursePageDTO list(@PositiveOrZero int page, @Positive @Max(100) int pageSize) {
        Page<Course> pageCourse = courseRepository.findAll(PageRequest.of(page, pageSize));
        List<CourseDTO> courses = pageCourse.get().map(courseMapper::toDTO).collect(Collectors.toList());
        return new CoursePageDTO(courses, pageCourse.getTotalElements(), pageCourse.getTotalPages());
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
                    Course course = courseMapper.toEntity(courseDTO);
                    record.setName(courseDTO.name());

                    Category category = courseMapper.getCategoryByValue(courseDTO.category());
                    record.setCategory(category);

                    record.getLessons().clear();
                    course.getLessons().forEach(lesson -> record.getLessons().add(lesson));
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
