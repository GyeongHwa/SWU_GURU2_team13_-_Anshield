package com.android.a13app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    private val TAG = "DBManager"
    companion object {
        //불러올 db 파일 이름(asset 폴더에 넣어놓음)
        /*
        * friendshipDBen.db는 데이터셋이 영어
        * friendshipDBkr.db는 데이터셋이 한국어
        */
        const val DB_NAME = "friendshipDBkr.db"
        private var DB_PATH = ""
    }

    //객체 생성 시 DB_PATH 지정 및 db 생성 확인
    init {
        DB_PATH = "/data/data/" + context!!.packageName + "/databases/"
        dataBaseCheck(context)
    }

    private fun dataBaseCheck(context: Context) {
        val dbFile = File(DB_PATH + DB_NAME)
        if (!dbFile.exists()) {
            dbCopy(context)
            Log.i(TAG, "Database is copied.")
        }
    }

    //asset에 저장된 db파일 가져오기
    private fun dbCopy(context: Context) { //db browser for sql에서 만든 db파일 import
        try {
            val folder = File(DB_PATH)
            if (!folder.exists()) folder.mkdir()

            val inputStream: InputStream = context.assets.open(DB_NAME)
            val out_filename = DB_PATH + DB_NAME
            val outputStream = FileOutputStream(out_filename)
            val mBuffer = ByteArray(1024)
            var mLength: Int
            while (inputStream.read(mBuffer).also { mLength = it } > 0) {
                outputStream.write(mBuffer, 0, mLength)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("dbCopy", "IOException 발생함")
        }
    }

    //asset에 db파일 없는 경우 테이블 생성
    override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.execSQL("CREATE TABLE IF NOT EXISTS tb_account ( id TEXT, pw TEXT, name TEXT, PRIMARY KEY(id) )")
        p0!!.execSQL("CREATE TABLE  IF NOT EXISTS tb_expense ( num INTEGER PRIMARY KEY AUTOINCREMENT, token TEXT, payer TEXT, expense INTEGER, location TEXT, date TEXT)")
        p0!!.execSQL("CREATE TABLE IF NOT EXISTS tb_group ( token TEXT, name TEXT, PRIMARY KEY(token) )")
        p0!!.execSQL("CREATE TABLE IF NOT EXISTS tb_member ( num INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT, token TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
}