package com.ruanyun.imagepicker.imagelist;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruanyun.imagepicker.R;
import com.ruanyun.imagepicker.base.NoDoubleClicksListener;

import java.util.ArrayList;


/**
 * Description:
 * author: zhangsan on 16/12/28 下午2:29.
 */
public class ShowImagesActivity extends AppCompatActivity {
    public static final int TYPE_STRINGS = 321;//传入值是String
    public static final int TYPE_GETTERS = 1233;//传入值是ImageUrlGetter;
    static final String INDICATOR_STR = "%s/%s";
    private ArrayList<ImageUrlGetter> imageUrlGetters;
    private ArrayList<String> urlStrings;
    private ViewPagerFixed viewpager;

    private ImageView imgBack;

    private TextView tvIndicator;
    private ShowImagesViewAdapter adapter;
    private int dataType;
    private int selectPosion;//上个页面选中位置

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_show_images);

        //就算是沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        initView();
    }

    private void initView() {
        dataType = getIntent().getIntExtra(ImageListUtil.SHOW_IMAGES_DATAS_TYPES, -1);
        selectPosion = getIntent().getIntExtra(ImageListUtil.SHOW_IMAGES_SELECT_POSTION, 0);
        viewpager = getView(R.id.viewpager);
        imgBack = getView(R.id.img_back);
        tvIndicator = getView(R.id.tv_indicator);
        imgBack.setOnClickListener(new NoDoubleClicksListener() {
            @Override
            public void noDoubleClick(View v) {
                finish();
            }
        });

        switch (dataType) {
            case TYPE_GETTERS:
                imageUrlGetters = getIntent().getParcelableArrayListExtra(ImageListUtil.IMAGE_URLS);
                if (imageUrlGetters != null) {
                    urlStrings = getImageUrls();
                    adapter = new ShowImagesViewAdapter(ShowImagesActivity.this, urlStrings);
                    viewpager.setAdapter(adapter);
                }
                break;
            case TYPE_STRINGS:
                urlStrings = getIntent().getStringArrayListExtra(ImageListUtil.IMAGE_URLS);
                if (urlStrings != null) {
                    adapter = new ShowImagesViewAdapter(ShowImagesActivity.this, urlStrings);
                    viewpager.setAdapter(adapter);
                }
                break;

        }
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPosion = position;
                setIndicatorStr(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(selectPosion);
        setIndicatorStr(selectPosion);
    }

    private <T extends View> T getView(int viewId) {
        return (T) findViewById(viewId);
    }

    private void setIndicatorStr(int currentPostion) {
        if (dataType == TYPE_GETTERS && imageUrlGetters != null) {
            tvIndicator.setText(String.format(INDICATOR_STR, currentPostion + 1, imageUrlGetters.size()));
        } else if (dataType == TYPE_STRINGS && urlStrings != null) {
            tvIndicator.setText(String.format(INDICATOR_STR, currentPostion + 1, urlStrings.size()));
        }
    }

    private ArrayList<String> getImageUrls() {
        ArrayList<String> urls = new ArrayList<>();
        for (ImageUrlGetter getter : imageUrlGetters) {
            urls.add(getter.getImageUrl());
        }
        return urls;
    }

}
