package net.sunxu.demo.sb.repository;

import net.sunxu.demo.sb.bo.CollectionBO;
import net.sunxu.demo.sb.entity.Article;
import net.sunxu.demo.sb.entity.Collection;
import net.sunxu.demo.sb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findAllByCreatorIdOrderByCreateTime(Long creatorId);

}
