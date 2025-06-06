package com.shrona.line_demo.admin.infrastructure;

import com.shrona.line_demo.admin.domain.AdminUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminJpaRepository extends JpaRepository<AdminUser, Long> {


    /**
     * 로그인 아이디를 기준으로 유저 조회
     */
    Optional<AdminUser> findByLoginId(String loginId);
}
