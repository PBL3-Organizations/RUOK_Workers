package com.example.ruok_workers

data class CountingDetailItem(
    var place: String,
    var worker: String,
    var women: Int,
    var men: Int,
    var sum: Int = women + men
)