package kr.chosun.educhatserver.security.entity;

import jakarta.persistence.*;
import kr.chosun.educhatserver.openai.entity.ChatRecord;
import kr.chosun.educhatserver.security.constant.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import lombok.Getter;

@Entity
@Getter
@Builder
@Table(name = "USER")
public class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "PROFILE", nullable = false)
	private String profile;

	@Column(name = "USER_KEY", nullable = false)
	private String userKey;

	@Enumerated(EnumType.STRING)
	@Column(name = "ROLE", nullable = false)
	private Role role;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "chat_record_id", referencedColumnName = "id")
	private ChatRecord categories;
}
