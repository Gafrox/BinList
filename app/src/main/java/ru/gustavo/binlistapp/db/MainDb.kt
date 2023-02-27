package ru.gustavo.binlistapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CardInfo::class], version = 1)
abstract class MainDb : RoomDatabase() {
    abstract fun cardInfoDao(): CardInfoDao

    companion object {
        fun getDb(context: Context): MainDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "main.db"
            ).build()
        }
    }
}