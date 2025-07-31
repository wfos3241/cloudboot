package com.smhrd.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user") // 이미 만들어 놓은 DB 사용 할 수없나요?, DB이름을 다르게 지정 하고 싶어요
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx; // 객체타입으로 삽입
	
	@Column(nullable = false, unique = true)
	private String id;
	
	
	private String password;
	private String name;
	private Integer age;
	
}
