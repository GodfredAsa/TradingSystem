package com.example.demo.repositories;
import com.example.demo.entities.AuthenticationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationLogRepository extends JpaRepository<AuthenticationLog,Long> {

   public AuthenticationLog findByUserID(Long userID);
   public AuthenticationLog findUserById(Long id);

}
