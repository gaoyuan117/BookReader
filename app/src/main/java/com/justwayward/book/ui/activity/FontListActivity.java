package com.justwayward.book.ui.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.book.R;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.base.Constant;
import com.justwayward.book.bean.FontListBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.FontListAdapter;
import com.justwayward.book.utils.AppUtils;
import com.justwayward.book.utils.FileUtils;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.utils.SharedPreferencesUtil;
import com.justwayward.book.utils.ToastUtils;
import com.justwayward.book.view.recyclerview.decoration.DividerDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FontListActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    private FontListAdapter mAdapter;
    private List<FontListBean> mList = new ArrayList<>();
    private DownloadManager downloadManager;
    private long mTaskId;
    private String path;


    @Override
    public int getLayoutId() {
        return R.layout.activity_font_list;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        mAdapter = new FontListAdapter(R.layout.item_font_list, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("字体选择");
    }

    @Override
    public void initDatas() {
        getFontList();
    }

    @Override
    public void configViews() {
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    private void getFontList() {
        RetrofitClient.getInstance().createApi().getFontList("")
                .compose(RxUtils.<HttpResult<List<FontListBean>>>io_main())
                .subscribe(new BaseObjObserver<List<FontListBean>>(this) {
                    @Override
                    protected void onHandleSuccess(List<FontListBean> list) {
                        mList.clear();

                        if (list == null || list.size() == 0) {
                            return;
                        }
                        mList.add(new FontListBean());
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        register();
        String path = mList.get(position).getPath();
        downloadFont(path);
    }


    //使用系统下载器下载
    private void downloadFont(String versionUrl) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir(getPackageName() + File.separator, getName(versionUrl));
        //将下载请求加入下载队列
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request);

        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };


    public static String getName(String s) {
        String s1 = s.replaceAll("/", "");
        String replace = s1.replace("\\", "");
        String[] fonts = replace.split("fonts");
        return fonts[1];
    }


    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    ToastUtils.showToast("下载暂停");
                case DownloadManager.STATUS_PENDING:
                    LogUtils.i(">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    ToastUtils.showToast("开始下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    ToastUtils.showToast("下载完成");
                    mAdapter.notifyDataSetChanged();
                    break;
                case DownloadManager.STATUS_FAILED:
                    ToastUtils.showToast("下载失败");
                    break;
            }
        }
    }

    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        TextView tvLoading = (TextView) view.findViewById(R.id.tv_loading);
        TextView tvDownload = (TextView) view.findViewById(R.id.tv_down_load);
        if (isVisible(tvLoading) || isVisible(tvDownload)) {
            ToastUtils.showSingleToast("请先下载，然后使用");
            return;
        }
        if (position == 0) {
            saveFontType("");
        } else {
            saveFontType(getName(mList.get(position).getPath()));
        }

        setResult(RESULT_OK);
        finish();
    }

    public void saveFontType(String path) { //搜索目录，扩展名，是否进入子文件夹
        if (TextUtils.isEmpty(path)) {
            SharedPreferencesUtil.getInstance().putString("font", "");
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/" + mContext.getPackageName());
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {

            File f = files[i];
            if (f.isFile()) {
                if (f.getName().equals(path)) { //判断扩展名
                    LogUtils.e("保存的字体类型：" + f.getAbsolutePath());
                    SharedPreferencesUtil.getInstance().putString("font", f.getAbsolutePath());
                }
            }
        }
    }
}
