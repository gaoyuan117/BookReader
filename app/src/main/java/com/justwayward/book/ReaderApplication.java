package com.justwayward.book;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;

import com.baidu.mobads.AppActivity;
import com.iflytek.cloud.SpeechUtility;
import com.justwayward.book.base.Constant;
import com.justwayward.book.base.CrashHandler;
import com.justwayward.book.bean.DaoMaster;
import com.justwayward.book.bean.DaoSession;
import com.justwayward.book.bean.UserBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.component.DaggerAppComponent;
import com.justwayward.book.module.AppModule;
import com.justwayward.book.module.BookApiModule;
import com.justwayward.book.utils.AppUtils;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.utils.ScreenUtils;
import com.justwayward.book.utils.SharedPreferencesUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

/**
 * @author yuyh.
 * @date 2016/8/3.
 */
public class ReaderApplication extends MultiDexApplication {

    private static ReaderApplication sInstance;
    private AppComponent appComponent;

    public static String token;
    public static String uid;
    public static UserBean user;
    private static DaoSession daoSession;
    public static int speed = 50;
    public static String vocher = "xiaoyan";
    public static String shareUrl;
    public static long days;//注册的时间
    public static boolean isvip = false;
    public static boolean net = false;
    public static boolean vipIsOpen = false;
    private RefWatcher refWatcher;


    public static RefWatcher getRefWatcher(Context context) {
        ReaderApplication application = (ReaderApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
        MultiDex.install(this);
        sInstance = this;
        initCompoent();
        AppUtils.init(this);
        refWatcher = LeakCanary.install(this);
        CrashHandler.getInstance().init(this);
        initPrefs();
        initNightMode();
        SpeechUtility.createUtility(this, "appid=" + "5a1a5dc3");
        String token = SharedPreferencesUtil.getInstance().getString("token");
        if (!TextUtils.isEmpty(token)) {
            ReaderApplication.token = token;
        }
        setupDatabase();

        AppActivity.setActionBarColorTheme(AppActivity.ActionBarColorTheme.ACTION_BAR_GREEN_THEME);

    }

    public static ReaderApplication getsInstance() {
        return sInstance;
    }

    private void initCompoent() {
        appComponent = DaggerAppComponent.builder()
                .bookApiModule(new BookApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

    protected void initNightMode() {
        boolean isNight = SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false);
        LogUtils.d("isNight=" + isNight);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "GreenDao.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }



}
