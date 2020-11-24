package com.hzsoft.musicdemo.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hzsoft.musicdemo.R
import com.hzsoft.musicdemo.view.LoadingInitView
import com.hzsoft.musicdemo.view.NoDataView

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/11/19
 */
abstract class BaseActivity : AppCompatActivity() {
    var TAG: String = BaseActivity::class.java.simpleName
    lateinit var mContext: Context;

    protected lateinit var mTxtTitle: TextView
    protected lateinit var tvToolbarRight: TextView
    protected lateinit var ivToolbarRight: ImageView
    protected lateinit var mToolbar: Toolbar

    protected var mNoDataView: NoDataView? = null
    protected var mLoadingInitView: LoadingInitView? = null

    private lateinit var mViewStubToolbar: ViewStub
    private lateinit var mViewStubContent: ViewStub
    private lateinit var mViewStubInitLoading: ViewStub
    private lateinit var mViewStubNoData: ViewStub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_root)
        initCommonView();
        initView()
        initListener()
        initData()
    }

    protected open fun initCommonView() {
        mContext = this

        mViewStubToolbar = findViewById(R.id.view_stub_toolbar)
        mViewStubContent = findViewById(R.id.view_stub_content)
        mViewStubInitLoading = findViewById(R.id.view_stub_init_loading)
        mViewStubNoData = findViewById(R.id.view_stub_nodata)

        if (enableToolbar()) {
            mViewStubToolbar.layoutResource = onBindToolbarLayout()
            val view = mViewStubToolbar.inflate()
            initToolbar(view)
        }
        mViewStubContent.layoutResource = onBindLayout()
        mViewStubContent.inflate()
    }

    protected fun initToolbar(view: View) {
        mToolbar = view.findViewById(R.id.toolbar_root)
        mTxtTitle = view.findViewById(R.id.toolbar_title)
        tvToolbarRight = view.findViewById(R.id.tv_toolbar_right)
        ivToolbarRight = view.findViewById(R.id.iv_toolbar_right)
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
            //是否显示标题
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            mToolbar.setNavigationOnClickListener { onBackPressed() }

            if (enableToolBarLeft()) {
                //设置是否添加显示NavigationIcon.如果要用
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                //设置NavigationIcon的icon.可以是Drawable ,也可以是ResId
                mToolbar.setNavigationIcon(getToolBarLeftIcon())
                mToolbar.setNavigationOnClickListener { onBackPressed() }
            }
            //当标题栏右边的文字不为空时进行填充文字信息
            if (tvToolbarRight != null && !TextUtils.isEmpty(getToolBarRightTxt())) {
                tvToolbarRight.setText(getToolBarRightTxt())
                tvToolbarRight.visibility = View.VISIBLE
                tvToolbarRight.setOnClickListener(getToolBarRightTxtClick())
            }
            //当标题右边的图标不为 默认0时进行填充图标
            if (ivToolbarRight != null && getToolBarRightImg() != 0) {
                ivToolbarRight.setImageResource(getToolBarRightImg())
                ivToolbarRight.visibility = View.VISIBLE
                ivToolbarRight.setOnClickListener(getToolBarRightImgClick())
            }
        }
    }

    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        if (mTxtTitle != null && !TextUtils.isEmpty(title)) {
            mTxtTitle.text = title
        }
        //可以再次覆盖设置title
        val tootBarTitle = getTootBarTitle()
        if (mTxtTitle != null && !TextUtils.isEmpty(tootBarTitle)) {
            mTxtTitle.text = tootBarTitle
        }
    }

    open fun getTootBarTitle(): String {
        return ""
    }

    /**
     * 设置返回按钮的图样，可以是Drawable ,也可以是ResId
     * 注：仅在 enableToolBarLeft 返回为 true 时候有效
     *
     * @return
     */
    open fun getToolBarLeftIcon(): Int {
        return R.drawable.ic_white_black_45dp
    }

    /**
     * 是否打开返回
     *
     * @return
     */
    open fun enableToolBarLeft(): Boolean {
        return false
    }

    /**
     * 设置标题右边显示文字
     *
     * @return
     */
    open fun getToolBarRightTxt(): String {
        return ""
    }

    /**
     * 设置标题右边显示 Icon
     *
     * @return int resId 类型
     */
    open fun getToolBarRightImg(): Int {
        return 0
    }

    /**
     * 右边文字监听回调
     * @return
     */
    open fun getToolBarRightTxtClick(): View.OnClickListener? {
        return null
    }

    /**
     * 右边图标监听回调
     * @return
     */
    open fun getToolBarRightImgClick(): View.OnClickListener? {
        return null
    }

    /**
     * 是否开启头部栏
     */
    open fun enableToolbar(): Boolean {
        return true
    }

    open fun onBindToolbarLayout(): Int {
        return R.layout.common_toolbar
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected abstract fun onBindLayout(): Int

    protected abstract fun initView()
    protected abstract fun initData()

    open fun initListener() {}

    fun finishActivity() {
        finish()
    }

    /**
     * 展示吐司
     */
    fun showToast(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }


    /**
     * 展示没有数据
     */
    fun showNoDataView() {
        showNoDataView(true)
    }

    /**
     * 展示自定义布局没有数据文件
     */
    fun showNoDataView(resid: Int) {
        showNoDataView(true, resid)
    }

    /**
     * 隐藏没有数据
     */
    fun hideNoDataView() {
        showNoDataView(false)
    }

    /**
     * 打开菊花
     */
    fun showInitLoadView() {
        showInitLoadView(true)
    }

    /**
     * 隐藏菊花
     */
    fun hideInitLoadView() {
        showInitLoadView(false)
    }

    private fun showNoDataView(show: Boolean) {
        if (mNoDataView == null) {
            val view = mViewStubNoData.inflate()
            mNoDataView = view.findViewById(R.id.view_no_data)
        }
        mNoDataView?.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showNoDataView(show: Boolean, resid: Int) {
        showNoDataView(show)
        if (show) {
            mNoDataView?.setNoDataView(resid)
        }
    }

    private fun showInitLoadView(show: Boolean) {
        if (mLoadingInitView == null) {
            val view = mViewStubInitLoading.inflate()
            mLoadingInitView = view.findViewById(R.id.view_init_loading)
        }
        mLoadingInitView?.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingInitView?.loading(show)
    }
}