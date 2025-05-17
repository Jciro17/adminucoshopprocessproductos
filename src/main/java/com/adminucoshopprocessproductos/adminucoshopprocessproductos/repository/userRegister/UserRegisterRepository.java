package com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.userRegister;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRegisterRepository extends JpaRepository<UserDomain, UUID> {

    boolean existsByEmail(String email);
}
