# Finance API: Information on Global Stocks!

Welcome to the Finance API, a service providing detailed information on stocks from markets worldwide. This API allows users to query specific stock data or obtain standardized details.

## Features

- Query data for any stock using various identifiers. For example, "btc", "BTC-USD", "BTC-BRL" for Bitcoin, or "facebook", "META", "META:NASDAQ" for Meta/Facebook stock. Identifiers are case-insensitive, meaning "btc", "Btc", and "BTC" will all return the same results.
- Access detailed information including price, currency, exchange, and a summary of the stock's background.

## Getting Started

To query information on a specific stock, use the API endpoint as follows:
```bash
curl http://localhost:8080/api/v1/stock/<stockName>
```
Replace <stockName> with the stock's identifier. For example, for Bitcoin, you can use btc, btc-usd, or btc-brl if you're interested in its value in Brazilian Real.

## Examples
1. Requesting Bitcoin Information
Request:
```bash
curl http://localhost:8080/api/v1/stock/btc
```
Response:
```json
{
    "success": true,
    "data": {
        "name": "Bitcoin to United States Dollar",
        "price": 39475.9,
        "currency": "USD",
        "exchange": "Cryptocurrency",
        "about": "Bitcoin is the first decentralized cryptocurrency. Nodes in the peer-to-peer bitcoin network verify transactions through cryptography and record them in a public distributed ledger, called a blockchain, without central oversight.",
        "link": "https://www.google.com/finance/quote/BTC-USD"
    }
}
```

2. Requesting Fundo Investimento Imobiliario Iridium Recebiveis Imobiliarios Information

Request:
```bash
curl http://localhost:8080/api/v1/stock/irdm11
```
Response:
```json
{
    "success": true,
    "data": {
        "name": "FI Imobiliario Iridium Recebiveis Imobiliarios",
        "price": 76.74,
        "currency": "BRL",
        "exchange": "BVMF",
        "about": "Founded in 2017, Iridium Recebiveis Imobiliarios focuses on real estate investments.",
        "link": "https://www.google.com/finance/quote/IRDM11:BVMF"
    }
}
```