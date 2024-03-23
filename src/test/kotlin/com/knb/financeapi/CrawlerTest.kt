package com.knb.financeapi

import org.jsoup.nodes.Document
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class CrawlerTest {

    @Test
    fun testGetStockSuccess() {
        val html = "<html><body>" +
                "<div class='zzDege'>Test Stock</div>" +
                "<div class='YMlKec fxKbKc'>R$1,234.56</div>" +
                "<div class='bLLb2d'>Informações sobre a ação</div>" +
                "</body></html>"
        val doc: Document = Jsoup.parse(html)

        val crawler = Mockito.spy(Crawler::class.java)
        Mockito.doReturn(doc).`when`(crawler).request(Mockito.anyString())

        val stock = crawler.getStock("TEST")

        assertNotNull(stock)
        assertEquals("Test Stock", stock?.name)
        assertEquals(1234.56, stock?.price)
        assertEquals("Informações sobre a ação", stock?.about)
    }

    @Test
    fun testExceptionHandling() {
        val crawler = Mockito.spy(Crawler::class.java)
        Mockito.doReturn(null).`when`(crawler).request(Mockito.anyString())

        val stock = crawler.getStock("INVALID")
        assertNull(stock)
    }

    @Test
    fun testMissingValues() {
        val html = "<html><body></body></html>"
        val doc: Document = Jsoup.parse(html)

        val crawler = Mockito.spy(Crawler::class.java)
        Mockito.doReturn(doc).`when`(crawler).request(Mockito.anyString())

        org.junit.jupiter.api.assertThrows<Exception>("Invalid stock name MISSING") {
            crawler.getStock("MISSING")
        }
    }

    @Test
    fun testValueFormatting() {
        val html = "<html><body><div class='YMlKec fxKbKc'>R$1,234.56</div></body></html>"
        val doc: Document = Jsoup.parse(html)

        val crawler = Mockito.spy(Crawler::class.java)
        Mockito.doReturn(doc).`when`(crawler).request(Mockito.anyString())

        val value = crawler.getValue(doc)
        assertEquals(1234.56, value)
    }
}
