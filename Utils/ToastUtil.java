
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtil {
    private static Context context;

    private static Toast toast;
    //  初始化上下文
    public static void initUtils(MyApplication myApplication) {
        context = myApplication;
    }

    public static void showToast( String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }

    public static void showToast( String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showToast( @StringRes int textId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(textId), duration);
        } else {
            toast.setText(context.getString(textId));
            toast.setDuration(duration);
        }
        toast.show();
    }

    public static void showToast( @StringRes int textId) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(textId), Toast.LENGTH_SHORT);
        } else {
            toast.setText(context.getString(textId));
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
