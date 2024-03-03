package com.example.lesson_25

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private  var bidding: FragmentFirstBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bidding = FragmentFirstBinding.inflate(inflater, container, false)
        return  bidding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val postService = getPostsService()
        val posts = CoroutineScope(Dispatchers.IO).async {
            postService.getPicture()
        }
        CoroutineScope(Dispatchers.IO).launch {
            val mainPost =  posts.await()

            withContext(Dispatchers.Main){
                Glide.with(requireContext())
                    .load(mainPost.photos[0].url)
                    .into(bidding!!.ivPicture)
                bidding?.tvTitle?.text = mainPost.photos[0].title
                bidding?.tvdescription?.text = mainPost.photos[0].description
            }
        }
    }


}