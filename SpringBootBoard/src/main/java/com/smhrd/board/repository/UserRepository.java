package com.smhrd.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.board.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

	// exists() --> 데이터의 존재 여부 판단 -> true/false
	// 커스텀 응용 existsBy컬럼명
	
	boolean existsById(String id);
	
	UserEntity findByIdAndPassword(String id, String password);
	
	
}
