package com.devwan.where

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_feed.*

lateinit var list_Where: ArrayList<Where>
lateinit var glide: RequestManager
lateinit var recyclerView: RecyclerView

class FeedFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_feed, container, false)
        list_Where = ArrayList<Where>()
        list_Where.add(Where("d", "d", ("android.resource://" + context?.packageName + "/" + R.drawable.icon_splash), "d", 0))
        list_Where.add(Where("d", "d", ("android.resource://" + context?.packageName + "/" + R.drawable.icon_feed), "d", 0))
        list_Where.add(Where("d", "d", ("android.resource://" + context?.packageName + "/" + R.drawable.icon_splash), "d", 0))
        glide = Glide.with(this@FeedFragment)

        recyclerView = rootView.findViewById(R.id.recyclerView_feed) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = FeedViewAdapter(list_Where, layoutInflater, glide)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

class FeedViewAdapter(
    var itemList: ArrayList<Where>,
    val inflater: LayoutInflater,
    val glide: RequestManager
) : RecyclerView.Adapter<FeedViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var whereImage: ImageView

        init {
            whereImage = itemView.findViewById(R.id.where_image)
//            whereImage.setOnClickListener {
//                val position: Int = adapterPosition
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.card_feed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        glide.load(itemList.get(position).where_image).into(holder.whereImage)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}