package com.example.ruok_workers

data class CountingDetailItem(
    val place: String,
    val worker: String,
    val women: Int,
    val men: Int,
    val sum: Int
)