package com.ruanyun.imagepicker.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oginotihiro.cropview.CropUtil;
import com.oginotihiro.cropview.CropView;
import com.ruanyun.imagepicker.R;
import com.ruanyun.imagepicker.Util;

import java.io.File;

/**
 * Description:
 * author: zhangsan on 17/1/16 下午5:04.
 */

public class CustomerCropImageActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView btnBackpress;
    // @butterknife.BindView(R.id.tv_title_count)
    TextView tvTitleCount;
    TextView btnOk;
    CropView cropView;
    Uri uri;
    String savedPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_crop);
        uri=getIntent().getData();
        savedPath=getIntent().getStringExtra("saved_path");
        if(null==uri)return;
        initView();
    }

    private void initView() {
        btnBackpress = (ImageView) findViewById(R.id.btn_backpress);
        cropView = (CropView) findViewById(R.id.cropView);
        btnOk = (TextView) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnBackpress.setOnClickListener(this);
        cropView.of(uri)
                .asSquare()
                .initialize(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_backpress) {
            finish();
        } else if (i == R.id.btn_ok) {
            final ProgressDialog dialog = ProgressDialog.show(this, null, "Please wait…", true, false);
            cropView.setVisibility(View.GONE);
            new Thread() {
                public void run() {
                    Bitmap croppedBitmap = cropView.getOutput();
//                    Uri destination = Uri.fromFile(new File(savedPath, String.valueOf(System.currentTimeMillis())+".jpg"));
                    File file = new File(savedPath, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    //通过FileProvider创建一个content类型的Uri
                    Uri destination = Util.getUriForFile(CustomerCropImageActivity.this, file);
                    CropUtil.saveOutput(CustomerCropImageActivity.this, destination, croppedBitmap, 80);
                    setResult(RESULT_OK,new Intent().setData(destination));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            }.start();
        }
    }
}
