package com.justwayward.book.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.justwayward.book.ConnectionChangeReceiver;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.ShareBean;
import com.justwayward.book.bean.SwitchBean;
import com.justwayward.book.bean.UserBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.manager.SettingManager;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.service.DownloadBookService;
import com.justwayward.book.ui.adapter.ViewPagerAdapter;
import com.justwayward.book.ui.fragment.BookCityFragment;
import com.justwayward.book.ui.fragment.BookShelfFragment;
import com.justwayward.book.ui.fragment.DiscoverFragment;
import com.justwayward.book.ui.fragment.MyFragment;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.view.AlphaTabsIndicator;
import com.justwayward.book.view.GenderPopupWindow;
import com.justwayward.book.view.OnTabChangedListner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class Main2Activity extends BaseActivity implements OnTabChangedListner {


    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    ViewPager viewPager;
    AlphaTabsIndicator mAlphaTabsIndicator;

    private List<Fragment> mList;
    private int target;
    private long firstTime = 0;
    private GenderPopupWindow genderPopupWindow;
    ViewPagerAdapter adapter;
    private ConnectionChangeReceiver myReceiver;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {


    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setVisibility(View.GONE);
    }

    @Override
    public void initDatas() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mAlphaTabsIndicator = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator);
        viewPager.setOffscreenPageLimit(4);
        startService(new Intent(this, DownloadBookService.class));
        getUser();
        getUrl();


        EventBus.getDefault().register(this);
        mList = new ArrayList<>();

        mList.add(new BookShelfFragment());
        mList.add(new BookCityFragment());
        mList.add(new DiscoverFragment());
        mList.add(new MyFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this, mList);
        viewPager.setAdapter(adapter);

        mAlphaTabsIndicator.setViewPager(viewPager);
        mAlphaTabsIndicator.setOnTabChangedListner(this);
        mAlphaTabsIndicator.removeAllBadge();
        mAlphaTabsIndicator.setTabCurrenItem(0);
        registerReceiver();

    }


    @Override
    public void configViews() {
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {//两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void showChooseSexPopupWindow() {
        if (genderPopupWindow == null) {
            genderPopupWindow = new GenderPopupWindow(Main2Activity.this);
        }
        if (!SettingManager.getInstance().isUserChooseSex()
                && !genderPopupWindow.isShowing()) {
            genderPopupWindow.showAtLocation(mCommonToolbar, Gravity.CENTER, 0, 0);
        }
    }

    @Subscribe
    public void change(String tab) {//切换底部栏目
        if (tab.equals("1")) {
            target = 1;

        } else if (tab.equals("2")) {
            target = 2;
        }

        mAlphaTabsIndicator.setTabCurrenItem(target);
    }

    /**
     * 获取用户信息
     */
    private void getUser() {
        RetrofitClient.getInstance().createApi().getUser(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<UserBean>>io_main())
                .subscribe(new BaseObjObserver<UserBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(UserBean userBean) {

                        long create_time = userBean.getCreate_time();//到期时间
                        Date currentTime = new Date();
                        long diff = currentTime.getTime() - create_time * 1000;//注册时间
                        long days = diff / (1000 * 60 * 60 * 24);
                        LogUtils.e("注册时间：" + create_time);
                        userBean.getId();

                        ReaderApplication.user = userBean;
                        ReaderApplication.days = days;
                        int like_type = userBean.getLike_type();
                        if (like_type == 0) {//未设置喜好
                            showChooseSexPopupWindow();
                        }
                    }
                });
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }

    /**
     * 获取分享链接
     */
    private void getUrl() {
        RetrofitClient.getInstance().createApi().getShareUrl("")
                .compose(RxUtils.<HttpResult<ShareBean>>io_main())
                .subscribe(new BaseObjObserver<ShareBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(ShareBean shareBean) {
                        ReaderApplication.shareUrl = shareBean.getShare_url();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMemberSwitch();
    }

    /**
     * 获取会员开关
     */
    private void getMemberSwitch() {
        RetrofitClient.getInstance().createApi().getMemberSwitch(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<SwitchBean>>io_main())
                .subscribe(new BaseObjObserver<SwitchBean>(this, "获取中") {
                    @Override
                    protected void onHandleSuccess(SwitchBean switchBean) {
                        ReaderApplication.vipIsOpen = switchBean.isMember_switch();
                    }});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        this.unregisterReceiver(myReceiver);
    }

    @Override
    public void onTabSelected(int tabNum) {
        viewPager.setCurrentItem(tabNum);
    }
}
