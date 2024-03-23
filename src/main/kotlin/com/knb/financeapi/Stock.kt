package com.knb.financeapi

data class Stock (
    var name: String?,
    var price: Double?,
    var currency: String?,
    var exchange: String?,
    var about: String?,
    var link: String
)