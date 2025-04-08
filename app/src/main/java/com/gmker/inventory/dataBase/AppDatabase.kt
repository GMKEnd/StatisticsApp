package com.gmker.inventory.dataBase

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile

@Database(entities = [Ingredient::class, Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao?

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "app_database")
//                        .fallbackToDestructiveMigration() // 数据库升级时清空数据
                        .build()
                }
            }
            Log.i("AppDatabase", "getInstance:$INSTANCE")
            return INSTANCE
        }
    }
}