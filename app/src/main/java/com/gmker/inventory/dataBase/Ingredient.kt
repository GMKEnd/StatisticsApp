package com.gmker.inventory.dataBase

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "Ingredients")
class Ingredient(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                 @field:ColumnInfo(name = "ingredient_name") var name: String,
                 @field:ColumnInfo(name = "ingredient_amount") var amount: Float,
                 @field:ColumnInfo(name = "ingredient_unit") var unit: String)

@Dao
interface IngredientDao {
    @Insert
    fun insert(ingredient: Ingredient)

    @Insert
    fun insertAll(vararg ingredient: Ingredient)

    @Update
    fun update(ingredient: Ingredient)

    @Delete
    fun delete(ingredient: Ingredient)

    @Query("DELETE FROM Ingredients")
    fun deleteAll()

    @Query("SELECT * FROM Ingredients")
    fun getAllIngredients(): Flow<List<Ingredient>>

    @Query("SELECT * FROM Ingredients WHERE id = :id")
    fun getIngredientByID(id: Int): Ingredient

    @Query("SELECT * FROM Ingredients WHERE ingredient_name LIKE :name LIMIT 1")
    fun getIngredientByName(name: String?): Ingredient

    @get:Query("SELECT COUNT(*) FROM Ingredients")
    val size: Int
}