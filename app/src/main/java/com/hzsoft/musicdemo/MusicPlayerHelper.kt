package com.hzsoft.musicdemo

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import com.hzsoft.musicdemo.model.SongModel
import com.hzsoft.musicdemo.utils.ScanMusicUtils
import java.lang.ref.WeakReference

/**
 * Describe:
 * <p>音乐播放器帮助类</p>
 *  可播放格式：AAC、AMR、FLAC、MP3、MIDI、OGG、PCM
 * @author zhouhuan
 * @Date 2020/11/19
 */
class MusicPlayerHelper constructor(seekBar: SeekBar, text: TextView) :
    MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
    SeekBar.OnSeekBarChangeListener {

    companion object {
        var TAG = MusicPlayerHelper::class.java.simpleName
        private val MSG_CODE = 0x01
        private val MSG_TIME = 1_000L

        class MusicPlayerHelperHanlder constructor(helper: MusicPlayerHelper) :
            Handler(Looper.getMainLooper()) {
            var weakReference: WeakReference<MusicPlayerHelper>

            init {
                weakReference = WeakReference(helper)
            }

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    MSG_CODE -> {
                        var pos = 0
                        //如果播放且进度条未被按压
                        if (weakReference.get()?.player!!.isPlaying && !weakReference.get()?.seekBar!!.isPressed()) {
                            val position: Int = weakReference.get()?.player!!.getCurrentPosition()
                            val duration: Int = weakReference.get()?.player!!.getDuration()
                            if (duration > 0) {
                                // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
                                pos =
                                    (weakReference.get()?.seekBar!!.max * position / duration.toLong()).toInt()
                            }
                            weakReference.get()?.text!!.text =
                                weakReference.get()?.getCurrentPlayingInfo(position, duration)
                        }
                        weakReference.get()?.seekBar!!.progress = pos
                        sendEmptyMessageDelayed(MSG_CODE, MSG_TIME)
                    }
                }
            }
        }
    }

    private var mHandler: MusicPlayerHelperHanlder

    /**
     * 播放器
     */
    private var player: MediaPlayer

    /**
     * 进度条
     */
    private var seekBar: SeekBar

    /**
     * 显示播放信息
     */
    private var text: TextView

    /**
     * 当前的播放歌曲信息
     */
    private var songModel: SongModel? = null

    init {
        mHandler = MusicPlayerHelperHanlder(this)
        player = MediaPlayer()
        // 设置媒体流类型
        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player.setOnBufferingUpdateListener(this)
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)

        this.seekBar = seekBar
        this.seekBar.setOnSeekBarChangeListener(this)
        this.text = text
    }

    /**
     * 更新缓冲
     */
    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        seekBar.setSecondaryProgress(percent)
        val currentProgress: Int =
            seekBar.getMax() * player.getCurrentPosition() / player.getDuration()
        Log.e(TAG, "$currentProgress% play --> $percent buffer")
    }

    /**
     * 当前 Song 已经准备好
     */
    override fun onPrepared(mp: MediaPlayer?) {
        Log.e(TAG, "onPrepared")
        mp!!.start()
    }

    /**
     * 当前 Song 播放完毕
     */
    override fun onCompletion(mp: MediaPlayer?) {
        Log.e(TAG, "onCompletion")
        mOnCompletionListener?.onCompletion(mp)
    }

    /**
     * 播放
     * @param songModel 播放源
     * @param isRestPlayer true 切换歌曲 false 不切换
     */
    fun playBySongModel(songModel: SongModel, isRestPlayer: Boolean) {
        this.songModel = songModel
        Log.e(TAG, "playBySongModel Url: ${songModel.path}")
        if (isRestPlayer) {
            //重置多媒体
            player.reset()
            // 设置数据源
            songModel.path?.let { player.setDataSource(it) }
            // 准备自动播放 同步加载，阻塞 UI 线程
            // player.prepare()
            // 建议使用异步加载方式，不阻塞 UI 线程
            player.prepareAsync()
        } else {
            player.start()
        }
        //发送更新命令
        mHandler.sendEmptyMessage(MSG_CODE)
    }

    /**
     * 暂停（Lambda 写法）
     *  等同于 var pause:() -> Unit = {if (player.isPlaying) player.pause()}
     */
    var pause = {
        Log.e(TAG, "pause")
        if (player.isPlaying) player.pause()
        //移除更新命令
        mHandler.removeMessages(MSG_CODE)
    }


    /**
     * 停止
     */
    fun stop() {
        Log.e(TAG, "stop")
        player.stop()
        seekBar.progress = 0
        text.text = "停止播放"
        //移除更新命令
        mHandler.removeMessages(MSG_CODE)
    }

    /**
     * 是否正在播放
     */
    var isPlaying = { player.isPlaying }

    fun getCurrentPlayingInfo(currentTime: Int, maxTime: Int): String {
        val info = songModel?.let { "正在播放:  ${songModel?.name}\t\t" }
        return "$info ${ScanMusicUtils.formatTime(currentTime)} / ${
            ScanMusicUtils.formatTime(
                maxTime
            )
        }"
    }

    /**
     * 消亡 必须在 Activity 或者 Frament onDestroy() 调用 以防止内存泄露
     */
    fun destroy() {
        // 释放掉播放器
        player.release()
        mHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 用于监听SeekBar进度值的改变
     */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

    }

    /**
     * 用于监听SeekBar开始拖动
     */
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        mHandler.removeMessages(MSG_CODE)
    }

    /**
     * 用于监听SeekBar停止拖动  SeekBar停止拖动后的事件
     */
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        val progress = seekBar!!.progress
        Log.i(TAG, "onStopTrackingTouch $progress")
        // 得到该首歌曲最长秒数
        val musicMax: Int = player.getDuration()
        // SeekBar最大值
        val seekBarMax = seekBar.max
        //计算相对当前播放器歌曲的应播放时间
        val msec: Double = progress / (seekBarMax * 1.0) * musicMax
        // 跳到该曲该秒
        player.seekTo(msec.toInt())
        mHandler.sendEmptyMessageDelayed(MSG_CODE, MSG_TIME)
    }


    private var mOnCompletionListener: OnCompletionListener? = null

    /**
     * Register a callback to be invoked when the end of a media source
     * has been reached during playback.
     *
     * @param listener the callback that will be run
     */
    fun setOnCompletionListener(listener: OnCompletionListener) {
        mOnCompletionListener = listener
    }

    /**
     * Interface definition for a callback to be invoked when playback of
     * a media source has completed.
     */
    interface OnCompletionListener {
        /**
         * Called when the end of a media source is reached during playback.
         *
         * @param mp the MediaPlayer that reached the end of the file
         */
        fun onCompletion(mp: MediaPlayer?)
    }


}