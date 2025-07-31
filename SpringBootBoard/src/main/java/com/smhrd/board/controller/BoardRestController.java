package com.smhrd.board.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.board.config.FileUploadConfig;
import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.service.BoardService;

@RestController
public class BoardRestController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	FileUploadConfig fileuploadConfig;
	
	@DeleteMapping("/delete/{id}")
	public void deleteBoard(@PathVariable Long id) {
		
		// 이미지 삭제
		// ID를 바탕으로 데이터 SELECT
		
		// Optional<BoardEntity> board = boardService.detailPage(id);
		// BoardEntity board = boardService.detailPage(id).get();
		
		String imgPath = boardService.detailPage(id).get().getImgPath();
		String uploadDir = fileuploadConfig.getUploadDir();
		
		if (imgPath != null && !imgPath.isEmpty()) {
			
			// 경로 접근 get(경로, 파일 이름)
			// imgPath = uploads/파일명
			// 실제 파일 명은 uploads를 제외한 나머지 문자들
			
			Path filePath = Paths.get(uploadDir, imgPath.replace("/uploads/", ""));
			
			try {
				
				Files.deleteIfExists(filePath);
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		boardService.deleteBoard(id);
		
	}
	
	// 검색 기능
	
	@GetMapping("/search")
	public List<BoardEntity> search(@RequestParam String type, @RequestParam String keyword) {
		
		return boardService.searchResult(type, keyword);
		
	}
	
	
	
}
