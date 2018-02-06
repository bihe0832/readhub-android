/**
 *
 */
package com.ottd.libs.utils.device;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.ottd.libs.utils.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class ExternalStorage {


    // 有sd卡时应用程序的存储根目录
    public static final String APP_SDCARD_AMOUNT_ROOT_PATH = "/tencent/JYGame";
    // 无sd卡时应用程序的存储根目录
    public static final String APP_SDCARD_UNAMOUNT_ROOT_PATH = "/JYGame";


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

    // 判断SDCard是否存在并且是可写的
    public static boolean isSDCardExistAndCanRead() {
        boolean result = false;
        try {
            result = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().canRead();
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }


    public static String getCommonRootDir(Context mContext) {

        String dirPath = null;

        // 判断SDCard是否存在并且是可用的
        if (isSDCardExistAndCanRead()) {
            try {
                dirPath = Environment.getExternalStorageDirectory().getPath() + APP_SDCARD_AMOUNT_ROOT_PATH;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        } else {
            try {
                dirPath = mContext.getFilesDir().getAbsolutePath() + APP_SDCARD_UNAMOUNT_ROOT_PATH;
            } catch (Exception e) {
                return "";
            }
        }
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String read(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            return br.readLine();
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
