package com.knb.financeapi

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

interface Response {
    val success: Boolean
}

data class ResponseSuccess(
    override val success: Boolean = true,
    val data: Any? = null
): Response

data class ResponseError(
    override val success: Boolean = false,
    val message: String? = "GENERIC ERROR."
): Response

@RestController
class Controller {

    @RequestMapping("/")
    fun home(): Response? {
        return ResponseSuccess(data = "Welcome!")
    }
}