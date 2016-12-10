
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {
    public final static String TAG = "LogUtils";

    public static boolean ENABLE = true;
    public static boolean LOG2FILE_ENABLE = false;
    public static boolean LE_LOG2FILE_ENABLE = false;
    public static String FILE_PREFIX = "LogUtilsLogger";
    private static BufferedWriter mWriter;

    public static boolean isLoggable(int level) {
        if (ENABLE)
            return Log.isLoggable(TAG, level);
        return false;
    }

    public static String getStackTraceString(Throwable th) {
        if (ENABLE)
            return Log.getStackTraceString(th);
        return th.getMessage();
    }

    public static int println(int level, String msg) {
        if (ENABLE)
            return Log.println(level, TAG, msg);
        return 0;
    }

    public static int v(String msg) {
        write2File(msg);
        if (ENABLE)
            return Log.v(TAG, msg);
        return 0;
    }

    public static int v(String msg, Throwable th) {
        write2File(msg);
        write2File(getStackTraceString(th));
        if (ENABLE)
            return Log.v(TAG, msg, th);
        return 0;
    }

    public static int d(String msg) {
        write2File(msg);
        if (ENABLE)
            return Log.d(TAG, msg);
        return 0;
    }

    public static int d(String msg, Throwable th) {
        write2File(msg);
        write2File(getStackTraceString(th));
        if (ENABLE)
            return Log.d(TAG, msg, th);
        return 0;
    }

    public static int i(String msg) {
        write2File(msg);
        if (ENABLE)
            return Log.i(TAG, msg);
        return 0;
    }

    public static int i(String msg, Throwable th) {
        write2File(msg);
        write2File(getStackTraceString(th));
        if (ENABLE)
            return Log.i(TAG, msg, th);
        return 0;
    }

    public static int w(String msg) {
        write2File(msg);
        if (ENABLE)
            return Log.w(TAG, msg);
        return 0;
    }

    public static int w(String msg, Throwable th) {
        write2File(msg);
        write2File(getStackTraceString(th));
        if (ENABLE)
            return Log.w(TAG, msg, th);
        return 0;
    }

    public static int w(Throwable th) {
        write2File(getStackTraceString(th));
        if (ENABLE)
            return Log.w(TAG, th);
        return 0;
    }

    public static int e(String msg) {
        if (ENABLE) {
           // write2File(msg);
            return Log.e(TAG, msg);
        }
        return 0;
    }

    public static int le(String msg) {
        if (ENABLE) {
            write2LeFile(msg);
            return Log.e(TAG, msg);
        }
        return 0;
    }

    public static int e(String msg, Throwable th) {
        write2File(msg);
        write2File(getStackTraceString(th));
        if (ENABLE)
            return Log.e(TAG, msg, th);
        return 0;
    }

    private static void write2File(String log) {
        if (LOG2FILE_ENABLE) {
            try {
                mWriter.write(log);
                mWriter.newLine();
                mWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void write2LeFile(String log) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:MM:ss ");
        String format1 = format.format(date);
        if (LE_LOG2FILE_ENABLE) {
            try {
                mWriter.write(format1 + ": \n" + log);
                mWriter.newLine();
                mWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void onCreate(String fileName) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getPath() + "/LogUtils");

        try {
            if (!dir.exists())
                dir.mkdir();
            File file = new File(dir, fileName);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            mWriter = new BufferedWriter(osw, 1024);
            mWriter.write(TAG + " begin : ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void onDestroy() {
        try {
            if (mWriter != null) {
                mWriter.write(TAG + " end : ");
                mWriter.close();
            }
        } catch (Exception e) {

        }
    }
}
