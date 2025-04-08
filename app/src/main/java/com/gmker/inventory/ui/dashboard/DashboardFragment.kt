package com.gmker.inventory.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmker.inventory.R
import com.gmker.inventory.dataBase.AppDatabase
import com.gmker.inventory.dataBase.Ingredient
import com.gmker.inventory.dataBase.IngredientDao
import com.gmker.inventory.databinding.FragmentDashboardBinding
import com.gmker.inventory.ui.BaseAdapter
import com.gmker.inventory.ui.DashBoardFragmentVarHolder
import com.gmker.inventory.ui.InsertBSDView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(), DashBoardFragmentVarHolder {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: BaseAdapter
    private lateinit var db: AppDatabase
    private lateinit var ingredientDao: IngredientDao

    private lateinit var addBtn: ImageView
    private lateinit var mBottomSheet: BottomSheetDialog

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

        doToolbarInit()
        doAdapterInit()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getBSD(): BottomSheetDialog {
        return mBottomSheet
    }

    override fun setBSD(dialog: BottomSheetDialog) {
        mBottomSheet = dialog
    }

    override fun getDao(): IngredientDao {
        return ingredientDao
    }

    override fun setDao(dao: IngredientDao) {
        ingredientDao = dao
    }

    private fun doToolbarInit() {
        val t = activity?.findViewById<Toolbar>(R.id.toolbar)
        t?.addView(LayoutInflater.from(activity).inflate(R.layout.toolbar_dashborad, t, false))

        val bottomSheetView = InsertBSDView(requireActivity())
        bottomSheetView.setHolder(this)

        mBottomSheet = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)
        mBottomSheet.setContentView(bottomSheetView)
        mBottomSheet.setOnShowListener {
            mBottomSheet.behavior.skipCollapsed = true
            mBottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        addBtn = activity?.findViewById(R.id.add_btn)!!
        addBtn.setOnClickListener {
            mBottomSheet.show()
        }
    }

    private fun doAdapterInit() {
        mAdapter = BaseAdapter(dataList)
        mRecyclerView = binding.recyclerView
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]
        // 随数据库更新
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashboardViewModel.list.collect { ingredients ->
                    mAdapter.submitList(ingredients)
                }
            }
        }
    }
}