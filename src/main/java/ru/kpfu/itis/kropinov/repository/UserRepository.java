package ru.kpfu.itis.kropinov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kropinov.model.User;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select u from User u where u.name = :name")
    Optional<User> findByName(String name);

    Optional<User> getUserByName(String name);

    @Query(value = "select * from users u where u.username = ?1", nativeQuery = true)
    Optional<User> getByName(String name);

    // findById есть

    // deleteById есть

    // save есть
}
