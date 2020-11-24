package com.hzsoft.musicdemo

import android.Manifest
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzsoft.musicdemo.adapter.SongAdapter
import com.hzsoft.musicdemo.base.BaseActivity
import com.hzsoft.musicdemo.base.BaseAdapter
import com.hzsoft.musicdemo.model.SongModel
import com.hzsoft.musicdemo.utils.ScanMusicUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

/**
 * <p>播放器的主页</p>
 *
 * @author zhouhuan
 * @Data 2020/11/19
 */
class MainActivity : BaseActivity() {

    lateinit var mAdapter: SongAdapter
    lateinit var helper: MusicPlayerHelper

    /**
     * 歌曲数据源
     */
    var songsList: MutableList<SongModel> = ArrayList()

    /**
     * 当前播放歌曲游标位置
     */
    var mPosition: Int = 0;

    /**
     * 绑定布局文件
     */
    override fun onBindLayout(): Int = R.layout.activity_main

    /**
     * 设置页面标题
     */
    override fun getTootBarTitle(): String = "音乐播放器"

    override fun getToolBarRightImg(): Int = R.drawable.ic_baseline_autorenew_24

    /**
     * 点击右上角刷新数据
     */
    override fun getToolBarRightImgClick(): View.OnClickListener? {
        return View.OnClickListener {
            startAnimation(it)
            initData()
        }
    }

    /**
     * 初始化页面组件
     */
    override fun initView() {
        // Init 播放 Helper
        helper = MusicPlayerHelper(seekbar, tvSongName)
        helper.setOnCompletionListener(object : MusicPlayerHelper.OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {
                Log.e(TAG, "next()")
                //下一曲
                next()
            }
        })
        // Init Adapter
        mAdapter = SongAdapter(mContext)
        //添加数据源
        mAdapter.addAll(songsList)
        // RecyclerView 增加适配器
        mRecyclerView.adapter = mAdapter
        // RecyclerView 增加布局管理器
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        //增加渲染特效
        mRecyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(this,R.anim.layout_anim_item_right_slipe)
        // 需要重新启动布局时调用此方法
        mRecyclerView.scheduleLayoutAnimation()
        // Adapter 增加 Item 监听
        mAdapter.setItemClickListener(object : BaseAdapter.OnItemClickListener<SongModel> {
            override fun onItemClick(e: SongModel, position: Int) {
                mPosition = position
                //播放歌曲
                play(e, true)
            }
        })

    }

    /**
     * 设置监听
     */
    override fun initListener() {
        btnStar.setOnClickListener(this::onClick)
        btnStop.setOnClickListener(this::onClick)
        btnLast.setOnClickListener(this::onClick)
        btnNext.setOnClickListener(this::onClick)
    }

    /**
     * 初始化数据局
     */
    @SuppressLint("CheckResult")
    override fun initData() {
        // 请求读写权限
        val rxPermissions: RxPermissions = RxPermissions(this)
        rxPermissions.request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe { aBoolean ->
            if (!aBoolean) {
                showToast("缺少存储权限，将会导致部分功能无法使用")
            } else {
                showInitLoadView()
                val musicData = ScanMusicUtils.getMusicData(mContext)
                if (musicData.isNotEmpty()) {
                    hideNoDataView()
                    songsList.addAll(musicData)
                    mAdapter.refresh(songsList)
                } else {
                    showNoDataView()
                }
                hideInitLoadView()
            }
        }
    }

    /**
     * 处理点击事件
     */
    fun onClick(v: View) {
        when (v.id) {
            // 上一曲
            R.id.btnLast -> last()
            // 播放/暂停
            R.id.btnStar -> play(songsList.get(mPosition), false)
            // 停止
            R.id.btnStop -> stop()
            // 下一曲
            R.id.btnNext -> next()
        }
    }

    /**
     * 播放歌曲
     * @param songModel 播放源
     * @param isRestPlayer true 切换歌曲 false 不切换
     */
    fun play(songModel: SongModel, isRestPlayer: Boolean) {
        if (!TextUtils.isEmpty(songModel.path)) {
            Log.e(TAG, "当前状态：${helper.isPlaying()}  是否切换歌曲：$isRestPlayer")
            // 当前若是播放，则进行暂停
            if (!isRestPlayer && helper.isPlaying()) {
                btnStar.text = getText(R.string.btn_play)
                pause()
            } else {
                //进行切换歌曲播放
                songModel.let {
                    helper.playBySongModel(
                        it,
                        isRestPlayer
                    )
                }
                btnStar.text = getText(R.string.btn_pause)
                // 正在播放的列表进行更新哪一首歌曲正在播放 主要是为了更新列表里面的显示
                for (index in 0 until songsList.size) {
                    songsList.get(index).isPlaying = mPosition == index
                }
                mAdapter.notifyDataSetChanged()
            }
        } else {
            showToast("当前的播放地址无效")
        }
    }

    /**
     * 上一首
     */
    fun last() {
        mPosition--
        //如果上一曲小于0则取最后一首
        if (mPosition < 0) {
            mPosition = songsList.size - 1
        }
        play(songsList.get(mPosition), true)
    }

    /**
     * 下一首
     */
    fun next() {
        mPosition++
        //如果下一曲大于歌曲数量则取第一首
        if (mPosition >= songsList.size) {
            mPosition = 0
        }
        play(songsList.get(mPosition), true)
    }

    /**
     * 暂停播放
     */
    var pause = { helper.pause() }

    /**
     * 停止播放
     */
    var stop = {
        btnStar.text = getText(R.string.btn_star)
        helper.stop()
    }

    /**
     * 开启动画360度旋转特效
     */
    fun startAnimation(v: View) {
        val loadAnimation =
            AnimationUtils.loadAnimation(mContext, R.anim.ic_baseline_autorenew_24_rotate)
        // 设置速度器 LinearInterpolator是匀速加速器
        loadAnimation.interpolator = LinearInterpolator()
        // 设置动画时长，以毫秒为单位
        loadAnimation.duration = 1_000 * 1
        // 参数为true时，动画播放完后，view会维持在最终的状态。而默认值是false，也就是动画播放完后，view会恢复原来的状态
        loadAnimation.fillAfter = false
        v.startAnimation(loadAnimation)
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.destroy()
    }

}