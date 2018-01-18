package com.justwayward.book.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gaoyuan on 2017/11/26.
 */

public class TtsUtis {

    private SpeechSynthesizer mTts;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private String text;
    private SynthesizerListener listener;
    private static TtsUtis instance;

    private TtsUtis() {
    }

    public static TtsUtis getInstance() {
        if (instance == null) {
            synchronized (TtsUtis.class) {
                if (instance == null) {
                    instance = new TtsUtis();
                }
            }
        }
        return instance;
    }

    public void play(String text, SynthesizerListener listener) {
        this.listener = listener;
        this.text = text;

        FlowerCollector.onEvent(ReaderApplication.getsInstance(), "tts_play");
        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(ReaderApplication.getsInstance(), mTtsInitListener);
        } else {
            mTts = SpeechSynthesizer.getSynthesizer();
        }

    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        if (mTts == null) {
            return;
        }
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, ReaderApplication.vocher);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, ReaderApplication.speed + "");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");

        mTts.startSpeaking(text, listener);
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("gy", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                ToastUtils.showToast("初始化失败,错误码：" + code);
            } else {
                setParam();

                if (code != 0) {
                    ToastUtils.showToast("语音合成失败,错误码: " + code);
                }
            }
        }
    };

    public void star(String text, SynthesizerListener listener) {
        mTts.startSpeaking(text, listener);
    }

    public boolean isPlay() {
        if (mTts == null) {
            return false;
        }
        return mTts.isSpeaking();
    }

    public void pause() {
        mTts.pauseSpeaking();
    }

    public void stop() {
        if (mTts == null) {
            return;
        }
        mTts.stopSpeaking();
        mTts.destroy();

        mTts = null;
    }

    public String getCurrentText() {
        return text;
    }

    public void start(String s) {
        text = s;
        mTts.startSpeaking(s, listener);
    }

    public void reStart() {
        if (mTts != null) {
            mTts.resumeSpeaking();
        }
    }

    public void destroy(){
        instance = null;
    }
}
