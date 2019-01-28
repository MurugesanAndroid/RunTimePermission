package mis.tvscs.com.runtimepermission.utility;

import android.util.Log;

public class CommonLog {
    public static final int LOG_TYPE_VERBOSE = 1;
    public static final int LOG_TYPE_DEBUG = 2;
    public static final int LOG_TYPE_INFO = 3;
    public static final int LOG_TYPE_WARN = 4;
    public static final int LOG_TYPE_ERROR = 5;

    public static void createLog(int logType, String tagName, String message) {
        switch (logType) {
            case LOG_TYPE_VERBOSE:
                Log.v(tagName, message);
                break;
            case LOG_TYPE_DEBUG:
                Log.d(tagName, message);
                break;
            case LOG_TYPE_INFO:
                Log.i(tagName, message);
                break;
            case LOG_TYPE_WARN:
                Log.w(tagName, message);
                break;
            case LOG_TYPE_ERROR:
                Log.e(tagName, message);
                break;
        }
    }
}
