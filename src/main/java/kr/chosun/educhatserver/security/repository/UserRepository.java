package kr.chosun.educhatserver.security.repository;

import kr.chosun.educhatserver.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByEmail(String email);
	boolean existsByEmail(String email);
}
