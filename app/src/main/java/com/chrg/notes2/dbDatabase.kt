package com.chrg.notes2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class dbDatabase{


    val dbName = "MyNotes"
    val dbTable = "Notes"
    val colID = "ID"
    val colTitle = "Title"
    val colDes = "Description"
    val dbVersion = 1
    // CREATE TABLE IF NOT EXISTS Notes ( ID INTEGER PRIMARY KEY, Title TEXT, Descriiption TEXT )
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS $dbTable ( $colID INTEGER PRIMARY KEY, $colTitle TEXT, $colDes TEXT );"
    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context) {
        var db = DatabseHelperNotes(context)
        sqlDB=db.writableDatabase

    }

    inner class DatabseHelperNotes:SQLiteOpenHelper {
        var context:Context?=null
        constructor(context: Context):super(context, dbName, null, dbVersion) {
            this.context = context
        }
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "Database is created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS $dbTable")
        }


    }

    fun Insert(values:ContentValues):Long {
        var ID = sqlDB!!.insert(dbTable, "", values)
        return ID
    }
    fun Query(projection:Array<String>,selection:String, selectionArgs:Array<String>, SortOrder:String):Cursor {

        var qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        var cursor = qb.query(sqlDB, projection, selection,selectionArgs,null, null, SortOrder)
        return cursor
    }

    fun Delete(selection: String, selectionArgs: Array<String>):Int {

        var count = sqlDB!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun Update(values: ContentValues, selection: String, selectionArgs: Array<String>):Int {
        val count = sqlDB!!.update(dbTable, values, selection, selectionArgs)
        return count
    }

}