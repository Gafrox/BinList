package ru.gustavo.binlistapp.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardInfoDao {
    @Query("SELECT * FROM info")
    fun getAll(): Flow<List<CardInfo>>

    @Query("DELETE FROM info")
    fun deleteAll()

    @Insert
    fun insert(cardInfo: CardInfo)

    @Delete
    fun delete(cardInfo: CardInfo)

    @Update
    fun update(cardInfo: CardInfo)
}