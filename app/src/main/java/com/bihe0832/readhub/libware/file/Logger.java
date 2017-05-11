package com.bihe0832.readhub.libware.file;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bihe0832.readhub.libware.encrypt.HexUtils;
import com.bihe0832.readhub.libware.util.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Log wrapper.
 */
public class Logger {

    // log记录方式
    public static final int LOG_NULL = 0; // null device
    public static final int LOG_CONSOLE = 1; // console log
    public static final int LOG_FILE = 2; // file log
    public static final int LOG_BOTH = 3; // both

    // log类型，与系统保持一致
    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;
    @SuppressWarnings("unused")
    private static final int ASSERT = 7;

    public static final String DEFAULT_TAG = "ZIXIE";
    public static final String DOCTOR_TAG = DEFAULT_TAG + "DOCTOR";

    private static int logDevice = LOG_CONSOLE;
    private static FileLogHandler filelogHandler = null;
    private static final int STACK_TRACE_DEEP = 4;
    private static final long LOG_FILE_SIZE = 10 * 1024 * 1024;
    private static String FILE_LOG = "readhub.log";

    public static void init(Context ctx, String logTypeString) {
        int logType = LOG_NULL;
        try {
            logType = Integer.parseInt(logTypeString);
        } catch (Exception e) {
            logType = LOG_CONSOLE;
        }
        
        Log.d(DEFAULT_TAG, "Logger type: " + logType);

        switch (logType) {
            case LOG_CONSOLE:
                logDevice = LOG_CONSOLE;
                break;
            case LOG_FILE:
                logDevice = LOG_FILE;
                break;
            case LOG_BOTH:
                logDevice = LOG_BOTH;
                break;
            case LOG_NULL:
                logDevice = LOG_NULL;
                break;
            default:
                logDevice = LOG_CONSOLE;
                break;
        }
        if (logDevice > LOG_CONSOLE) {
            if (filelogHandler == null) {
                filelogHandler = new FileLogHandler(ctx);
            }
        }
    }

    private static File getLogFile(Context ctx) {
        File file = new File(FileUtil.getCommonRootDir(ctx), FILE_LOG);
        Logger.d(DOCTOR_TAG, "LogFile:" + file.toString());
        return file;
    }

    private static class FileLogHandler extends Handler {
        private boolean hasSDCard = true;
        private FileOutputStream logOutput;
        private File logFile;

        FileLogHandler(Context ctx) {
            try {
                logFile = getLogFile(ctx);
                if (!logFile.exists()) {
                    logFile.createNewFile();
                } else {
                    long logLength = logFile.length();
                    if (logLength <= LOG_FILE_SIZE) {
                        return;
                    }
                    // 清空文件
                    Log.d(DEFAULT_TAG, "Log size larger than LOG_FILE_SIZE:"
                            + String.valueOf(logLength));
                    logFile.delete();
                    logFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(DEFAULT_TAG, "Logger file: " + logFile.toString());
        }

        public void handleMessage(Message msg) {
            if (!hasSDCard) {
                return;
            }
            try {
                String log = (String) msg.obj + "\n";
                if (log != null) {
                    byte[] logData = log.getBytes();
                    getLogOutput().write(logData, 0, logData.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileOutputStream getLogOutput() throws Exception {
            if (logOutput == null) {
                logOutput = new FileOutputStream(logFile, true);
            }
            return logOutput;
        }

    }

    private static void showLog(int logType, String tag, String msg, int device) {
        if (TextUtils.ckIsEmpty(msg)) {
            msg = "NULL MSG";
        }
        if (tag.length() > 89) {
            Logger.showInConsole(ERROR, DEFAULT_TAG, "tag is longer than 89");
            tag = tag.substring(0, 86) + "...";
        }
        switch (device) {
            case LOG_CONSOLE:
                showInConsole(logType, tag, msg);
                break;
            case LOG_FILE:
                writeToLog(System.currentTimeMillis() / 1000 + "\t" + tag + "\t"
                        + msg);
                break;
            case LOG_BOTH:
                showInConsole(logType, tag, msg);
                writeToLog(System.currentTimeMillis() / 1000 + "\t" + tag + "\t"
                        + msg);
                break;
            case LOG_NULL:
            default:
                break;
        }
    }

    private static void showInConsole(int logType, String tag, String msg) {
        if (msg == null) {
            msg = "NULL MSG";
        }
        switch (logType) {
            case ERROR:
                Log.e(DEFAULT_TAG+tag, msg);
                break;
            case WARN:
                Log.w(DEFAULT_TAG+tag, msg);
                break;
            case DEBUG:
                Log.d(DEFAULT_TAG+tag, msg);
                break;
            case INFO:
                Log.i(DEFAULT_TAG+tag, msg);
                break;
            case VERBOSE:
                Log.v(DEFAULT_TAG+tag, msg);
                break;
            default:
                break;
        }
    }

    private static void writeToLog(String log) {
        Message msg = filelogHandler.obtainMessage();
        msg.obj = log;
        filelogHandler.sendMessage(msg);
    }

    private static String getTag(String subTag, int index) {
        String tag = "";
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        if (index < 0 || index >= traces.length) {
            return tag;
        }
        String clsName = traces[index].getClassName();
        String methodName = traces[index].getMethodName();
        String shortClsName = "";
        int dot = clsName.lastIndexOf('.');
        if (dot != -1) {
            shortClsName = clsName.substring(dot + 1);
        }

        if (TextUtils.ckIsEmpty(subTag)) {
            tag = DEFAULT_TAG + " " + shortClsName + "." + methodName;
        } else {
            tag = DEFAULT_TAG + ">" + subTag + " " + shortClsName + "."
                    + methodName;
        }
        return tag;
    }

    public static void e(String msg) {
        if (logDevice > LOG_NULL) {
            Logger.showLog(ERROR, getTag(null, STACK_TRACE_DEEP), msg,
                    logDevice);
        }
    }

    public static void e(String tag, Throwable throwable) {
        if (throwable == null) {
            return;
        }

        StackTraceElement[] stacks = new Throwable().getStackTrace();
        if (stacks.length > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("class : ").append(stacks[1].getClassName())
                    .append("; line : ").append(stacks[1].getLineNumber());
            Logger.showLog(ERROR, tag, sb.toString(), logDevice);
        }

        throwable.printStackTrace();
    }

    public static void e(String tag, String msg) {
        if (logDevice > LOG_NULL) {
            Logger.showLog(ERROR, tag, msg, logDevice);
        }
    }

    public static void w(String msg) {
        if (logDevice > LOG_NULL) {
            Logger.showLog(WARN, getTag(null, STACK_TRACE_DEEP), msg, logDevice);
        }
    }

    public static void w(String tag, String msg) {
        if (logDevice > LOG_NULL) {
            Logger.showLog(WARN, tag, msg, logDevice);
        }
    }

    public static void d(String msg) {
        if (logDevice > LOG_NULL) {
            Logger.showLog(DEBUG, getTag(null, STACK_TRACE_DEEP), " " + msg,
                    logDevice);
        }
    }

    public static void d(Object msg) {
        if (logDevice == LOG_NULL) {
            return;
        }
        String tag = getTag(null, STACK_TRACE_DEEP);
        d(tag, msg);
    }

    public static void d(String tag, Object msg) {
        if (logDevice == LOG_NULL) {
            return;
        }
        if (null == msg) {
            Logger.showLog(DEBUG, tag, "empty msg", logDevice);
            return;
        }
        tag = null == tag ? DEFAULT_TAG : tag;
        Logger.showLog(DEBUG, tag, msg.toString(), logDevice);
    }

    public static void d(Bundle b) {
        if (logDevice == LOG_NULL) {
            return;
        }
        String tag = getTag(null, STACK_TRACE_DEEP);
        if (b == null) {
            Logger.showLog(DEBUG, tag, "empty bundle", logDevice);
            return;
        }

        Set<String> keys = b.keySet(); // 手Q通过这种方式传 platformId
        for (String key : keys) {
            if (b.get(key) instanceof byte[]) {
                Logger.showLog(DEBUG, tag,
                        key + ":" + HexUtils.bytes2HexStr(b.getByteArray(key)),
                        logDevice);
            } else if (b.get(key) instanceof String) {
                Logger.showLog(DEBUG, tag, key + ":" + b.getString(key),
                        logDevice);
            } else if (b.get(key) instanceof Long) {
                Logger.showLog(DEBUG, tag, key + ":" + b.getLong(key),
                        logDevice);
            } else if (b.get(key) instanceof Integer) {
                Logger.showLog(DEBUG, tag, key + ":" + b.getInt(key), logDevice);
            } else {
                Logger.showLog(DEBUG, tag, key, logDevice);
            }
        }
    }

    public static void d(Intent i) {
        if (logDevice == LOG_NULL) {
            return;
        }
        String tag = getTag(null, STACK_TRACE_DEEP);
        d(tag, i);
    }

    public static void d(String tag, Intent i) {
        if (logDevice == LOG_NULL) {
            return;
        }
        if (i == null || i.getExtras() == null) {
            Logger.d(tag, "********************** INTENT START **************************");
            Logger.showLog(DEBUG, tag, "empty Intent", logDevice);
            Logger.d(tag, "********************** INTENT END **************************");
            return;
        }
        Logger.d(tag, "********************** INTENT START **************************");
        Logger.showLog(DEBUG, tag, "Action: " + i.getAction(), logDevice);
        Logger.showLog(DEBUG, tag, "Component: " + i.getComponent(), logDevice);
        Logger.showLog(DEBUG, tag, "Flags: " + i.getFlags(), logDevice);
        Logger.showLog(DEBUG, tag, "Scheme: " + i.getScheme(), logDevice);

        Bundle b = i.getExtras();
        Set<String> keys = b.keySet(); // 手Q通过这种方式传 platformId
        for (String key : keys) {
            if (b.get(key) instanceof byte[]) {
                Logger.showLog(DEBUG, tag,
                        key + ":" + HexUtils.bytes2HexStr(b.getByteArray(key)),
                        logDevice);
            } else if (b.get(key) instanceof String) {
                Logger.showLog(DEBUG, tag, key + ":" + b.getString(key),
                        logDevice);
            } else if (b.get(key) instanceof Long) {
                Logger.showLog(DEBUG, tag, key + ":" + b.getLong(key),
                        logDevice);
            } else if (b.get(key) instanceof Integer) {
                Logger.showLog(DEBUG, tag, key + ":" + b.getInt(key), logDevice);
            } else {
                Logger.showLog(DEBUG, tag, key, logDevice);
            }
        }
        Logger.d(tag, "********************** INTENT END **************************");
    }

    public static void d(String tag, String msg) {
        if (logDevice > LOG_NULL) {
            Logger.showLog(DEBUG, tag, msg, logDevice);
        }
    }

}
