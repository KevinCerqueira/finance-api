package com.knb.financeapi

import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stock")
class StockController {

    @GetMapping("/{stockName}")
    fun getStock(
        model: ModelMap,
        @PathVariable(required = false) stockName: String
    ): Response? {
        val crawler = Crawler()

        return try {
            val stock = crawler.getStock(stockName)
            val success: Boolean = stock?.price != null && !stock.name.isNullOrEmpty()

            if (success) {
                ResponseSuccess(data = stock)
            } else {
                ResponseError(message = "Stock not found at ${stock?.link}.")
            }
        } catch (e: Exception) {
            ResponseError(message = e.message)
        }
    }

    @GetMapping
    fun getDefaultStock(
        model: ModelMap,
    ): Response? {
        val crawler = Crawler()
        val stock = crawler.getStock()
        val success: Boolean = stock?.name != null && !stock.name.isNullOrEmpty()

        return if (success) {
            ResponseSuccess(data = stock)
        } else {
            ResponseError(message = "STOCK NOT FOUND AT ${stock?.link}.")
        }
    }
}