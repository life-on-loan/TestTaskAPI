package ru.test.test_task.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.test.test_task.dto.ParagraphDto;
import ru.test.test_task.dto.UploadDto;
import ru.test.test_task.error.NotFoundException;
import ru.test.test_task.service.ParagraphService;

import java.util.List;

import static ru.test.test_task.utils.ApiParams.*;
import static ru.test.test_task.utils.Constants.SUCCESS;

@RestController
public class TextControllerImpl implements TextController{
    private final ParagraphService paragraphService;

    public TextControllerImpl(ParagraphService paragraphService) {
        this.paragraphService = paragraphService;
    }

    @Override
    public ResponseEntity<UploadDto> create(@RequestParam(KEY_FILE) MultipartFile text) {
        UploadDto uploadDto = paragraphService.create(text);
        return uploadDto.getSuccess().equals(SUCCESS)
                ? new ResponseEntity<>(uploadDto, HttpStatus.OK)
                : new ResponseEntity<>(uploadDto, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<ParagraphDto>> read() {
        final List<ParagraphDto> paragraphDtos = paragraphService.readAll();
        return paragraphDtos != null && !paragraphDtos.isEmpty()
                ? new ResponseEntity<>(paragraphDtos, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ParagraphDto> read(@PathVariable(name = ID) int id) throws NotFoundException {
        final ParagraphDto paragraphDto = paragraphService.read(id);

        return paragraphDto != null
                ? new ResponseEntity<>(paragraphDto, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
