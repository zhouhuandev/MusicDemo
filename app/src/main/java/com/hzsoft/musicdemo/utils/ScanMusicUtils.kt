package com.hzsoft.musicdemo.utils

import android.R.attr.path
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.hzsoft.musicdemo.model.SongModel


/**
 * 扫描本地音乐文件
 * @author zhouhuan
 * @time 2020/11/19 21:01
 */
open class ScanMusicUtils {
    companion object{
        /**
         * 扫描系统里面的音频文件，返回一个list集合
         */
        fun getMusicData(context: Context): List<SongModel> {
            val list: MutableList<SongModel> = ArrayList()
            val selectionArgs = arrayOf("%Music%")
            val selection = MediaStore.Audio.Media.DATA + " like ? "
            // 媒体库查询语句（写一个工具类MusicUtils）
            val cursor: Cursor? = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection,
                selectionArgs, MediaStore.Audio.AudioColumns.IS_MUSIC
            )
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val songModel = SongModel()
                    songModel.name =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    songModel.singer =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    songModel.path =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    songModel.duration =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    songModel.size =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    if (songModel.size > 1000 * 800) {
                        // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                        val name = songModel.name
                        if (name != null && name.contains("-")) {
                            val str: List<String> = name.split("-")
                            songModel.singer = str[0]
                            songModel.name = str[1]
                        }
                        list.add(songModel)
                    }
                }
                // 释放资源
                cursor.close()
            }
            return list
        }

        /**
         * 定义一个方法用来格式化获取到的时间
         */
        fun formatTime(time: Int): String? {
            return if (time / 1000 % 60 < 10) {
                (time / 1000 / 60).toString() + ":0" + time / 1000 % 60
            } else {
                (time / 1000 / 60).toString() + ":" + time / 1000 % 60
            }
        }
    }
}