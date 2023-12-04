package ru.test.test_task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.test.test_task.dto.ParagraphDto;
import ru.test.test_task.entity.Paragraph;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParagraphMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fileName", target = "fileName")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "lineNumber", target = "lineNumber")
    @Mapping(source = "children", target = "children")
    ParagraphDto toDto(Paragraph paragraph);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fileName", target = "fileName")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "lineNumber", target = "lineNumber")
    @Mapping(source = "children", target = "children")
    Paragraph toEntity (ParagraphDto paragraph);

    List<Paragraph> toEntityList(List<ParagraphDto> paragraphDtos);
    List<ParagraphDto> toDtoList(List<Paragraph> paragraphs);
}
