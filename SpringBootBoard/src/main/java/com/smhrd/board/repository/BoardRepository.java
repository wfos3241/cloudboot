package com.smhrd.board.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smhrd.board.entity.BoardEntity;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
	
	// 현재 : 게시글이 오래된 순으로 출력되고 있음 -> 변경 : 게시글이 최신 순으로 출력되게 하고 싶음
	// => SELECT * FROM board_entity ORDER BY write_day DESC
	
	ArrayList<BoardEntity> findAllByOrderByWriteDayDesc();
	
	// LIKE문 사용해서 title을 가지고 오는 메소드
	// Containing --> LIKE문 (매개변수 안의 값을 바탕으로)
	
	List<BoardEntity> findByTitleContaining(String keyword);
	
	// 내가 원하는 쿼리문 바로 실행 -> @Query
	// LIKE문 사용해서 contents 가지고 오는 메소드
	// :변수 --> 매개변수로 가지고 온 값을 사용
	// @Query 메소드 명은 자유롭게 지어도 무관
	
	@Query("SELECT b FROM BoardEntity b WHERE b.contents LIKE %:keyword%")
	List<BoardEntity> searchContent(@Param("keyword") String keyword);
	
}
