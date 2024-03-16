package com.example.lesson_25

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_25.retrofit.PostClient.getPostsService
import com.example.lesson_25.databinding.FragmentFirstBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.lesson_25.retrofit.MainPost
import com.example.lesson_25.retrofit.Post
import com.example.lesson_25.retrofit.PostService
import com.example.lesson_25.retrofit.toPostDB
import com.example.lesson_25.room.PostDB
import com.example.lesson_25.room.toPost

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
            if((requireActivity().applicationContext as App).postDao?.getPosts()?.size!! == 0){
                postService?.getPicture(limit = 136, offset = offset)
            }else{
                MainPost(listOf())
            }

        }
        CoroutineScope(Dispatchers.IO).launch {
            mainPost = posts.await()
            (requireActivity().applicationContext as App).postDao?.savePosts(mainPost!!.photos.map {
                post -> post.toPostDB()
            })

            withContext(Dispatchers.Main) {
                initPostAdapter()
            }

        }
    }



    private suspend fun getPostsFromDB(offset:Int, limit:Int):List<Post>?{
        val postDbDefault = CoroutineScope(Dispatchers.IO).async {
            (requireActivity().applicationContext as App).postDao?.getPosts()
        }
        val resultPost =  CoroutineScope(Dispatchers.IO).async {
            val post = postDbDefault.await()

            post?.dropLast(post.size - (offset+limit))?.map{it.toPost()}
        }
        val finelData = resultPost.await()
        return finelData

    }

    private suspend fun initPostAdapter() {
        adapter = PostAdapter()
        bidding?.rvPost?.adapter = adapter
        bidding?.rvPost?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        adapter?.posts = listOf(Post("https://api.slingacademy.com/public/sample-photos/1.jpeg", "", "")).toMutableList()
        adapter?.posts = getPostsFromDB(0, 10)!!.toMutableList()
        adapter?.notifyItemRangeInserted(0, 3)
        bidding?.rvPost?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val llm = bidding?.rvPost?.layoutManager as LinearLayoutManager

                val lastVisiblePost = llm.findLastCompletelyVisibleItemPosition()

                if (lastVisiblePost == adapter!!.posts.size - 1) {

                    val previousSize = adapter!!.posts.size
                    offset = adapter!!.posts.size

//                    val newPosts = CoroutineScope(Dispatchers.IO).async {
//                        postService?.getPicture(limit = 3, offset = offset)
//                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        adapter?.posts = getPostsFromDB(offset, 10)!!.toMutableList()
                        withContext(Dispatchers.Main) {
                            adapter?.notifyItemRangeInserted(previousSize, 3)
                        }
                    }

                }
            }
        })


    }
}