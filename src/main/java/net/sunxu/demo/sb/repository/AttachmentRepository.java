package net.sunxu.demo.sb.repository;

import net.sunxu.demo.sb.entity.Attachment;
import net.sunxu.demo.sb.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    void deleteByArticleId(Long articleId);

    List<Attachment> findAllByArticleId(Long articleId);
}
