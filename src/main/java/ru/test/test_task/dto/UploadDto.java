package ru.test.test_task.dto;

import lombok.Data;

@Data
public class UploadDto {
    private String fileName;
    private String success;
    private Integer countLines;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Integer getCountLines() {
        return countLines;
    }

    public void setCountLines(Integer countLines) {
        this.countLines = countLines;
    }
}
