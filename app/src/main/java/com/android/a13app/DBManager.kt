package com.android.a13app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.execSQL("CREATE TABLE IF NOT EXISTS tb_account ( id TEXT, pw TEXT, name TEXT, PRIMARY KEY(id) )")
        p0!!.execSQL("CREATE TABLE  IF NOT EXISTS tb_expense ( num INTEGER, token TEXT, payer TEXT, expense INTEGER, location TEXT, date TEXT, PRIMARY KEY(num) )")
        p0!!.execSQL("CREATE TABLE IF NOT EXISTS tb_group ( token TEXT, name TEXT, PRIMARY KEY(token) )")
        p0!!.execSQL("CREATE TABLE IF NOT EXISTS tb_member ( num INTEGER, id TEXT, token TEXT, PRIMARY KEY(num) )")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
}