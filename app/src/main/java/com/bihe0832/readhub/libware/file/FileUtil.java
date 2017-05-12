package com.bihe0832.readhub.libware.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.bihe0832.readhub.libware.util.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件相关操作的工具类
 */
public class FileUtil {

    // 有sd卡时应用程序的存储根目录
    public static final String APP_SDCARD_AMOUNT_ROOT_PATH = "/zixie";
    // 无sd卡时应用程序的存储根目录
    public static final String APP_SDCARD_UNAMOUNT_ROOT_PATH = "/zixie";


    public static boolean copy(String from, String dest) throws IOException {
        if (TextUtils.ckIsEmpty(from) || TextUtils.ckIsEmpty(dest)) {
            return false;
        }
        File file = new File(from);
        if (!file.exists()) {
            return false;
        }
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(file));
            outBuff = new BufferedOutputStream(new FileOutputStream(new File(dest)));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
            return true;
        } finally {
            // 关闭流
            if (inBuff != null)
                try {
                    inBuff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (outBuff != null)
                try {
                    outBuff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    // 判断SDCard是否存在并且是可写的
    public static boolean isSDCardExistAndCanWrite() {
        boolean result = false;
        try {
            result = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().canWrite();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
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

    /**
     * 获取程序运行期间文件保存的根目录
     *
     * @return SD卡可用的时候返回的是
     * /packagename/files
     */
    public static String getCommonRootDir(Context mContext) {

        String dirPath = null;

        // 判断SDCard是否存在并且是可用的
        if (isSDCardExistAndCanWrite()) {
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

    /**
     * 指定路径创建目录, 并提供指定momedia的接口
     *
     * @param nomedia : 是否需要nomedia文件，只有存图片的目录需要，有nomedia图片在相册不可见
     * @return 完整路径
     */
    public static String getPath(String path, boolean nomedia) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
            if (nomedia) {
                File nomediaFile = new File(path + File.separator + ".nomedia");
                try {
                    nomediaFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    // 得到系统分配的程序缓存目录
    public static String getInternalCachePath(Context mContext) {
        String dirPath = mContext.getCacheDir() + File.separator;
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dirPath;
    }

    // 清空缓存目录
    public static void clearInternalCache(String dir, Context mContext) {
        File f = new File(getInternalCachePath(mContext) + dir);
        if (f.exists()) {
            File files[] = f.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 获得指定文件路径的文件名
     *
     * @param path 文件的完整路径，包括文件名和后缀
     * @return 文件名
     */
    public static String getFileName(String path) {
        String file = path.substring(path.lastIndexOf("/") + 1);
        if (file.contains(".")) {
            file = file.substring(0, file.indexOf("."));
        }
        return file;
    }

    /**
     * 获得指定文件路径的文件后缀名
     *
     * @param path 文件的完整路径，包括文件名和后缀
     * @return 文件后缀，如果无后缀return null
     */
    public static String getFileExtension(String path) {
        String file = path.substring(path.lastIndexOf("/") + 1);
        if (file.contains(".")) {
            return file.substring(file.indexOf(".") + 1);
        } else {
            return null;
        }
    }

    /**
     * 获取制定路径文件的大小
     *
     * @param path 文件的完整路径，包括文件名和后缀
     * @return 文件大小，文件不存在return 0
     */
    public static long getFileSize(String path) {
        if (!TextUtils.ckIsEmpty(path)) {
            File file = new File(path);
            if (!file.exists()) {
                return 0;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }

    }

    /*
     * 获取指定路径文件的最近修改时间
     *
     * @param path文件的完整路径，包括文件名和后缀
     *
     * @return 文件最近修改时间，文件不存在返回0
     */
    public static long getFileLastModified(String path) {
        long ret = 0;

        if (null != path) {
            File file = new File(path);
            if (file.exists()) {
                ret = file.lastModified();
            }
        }

        return ret;
    }

    /*
     * 删除指定路径文件
     *
     * @param path 文件的完整路径，包括文件名和后缀
     *
     * @return boolean，true代表成功
     */
    public static boolean deleteFile(String path) {
        File dir = new File(path);
        if (dir.exists() && dir.isFile()) {
            return dir.delete();
        }
        return false;
    }


    /**
     * 读取文件到字节流 baos baos从外部来，外部close，这里不要close baos
     *
     * @param baos          字节流数据
     * @param dest          要读取的文件完整路径，包括文件名和后缀
     * @param byteArrayPool 分配byte数组的池子，非必须
     */
    public static boolean readFile(String dest, ByteArrayOutputStream baos, ByteArrayPool byteArrayPool) {
        File file = new File(dest);
        if (null == baos || file.length() == 0) {

            return false;
        }
        FileInputStream fis = null;
        byte[] buf = null;
        try {
            fis = new FileInputStream(file);
            if (byteArrayPool != null) {
                buf = byteArrayPool.getBuf(1024);
            } else {
                buf = new byte[1024];
            }
            while (true) {
                int numread = fis.read(buf);
                if (-1 == numread) {
                    break;
                }
                baos.write(buf, 0, numread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (buf != null && byteArrayPool != null) {
                byteArrayPool.returnBuf(buf);
            }
        }
        if (baos.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public static byte[] compressBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int bitRate) {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(format, bitRate, localByteArrayOutputStream);
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
        try {
            localByteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayOfByte;
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
