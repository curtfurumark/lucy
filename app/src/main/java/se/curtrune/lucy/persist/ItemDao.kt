package se.curtrune.lucy.persist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import se.curtrune.lucy.classes.item.Item

@Dao
interface  ItemDao {
    @Query("SELECT * FROM itemEntity")
    suspend fun getAll(): List<ItemEntity>
    @Query("SELECT * FROM itemEntity WHERE id = :id")
    suspend fun getItem(id: Long): ItemEntity
    @Update
    suspend fun update(item: ItemEntity)
    @Insert
    suspend fun insert(item: ItemEntity)
    @Delete
    suspend fun delete(item: ItemEntity)
}
