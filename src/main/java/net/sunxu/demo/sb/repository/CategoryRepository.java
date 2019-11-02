package net.sunxu.demo.sb.repository;

import net.sunxu.demo.sb.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByCreatorId(Long userId);
}
