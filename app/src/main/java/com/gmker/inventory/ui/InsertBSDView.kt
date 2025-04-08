package com.gmker.inventory.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.gmker.inventory.R
import com.gmker.inventory.dataBase.Ingredient

class InsertBSDView(context: Context) : LinearLayout(context) {

    private var mEditName: EditText
    private var mEditAmount: EditText
    private var mEditUnit: EditText
    private var mAddBtn: Button
    private var mCancelBtn: Button

    private var mDashBoardFragmentVarHolder: DashBoardFragmentVarHolder? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.insert_bs_layout, this, true)
        mEditName = findViewById(R.id.editText_ingredientName)
        mEditAmount = findViewById(R.id.editText_ingredientAmount)
        mEditUnit = findViewById(R.id.editText_ingredientUnit)
        mAddBtn = findViewById(R.id.add_button)
        mCancelBtn = findViewById(R.id.cancel_button)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mEditName.text = null
        mEditAmount.text = null
        mEditUnit.text = null

        mCancelBtn.setOnClickListener {
            hideDialog()
        }
        mAddBtn.setOnClickListener {
            if (checkInput(mutableListOf(mEditName, mEditAmount, mEditUnit))) {
                val number: Float
                try {
                    number = mEditAmount.text.toString().trim().toFloat()
                } catch (e: NumberFormatException) {
                    // 处理无效输入
                    Toast.makeText(context, "请输入有效的数字", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val name = mEditName.text.toString()
                val unit = mEditUnit.text.toString()
                val temp = Ingredient(name = name, amount = number, unit = unit)
                Thread {
                    mDashBoardFragmentVarHolder?.getDao()?.insert(temp)
                }.start()
                hideDialog()
            } else {
                Toast.makeText(context, "存在空值", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }

    private fun checkInput(editTextList: MutableList<EditText>): Boolean {
        for (i in editTextList) {
            val inputText = i.text.toString()
            if (inputText == "") {
                return false
            }
        }
        return true
    }

    private fun hideDialog() {
        mDashBoardFragmentVarHolder?.getBSD()?.dismiss()
    }

    fun setHolder(holder: DashBoardFragmentVarHolder) {
        mDashBoardFragmentVarHolder = holder
    }
}