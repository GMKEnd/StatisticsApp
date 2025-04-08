package com.gmker.inventory.ui

import com.gmker.inventory.dataBase.IngredientDao
import com.google.android.material.bottomsheet.BottomSheetDialog

interface DashBoardFragmentVarHolder {
    fun setBSD(dialog: BottomSheetDialog)

    fun getBSD(): BottomSheetDialog

    fun setDao(dao: IngredientDao)

    fun getDao(): IngredientDao
}