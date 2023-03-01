package com.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserDto {
	
	private int id;
	
	@NotEmpty
	@Size(min=4,message="Name must be minimum of 4 characters!!!")
	private String name;
	
	@Email(message="Enter valid email!!!")
	private String email;
	
	@NotEmpty
	@Size(min=3, max=10,message="Password must be of minimum 3 chars and maximum of 10 chars!!!")
	private String password;
	
	@NotEmpty
	private String about;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
}