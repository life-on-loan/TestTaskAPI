package ru.test.test_task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.test.test_task.entity.Paragraph;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Integer> {
}
