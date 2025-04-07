package com.gmker.inventory.card

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

class IngredientCard : BaseCard() {
    private var ingredientName = ""
    private var amount = 0
    private var unit = ""
}

@Entity(tableName = "users")
class User(@field:ColumnInfo(name = "user_name") var name: String,
           @field:ColumnInfo(name = "user_age") var age: Int,
           @field:ColumnInfo(name = "registration_date") var registrationDate: Long) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Insert
    fun insertAll(vararg users: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM users")
    fun deleteAll()

    @get:Query("SELECT * FROM users ORDER BY user_name ASC")
    val allUsers: List<User>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): User

    @Query("SELECT * FROM users WHERE user_name LIKE :name LIMIT 1")
    fun findUserByName(name: String?): User

    @get:Query("SELECT COUNT(*) FROM users")
    val userCount: Int
}