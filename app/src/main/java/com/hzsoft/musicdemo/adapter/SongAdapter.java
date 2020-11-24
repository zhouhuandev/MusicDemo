package com.hzsoft.musicdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzsoft.musicdemo.R;
import com.hzsoft.musicdemo.base.BaseAdapter;
import com.hzsoft.musicdemo.model.SongModel;

/**
 * Describe:
 * <p>歌曲适配器</p>
 *
 * @author zhouhuan
 * @Date 2020/11/20
 */
public class SongAdapter extends BaseAdapter<SongModel, SongAdapter.SongViewHodler> {
    public SongAdapter(Context context) {
        super(context);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.item_songs_list;
    }

    @Override
    protected SongAdapter.SongViewHodler onCreateHolder(View view) {
        return new SongViewHodler(view);
    }

    @Override
    protected void onBindData(SongAdapter.SongViewHodler holder, SongModel songModel, int positon) {
        holder.tvSongName.setText(songModel.getName());
        holder.ivSongImage.setTag(songModel.getName());
        if (TextUtils.equals((String) holder.ivSongImage.getTag(), songModel.getName()) && songModel.getPlaying()) {
            holder.ivSongImage.setImageResource(R.drawable.ic_baseline_headset_24);
        } else {
            holder.ivSongImage.setImageResource(R.drawable.ic_baseline_music_note_24);
        }
    }

    static class SongViewHodler extends RecyclerView.ViewHolder {

        ImageView ivSongImage;
        TextView tvSongName;

        public SongViewHodler(@NonNull View itemView) {
            super(itemView);
            ivSongImage = itemView.findViewById(R.id.ivSongImage);
            tvSongName = itemView.findViewById(R.id.tvSongName);
        }


    }
}
