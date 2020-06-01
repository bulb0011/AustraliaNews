package com.ruanyun.australianews.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.ruanyun.australianews.util.CommonUtil;
import com.ruanyun.australianews.util.TtsSettings;

public class TtsHolder {

    // 语音合成对象
    private SpeechSynthesizer mTts;

    private SharedPreferences mSharedPreferences;

    Context context;
    String text;

    public TtsHolder(Context context, String text) {
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        this.context=context;
        this.text = text;
        init();
    }

    void init(){
        mSharedPreferences = context.getSharedPreferences(TtsSettings.PREFER_NAME, context.MODE_PRIVATE);
        setParam();
    }

    public void start(){
        int code=  mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            CommonUtil.showToast("语音合成失败");
        }
    }

    // 暂停播放
    public void pauseSpeaking(){
        mTts.pauseSpeaking();
    }

    // 继续播放
    public void resumeSpeaking(){
        mTts.resumeSpeaking();
    }

    //暂停
    public void stopSpeaking(){
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }





    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                CommonUtil.showToast("初始化失败");
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {


//            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {


            onspeakprogress.onSpeakProgress(percent);


//            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
//
//            SpannableStringBuilder style=new SpannableStringBuilder(texts);
//            Log.e(TAG,"beginPos = "+beginPos +"  endPos = "+endPos);
//            style.setSpan(new BackgroundColorSpan(Color.RED),beginPos,endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((EditText) findViewById(R.id.tts_text)).setText(style);
        }

        @Override
        public void onCompleted(SpeechError error) {
//            System.out.println("oncompleted");
//            if (error == null) {
//                //	showTip("播放完成");
//                DebugLog.LogD("播放完成,"+container.size());
//                try {
//                    for(int i=0;i<container.size();i++) {
//                        writeToFile(container.get(i));
//                    }
//                }catch (IOException e) {
//
//                }
//                FileUtil.saveFile(memFile,mTotalSize,Environment.getExternalStorageDirectory()+"/1.pcm");
//
//
//            } else if (error != null) {
//                showTip(error.getPlainDescription(true));
//            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	 若使用本地能力，会话id为null
//            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
//                Log.d(TAG, "session id =" + sid);
//            }
//
//            //当设置SpeechConstant.TTS_DATA_NOTIFY为1时，抛出buf数据
//            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
//                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
//                Log.e("MscSpeechLog_", "bufis =" + buf.length);
//                container.add(buf);
//            }


        }
    };


    /**
     * 参数设置
     * @return
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(SpeechConstant.TYPE_CLOUD.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");

            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.pcm");
    }


    public interface OnSpeakProgress{

        void onSpeakProgress(int percent);
    }


    OnSpeakProgress onspeakprogress;

    public void setOnSpeakProgress(OnSpeakProgress onspeakprogress){

        this.onspeakprogress=onspeakprogress;

    }



}
