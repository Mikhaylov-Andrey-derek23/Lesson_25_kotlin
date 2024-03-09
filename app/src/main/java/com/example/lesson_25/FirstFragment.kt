package com.example.lesson_25

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_25.UserClient.getUsersService
import com.example.lesson_25.databinding.FragmentFirstBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstFragment : Fragment() {
    private var bidding: FragmentFirstBinding? = null
    private var adapter: UserAdapter? = null
    private var mainUsers: MainUsers? = null
    private var page: Int = 1
    private var usersService: UsersService? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bidding = FragmentFirstBinding.inflate(inflater, container, false)
        return bidding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usersService = getUsersService()
        val users = CoroutineScope(Dispatchers.IO).async {
            usersService?.getPicture(page = page)
        }
        CoroutineScope(Dispatchers.IO).launch {
            mainUsers = users.await()
            withContext(Dispatchers.Main) {
                initUsersAdapter()
            }

        }
    }

    private fun initUsersAdapter() {
        adapter = UserAdapter()
        bidding?.rvPost?.adapter = adapter
        bidding?.rvPost?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter?.users = mainUsers!!.data.toMutableList()
        bidding?.rvPost?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val llm = bidding?.rvPost?.layoutManager as LinearLayoutManager

                val lastVisiblePost = llm.findLastCompletelyVisibleItemPosition()

                if (lastVisiblePost == adapter!!.users.size - 1) {

                    val previousSize = adapter!!.users.size
                    page = 2

                    val newUsers = CoroutineScope(Dispatchers.IO).async {
                        usersService?.getPicture(page=page)
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        adapter?.users?.addAll(newUsers.await()!!.data)
                        withContext(Dispatchers.Main) {
                            adapter?.notifyItemRangeInserted(previousSize, 6)
                        }
                    }

                }
            }
        })


    }

}