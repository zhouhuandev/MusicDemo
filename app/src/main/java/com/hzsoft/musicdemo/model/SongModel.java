package com.hzsoft.musicdemo.model;

/**
 * Describe:
 * <p>歌曲实体模型</p>
 *
 * @author zhouhuan
 * @Date 2020/11/20
 */
public class SongModel {
    /**
     * 歌曲名字
     */
    private String name;
    /**
     * 歌曲照片
     */
    private String imagePath;
    /**
     * 作家
     */
    private String singer;
    /**
     * 路径
     */
    private String path;
    /**
     * 时长
     */
    private int duration;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 是否正在播放
     */
    private Boolean isPlaying = false;

    public SongModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
    }
}
