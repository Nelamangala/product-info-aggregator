package com.target.product.aggregator.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class ExceptionControllerAdvice {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
	public static class ErrorResponse {
		private int errorCode;
		private String message;

		public int getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(int errorCode) {
			this.errorCode = errorCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		logger.info(ex.getMessage());
		logger.error(ex.getCause().getMessage());
		ex.printStackTrace();
		if(ex instanceof HttpMessageNotReadableException || ex instanceof InvalidFormatException 
				|| ex instanceof MethodArgumentNotValidException || ex instanceof IllegalArgumentException) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode(HttpStatus.BAD_REQUEST.value());
			String errorMessage = "Invalid input, please check input format against https://target-product-info-aggregator.cfapps.io/swagger-ui.html ";
			error.setMessage(errorMessage);
			return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
		}
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setMessage("Please contact your administrator");
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);
	}
}
