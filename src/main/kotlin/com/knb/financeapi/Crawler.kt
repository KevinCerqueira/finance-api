package com.knb.financeapi

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.Locale
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger(Crawler::class.java)
class Crawler {

    private val baseUrl: String = "https://www.google.com/finance/quote/"

    fun request(url: String = ""): Document? {
        return try {
            Jsoup.connect(url).get()
        } catch (e: Exception) {
            logger.error("Error request url $url. Error: $e")
            null
        }
    }

    fun getValue(response: Document, divClass: String = "YMlKec fxKbKc"): Double? {
        val divValue = response.selectFirst("div[class=$divClass]")
        var value: Double? = null
        if (divValue != null) {
            value = divValue.text().replace(",", "").let {
                if (it.contains("R$"))
                    it.replace("R$", "")
                else if (it.contains("$"))
                    it.replace("$", "")
                else it
            }.toDoubleOrNull()
        }
        return value
    }

    private fun getAbout(response: Document, divClass: String = "bLLb2d"): String? {
        val divAbout = response.selectFirst("div[class=$divClass]")
            ?: response.selectFirst("span[class=w4txWc oJeWuf]")
        return divAbout?.text()
    }

    private fun getName(response: Document, divClass: String = "zzDege"): String? {
        val divName = response.selectFirst("div[class=$divClass]")
        return divName?.text()
    }

    private fun getCurrency(response: Document, divClass: String = "NdbN0c"): String? {
        val divCurrence = response.selectFirst("div[jscontroller=$divClass]")
        var currency = divCurrence?.attr("data-currency-code")
        if (currency.isNullOrEmpty()) {
            currency = divCurrence?.attr("data-target")
        }
        return currency
    }

    private fun getExchange(response: Document, divClass: String = "PdOqHc"): String? {
        val divCurrence = response.selectFirst("div[class=$divClass]")
        return if (divCurrence != null) {
            divCurrence.text().split("•")[1].trim()
        } else {
            null
        }
    }

    private fun getRightStockName(response: Document): String? {
        val aLink = response.select("li a[href]").firstOrNull { it.attr("href").startsWith("./quote/") }?.attr("href")
        return aLink?.replace("./quote/", "")
    }

    fun getStock(stock: String = "BTC-USD"): Stock? {

        val stockName: String = stock.uppercase(Locale.getDefault())

        var url = "$baseUrl$stockName"

        var response: Document = request(url) ?: return null

        var value: Double? = getValue(response)
        if (value == null) {
            value = getValue(response, "YMlKec")

            val rightStockName: String? = getRightStockName(response)
            url = "$baseUrl$rightStockName"
            val newResponse = request(url)
            response = newResponse ?: response
        }

        var name: String? = getName(response)
        if (name == null) {
            name = getName(response, "ZvmM7")
        }

        var about: String? = getAbout(response)
        if (about == null) {
            about = getAbout(response, "w4txWc oJeWuf")
        }

        val exchange: String? = getExchange(response)
        val currency: String? = getCurrency(response)

        return Stock(
            name = name,
            price = value,
            currency = currency,
            exchange = exchange,
            about = about,
            link = url
        )
    }
}

fun main() {
    val crawler = Crawler()
    println(crawler.getStock("meta")?.currency)
    println(crawler.getStock("btc-usd")?.currency)

}


