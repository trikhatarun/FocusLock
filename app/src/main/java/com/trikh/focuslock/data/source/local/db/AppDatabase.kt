package com.trikh.focuslock.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.local.db.DbConstants.Companion.DB_NAME
import com.trikh.focuslock.data.utils.CalendarTypeConverters

@Database(entities = [Schedule::class], version = 1)
@TypeConverters(CalendarTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).build()
    }
}