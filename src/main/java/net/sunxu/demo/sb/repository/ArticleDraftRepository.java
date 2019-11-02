package net.sunxu.demo.sb.repository;

import net.sunxu.demo.sb.entity.ArticleDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDraftRepository extends JpaRepository<ArticleDraft, Long> {

}
