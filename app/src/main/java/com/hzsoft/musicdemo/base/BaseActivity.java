package com.hzsoft.musicdemo.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.musicdemo.R;
import com.hzsoft.musicdemo.view.LoadingInitView;
import com.hzsoft.musicdemo.view.NoDataView;

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/11/20
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static String TAG = BaseActivity.class.getSimpleName();
    public Context mContext;

    protected TextView mTxtTitle;
    protected TextView tvToolbarRight;
    protected ImageView ivToolbarRight;
    protected Toolbar mToolbar;

    protected NoDataView mNoDataView;
    protected LoadingInitView mLoadingInitView;

    private ViewStub mViewStubToolbar;
    private ViewStub mViewStubContent;
    private ViewStub mViewStubInitLoading;
    private ViewStub mViewStubNoData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_root);
        initCommonView();
        initView();
        initListener();
        initData();
    }

    protected void initCommonView() {
        mContext = this;

        mViewStubToolbar = findViewById(R.id.view_stub_toolbar);
        mViewStubContent = findViewById(R.id.view_stub_content);
        mViewStubInitLoading = findViewById(R.id.view_stub_init_loading);
        mViewStubNoData = findViewById(R.id.view_stub_nodata);

        if (enableToolbar()) {
            mViewStubToolbar.setLayoutResource(onBindToolbarLayout());
            View view = mViewStubToolbar.inflate();
            initToolbar(view);
        }
        mViewStubContent.setLayoutResource(onBindLayout());
        mViewStubContent.inflate();
    }

    protected void initToolbar(View view) {
        mToolbar = view.findViewById(R.id.toolbar_root);
        mTxtTitle = view.findViewById(R.id.toolbar_title);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            //是否显示标题
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            if (enableToolBarLeft()) {
                //设置是否添加显示NavigationIcon.如果要用
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                //设置NavigationIcon的icon.可以是Drawable ,也可以是ResId
                mToolbar.setNavigationIcon(getToolBarLeftIcon());
                mToolbar.setNavigationOnClickListener(v -> onBackPressed());
            }
            //当标题栏右边的文字不为空时进行填充文字信息
            if (tvToolbarRight != null && !TextUtils.isEmpty(getToolBarRightTxt())) {
                tvToolbarRight.setText(getToolBarRightTxt());
                tvToolbarRight.setVisibility(View.VISIBLE);
                tvToolbarRight.setOnClickListener(getToolBarRightTxtClick());
            }
            //当标题右边的图标不为 默认0时进行填充图标
            if (ivToolbarRight != null && getToolBarRightImg() != 0) {
                ivToolbarRight.setImageResource(getToolBarRightImg());
                ivToolbarRight.setVisibility(View.VISIBLE);
                ivToolbarRight.setOnClickListener(getToolBarRightImgClick());
            }

        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mTxtTitle != null && !TextUtils.isEmpty(title)) {
            mTxtTitle.setText(title);
        }
        //可以再次覆盖设置title
        String tootBarTitle = getTootBarTitle();
        if (mTxtTitle != null && !TextUtils.isEmpty(tootBarTitle)) {
            mTxtTitle.setText(tootBarTitle);
        }
    }

    public String getTootBarTitle() {
        return "";
    }

    /**
     * 设置返回按钮的图样，可以是Drawable ,也可以是ResId
     * 注：仅在 enableToolBarLeft 返回为 true 时候有效
     *
     * @return
     */
    public int getToolBarLeftIcon() {
        return R.drawable.ic_white_black_45dp;
    }

    /**
     * 是否打开返回
     *
     * @return
     */
    public boolean enableToolBarLeft() {
        return false;
    }

    /**
     * 设置标题右边显示文字
     *
     * @return
     */
    public String getToolBarRightTxt() {
        return "";
    }

    /**
     * 设置标题右边显示 Icon
     *
     * @return int resId 类型
     */
    public int getToolBarRightImg() {
        return 0;
    }

    /**
     * 右边文字监听回调
     *
     * @return
     */
    public View.OnClickListener getToolBarRightTxtClick() {
        return null;
    }

    /**
     * 右边图标监听回调
     *
     * @return
     */
    public View.OnClickListener getToolBarRightImgClick() {
        return null;
    }


    /**
     * 是否打开状态栏
     *
     * @return
     */
    public boolean enableToolbar() {
        return true;
    }


    /**
     * 绑定状态栏
     *
     * @return
     */
    public int onBindToolbarLayout() {
        return R.layout.common_toolbar;
    }

    /**
     * 绑定主页
     *
     * @return
     */
    public abstract int onBindLayout();

    /**
     * 初始化页面部分组件绘制内容
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化监听
     */
    public void initListener() {
    }

    public void finishActivity() {
        finish();
    }

    /**
     * toast
     *
     * @param msg msg
     */
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    }


    /**
     * 展示没有数据
     */
    public void showNoDataView() {
        showNoDataView(true);
    }

    /**
     * 展示自定义布局没有数据文件
     */
    public void showNoDataView(int resid) {
        showNoDataView(true, resid);
    }

    /**
     * 隐藏没有数据
     */
    public void hideNoDataView() {
        showNoDataView(false);
    }


    /**
     * 打开菊花
     */
    public void showInitLoadView() {
        showInitLoadView(true);
    }

    /**
     * 隐藏菊花
     */
    public void hideInitLoadView() {
        showInitLoadView(false);
    }

    /**
     * 展示没有数据页面
     *
     * @param show
     */
    public void showNoDataView(boolean show) {
        if (mNoDataView == null) {
            View view = mViewStubNoData.inflate();
            mNoDataView = view.findViewById(R.id.view_no_data);
        }
        mNoDataView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showNoDataView(Boolean show, int resid) {
        showNoDataView(show);
        if (show) {
            mNoDataView.setNoDataView(resid);
        }
    }


    /**
     * 是否展示正在加载窗口
     *
     * @param show
     */
    public void showInitLoadView(boolean show) {
        if (mLoadingInitView == null) {
            View view = mViewStubInitLoading.inflate();
            mLoadingInitView = view.findViewById(R.id.view_init_loading);
        }
        mLoadingInitView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoadingInitView.loading(show);
    }

}
