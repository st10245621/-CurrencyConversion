package com.example.currencyconversion

data class CurrencyResponse(
    val base_currency: String,
    val base_currency_code: String,
    val rates: Map<String, Rate>
)

data class Rate(
    val rate: String,
    val rate_for_amount: Double
)
