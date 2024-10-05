package com.example.ruok_workers

data class CountingRecord(
    val date: String,
    val male: Int,
    val female: Int,
    val sum: Int,
    val areaName: String // 구역 이름 추가
)
