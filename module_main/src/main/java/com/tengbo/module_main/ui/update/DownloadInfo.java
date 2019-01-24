package com.tengbo.module_main.ui.update;

/**
*@author yk
*@Description  文件下载信息
*/
class DownloadInfo {
    /**
     * 文件大小
     */
    private long total;
    /**
     * 已下载大小
     */
    private long progress;

    long getProgress() {
        return progress;
    }

    long getTotal() {
        return total;
    }

    DownloadInfo(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }
}
