package com.leo.crudspring.dto.mapper;

import com.leo.crudspring.dto.CourseDTO;
import com.leo.crudspring.dto.LessonDTO;
import com.leo.crudspring.enums.Category;
import com.leo.crudspring.model.Course;
import com.leo.crudspring.model.Lesson;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseDTO  toDTO(Course course) {
        if (course == null) {
            return null;
        }
        List<LessonDTO> lessons = course.getLessons()
                .stream()
                .map(lesson -> new LessonDTO(lesson.getId(), lesson.getName(), lesson.getYoutubeUrl()))
                .collect(Collectors.toList());

        return new CourseDTO(course.getId(), course.getName(), course.getCategory().getValue(),
                lessons);
    }

    public Course toEntity(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }

        Course course = new Course();
        if (courseDTO.id() != null) {
            course.setId(courseDTO.id());
        }
        course.setName(courseDTO.name());

        Category category = getCategoryByValue(courseDTO.category());
        course.setCategory(category);

        List<Lesson> lessons = courseDTO.lessons().stream().map(lessonDTO -> {
            var lesson = new Lesson();
            lesson.setId(lessonDTO.id());
            lesson.setName(lessonDTO.name());
            lesson.setYoutubeUrl(lessonDTO.youtubeUrl());
            lesson.setCourse(course);
            return lesson;
        }).collect(Collectors.toList());

        course.setLessons(lessons);

        return course;
    }

    public Category getCategoryByValue(String categoryValue) {
        for (Category category : Category.values()) {
            if (category.getValue().equalsIgnoreCase(categoryValue)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid category value: " + categoryValue);
    }
}