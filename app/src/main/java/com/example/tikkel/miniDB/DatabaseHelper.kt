package com.example.tikkel.miniDB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper (val context: Context?) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        const val DATABASE_NAME = "Preference.db"
        const val DATABASE_VERSION = 1
        //Table_1 에 관한 것들
        const val TABLE_1_NAME = "display_mode"
        const val TABLE_1_COL_ID = "_id"
        const val TABLE_1_COL_MODE = "mode"
        // 이후 추가할 Table 2, 3, ...등 기입해라.

        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(DatabaseHelper::class.java){
                instance ?: DatabaseHelper(context).also{
                    instance = it
                }
            }
        }

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = "CREATE TABLE $TABLE_1_NAME (" +
                "$TABLE_1_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "$TABLE_1_COL_MODE TEXT "+
                ")"
        db?.execSQL(createQuery)
        // preference 변수들 만을 위한 초기 테이블 세팅
        val contentValues = ContentValues().apply{
            put(TABLE_1_COL_MODE, "banner")
        }
        db?.insert(TABLE_1_NAME, null, contentValues)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    // CRUD 함수들을 아래에 정의할 것임. 데이터베이스에 여러 개의 테이블이 존재할 수 있으므로
    // 각 테이블과 관련된 CRUD 함수들을 정의할때는 "테이블명_create" 의 예시처럼 규칙을 정해놓자.
    // 그리고 CURD 이름에 맞추어 생성은 create, 변경은 update, 검색 및 읽기는 read, 삭제는 delete로 함수의 이름 끝부분을 표기하자.

    // display_mode 테이블 관련 CRUD 함수들
    fun display_mode_create(number: String){
        val db = this.writableDatabase
        val contentValues = ContentValues().apply{
            put(TABLE_1_COL_MODE, number)
        }
        db.insert(TABLE_1_NAME, null, contentValues)

    }


    fun display_mode_update(id: String, number: String){
        val db = this.writableDatabase
        val contentValues = ContentValues().apply{
            put(TABLE_1_COL_ID, id)
            put(TABLE_1_COL_MODE, number)
        }
        db.update(TABLE_1_NAME, contentValues, "$TABLE_1_COL_ID = ?", arrayOf(id))
        Log.d("#########################","mode is updated to $number ")
    }

    fun display_mode_read():String{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_1_NAME", null)
        cursor.moveToNext()
        val answer = cursor.getString(1)

        cursor.close()
        db.close()
        Log.d("#########################","selected mode is $answer")

        return answer

    }

    //다른 테이블 들 2, 3 , ... 을 위한 CRUD 함수들 아래에 정의하시오.




}