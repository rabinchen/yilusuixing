package com.clb.school.opencv__ndk.utils;

import com.clb.school.opencv__ndk.utils.ICallBack;

/**
 * Created by Administrator on 2017/11/27.
 */

public interface TTS {
    public void init();
    public void playText(String playText);
    public void stopSpeak();
    public void destroy();
    public boolean isPlaying();
    public void setCallback(ICallBack callback);
}
