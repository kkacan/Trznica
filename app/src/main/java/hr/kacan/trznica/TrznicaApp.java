package hr.kacan.trznica;

import android.app.Application;
import android.content.Context;

public class TrznicaApp extends Application {

    private static TrznicaApp mInstance;

    public static Context getContext() {
        return mInstance;
    }
}
