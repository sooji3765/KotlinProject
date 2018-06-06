package com.example.user.myapplication

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.user.myapplication.model.Memo
import kotlinx.android.synthetic.main.list_layout.view.*

class MemoRecycleAdapter(val contetext :Context, val memoData : ArrayList<Memo>, val itemClick : (Memo)->Unit) : RecyclerView.Adapter<MemoRecycleAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(memoData[position],contetext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(contetext).inflate(R.layout.list_layout,parent,false)
        return Holder(view)
    }


    override fun getItemCount(): Int {
        return memoData.size
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val memoNum = itemView?.memoNum
        val memoContent = itemView?.memoContent
        val memoDate = itemView?.memoDate
        /* 한 줄에 해당하는 Holder를 만들고, findViewById로 각각의 View 참조를 한다. */

        fun bind (memo: Memo, context: Context) {

            memoNum?.text = memo.num
            memoContent?.text = memo.content
            memoDate?.text = memo.date
            /* bind 함수는 ViewHolder와 실제 데이터를 연동하는 역할을 한다. onBindViewHolder에서 사용. */

            itemView.setOnClickListener{itemClick(memo)}
        }
    }
}