package com.evg_ivanoff.roomirovanie.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.evg_ivanoff.roomirovanie.data.UserDataBase

@Database(entities = [UserDataBase::class], version = 1, exportSchema = false)
abstract class UserRoomDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        fun getDatabase(context: Context): UserRoomDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                UserRoomDataBase::class.java,
                "user_db"
            ).build()
        }
    }
}