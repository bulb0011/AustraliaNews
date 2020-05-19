package com.ruanyun.australianews.ui.my

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.PopupWindow
import com.ruanyun.imagepicker.AndroidImagePicker
import com.ruanyun.imagepicker.Util
import com.ruanyun.imagepicker.permissions.PermissionsManager
import com.ruanyun.imagepicker.permissions.PermissionsResultAction
import com.ruanyun.imagepicker.ui.CustomerCropImageActivity
import com.ruanyun.imagepicker.ui.ImageCropActivity
import com.ruanyun.imagepicker.ui.ImagesGridActivity
import com.ruanyun.imagepicker.ui.ImagesGridActivity.SELECTED_PIC
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.util.FileUtil
import com.ruanyun.australianews.util.LogX
import com.ruanyun.australianews.widget.RYSelectPopWindow
import java.io.File
import java.io.IOException

/**
 * Created by Sxq on 2016/9/9.
 */
abstract class HeaderPickerActivity : BaseActivity(), RYSelectPopWindow.OnSelectListener, AndroidImagePicker.OnImageCropCompleteListener, AndroidImagePicker.OnPictureTakeCompleteListener, PopupWindow.OnDismissListener {
    /**
     * 根据需求重写
     * @param file
     */
    protected abstract fun saveUserHeaderImage(file: File)

    /**
     * 根据需求重写
     * @param uri
     */
    protected open fun selectImageFile(uri: Uri?) {
        uri?.apply {
            saveUserHeaderImage(FileUtil.getFileByUri(uri, mContext))
        }
    }


    protected lateinit var mRYSelectPopWindow: RYSelectPopWindow
    protected var androidImagePicker: AndroidImagePicker? = null
    protected var isCrop = false//是否开启裁剪图片
    protected var isHeader = false//true圆形裁剪；false矩形裁剪

    /**
     * 初始化 弹出选择 PopuWindow  选取照片或相机   (用户选择头像初始化)
     */
    protected fun initSelectImageHeaderPopView() {
        androidImagePicker = AndroidImagePicker.getInstance()
        mRYSelectPopWindow = RYSelectPopWindow(mContext, this@HeaderPickerActivity)
        mRYSelectPopWindow.setOnSelectListener(this@HeaderPickerActivity)
        mRYSelectPopWindow.setOnDismissListener(this@HeaderPickerActivity)
        initImagePickerListener()
    }

    /**
     * imagePicker的监听设置
     */
    private fun initImagePickerListener() {
        androidImagePicker!!.selectMode = AndroidImagePicker.Select_Mode.MODE_SINGLE
        androidImagePicker!!.isShouldShowCamera = false
        androidImagePicker!!.addOnImageCropCompleteListener(this)
        androidImagePicker!!.addOnPictureTakeCompleteListener(this)
    }

    /**
     * 选择图片不进行裁剪
     */
    protected fun setSelectTypeDefault(){
        isCrop = false
        isHeader = false
    }

    /**
     * 选择图片，然后进行圆形图片裁剪
     */
    protected fun setSelectTypeRound(){
        isCrop = true
        isHeader = true
    }

    /**
     * 选择图片，然后进行矩形图片裁剪
     */
    protected fun setSelectTypeRectangle(){
        isCrop = true
        isHeader = false
    }

    override fun onAlbumSelectClick() {
        val intent = Intent()
        intent.setClass(mContext, ImagesGridActivity::class.java)
        intent.putExtra("isCrop", isCrop && isHeader)
        startActivityForResult(intent, SELECT_PHOTO_REQUEST_CODE)
        mRYSelectPopWindow.dismiss()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }

    override fun takePicClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionsManager.getInstance()
                    .requestPermissionsIfNecessaryForResult(this, PERMISSION, object : PermissionsResultAction() {

                        override fun onGranted() {
                            takePicClick2()
                        }

                        override fun onDenied(permission: String) {
                            showToast("请求相机权限被拒绝")
                        }
                    })
        } else {
            takePicClick2()
        }
    }

    private fun takePicClick2() {
        try {
            androidImagePicker!!.takePicture(this@HeaderPickerActivity, AndroidImagePicker.REQ_CAMERA)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mRYSelectPopWindow.dismiss()
    }

    override fun onImageCropComplete(bmp: Bitmap, ratio: Float) {
        val file = FileUtil.saveBitmapFile(bmp, "head"+System.currentTimeMillis()+".jpg")
        //上传头像
        saveUserHeaderImage(file)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AndroidImagePicker.REQ_CAMERA -> // 相机拍照返回结果
                    if (!TextUtils.isEmpty(androidImagePicker!!.currentPhotoPath)) {
                        LogX.d(TAG, " path :" + androidImagePicker!!.currentPhotoPath)
                        androidImagePicker!!.notifyPictureTaken()
                    } else {
                        LogX.d(TAG, "didn't save to your path")
                    }
                SELECT_PHOTO_REQUEST_CODE -> {
                    var path = ""
                    if (data != null) {
                        path = data.getStringExtra(SELECTED_PIC)
                    }
                    saveUserHeaderImage(File(path))
                }
                REQUEST_CODE_CROP -> {
                    var uri: Uri? = null
                    if (data != null) {
                        uri = data.data
                    }
                    selectImageFile(uri)
                }
                else -> {
                }
            }
        }
    }

    override fun onPictureTakeComplete(picturePath: String, requestCode: Int) {
        if (isCrop) { //  拍照需要裁切
            if (isHeader) {
                val intent = Intent()
                intent.setClass(mContext, ImageCropActivity::class.java)
                intent.putExtra(AndroidImagePicker.KEY_PIC_PATH, picturePath)
                startActivity(intent)
            } else {
                val intent = Intent()
                intent.setClass(mContext, CustomerCropImageActivity::class.java)
                intent.data = uriFormFilePath(picturePath)
                intent.putExtra("saved_path", FileUtil.getCropPath())
                startActivityForResult(intent, REQUEST_CODE_CROP)
            }
        } else { //  拍照不需要裁切
            saveUserHeaderImage(File(picturePath))
        }
    }

    /**
     * 显示选择pop窗口
     *
     * @param view
     */
    protected fun showSelectPopView(view: View?) {
        AndroidImagePicker.getInstance().selectMode = AndroidImagePicker.Select_Mode.MODE_SINGLE
        mRYSelectPopWindow.showPopupWindow(view)
    }


    override fun onDestroy() {
        super.onDestroy()
        removeOnImageListener()
    }

    private fun removeOnImageListener() {
        if (androidImagePicker != null) {
            androidImagePicker!!.removeOnImageCropCompleteListener(this)
            androidImagePicker!!.removeOnPictureTakeCompleteListener(this)
        }
    }

    /**
     * @param path
     * @return
     */
    private fun uriFormFilePath(path: String): Uri? {
        var uri: Uri? = null
        if (!TextUtils.isEmpty(path)) {
            val file = File(path)
            uri = Util.getUriForFile(mContext, file)
        }
        return uri
    }

    override fun onDismiss() {
        mRYSelectPopWindow.onDismiss()
    }

    companion object {

        /**
         * 选择图片请求 code
         */
        val SELECT_PHOTO_REQUEST_CODE = 98

        val REQUEST_CODE_CROP = 45

        private val PERMISSION = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}
