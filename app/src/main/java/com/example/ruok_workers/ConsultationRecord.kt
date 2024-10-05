package com.example.ruok_workers

data class ConsultationRecord(
    val hNum: Int,
    val hName: String,
    val hBirth: String,
    val hPhoto: String,
    val mName: String,
    val cTime: String,
    val cUnusual: String,
    val cMeasure: String,
    val cContent: String
)

