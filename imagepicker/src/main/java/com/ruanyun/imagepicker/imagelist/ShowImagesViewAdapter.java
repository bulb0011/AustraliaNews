package com.ruanyun.imagepicker.imagelist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ruanyun.imagepicker.R;
import com.ruanyun.imagepicker.imagelist.photoview.EasePhotoView;

import java.util.ArrayList;


/**
 * Description:
 * author: zhangsan on 16/12/28 下午2:38.
 */
public class ShowImagesViewAdapter extends PagerAdapter {

    ArrayList<String> mDatas;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public ShowImagesViewAdapter(Context context, ArrayList<String> data) {
        layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        mDatas = data;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(View container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View contentView = null;
        // final ImageHolder holder;

        contentView = layoutInflater.inflate(R.layout.item_show_images, container, false);
        EasePhotoView photoView = (EasePhotoView) contentView.findViewById(R.id.img_content);
        final ProgressBar progressBar = (ProgressBar) contentView.findViewById(R.id.progressbar);
        //AutoUtils.auto(contentView);
        //holder=new ImageHolder();
        //holder.photoView= (EasePhotoView) contentView.findViewById(R.id.img_content);
        //holder.progressBar= (ProgressBar) contentView.findViewById(R.id.progressbar);
        //contentView.setTag(holder);
       /* }else {
            holder= (ImageHolder) contentView.getTag();
        }*/
//        Glide.with(mContext)
//                .load(mDatas.get(position))
//                .error(R.drawable.default_img)
//                .into(new GlideDrawableImageViewTarget(photoView) {
//                    @Override
//                    public void onLoadStarted(Drawable placeholder) {
//                        progressBar.setVisibility(View.VISIBLE);
//                        super.onLoadStarted(placeholder);
//                        //holder.progressBar.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                        progressBar.setVisibility(View.GONE);
//                        super.onResourceReady(resource, animation);
//                        // holder.progressBar.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        progressBar.setVisibility(View.GONE);
//                        // holder.progressBar.setVisibility(View.GONE);
//                        super.onLoadFailed(e, errorDrawable);
//                    }
//                });

        Glide.with(mContext)
                .load(mDatas.get(position))
                .apply(new RequestOptions()
                        .error(R.drawable.default_img))
                .into(new DrawableImageViewTarget(photoView) {

                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        progressBar.setVisibility(View.GONE);
                        super.onResourceReady(resource, transition);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        progressBar.setVisibility(View.VISIBLE);
                        super.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        progressBar.setVisibility(View.GONE);
                        super.onLoadFailed(errorDrawable);
                    }
                });

        container.addView(contentView);
        return contentView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /*static class ImageHolder {
        public EasePhotoView photoView;
        public ProgressBar progressBar;
    }*/
}
