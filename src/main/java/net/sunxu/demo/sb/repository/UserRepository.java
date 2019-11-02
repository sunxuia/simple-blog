package net.sunxu.demo.sb.repository;

import net.sunxu.demo.sb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNameIgnoreCase(String name);

}
