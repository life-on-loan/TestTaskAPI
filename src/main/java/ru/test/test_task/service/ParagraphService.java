package ru.test.test_task.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.test.test_task.dto.ParagraphDto;
import ru.test.test_task.dto.UploadDto;
import ru.test.test_task.entity.Paragraph;
import ru.test.test_task.error.NotFoundException;
import ru.test.test_task.mapper.ParagraphMapper;
import ru.test.test_task.repository.ParagraphRepository;

import static ru.test.test_task.utils.Constants.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Сервис для работы со строками
 */
@Service
@Transactional
public class ParagraphService {
    private final ParagraphRepository paragraphRepository;
    private final ParagraphMapper paragraphMapper;
    private String fileName;

    public ParagraphService(ParagraphRepository paragraphRepository, ParagraphMapper paragraphMapper) {
        this.paragraphRepository = paragraphRepository;
        this.paragraphMapper = paragraphMapper;
    }

    /**
     * Создание новых строк в базе, полученных из текстового файла
     *
     * @param multipartFile текстовый файл
     * @return сообщение об успешном выполнении операции или причина отказа
     */
    public UploadDto create(MultipartFile multipartFile) {
        UploadDto uploadDto = new UploadDto();
        fileName = multipartFile.getOriginalFilename();
        uploadDto.setFileName(fileName);
        if (!(Objects.equals(multipartFile.getContentType(), MediaType.TEXT_PLAIN_VALUE))) {
            uploadDto.setCountLines(-1);
            uploadDto.setSuccess(BAD_REQUEST_MESSAGE);
        } else {
            try {
                List<String> lines = parser(multipartFile.getBytes());
                uploadDto.setCountLines(lines.size());
                List<Paragraph> paragraphs = strToParagraph(lines);
                paragraphRepository.saveAll(paragraphs);
                uploadDto.setSuccess(SUCCESS);
            } catch (IOException e) {
                uploadDto.setCountLines(-1);
                uploadDto.setSuccess(ERROR_READ_MESSAGE);
            }
        }
        return uploadDto;
    }

    /**
     * Сбор сведений о каждой строке и создание ее сущности
     *
     * @param lines набор исходных строк
     * @return лист сущностей с доп. информацией о каждой строке
     */
    private List<Paragraph> strToParagraph(List<String> lines) {
        int[] countSharps = countSharpsInLine(lines);
        List<Paragraph> paragraphList = createEntity(lines);
        addParent(paragraphList, countSharps, lines.size());
        return paragraphList;
    }

    /**
     * Подсчет количества # в каждой строке
     *
     * @param lines исходные строки
     * @return массив с количеством # в начале каждой строки
     */
    private int[] countSharpsInLine(List<String> lines) {
        int[] countSharps = new int[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            int j = 0;
            String cur = lines.get(i);
            while (cur.charAt(j) == '#') {
                j++;
            }
            countSharps[i] = j;
        }
        return countSharps;
    }

    /**
     * Создание сущностей исходных строк
     *
     * @param lines исходные строки
     * @return лист сущностей
     */
    private List<Paragraph> createEntity(List<String> lines) {
        List<Paragraph> paragraphList = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            Paragraph paragraph = new Paragraph(
                    fileName,
                    lines.get(i),
                    (i + 1),
                    null,
                    new ArrayList<>()
            );
            paragraphList.add(paragraph);
        }
        return paragraphList;
    }

    /**
     * Добавление родителя (заголовка с более высоким рангом) для каждой строки
     *
     * @param paragraphList лист сущностей строк
     * @param countSharps   массив с количеством # в начале каждой строки
     * @param countLines    количество строк
     * @return обновленный лист сущностей с добавленными родителями
     * @throws NotFoundException если нарушена вложенность
     */
    private List<Paragraph> addParent(List<Paragraph> paragraphList, int[] countSharps, Integer countLines) throws NotFoundException {
        int currCountSharp = countSharps[0];
        for (int i = 0; i < countLines; i++) {
            if (i == 0 && currCountSharp == 0) {
                continue;
            }
            /*
                Ситуации, когда глубина вложенности задана некорректно, к примеру:
                #
                ###
                или 2 и более # в первой строке (отсчет с 1), считаются ошибочными
             */
            if (paragraphList.get(i).getValue().charAt(0) == '#'
                    && paragraphList.get(i).getValue().charAt(0) == paragraphList.get(i).getValue().charAt(1)
                    && paragraphList.get(i).getParent() == null) {
                throw new NotFoundException(MessageFormat.format("Количество '#' превышает в строке {0} как минимум на 1. Проверьте корректность ввода.", i + 1));
            }
            currCountSharp = countSharps[i];
            if (currCountSharp == 0) {
                continue;
            }
            for (int k = i + 1; k < countLines; k++) {
                if (countSharps[k] - currCountSharp == 0 || (countSharps[k] - currCountSharp <= -1 && currCountSharp > 1)) {
                    break;
                }
                if (countSharps[k] - currCountSharp == 1 || (countSharps[k] == 0 && countSharps[k] != currCountSharp)) {
                    paragraphList.get(k).setParent(paragraphList.get(i));
                }
            }
        }
        return paragraphList;
    }

    /**
     * Парсинг по символам в стиле CRLF, CR и/или LF
     *
     * @param bytes содержимое файла
     * @return строки после парсинга
     */
    private List<String> parser(byte[] bytes) {
        String text = new String(bytes, StandardCharsets.UTF_8);
        return List.of(text.split("\r\n|\r|\n"));
    }

    /**
     * Возвращает весь список строк из бд
     *
     * @return список строк с доп.данными
     */
    public List<ParagraphDto> readAll() {
        return paragraphMapper.toDtoList(paragraphRepository.findAll());
    }

    /**
     * Возвращает информацию о строке по ее id
     *
     * @param id id строки
     * @return строка и доп.данные о ней
     * @throws NotFoundException если нет строки по данному id
     */
    public ParagraphDto read(int id) throws NotFoundException {
        return paragraphMapper.toDto(paragraphRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE)));
    }
}
