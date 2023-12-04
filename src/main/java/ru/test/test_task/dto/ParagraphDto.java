package ru.test.test_task.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParagraphDto {
    private String fileName;
    private Integer id;
    private String value;
    private Integer lineNumber;
    private List<ParagraphDto> children;

    public ParagraphDto(String fileName, Integer id, String value, Integer lineNumber, List<ParagraphDto> children) {
        this.fileName = fileName;
        this.id = id;
        this.value = value;
        this.lineNumber = lineNumber;
        this.children = children;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public List<ParagraphDto> getChildren() {
        return children;
    }

    public void setChildren(List<ParagraphDto> children) {
        this.children = children;
    }
}
