package com.ruanyun.imagepicker.widget;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ruanyun.imagepicker.AndroidImagePicker;
import com.ruanyun.imagepicker.R;
import com.ruanyun.imagepicker.base.CommonAdapter;
import com.ruanyun.imagepicker.base.CommonViewHolder;
import com.ruanyun.imagepicker.bean.ImageItem;

import java.util.List;

public class ImagePickerAdapter extends CommonAdapter<ImageItem> {
    private OnImageChangeListener listener;
    private int sizeLimit = 9;
    private int bindId;
    private ImageItem addItem;

    public ImagePickerAdapter(Context context, List<ImageItem> datas, int layoutId, int sizeLimit, ImageItem addItem) {
        this(context, datas, layoutId);
        this.sizeLimit = sizeLimit;
        this.addItem = addItem;
    }

    public ImagePickerAdapter(Context context, List<ImageItem> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final CommonViewHolder holder, final ImageItem imageItem) {
        ImageView ivPicker = holder.getView(R.id.iv_picker);
        ImageButton ivBtnDelete = holder.getView(R.id.iv_btn_delete);

        if (!imageItem.isAdd) {
            ivPicker.setVisibility(View.VISIBLE);
            ivPicker.setOnClickListener(null);
            Glide.with(mContext).load(imageItem.path).into(ivPicker);
            ivBtnDelete.setVisibility(View.VISIBLE);
            ivBtnDelete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mDatas.remove(imageItem);
                    int limit = AndroidImagePicker.getInstance().getSelectLimit();
                    AndroidImagePicker.getInstance().deleteSelectedImageItem(holder.getmPosition(), imageItem);
                    AndroidImagePicker.getInstance().setSelectLimit(limit + 1);
                    sortListData(addItem);
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.onDelete(imageItem);
                    }
                }
            });
        } else {
            ivBtnDelete.setOnClickListener(null);
            ivBtnDelete.setVisibility(View.GONE);
            ivPicker.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(imageItem.resId).into(ivPicker);
            ivPicker.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAddBtnClick(bindId, holder.getmPosition());
                    }
                }
            });
        }
    }

    public void sortListData(ImageItem item) {
        if (addItem == null) {
            return;
        }
        if (mDatas.size() == 0) {
            return;
        } else {
            mDatas.remove(addItem);
            if (mDatas.size() < sizeLimit) {
                mDatas.add(addItem);
            } else {

            }
        }
    }

    public void setOnImageChangeListener(OnImageChangeListener clickListener) {
        this.listener = clickListener;
    }

    public void setBindId(int bindId) {
        this.bindId = bindId;
    }

    public int getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public void setAddItem(ImageItem addItem) {
        mDatas.remove(this.addItem);
        this.addItem = addItem;
        if (addItem != null) {
            mDatas.add(addItem);
        }
    }

    public interface OnImageChangeListener {
        void onAddBtnClick(int bindId, int posiont);

        void onDelete(ImageItem item);
    }
}
