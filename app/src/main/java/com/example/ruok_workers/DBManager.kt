package com.example.ruok_workers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        //CRAETE 쿼리문 실행
        db!!.execSQL("CREATE TABLE IF NOT EXISTS member (m_num INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,m_name TEXT NOT NULL,m_id TEXT NOT NULL,m_pw TEXT NOT NULL,m_birth TEXT,m_type INTEGER NOT NULL,m_photo TEXT,wf_num INTEGER NOT NULL);")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS welfare_facilities (wf_num INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,wf_name TEXT NOT NULL,wf_addr TEXT);")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS homeless (h_num INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,h_name TEXT NOT NULL,h_birth TEXT,h_phone TEXT,h_photo TEXT, h_unusual TEXT);")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS bookmark (h_num INTEGER NOT NULL,m_num INTEGER NOT NULL,PRIMARY KEY(h_num,m_num));")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS consultation (c_num INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,m_num INTEGER NOT NULL,h_num INTEGER,c_time TEXT NOT NULL,c_health INTEGER NOT NULL,c_unusual TEXT,c_measure TEXT,c_content TEXT);")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS location (c_num INTEGER NOT NULL,l_addr TEXT NOT NULL,l_lat REAL NOT NULL,l_lon REAL NOT NULL,PRIMARY KEY(c_num));")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS photo (p_filename TEXT NOT NULL,c_num INTEGER NOT NULL,PRIMARY KEY(p_filename));")
        //db!!.execSQL("CREATE TABLE IF NOT EXISTS briefing (b_num INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,b_title TEXT NOT NULL,b_content TEXT NOT NULL,m_num INTEGER NOT NULL,b_time TEXT NOT NULL,b_type INTEGER NOT NULL,b_notice INTEGER NOT NULL);")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS counting_course (cc_num INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,cc_name TEXT NOT NULL,wf_num INTEGER NOT NULL);")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS counting_area (ca_num INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ca_name TEXT NOT NULL,cc_num INTEGER NOT NULL);")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS counting_record (cl_date TEXT NOT NULL,cl_order INTEGER NOT NULL,ca_num INTEGER NOT NULL,m_num INTEGER NOT NULL,cr_male INTEGER NOT NULL,cr_female INTEGER NOT NULL,cr_sum INTEGER NOT NULL,PRIMARY KEY(cl_date,cl_order,ca_num));")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS counting_list (cl_date TEXT NOT NULL,cl_order INTEGER NOT NULL,cc_num INTEGER NOT NULL,cl_title TEXT NOT NULL,cl_sum INTEGER NOT NULL,PRIMARY KEY(cl_date,cl_order,cc_num));")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS time (m_num INTEGER NOT NULL, t_time INTEGER, c_num	INTEGER NOT NULL, PRIMARY KEY(m_num));")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}