package com.gmker.inventory.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmker.inventory.dataBase.AppDatabase
import com.gmker.inventory.dataBase.Ingredient
import com.gmker.inventory.dataBase.IngredientDao
import com.gmker.inventory.databinding.FragmentDashboardBinding
import com.gmker.inventory.ui.BaseAdapter
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: BaseAdapter
    private lateinit var db: AppDatabase
    private lateinit var ingredientDao: IngredientDao

    private lateinit var mBtn: Button
    private var count = 0

    private var dataList = mutableListOf<Ingredient?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.INSTANCE!!
        ingredientDao = db.ingredientDao()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        doAdapterInit()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun doAdapterInit() {
        mAdapter = BaseAdapter(dataList)
        mRecyclerView = binding.recyclerView
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashboardViewModel.list.collect { ingredients ->
                    mAdapter.submitList(ingredients)
                }
            }
        }

        mBtn = binding.addBtn
        mBtn.setOnClickListener {
            Thread {
                val t = Ingredient(name = "test", amount = count, unit = "kg")
                count += 1
                ingredientDao.insert(t)
            }.start()
        }
    }
}