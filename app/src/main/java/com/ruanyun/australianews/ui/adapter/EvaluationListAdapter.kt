package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.refreshview.impl.RvMuiltItemAdapter
import com.ruanyun.australianews.ext.*
import com.ruanyun.australianews.model.Evaluation
import com.ruanyun.australianews.util.WebViewUrlUtil
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder


/**
 * @description 我的评论列表
 * @author hdl
 * @date 2019/5/5
 */
class EvaluationListAdapter(context: Context, datas: List<Evaluation>) : RvMuiltItemAdapter<Evaluation>(context, datas) {

    var isEditorMode = false

    init {
        /**
         * 我的评论
         */
        addItemViewDelegate(object : ItemViewDelegate<Evaluation> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_my_evaluation
            }

            override fun isForViewType(item: Evaluation, position: Int): Boolean {
                return true
            }

            override fun convert(holder: ViewHolder, item: Evaluation, position: Int) {
                holder.getView<ImageView>(R.id.iv_user_photo).loadCircleImage(item.headImageAttaOid.toImgUrl())
                holder.setText(R.id.tv_user_name, item.name)
//                holder.setText(R.id.tv_time, StringUtil.getNewsTime(item.createTime))
                holder.setText(R.id.tv_time, item.createTime)
                holder.setText(R.id.tv_content, item.replyMsg)

                val ivSelect = holder.getView<ImageView>(R.id.iv_select)
                ivSelect.isSelected = item.isSelect
                holder.convertView.click {
                    if(isEditorMode) {
                        item.isSelect = !item.isSelect
                        ivSelect.isSelected = item.isSelect
                    }
                }
                holder.setOnClickListener(R.id.rl_news_info){
                    if(isEditorMode) {
                        item.isSelect = !item.isSelect
                        ivSelect.isSelected = item.isSelect
                    }else {
                        WebViewUrlUtil.showNewsDetailsWeb(mContext, item.newsInfo)
                    }
                }
                ivSelect.visibility = if(isEditorMode) View.VISIBLE else View.GONE
                holder.setText(R.id.tv_news_title, item.newsInfo?.title)
                holder.getView<ImageView>(R.id.iv_news_photo).loadImage(item.newsInfo?.mainPhoto?.toImgUrl())
                holder.setVisible(R.id.iv_start_play, item.newsInfo?.type == 2)
            }
        })

    }

    override fun refresh(datas: MutableList<Evaluation>) {
        mDatas = datas
        notifyDataSetChanged()
    }

    override fun loadMore(datas: MutableList<Evaluation>) {
        mDatas.addAll(datas)
        notifyDataSetChanged()
    }

}
