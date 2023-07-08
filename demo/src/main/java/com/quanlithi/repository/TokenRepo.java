package com.quanlithi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quanlithi.entity.Token;

public interface TokenRepo extends JpaRepository<Token, Long> {
	@Query(value = """
	      select t from Token t inner join UserEntity u\s
	      on t.user.id = u.id\s
	      where u.id = :id and (t.expired = false or t.revoked = false)\s
	      """)
	List<Token> findAllValidTokenByUser(long id);

	Optional<Token> findByToken(String token);
}
