package com.smhrd.board.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smhrd.board.config.FileUploadConfig;
import com.smhrd.board.config.WebConfig;
import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.entity.UserEntity;
import com.smhrd.board.service.BoardService;

import jakarta.servlet.http.HttpSession;

// controller의 default mapping 주소 설정

@Controller
@RequestMapping("/board")
public class BoardController {

	
	@Autowired
	BoardService boardService;
	
	@Autowired
	FileUploadConfig fileUploadConfig;

	// 게시글 작성 기능
	
	@PostMapping("/write")
	public String write(@RequestParam String title, @RequestParam String content, HttpSession session, @RequestParam MultipartFile image) {
		
		// input 태그에서는 file 넘어 오는 중 (file을 받아주어야 합니다)
		
		// image 처리
		
		String imgPath = "";
		String uploadDir = fileUploadConfig.getUploadDir(); // 이미지를 저장할 경로 - C:upload/
		
		if (!image.isEmpty()) {
			
			// 1. 파일의 이름 설정
			
			String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename(); // uuid : 고유 식별자 (중복을 막을려고)
			
			// 2. 파일이 저장될 이름과 경로 설정
			
			String filePath = Paths.get(uploadDir, fileName).toString();
			
			// 3. 서버에 저장 및 경로 설정
			
			try {
				
				image.transferTo(new File(filePath));
			
			} catch (IllegalStateException e) {
				
				e.printStackTrace();
			
			} catch (IOException e) {
				
				e.printStackTrace();
			
			}
			
			// 4. DB에 저장 될 경로 문자열 설정
			
			imgPath = "/uploads/"+fileName;
			
		}
		
		BoardEntity board = new BoardEntity();
		board.setTitle(title);
		board.setContents(content);
		UserEntity user = (UserEntity)session.getAttribute("user");
		board.setWriter(user.getId());
		board.setImgPath(imgPath);
		
		boardService.write(board);
		
		return "redirect:/";
		
	}
	
	// 게시글 상세페이지 이동
	
	@GetMapping("/detail/{id}") // URL에 담긴 값 가지고 오는 방법 @PathVariable
	public String detail(@PathVariable Long id, Model model) {
		
		System.out.println(id);
		
		Optional<BoardEntity> detail = boardService.detailPage(id);
		model.addAttribute("detail", detail.get());
		
		
		return "detail";
		
	}
	
	// 게시판 수정 기능 edit.html
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		
		Optional<BoardEntity> board = boardService.detailPage(id);
		model.addAttribute("board", board.get());
		
		return "edit";
	}
	
	// 게시판 수정 기능
	
	@PostMapping("/update")
	public String update(@RequestParam String title, @RequestParam String contents, @RequestParam MultipartFile image,
						 @RequestParam String oldImgPath, @RequestParam Long id) {
		
		// DB 점근해서 해당 게시글의 정보 다시 가지고 오겠습니다!!
		
		BoardEntity entity = boardService.detailPage(id).get();
		
		// File 업로드 경로
		
		String uploadDir = fileUploadConfig.getUploadDir();
		
		// 기존 이미지 처리
		
		if (!image.isEmpty()) { // 새롭게 이미지 업로드
			
			// 기존에 있던 이미지 삭제
			
			if(oldImgPath != null && !oldImgPath.isEmpty()) {
				
				Path oldFilePath = Paths.get(uploadDir, oldImgPath.replace("/uploads/", ""));
				
				try {
					
					Files.deleteIfExists(oldFilePath);
					
				} catch (IOException e) {
					
					e.printStackTrace();
					
				}
			
			// 새로운 이미지 저장
			
			String newFileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
			Path newFilePath = Paths.get(uploadDir, newFileName);
			
			try {
				
				image.transferTo(newFilePath);
				
			} catch (IllegalStateException e) {
				
				e.printStackTrace();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
			entity.setImgPath("/uploads/" + newFileName);
					
			}
			
	
		}
		
		//BoardEntity entity = new BoardEntity();
		entity.setTitle(title);
		entity.setContents(contents);
		
		// update문 실행 service에 연결
		/*
		 * JPA에서 save 했을때 insert문이 아닌 update문이 실행되는 조건
		 * 
		 * findById() 해준 후 save 코드를 실행하면 JPA가 자동으로 update로 인식을 함
		 * 
		 * 대규모로 복잡한 update @Query("SQL문 작성")
		 * 
		 * */
		
		boardService.write(entity);
		
		return "redirect:/board/detail/"+id;
		
	}
	
}
