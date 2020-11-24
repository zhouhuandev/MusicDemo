package com.hzsoft.musicdemo.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hzsoft.musicdemo.R
import com.hzsoft.musicdemo.base.BaseAdapter
import com.hzsoft.musicdemo.model.SongModel

/**
 * Describe:
 * <p>歌曲适配器</p>
 *
 * @author zhouhuan
 * @Date 2020/11/19
 */
class SongAdapter(context: Context) : BaseAdapter<SongModel, SongAdapter.SongViewHodler>(context) {

    override fun onBindLayout(): Int {
        return R.layout.item_songs_list
    }

    override fun onCreateHolder(view: View): SongViewHodler {
        return SongViewHodler(view)
    }

    override fun onBindData(holder: SongViewHodler, e: SongModel, positon: Int) {
        holder.tvSongName.text = e.name
        holder.ivSongImage.setTag(e.name)
        if (TextUtils.equals(holder.ivSongImage.tag as String, e.name) && e.isPlaying) {
            holder.ivSongImage.setImageResource(R.drawable.ic_baseline_headset_24)
        } else {
            holder.ivSongImage.setImageResource(R.drawable.ic_baseline_music_note_24)
        }
    }

    inner class SongViewHodler(view: View) : RecyclerView.ViewHolder(view) {
        var ivSongImage: ImageView = view.findViewById(R.id.ivSongImage)
        var tvSongName: TextView = view.findViewById(R.id.tvSongName)
    }
}