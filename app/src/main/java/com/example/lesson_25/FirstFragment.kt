package com.example.lesson_25

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lesson_25.PostClient.getPostsService
import com.example.lesson_25.databinding.FragmentFirstBinding
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstFragment : Fragment() {
    private var bidding: FragmentFirstBinding? = null
    private var adapter: PostAdapter? = null
    private var mainPost: MainPost? = null
    private var offset: Int = 0
    private var postService: PostService? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bidding = FragmentFirstBinding.inflate(inflater, container, false)
        return bidding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postService = getPostsService()
        val posts = CoroutineScope(Dispatchers.IO).async {
            postService?.getPicture(limit = 3, offset = offset)
        }
        CoroutineScope(Dispatchers.IO).launch {
            mainPost = posts.await()
            withContext(Dispatchers.Main) {
                initPostAdapter()
            }

        }
    }

    private fun initPostAdapter() {
        adapter = PostAdapter()
        bidding?.rvPost?.adapter = adapter
        bidding?.rvPost?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter?.posts = mainPost!!.photos.toMutableList()
        bidding?.rvPost?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val llm = bidding?.rvPost?.layoutManager as LinearLayoutManager

                val lastVisiblePost = llm.findLastCompletelyVisibleItemPosition()

                if (lastVisiblePost == adapter!!.posts.size - 1) {

                    val previousSize = adapter!!.posts.size
                    offset = adapter!!.posts.size

                    val newPosts = CoroutineScope(Dispatchers.IO).async {
                        postService?.getPicture(limit = 3, offset = offset)
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        adapter?.posts?.addAll(newPosts.await()!!.photos)
                        withContext(Dispatchers.Main) {
                            adapter?.notifyItemRangeInserted(previousSize, 3)
                        }
                    }

                }
            }
        })


    }
}