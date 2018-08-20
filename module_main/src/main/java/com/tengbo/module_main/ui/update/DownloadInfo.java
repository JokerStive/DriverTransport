package com.tengbo.module_main.ui.update;

/**
*@author yk
*@Description  文件下载信息
*/
public class DownloadInfo {
    /**
     * 文件大小
     */
    long total;
    /**
     * 已下载大小
     */
    long progress;

    public long getProgress() {
        return progress;
    }

    public long getTotal() {
        return total;
    }

    public DownloadInfo(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }
}
