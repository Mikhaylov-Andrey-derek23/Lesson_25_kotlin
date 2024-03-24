package com.example.lesson_25.main.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.lesson_25.App
import com.example.lesson_25.retrofit.MainPost
import com.example.lesson_25.retrofit.Post
import com.example.lesson_25.retrofit.PostClient.getPostsService
import com.example.lesson_25.retrofit.PostService
import com.example.lesson_25.retrofit.toPostDB
import com.example.lesson_25.room.PostDB
import com.example.lesson_25.room.PostDao
import com.example.lesson_25.room.toPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(app: Application):AndroidViewModel(app) {

    private  var postService:PostService? = null
    private  var postDao:PostDao? = null
    var postLiveData:MutableLiveData<MainPost> = MutableLiveData()
    var postLiveDBData:MutableLiveData<List<Post>> = MutableLiveData()
    init {
        postService = getPostsService()
        postDao = (app as App).postDao
    }

    fun getPostFromNetwork(limit:Int, offset:Int){

        var postsFromDB:List<PostDB>? = null

        val postsFromServerDefault =  CoroutineScope(Dispatchers.IO).async {
            postDao?.getPosts()
        }

        CoroutineScope(Dispatchers.IO).launch {
            postsFromDB = postsFromServerDefault.await()
            if(postsFromDB?.size == 0) {


                val postServiceResult = CoroutineScope(Dispatchers.IO).async {
                    postService?.getPostForNetwork(limit, offset)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val result = postServiceResult.await()
                    result?.let{
                        savePostForDB(result.photos)
                        postLiveData.postValue(result)
                    }

                }
            } else {
                postLiveData.value = MainPost(listOf())
            }
        }




    }

    fun getPostFromDB(offset:Int, limit:Int){

        val postDbDefault = CoroutineScope(Dispatchers.IO).async {
           postDao?.getPosts()
        }
        val resultPost =  CoroutineScope(Dispatchers.IO).async {
            val post = postDbDefault.await()

            post?.dropLast(post.size - (offset+limit))?.map{it.toPost()}
        }

        CoroutineScope(Dispatchers.IO).launch {
            postLiveDBData.postValue(resultPost.await())
        }

    }

    fun savePostForDB(posts:List<Post>) {
        postDao?.savePosts(posts.map{post -> post.toPostDB()})
    }

}