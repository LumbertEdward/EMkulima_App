package com.example.emkulimaapp.adapters

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteOpenHelper(val context: Context): SQLiteOpenHelper(context, "Favourites", null, 1) {
    //table
    private val TABLE_NAME: String = "TableFavourites"

    //columns
    var NAME: String = "product_name"
    var PRODUCT_ID: String = "product_id"
    var FARMER_ID: String = "farmer_id"
    var PRICE: String = "product_price"
    var IMAGE: String = "product_image"
    var DESCRIPTION: String = "product_description"
    var TIME: String = "product_delivery_time"
    var CALCS: String = "product_calcs"
    var TYPE: String? = "product_type"

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_FAVOURITES_TABLE: String = "CREATE TABLE" + TABLE_NAME + "(" +
                PRODUCT_ID + "INTEGER PRIMARY KEY," +
                NAME + "TEXT NOT NULL," +
                FARMER_ID + "INTEGER NOT NULL," +
                PRICE + "INTEGER NOT NULL," +
                IMAGE + "TEXT NOT NULL," +
                DESCRIPTION + "TEXT NOT NULL," +
                TIME + "TEXT NOT NULL," +
                CALCS + "TEXT NOT NULL," +
                TYPE + "TEXT NOT NULL," + ")";

        db!!.execSQL(CREATE_FAVOURITES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion){
            db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
            onCreate(db)
        }
    }
}