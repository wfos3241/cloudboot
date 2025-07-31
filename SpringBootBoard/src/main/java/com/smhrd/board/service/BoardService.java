package com.smhrd.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.repository.BoardRepository;

@Service
public class BoardService {
	
	@Autowired
	BoardRepository boardRepository;
	
	// 게시글 작성 기능
	
	public BoardEntity write(BoardEntity entity) {
		
		return boardRepository.save(entity);
		
	}
	
	public ArrayList<BoardEntity> readAll(Model model) {
		
		// 게시글이 최신순으로 출력하게 만들고 싶습니다 -> ORDER BY write_day DESC
		
		return (ArrayList<BoardEntity>)boardRepository.findAllByOrderByWriteDayDesc();
		
	}
	
	// 게시글 상세보기
	
	public Optional<BoardEntity> detailPage(Long id) {
		
		// 내장 되어있는 테소드 사용 할때는 Repository 코드 수정 X
		
		// SELECT * FROM TABLE WHERE ID = ?
		
		// Optional 객체
		// findById의 return 타입이 Optional
		// null 체크하는 객체 -> BoardEntity가 null일 수도 있다.
		// 장점 : npe 에러(null pointer exception)방지
		
		return boardRepository.findById(id);
	
	}
	
	public void deleteBoard(Long id) {
		
		boardRepository.deleteById(id);
		
	}
	
	// 검색 기능
	
	public List<BoardEntity> searchResult(String type, String keyword) {
		
		List<BoardEntity> list = null;
		
		// 조건 처리
		
		switch(type) {
			
		case "title" :
			
			list = boardRepository.findByTitleContaining(keyword);
			break;
		
		case "contents" :
			
			list = boardRepository.searchContent(keyword);
			break;
			
		case "writer" :
			
			break;
			
		default :
			
			break;
		
		}
		
		return list;
		
	}
	
}
