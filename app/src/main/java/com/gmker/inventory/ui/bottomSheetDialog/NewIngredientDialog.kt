package com.gmker.inventory.ui.bottomSheetDialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.gmker.inventory.R
import com.gmker.inventory.dataBase.AppDatabase
import com.gmker.inventory.dataBase.Ingredient
import com.google.android.material.textfield.TextInputEditText

class NewIngredientDialog(context: Context) : BaseBsd(context) {

    private var mEditName: TextInputEditText? = null
    private var mEditAmount: TextInputEditText? = null
    private var mEditUnit: TextInputEditText? = null
    private var mAddBtn: Button? = null
    private var mCancelBtn: Button? = null

    override fun getView(context: Context): View {
        return layoutInflater.inflate(R.layout.insert_bs_layout, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mEditName = findViewById(R.id.editText_ingredientName)
        mEditAmount = findViewById(R.id.editText_ingredientAmount)
        mEditUnit = findViewById(R.id.editText_ingredientUnit)
        mAddBtn = findViewById(R.id.add_button)
        mCancelBtn = findViewById(R.id.cancel_button)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mCancelBtn?.setOnClickListener {
            mEditName?.text = null
            mEditAmount?.text = null
            mEditUnit?.text = null
            dismiss()
        }
        mAddBtn?.setOnClickListener {
            if (checkInput(mutableListOf(mEditName!!, mEditAmount!!, mEditUnit!!))) {
                val number: Float
                try {
                    number = mEditAmount?.text.toString().trim().toFloat()
                } catch (e: NumberFormatException) {
                    // 处理无效输入
                    Toast.makeText(context, "请输入有效的数字", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val name = mEditName?.text.toString()
                val unit = mEditUnit?.text.toString()
                val temp = Ingredient(name = name, amount = number, unit = unit)
                Thread {
                    AppDatabase.INSTANCE?.ingredientDao()?.insert(temp)
                }.start()

                mEditName?.text = null
                mEditAmount?.text = null
                mEditUnit?.text = null
                dismiss()
            } else {
                Toast.makeText(context, "存在空值", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        mEditName?.clearFocus()
        mEditAmount?.clearFocus()
        mEditUnit?.clearFocus()
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
}