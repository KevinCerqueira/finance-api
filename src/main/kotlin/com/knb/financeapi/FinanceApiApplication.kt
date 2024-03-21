package com.knb.financeapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.NoHandlerFoundException

@SpringBootApplication
class FinanceApiApplication

fun main(args: Array<String>) {
	runApplication<FinanceApiApplication>(*args)
}

@ControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(NoHandlerFoundException::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun handleNoHandlerFoundException(e: NoHandlerFoundException): ResponseEntity<String> {
		return ResponseEntity("Page Not Found: ${e.requestURL}", HttpStatus.NOT_FOUND)
	}
}