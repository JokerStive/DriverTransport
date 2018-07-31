package com.tengbo.commonlibrary.service;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

public class RuntimeCompat {
    private static final String TAG = "GlideRuntimeCompat";
    private static final String CPU_NAME_REGEX = "cpu[0-9]+";
    private static final String CPU_LOCATION = "/sys/devices/system/cpu/";

    private RuntimeCompat() {
    }
    static int availableProcessors() {
        int cpus = Runtime.getRuntime().availableProcessors();
        if (Build.VERSION.SDK_INT < 17) {
            cpus = Math.max(getCoreCountPre17(), cpus);
        }
        return cpus;
    }

    @SuppressWarnings("PMD")
    private static int getCoreCountPre17() {
        File[] cpus = null;
        StrictMode.ThreadPolicy originalPolicy = StrictMode.allowThreadDiskReads();
        try {
            File cpuInfo = new File(CPU_LOCATION);
            final Pattern cpuNamePattern = Pattern.compile(CPU_NAME_REGEX);
            cpus = cpuInfo.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return cpuNamePattern.matcher(s).matches();
                }
            });
        } catch (Throwable t) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Failed to calculate accurate cpu count", t);
            }
        } finally {
            StrictMode.setThreadPolicy(originalPolicy);
        }
        return Math.max(1, cpus != null ? cpus.length : 0);
    }
}
