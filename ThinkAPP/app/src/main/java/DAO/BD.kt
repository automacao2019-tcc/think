package DAO

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BD(context: Context) : SQLiteOpenHelper(context, "think", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create table casa(\n" +
                "\tuuid varchar(100) not null primary key,\n" +
                "\tcomodos TEXT\n" +
                ");\n")

        db.execSQL("create table rotina(" +
                "id integer not null primary key" +
                ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}