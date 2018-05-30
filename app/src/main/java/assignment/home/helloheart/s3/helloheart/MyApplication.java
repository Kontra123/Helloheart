package assignment.home.helloheart.s3.helloheart;

import android.app.Application;
import android.content.Context;

/**
 * Created by AmirG on 5/29/2018.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

}
