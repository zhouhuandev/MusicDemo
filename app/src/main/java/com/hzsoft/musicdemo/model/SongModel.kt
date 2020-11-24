package com.hzsoft.musicdemo.model

/**
 * Describe:
 * <p>歌曲实体模型</p>
 *
 * @author zhouhuan
 * @Date 2020/11/19
 */
class SongModel() {
    /**
     * 歌曲名字
     */
    var name: String? = null

    /**
     * 歌曲照片
     */
    var imagePath: String? = null

    /**
     * 作家
     */
    var singer: String? = null

    /**
     * 路径
     */
    var path: String? = null

    /**
     * 时长
     */
    var duration: Int = 0

    /**
     * 文件大小
     */
    var size: Long = 0

    /**
     * 是否正在播放
     */
    var isPlaying: Boolean = false
}