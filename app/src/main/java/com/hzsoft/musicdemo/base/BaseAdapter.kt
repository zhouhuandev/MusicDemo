package com.hzsoft.musicdemo.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

/**
 * <p>BaseAdapter</p>
 *
 * @author zhouhuan
 * @Data 2020/11/19
 */
abstract class BaseAdapter<E, VH : androidx.recyclerview.widget.RecyclerView.ViewHolder>(protected var mContext: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<VH>() {
    protected var mList: MutableList<E>
    protected var mItemClickListener: OnItemClickListener<E>? = null
    protected var mOnItemLongClickListener: OnItemLongClickListener<E>? = null

    val dataList: List<E>
        get() = mList

    val listData: List<E>
        get() = mList

    init {
        mList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutid = onBindLayout()
        val view = LayoutInflater.from(mContext).inflate(layoutid, parent, false)
        return onCreateHolder(view)
    }

    //绑定布局文件
    protected abstract fun onBindLayout(): Int

    //创建一个holder
    protected abstract fun onCreateHolder(view: View): VH

    //绑定数据
    protected abstract fun onBindData(holder: VH, e: E, positon: Int)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = mList[position]
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(e, position) }
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener { mOnItemLongClickListener!!.onItemLongClick(e, position) }
        }
        onBindData(holder, e, position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun addAll(list: List<E>?) {
        if (list != null && list.size > 0) {
            mList.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun refresh(list: List<E>?) {
        mList.clear()
        if (list != null && list.size > 0) {
            mList.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun remove(positon: Int) {
        mList.removeAt(positon)
        notifyDataSetChanged()
    }

    fun remove(e: E) {
        mList.remove(e)
        notifyDataSetChanged()
    }

    fun add(e: E) {
        mList.add(e)
        notifyDataSetChanged()
    }

    fun addLast(e: E) {
        add(e)
    }

    fun addFirst(e: E) {
        mList.add(0, e)
        notifyDataSetChanged()
    }

    fun clear() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener<E>) {
        mItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<E>) {
        mOnItemLongClickListener = onItemLongClickListener
    }

    interface OnItemClickListener<E> {
        fun onItemClick(e: E, position: Int)
    }

    interface OnItemLongClickListener<E> {
        fun onItemLongClick(e: E, postion: Int): Boolean
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}
