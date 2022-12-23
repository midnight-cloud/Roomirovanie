package com.evg_ivanoff.roomirovanie.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_db")
data class UserDataBase(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val id: Int?,
    @ColumnInfo(name = "user_name")
    val name: String,
    @ColumnInfo(name = "user_age")
    val age: String,
    @ColumnInfo(name = "user_email")
    val email: String
): Parcelable