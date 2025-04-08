package com.gmker.inventory.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmker.inventory.dataBase.AppDatabase
import com.gmker.inventory.dataBase.Ingredient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private var ingredientDao = AppDatabase.INSTANCE?.ingredientDao()!!
    val list: Flow<List<Ingredient>> = ingredientDao.getAllIngredients()

    fun insert(ingredient: Ingredient) = viewModelScope.launch {
        ingredientDao.insert(ingredient)
    }

    fun update(ingredient: Ingredient) = viewModelScope.launch {
        ingredientDao.update(ingredient)
    }

    fun delete(ingredient: Ingredient) = viewModelScope.launch {
        ingredientDao.delete(ingredient)
    }
}