package com.trikh.focuslock.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.trikh.focuslock.data.model.Application
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.local.db.DbConstants.Companion.DB_NAME
import com.trikh.focuslock.data.utils.BooleanListConverter
import com.trikh.focuslock.data.utils.CalendarTypeConverters
import com.trikh.focuslock.data.utils.StringListConverter

@Database(entities = [Schedule::class, Application::class, InstantLockSchedule::class], version = 1)
@TypeConverters(CalendarTypeConverters::class, StringListConverter::class, BooleanListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun instantLockDao() : InstantLockDao
    abstract fun applicationsDao(): ApplicationsDao

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