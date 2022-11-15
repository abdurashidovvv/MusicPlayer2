package com.abdurashidov.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdurashidov.musicplayer.databinding.RvItemBinding
import com.abdurashidov.musicplayer.models.Music

class MyRvAdapter(var list:ArrayList<Music>, val rvClick: RvClick): RecyclerView.Adapter<MyRvAdapter.Vh>() {

    inner class Vh(val rvItem: RvItemBinding): RecyclerView.ViewHolder(rvItem.root){
        fun onBind(music: Music){
            rvItem.musicTitle.text=music.title
            rvItem.musicDuration.text=music.author
//            rvItem.musicImage.setImageBitmap(BitmapFactory.decodeFile(music.imagePath))
            rvItem.rvCard.setOnClickListener {
                rvClick.onClick(music)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface RvClick{
        fun onClick(music: Music)
    }
}