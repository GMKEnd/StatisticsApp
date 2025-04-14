package com.gmker.inventory.ui.bottomSheetDialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.gmker.inventory.R
import com.gmker.inventory.dataBase.AppDatabase
import com.gmker.inventory.dataBase.Ingredient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoreOptionsDialog(context: Context) : BaseBsd(context) {

    private var mEditName: TextInputEditText? = null
    private var mEditAmount: TextInputEditText? = null
    private var mEditUnit: TextInputEditText? = null
    private var mAddBtn: Button? = null
    private var mCancelBtn: Button? = null

    private var mIngredient: Ingredient? = null
    private var mId = 0

    override fun getView(context: Context): View {
        return layoutInflater.inflate(R.layout.bsd_new_ingredient, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mEditName = findViewById(R.id.editText_ingredientName)
        mEditAmount = findViewById(R.id.editText_ingredientAmount)
        mEditUnit = findViewById(R.id.editText_ingredientUnit)
        mAddBtn = findViewById(R.id.add_button)
        mCancelBtn = findViewById(R.id.cancel_button)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 更新Ui
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                mIngredient = AppDatabase.INSTANCE?.ingredientDao()?.getIngredientByID(mId)
            }
            mEditName?.setText(mIngredient?.name)
            mEditAmount?.setText(mIngredient?.amount.toString())
            mEditUnit?.setText(mIngredient?.unit)
        }
        mAddBtn?.text = "修改"
        mCancelBtn?.text = "删除"

        mCancelBtn?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.isPressed = true
                    // 启动协程
                    v.tag = CoroutineScope(Dispatchers.Main).launch {
                        delay(1500) // 延迟1.5秒
                        // 长按后执行的操作
                        Thread {
                            AppDatabase.INSTANCE?.ingredientDao()?.delete(mIngredient!!)
                        }.start()
                        dismiss()
                    }
                    true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    (v.tag as? Job)?.cancel() // 取消协程
                    v.isPressed = false
                    true
                }

                else -> false
            }
        }

//        mCancelBtn?.setOnClickListener {
//            Thread {
//                AppDatabase.INSTANCE?.ingredientDao()?.delete(mIngredient!!)
//            }.start()
//            dismiss()
//        }
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

                mIngredient?.name = name
                mIngredient?.amount = number
                mIngredient?.unit = unit

                Thread {
                    AppDatabase.INSTANCE?.ingredientDao()?.update(mIngredient!!)
                }.start()

                dismiss()
            } else {
                Toast.makeText(context, "存在空值", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }

    fun setId(id: Int) {
        mId = id
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