package com.gmker.inventory.dataBase

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "Products")
class Product(@field:ColumnInfo(name = "ingredient_name") var name: String,
              @field:ColumnInfo(name = "ingredient_amount") var amount: Int,
              @field:ColumnInfo(name = "ingredient_unit") var unit: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Dao
interface ProductDao {
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

    @get:Query("SELECT * FROM Ingredients ORDER BY ingredient_name ASC")
    val allIngredients: List<Ingredient>

    @Query("SELECT * FROM Ingredients WHERE id = :id")
    fun getIngredientByID(id: Int): Ingredient

    @Query("SELECT * FROM Ingredients WHERE ingredient_name LIKE :name LIMIT 1")
    fun getIngredientByName(name: String?): Ingredient

    @get:Query("SELECT COUNT(*) FROM Ingredients")
    val size: Int
}