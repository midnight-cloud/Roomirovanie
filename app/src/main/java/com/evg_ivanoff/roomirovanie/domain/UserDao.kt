package com.evg_ivanoff.roomirovanie.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.evg_ivanoff.roomirovanie.data.UserDataBase
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_db ORDER BY user_id ASC")
    fun getAllUsers(): Flow<List<UserDataBase>>

    @Query("SELECT * FROM user_db WHERE user_id = :id")
    suspend fun getUserById(id: Int): UserDataBase

    @Insert
    suspend fun addUser(user: UserDataBase)

    @Query("DELETE FROM user_db")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM user_db WHERE user_id = :id")
    suspend fun deleteUserById(id: Int)

    @Update
    suspend fun updateUser(user: UserDataBase)
}