package com.justwayward.book.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.ui.activity.login.LoginActivity;
import com.justwayward.book.utils.CountDownUtils;
import com.justwayward.book.utils.SharedPreferencesUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements CountDownUtils.CountdownListener {

    @Bind(R.id.tvSkip)
    TextView tvSkip;

    private boolean flag = false;
    private Runnable runnable;
    private CountDownUtils countDown;

    /**
     * Android6.0以上的权限适配简单示例：
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE))) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION))) {
            lackedPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (lackedPermission.size() == 0) {
            // 权限都已经有了，那么直接调用SDK
            fetchSplashAD();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1000);
        }
    }


    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && hasAllPermissionsGranted(grantResults)) {
            fetchSplashAD();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    private void fetchSplashAD() {
        FrameLayout adsParent = (FrameLayout) this.findViewById(R.id.layout);
        SplashAdListener listener = new SplashAdListener() {
            @Override
            public void onAdDismissed() {
                Log.i("RSplashActivity", "onAdDismissed");
                jumpWhenCanClick(); // 跳转至您的应用主界面
            }

            @Override
            public void onAdFailed(String arg0) {
                Log.i("RSplashActivity", "onAdFailed");
                jump();
            }

            @Override
            public void onAdPresent() {
                tvSkip.setVisibility(View.VISIBLE);
                Log.i("RSplashActivity", "onAdPresent");

//                countDown.start();
            }

            @Override
            public void onAdClick() {
                Log.i("RSplashActivity", "onAdClick");
                // 设置开屏可接受点击时，该回调可用
            }
        };
        String adPlaceId = "2058622"; // 重要：请填上您的广告位ID，代码位错误会导致无法请求到广告
        new SplashAd(this, adsParent, listener, adPlaceId, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        countDown = new CountDownUtils(tvSkip, "%s秒", 5);
        countDown.setCountdownListener(this);

        PackageInfo info;
        int targetVersion = 0;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
            targetVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 如果targetSDKVersion >= 23, 并且运行环境在Android6.0版本之上, 就要申请好权限。
        // 否则, 只需要在这里直接调用fetchSplashAD接口
        if (Build.VERSION.SDK_INT >= 23 && targetVersion >= 23) {
            checkAndRequestPermission();
        } else {
            // 如果是Android6.0以下的机器, 或者targetSDKVersion < 23，默认在安装时获得了所有权限，可以直接调用SDK
            fetchSplashAD();
        }

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpWhenCanClick(); // 跳转至您的应用主界面
            }
        });
    }

    private synchronized void goHome() {
        if (!flag) {
            flag = true;
            String token = SharedPreferencesUtil.getInstance().getString("token");
            ReaderApplication.uid = SharedPreferencesUtil.getInstance().getString("uid");

            if (TextUtils.isEmpty(token)) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, Main2Activity.class));
            }

            finish();
        }
    }


    /**
     * 当设置开屏可点击时，需要等待跳转页面关闭后，再切换至您的主窗口。故此时需要增加canJumpImmediately判断。 另外，点击开屏还需要在onResume中
     * 调用jumpWhenCanClick接口。
     */
    public boolean canJumpImmediately = false;

    private void jumpWhenCanClick() {
        Log.d("test", "this.hasWindowFocus():" + this.hasWindowFocus());
        if (canJumpImmediately) {
            this.startActivity(new Intent(SplashActivity.this, Main2Activity.class));
            this.finish();
        } else {
            canJumpImmediately = true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        canJumpImmediately = false;
    }

    /**
     * 不可点击的开屏，使用该jump方法，而不是用jumpWhenCanClick
     */
    private void jump() {
        this.startActivity(new Intent(SplashActivity.this, Main2Activity.class));
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJumpImmediately) {
            jumpWhenCanClick();
        }
        canJumpImmediately = true;
    }

    public static boolean checkSelfPermission(Context context, String permission) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                Method method = Context.class.getMethod("checkSelfPermission",
                        String.class);
                return (Integer) method.invoke(context, permission) == PackageManager.PERMISSION_GRANTED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onStartCount() {

    }

    @Override
    public void onFinishCount() {
        if (tvSkip == null) {
            return;
        }
        tvSkip.setText("跳过");
    }

    @Override
    public void onUpdateCount(int currentRemainingSeconds) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDown.isRunning()) {
            countDown.stop();
        }

        flag = true;
        tvSkip.removeCallbacks(runnable);
        ButterKnife.unbind(this);
    }
}


