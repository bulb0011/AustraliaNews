package com.ruanyun.imagepicker.compressimage;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;


/**
 * Description:图片压缩任务类 单线程
 * author: zhangsan on 16/9/18 下午1:50.
 */
public class CompressImageTask extends Thread {
    private static final String TAG = "CompressImageTask";
    private static final int MSG_COMPLETE = 0x111;
    private static final int MSG_ERROR = 0x112;
    private static final int MSG_CANCEL = 0x113;

    private volatile boolean isExcuted = false;
    public static final int FIRST_GEAR = 1;//压缩比例
    public static final int THIRD_GEAR = 3;
    //private List<CompressTaskResult> compressResults = new ArrayList<>();
    private CompressTaskResult[] compressResults;
    private CompressImageResultAdapter compressImageResultAdapter;
    private ExecutorService executorService;
    private CompressTaskCallback compressTaskCallback;
    private CompressImageInfoGetter[] params;

    //private File mFile;
    private int gear = THIRD_GEAR;//压缩等级 默认三挡 最高级压缩

    private static final int MAX_THRED_SIZE = 3;

    public CompressImageTask(int gear, ExecutorService executor, CompressImageResultAdapter converter) {
        this.gear = gear;
        this.executorService = executor;
        this.compressImageResultAdapter = converter;
    }

    public void setParams(CompressImageInfoGetter[] params) {
        this.params = params;
    }

    @MainThread
    public void start(CompressTaskCallback callback) {
        if (params != null) {
            this.compressTaskCallback = callback;
            if (params.length == 0) {
                compressResults = new CompressTaskResult[0];
                onComplete();
                return;
            }
            executorService.submit(this);
        }
    }

    public void cancel() {
        isExcuted = true;
        // executorService.shutdownNow();
        interrupt();
        compressTaskCallback = null;
    }

    @WorkerThread
    private void doCompress(CompressImageInfoGetter[] paths) throws InterruptedException {
        compressResults = new CompressTaskResult[paths.length];
        int pointer = 0;
        for (CompressImageInfoGetter pathGetter : paths) {
            if (Thread.interrupted()) {
                getHandler().sendEmptyMessage(MSG_CANCEL);
                throw new InterruptedException("compress task canceled");
            }
            File imageFile = new File(pathGetter.imageFilePath());
            CompressTaskResult result = new CompressTaskResult();
            result.paramsName = pathGetter.requestPramsName();
            switch (gear) {
                case FIRST_GEAR:
                    result.imageFile = CompressImageUtil.firstCompress(imageFile, "");
                    break;
                case THIRD_GEAR:
                    result.imageFile = CompressImageUtil.thirdCompress(imageFile, "");
                    break;
            }
            compressResults[pointer] = result;
            pointer++;

        }
        isExcuted = true;
        getHandler().sendEmptyMessage(MSG_COMPLETE);
    }

    private InternalHandler mHandler;

    private InternalHandler getHandler() {
        synchronized (CompressImageTask.class) {
            if (mHandler == null) {
                mHandler = new InternalHandler(CompressImageTask.this);
            }
            return mHandler;
        }
    }

    @MainThread
    private void onComplete() {
        if (compressTaskCallback != null) {
            compressTaskCallback.onCompresComplete(compressImageResultAdapter == null ? compressResults : compressImageResultAdapter.convert(compressResults));
        }
    }

    @MainThread
    private void onCancel() {
        if (compressTaskCallback != null) {
            compressTaskCallback.onCompresFail(new Throwable("task canceled"));
        }
    }

    @MainThread
    private void onError() {

    }

    @Override
    public void run() {
        while (!isExcuted) {
            try {
                doCompress(params);
            } catch (InterruptedException e) {
                if (isExcuted) {
                    return;
                }
                continue;
            }
        }

    }

    static class InternalHandler extends Handler {
        WeakReference<CompressImageTask> taskWeakReference;

        public InternalHandler(CompressImageTask task) {
            super(Looper.getMainLooper());
            taskWeakReference = new WeakReference<CompressImageTask>(task);
        }

        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            CompressImageTask task = taskWeakReference.get();
            if (task != null) {
                switch (msg.what) {
                    case MSG_COMPLETE:
                        task.onComplete();
                        break;
                    case MSG_CANCEL:
                        task.onCancel();
                        break;
                    case MSG_ERROR:
                        task.onError();
                        break;
                }
            }
        }
    }
}
