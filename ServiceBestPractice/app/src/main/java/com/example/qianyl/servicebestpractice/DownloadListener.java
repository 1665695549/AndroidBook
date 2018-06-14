package com.example.qianyl.servicebestpractice;

/**
 * Created by qianyl on 2018/4/28.
 */

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();
}
