package com.gmker.inventory.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gmker.inventory.dataBase.AppDatabase
import com.gmker.inventory.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var db: AppDatabase? = null
//    private var mUserDao: UserDao? = null

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(requireActivity())
//        mUserDao = db?.userDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
//        Thread {
//            val user1 = mUserDao?.allUsers?.get(0)
//            textView.post {
//                textView.text = "UserInfo:${user1?.name}, ${user1?.id}, ${user1?.age}"
//            }
//        }
////        notificationsViewModel.text.observe(viewLifecycleOwner) {
////            textView.text = it
////        }
//
//        binding.InsertBtn.setOnClickListener {
//            Thread {
//                val user = User("test1", 10, System.currentTimeMillis())
//                mUserDao?.insertAll(user)
//
//                val user1 = mUserDao?.allUsers?.get(0)
//                id = user1?.id ?: 0
//                textView.post {
//                    textView.text = "UserInfo:${user1?.name}, ${user1?.id}, ${user1?.age}"
//                }
//            }.start()
//        }
//
//        binding.IncreaseBtn.setOnClickListener {
//
//            Thread {
//                val temp = mUserDao!!.getUserById(id)
//                temp.age += 3
//                mUserDao?.update(temp)
//
//                val user1 = mUserDao?.allUsers?.get(0)
//                textView.post {
//                    textView.text = "UserInfo:${user1?.name}, ${user1?.id}, ${user1?.age}"
//                }
//            }.start()
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}