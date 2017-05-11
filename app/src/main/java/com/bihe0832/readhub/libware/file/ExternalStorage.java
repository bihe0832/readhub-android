/**
 *
 */
package com.bihe0832.readhub.libware.file;

import android.os.Environment;
import android.os.StatFs;

import com.bihe0832.readhub.libware.util.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class ExternalStorage {
    public static final String SD_CARD = "sdCard";
    public static final String EXTERNAL_SD_CARD = "externalSdCard";

    /**
     * @return True if the external storage is available. False otherwise.
     */
    public static boolean isAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/";
    }

    public static File getExternalSDCardDirectory() {
        File innerDir = Environment.getExternalStorageDirectory();
        File rootDir = innerDir.getParentFile();
        File firstExtSdCard = innerDir;
        File[] files = rootDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.compareTo(innerDir) != 0) {
                    firstExtSdCard = file;
                    break;
                }
            }
        }
        return firstExtSdCard;
    }

    /**
     * 获取手机外置sdcard的可用空间大小
     * 0 则没有取到
     */
    static public long getAvailableExternalSdCardSize() {
        List<String> paths = getExternalSDCardPaths();
        long availableSize = 0;
        for (int i = 0; i < paths.size(); i++) {
            try {
                StatFs stat = new StatFs(paths.get(i));
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                availableSize += availableBlocks * blockSize;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                continue;
            }
        }
        return availableSize;
    }

    /**
     * 获取手机外置sdcard的总容量的大小
     * 0 则没有取到
     */
    static public long getTotalExternalSdCardSize() {
        List<String> paths = getExternalSDCardPaths();
        long totalSize = 0;
        for (int i = 0; i < paths.size(); i++) {
            try {
                StatFs stat = new StatFs(paths.get(i));
                long blockSize = stat.getBlockSize();
                long totalBlocks = stat.getBlockCount();
                totalSize += totalBlocks * blockSize;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                continue;
            }
        }
        return totalSize;
    }

    /**
     * 得到外置sdcard的路径，即path与Environment.getExternalStorageDirectory()的文件路径是不一样的
     * 但也是在同一个根目录下的，因为sdcard的挂载根目录是一致的
     *
     * @return
     */
    public static List<String> getExternalSDCardPaths() {
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        List<String> paths = new ArrayList<String>();
        String sysPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = Environment.getExternalStorageDirectory().getParent();
        String parentPaths[] = null;
        if(!TextUtils.ckIsEmpty(path)){
            parentPaths = path.split("/");
        }

        String pixPath;
        if (null != parentPaths && parentPaths.length > 1) {
            pixPath = "/" + parentPaths[1];
        } else {
            pixPath = Environment.getExternalStorageDirectory().getParent();
            if(TextUtils.ckIsEmpty(pixPath)){
                pixPath = "";
            }
        }

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            proc = runtime.exec("mount");
            is = proc.getInputStream();
            isr = new InputStreamReader(is);
            String line;
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure")) continue;
                if (line.contains("asec")) continue;

                if (line.contains("fat")) {
                    //TF card
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (!sysPath.equals(columns[1]) && pixPath.length() > 0 && columns[1].contains(pixPath)) {
                            paths.add(columns[1]);
                        }
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (proc != null) { //主动destory,不给FinalizerDeamon线程增加负担。
                proc.destroy();
            }
        }
        return paths;
    }
}
