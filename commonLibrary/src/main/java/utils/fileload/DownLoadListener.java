package utils.fileload;

public interface DownLoadListener {
    void onStart();

    void onProgress(long progress);

    void onFinish(String filePath);

    void onFail();
}
