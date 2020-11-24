package com.hzsoft.musicdemo.view

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.hzsoft.musicdemo.R


/**
 * <p>NoDataView 没有数据页面</p>
 *
 * @author zhouhuan
 * @Data 2020/11/19
 */
class NoDataView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private val mRlNoDataRoot: RelativeLayout
    private val mImgNoDataView: ImageView

    init {
        View.inflate(context, R.layout.view_no_data, this)
        mRlNoDataRoot = findViewById(R.id.rl_no_data_root)
        mImgNoDataView = findViewById(R.id.img_no_data)
    }

    fun setNoDataBackground(@ColorRes colorResId: Int) {
        mRlNoDataRoot.setBackgroundColor(resources.getColor(colorResId))
    }

    fun setNoDataView(@DrawableRes imgResId: Int) {
        mImgNoDataView.setImageResource(imgResId)
    }
}