package ru.test.test_task.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "paragraphs")
public class Paragraph {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paragraphs_seq")
    @SequenceGenerator(name = "paragraphs_seq", sequenceName = "seq_paragraphs", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "value")
    private String value;

    @Column(name = "line_number")
    private Integer lineNumber;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Paragraph parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Paragraph> children = new ArrayList<>();

    public Paragraph(String fileName, String value, Integer lineNumber, Paragraph parent, List<Paragraph> children) {
        this.fileName = fileName;
        this.value = value;
        this.lineNumber = lineNumber;
        this.parent = parent;
        this.children = children;
    }

    public Paragraph() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public Paragraph getParent() {
        return parent;
    }

    public void setParent(Paragraph parent) {
        this.parent = parent;
    }

    public List<Paragraph> getChildren() {
        return children;
    }

    public void setChildren(List<Paragraph> children) {
        this.children = children;
    }
}
