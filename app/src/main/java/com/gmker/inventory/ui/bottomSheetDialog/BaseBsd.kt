package com.gmker.inventory.ui.bottomSheetDialog

import android.content.Context
import android.view.View
import com.gmker.inventory.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBsd(context: Context) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {

    init {
        this.setContentView(getView(context))
    }

    override fun onStart() {
        super.onStart()
        this.behavior.skipCollapsed = true
        this.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    abstract fun getView(context: Context): View
}