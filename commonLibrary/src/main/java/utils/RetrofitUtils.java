package utils;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RetrofitUtils {
    @NonNull
    public static Map<String, RequestBody> getRequestBodyMap(File file) {
        Map<String, RequestBody> params = new HashMap<>();
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/" + suffix), file);
        params.put("file\";filename=\"" + fileName, requestFile);
        return params;
    }

    @NonNull
    public static Map<String, RequestBody> getRequestBodyMap(String path) {
        return getRequestBodyMap(new File(path));
    }

    @NonNull
    public static MultipartBody createMultipartBody(List<String> paths) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (String path : paths) {
            File file = new File(path);
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
            builder.addFormDataPart("file", fileName, requestBody);
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }
}
