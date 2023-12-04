package ru.test.test_task.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.test.test_task.dto.ParagraphDto;
import ru.test.test_task.dto.UploadDto;
import ru.test.test_task.error.NotFoundException;

import java.util.List;

import static ru.test.test_task.utils.ApiParams.*;

@RequestMapping(REQUEST_PATH)
public interface TextController {
    /**
     * Разбиение текста по строкам с указанием вложенности
     *
     * @param text - исходный текст
     * @return разбиение по строкам с вложенностью
     */
    @PostMapping
    ResponseEntity<UploadDto> create(@RequestParam("file") MultipartFile text);

    /**
     * Запрос на итоговую сводку
     *
     * @return разбиение по строкам с вложенностью
     */
    @GetMapping(GET_INFO)
    ResponseEntity<List<ParagraphDto>> read();

    /**
     * Чтение строки по id и получение доп.сведений
     *
     * @param id - id строки
     * @return строка и доп.сведения(название файла, номер строки в файле, вложенные объекты)
     */
    @GetMapping(GET_BY_ID)
    ResponseEntity<ParagraphDto> read(@PathVariable(name = ID) int id) throws NotFoundException;
}
