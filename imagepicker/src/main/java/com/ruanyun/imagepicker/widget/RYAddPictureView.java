package com.ruanyun.imagepicker.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

import com.ruanyun.imagepicker.AndroidImagePicker;
import com.ruanyun.imagepicker.R;
import com.ruanyun.imagepicker.bean.ImageItem;
import com.ruanyun.imagepicker.ui.ImagesGridActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author Daniel
 */
public class RYAddPictureView extends GridView implements ImagePickerAdapter.OnImageChangeListener,
        AndroidImagePicker.OnPictureTakeCompleteListener, AndroidImagePicker.OnImagePickCompleteListener {

    private static final String TAG = "RYAddPictureView";

    public int requestCode = 1433;
    Context mContext;
    ImagePickerAdapter adapter;
    ImageItem addItem;
    //    ImageSet imageSetCache = new ImageSet();
    onPickResultChangedListener onPickResultChangedListener;
    List<ImageItem> imageList;
    private int sizeLimit = 1;

    public RYAddPictureView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        this.mContext = mContext;
        initView();
    }

    private void initView() {
        imageList = new ArrayList<>();
        addItem = new ImageItem("", "", 1);
        addItem.isAdd = true;
        addItem.resId = R.drawable.release_btn_uploadphotos;
        imageList.add(addItem);
        adapter = new ImagePickerAdapter(mContext, imageList, R.layout.grid_item_select_image, sizeLimit, addItem);
        setAdapter(adapter);
        adapter.setOnImageChangeListener(this);

        AndroidImagePicker.getInstance().setSelectLimit(sizeLimit);
    }

    /**
     * description: 调用时需要在onStart加上 author: Daniel
     */
    public void setOnListeners() {
        AndroidImagePicker.getInstance().addOnPictureTakeCompleteListener(this);
        AndroidImagePicker.getInstance().addOnImagePickCompleteListener(this);
    }

    /**
     * description: 调用时需要在onDestroy加上 author: Daniel
     */
    public void destroyListeners() {
        AndroidImagePicker.getInstance().removeOnPictureTakeCompleteListener(this);
        AndroidImagePicker.getInstance().removeOnImagePickCompleteListener(this);
        if (imageList != null) {
            imageList.clear();
            imageList = null;
        }
    }

    @Override
    public void onAddBtnClick(int bindId, int posiont) {
        handleAddImageClick();
    }

    @Override
    public void onDelete(ImageItem item) {
//        imageList = adapter.getData();
//        imageSetCache.imageItems = imageList;

        if (onPickResultChangedListener != null) {
            onPickResultChangedListener.onPicDelete(item);
            onPickResultChangedListener.onPicChanged(getHasInt());
        }
    }

    /**
     * description: 添加图片调用接口 author: Daniel
     */
    public void handleAddImageClick() {
        Intent intent = new Intent();
        AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_MULTI);
        AndroidImagePicker.getInstance().setShouldShowCamera(true);
        AndroidImagePicker.getInstance().setRequestCode(requestCode);
        AndroidImagePicker.getInstance().setSelectLimit(sizeLimit - (getHasInt()));
        intent.setClass(mContext, ImagesGridActivity.class);
        ((Activity) mContext).startActivityForResult(intent, requestCode);
    }

    public void refreshImage(List<ImageItem> imageItems){
        this.imageList = imageItems;
        adapter.setData(this.imageList);
        adapter.sortListData(addItem);
        adapter.notifyDataSetChanged();
        AndroidImagePicker.getInstance().setSelectLimit(sizeLimit - (adapter.getCount() - 1));
    }


    /**
     * description: 选完图片onActivityResult需要调用该方法 author: Daniel
     */
    public void onImageActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == this.requestCode) {
                imageList.addAll(AndroidImagePicker.getInstance().getSelectedImages());
                refreshGrid();
            }
        }
    }

    @Override
    public void onPictureTakeComplete(String picturePath, int requestCode) {
        if (requestCode == this.requestCode) {
            imageList.add(new ImageItem("", picturePath, 1));
            refreshGrid();
        }
    }

    @Override
    public void onImagePickComplete(List<ImageItem> items, int requestCode) {
        if (requestCode == this.requestCode) {
            imageList.addAll(items);
            refreshGrid();
        }
    }

    /**
     * description: 更新图片列表 author: Daniel
     */
    private void refreshGrid() {
//        adapter.setData(this.imageList);
        adapter.sortListData(addItem);
        adapter.notifyDataSetChanged();
        AndroidImagePicker.getInstance().setSelectLimit(sizeLimit - getHasInt());
//        imageSetCache.imageItems = imageList;

        if (onPickResultChangedListener != null) {
            onPickResultChangedListener.onPicChanged(getHasInt());
        }
    }

    /*获取已有 有效选择数量*/
    private int getHasInt() {
        return adapter.getCount() - (this.imageList.contains(addItem)?1:0);
    }

//    public void clearImageList(List<ImageItem> items) {
//        if (imageList == null) {
//            imageList = new ArrayList<>();
//        }
//        imageList.clear();
//        imageList.add(addItem);
//        imageList.addAll(items);
//    }

    public ImageItem getAddItem() {
        return addItem;
    }

    public void setAddItem(ImageItem addItem) {
        this.addItem = addItem;
        adapter.setAddItem(addItem);
        adapter.notifyDataSetChanged();
        Log.d(TAG, "addItem = " + addItem);
    }

    public int getSizeLimit() {
        return sizeLimit;
    }

    /**
     * description: 设置图片限制 author: Daniel
     */
    public void setSizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        AndroidImagePicker.getInstance().setSelectLimit(sizeLimit - (getHasInt()));
        adapter.setSizeLimit(sizeLimit);
    }

    /**
     * description: 获取输入图片列表 author: Daniel
     */
    public List<ImageItem> getImageList() {
        return imageList;
    }

//    public void setImageList(List<ImageItem> imageList) {
//        if (imageList == null) {
//            imageList = new ArrayList<>();
//        }
//        if (imageList.size() < 1) {
//            imageList.add(addItem);
//        }
//        this.imageList = imageList;
//        adapter.setData(this.imageList);
//        adapter.notifyDataSetChanged();
//        AndroidImagePicker.getInstance().setSelectLimit(sizeLimit - (getHasInt()));
//    }

    public void setOnPickResultChangedListener(RYAddPictureView.onPickResultChangedListener onPickResultChangedListener) {
        this.onPickResultChangedListener = onPickResultChangedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public interface onPickResultChangedListener {
        //点击图片删除时触发
        void onPicDelete(ImageItem item);

        void onPicChanged(int hasNum);
        // void onImage
    }

}
