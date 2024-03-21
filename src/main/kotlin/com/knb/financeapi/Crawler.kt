package com.knb.financeapi

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.Locale
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger(Crawler::class.java)
class Crawler {

    private val baseUrl: String = "https://www.google.com/finance/quote/"

    private fun request(url: String = "", deepRequest: Boolean = false): Document? {
        return try {
            var doc = Jsoup.connect(url).get()
            if (deepRequest && doc.toString().contains("window.location.replace")) {
                val newUrl = Regex("""window.location.replace\("(.*?)"\)""").find(doc.toString())?.groupValues?.get(1) ?: ""
                doc = Jsoup.connect(newUrl).get()
            }
            doc
        } catch (e: Exception) {
            logger.error("Error request url $url. Error: $e")
            null
        }
    }

    private fun getValue(response: Document, divClass: String = "YMlKec fxKbKc"): Double? {
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

    fun getStock(stock: String = "BTC-USD"): Stock? {

        val stockName: String = stock.uppercase(Locale.getDefault())

        val url = "$baseUrl$stockName"

        val response = request(url) ?: return null

        var value: Double? = getValue(response)
        if (value == null) {
           value = getValue(response, "YMlKec")
        }

        var name: String? = getName(response)
        if (name == null) {
            name = getName(response, "ZvmM7")
        }

        val about: String? = getAbout(response)

        return Stock(name, value, about, url)
    }
}

fun main() {
    val crawler = Crawler()
    println(crawler.getStock("IRDM11"))
}


