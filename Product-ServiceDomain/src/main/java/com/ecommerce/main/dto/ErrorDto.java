package com.ecommerce.main.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDto {
	private String message;
	private String Date;
	private String time;
	
	
	public ErrorDto(String message) {
		this.message=message;
		this.Date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		this.time=new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
}
