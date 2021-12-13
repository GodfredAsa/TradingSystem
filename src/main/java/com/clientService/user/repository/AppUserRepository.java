package com.clientService.user.repository;

import com.clientService.user.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser getAppUserByEmail(String email);

    @Query("SELECT count(*) FROM AppUser u WHERE u.email = :email")
    Integer getCountOfEmail(@Param("email") String email);
}
