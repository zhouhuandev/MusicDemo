package com.hzsoft.musicdemo;

import android.Manifest;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hzsoft.musicdemo.adapter.SongAdapter;
import com.hzsoft.musicdemo.base.BaseActivity;
import com.hzsoft.musicdemo.model.SongModel;
import com.hzsoft.musicdemo.utils.ScanMusicUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * <p>播放器的主页</p>
 *
 * @author zhouhuan
 * @Date 2020/11/20
 */
public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private SeekBar seekbar;
    private TextView tvSongName;
    private Button btnLast;
    private Button btnStar;
    private Button btnStop;
    private Button btnNext;

    private SongAdapter mAdapter;
    private MusicPlayerHelper helper;
    /**
     * 歌曲数据源
     */
    private List<SongModel> songsList = new ArrayList<>();
    /**
     * 当前播放歌曲游标位置
     */
    private int mPosition = 0;


    /**
     * 设置页面标题
     */
    @Override
    public String getTootBarTitle() {
        return "音乐播放器";
    }

    @Override
    public int getToolBarRightImg() {
        return R.drawable.ic_baseline_autorenew_24;
    }

    /**
     * 点击右上角刷新数据
     */
    @Override
    public View.OnClickListener getToolBarRightImgClick() {
        return v -> {
            startAnimation(v);
            initData();
        };
    }

    /**
     * 绑定布局文件
     */
    @Override
    public int onBindLayout() {
        return R.layout.activity_main;
    }

    /**
     * 初始化页面组件
     */
    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        tvSongName = (TextView) findViewById(R.id.tvSongName);
        btnLast = (Button) findViewById(R.id.btnLast);
        btnStar = (Button) findViewById(R.id.btnStar);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnNext = (Button) findViewById(R.id.btnNext);

        // Init 播放 Helper
        helper = new MusicPlayerHelper(seekbar, tvSongName);
        helper.setOnCompletionListener(mp -> {
            Log.e(TAG, "next()");
            //下一曲
            next();
        });
        // Init Adapter
        mAdapter = new SongAdapter(mContext);
        //添加数据源
        mAdapter.addAll(songsList);
        // RecyclerView 增加适配器
        mRecyclerView.setAdapter(mAdapter);
        // RecyclerView 增加布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //增加渲染特效
        mRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_item_right_slipe));
        // 需要重新启动布局时调用此方法
        mRecyclerView.scheduleLayoutAnimation();
        // Adapter 增加 Item 监听
        mAdapter.setItemClickListener((object, position) -> {
            mPosition = position;
            //播放歌曲
            play((SongModel) object, true);
        });
    }

    /**
     * 设置监听
     */
    @Override
    public void initListener() {
        btnStar.setOnClickListener(this::onClick);
        btnStop.setOnClickListener(this::onClick);
        btnLast.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this::onClick);
    }

    /**
     * 初始化数据局
     */
    @Override
    public void initData() {
        // 请求读写权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(aBoolean -> {
            if (!aBoolean) {
                showToast("缺少存储权限，将会导致部分功能无法使用");
            } else {
                showInitLoadView();
                List<SongModel> musicData = ScanMusicUtils.getMusicData(mContext);
                if (!musicData.isEmpty()) {
                    hideNoDataView();
                    songsList.addAll(musicData);
                    mAdapter.refresh(songsList);
                } else {
                    showNoDataView();
                }
                hideInitLoadView();
            }
        });
    }


    /**
     * 处理点击事件
     */
    private void onClick(View v) {
        switch (v.getId()) {
            // 上一曲
            case R.id.btnLast:
                last();
                break;
            // 播放/暂停
            case R.id.btnStar:
                play(songsList.get(mPosition), false);
                break;
            // 停止
            case R.id.btnStop:
                stop();
                break;
            // 下一曲
            case R.id.btnNext:
                next();
                break;
            default:
                break;
        }
    }

    /**
     * 播放歌曲
     *
     * @param songModel    播放源
     * @param isRestPlayer true 切换歌曲 false 不切换
     */
    private void play(SongModel songModel, Boolean isRestPlayer) {
        if (!TextUtils.isEmpty(songModel.getPath())) {
            Log.e(TAG, String.format("当前状态：%s  是否切换歌曲：%s", helper.isPlaying(), isRestPlayer));
            // 当前若是播放，则进行暂停
            if (!isRestPlayer && helper.isPlaying()) {
                btnStar.setText(R.string.btn_play);
                pause();
            } else {
                //进行切换歌曲播放
                helper.playBySongModel(songModel, isRestPlayer);
                btnStar.setText(R.string.btn_pause);
                // 正在播放的列表进行更新哪一首歌曲正在播放 主要是为了更新列表里面的显示
                for (int i = 0; i < songsList.size(); i++) {
                    songsList.get(i).setPlaying(mPosition == i);
                }
                mAdapter.notifyDataSetChanged();
            }
        } else {
            showToast("当前的播放地址无效");
        }
    }


    /**
     * 上一首
     */
    private void last() {
        mPosition--;
        //如果上一曲小于0则取最后一首
        if (mPosition < 0) {
            mPosition = songsList.size() - 1;
        }
        play(songsList.get(mPosition), true);
    }

    /**
     * 下一首
     */
    private void next() {
        mPosition++;
        //如果下一曲大于歌曲数量则取第一首
        if (mPosition >= songsList.size()) {
            mPosition = 0;
        }
        play(songsList.get(mPosition), true);
    }

    /**
     * 暂停播放
     */
    private void pause() {
        helper.pause();
    }

    /**
     * 停止播放
     */
    private void stop() {
        btnStar.setText(R.string.btn_star);
        helper.stop();
    }

    /**
     * 开启动画360度旋转特效
     */
    private void startAnimation(View v) {
        Animation loadAnimation =
                AnimationUtils.loadAnimation(mContext, R.anim.ic_baseline_autorenew_24_rotate);
        // 设置速度器 LinearInterpolator是匀速加速器
        loadAnimation.setInterpolator(new LinearInterpolator());
        // 设置动画时长，以毫秒为单位
        loadAnimation.setDuration(1_000);
        // 参数为true时，动画播放完后，view会维持在最终的状态。而默认值是false，也就是动画播放完后，view会恢复原来的状态
        loadAnimation.setFillAfter(false);
        v.startAnimation(loadAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.destroy();
    }
}
