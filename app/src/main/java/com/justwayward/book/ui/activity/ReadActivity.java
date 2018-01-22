package com.justwayward.book.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.WebViewActivity;
import com.justwayward.book.api.CoomonApi;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.base.Constant;
import com.justwayward.book.bean.BookBean;
import com.justwayward.book.bean.BookMixAToc;
import com.justwayward.book.bean.BookSourceBean;
import com.justwayward.book.bean.BookSourceBeanDao;
import com.justwayward.book.bean.ChapterBean;
import com.justwayward.book.bean.ChapterList;
import com.justwayward.book.bean.ChapterListBean;
import com.justwayward.book.bean.ChapterListDao;
import com.justwayward.book.bean.CommonBean;
import com.justwayward.book.bean.HistoryBean;
import com.justwayward.book.bean.HistoryBeanDao;
import com.justwayward.book.bean.SourceBean;
import com.justwayward.book.bean.TtsThemeBean;
import com.justwayward.book.bean.support.BookMark;
import com.justwayward.book.bean.support.DownloadMessage;
import com.justwayward.book.bean.support.DownloadProgress;
import com.justwayward.book.bean.support.DownloadQueue;
import com.justwayward.book.bean.support.ReadTheme;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.component.DaggerBookComponent;
import com.justwayward.book.manager.CacheManager;
import com.justwayward.book.manager.CollectionsManager;
import com.justwayward.book.manager.EventManager;
import com.justwayward.book.manager.SettingManager;
import com.justwayward.book.manager.ThemeManager;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.service.DownloadBookService;
import com.justwayward.book.ui.adapter.BookMarkAdapter;
import com.justwayward.book.ui.adapter.TocListAdapter;
import com.justwayward.book.ui.adapter.TtsAdapter;
import com.justwayward.book.ui.contract.BookReadContract;
import com.justwayward.book.ui.easyadapter.ReadThemeAdapter;
import com.justwayward.book.ui.presenter.BookReadPresenter;
import com.justwayward.book.utils.AppUtils;
import com.justwayward.book.utils.FileUtils;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.utils.ScreenUtils;
import com.justwayward.book.utils.SharedPreferencesUtil;
import com.justwayward.book.utils.ToastUtils;
import com.justwayward.book.utils.TtsUtis;
import com.justwayward.book.view.readview.BaseReadView;
import com.justwayward.book.view.readview.NoAimWidget;
import com.justwayward.book.view.readview.OnReadStateChangeListener;
import com.justwayward.book.view.readview.OverlappedWidget;
import com.justwayward.book.view.readview.PageWidget;
import com.justwayward.book.view.recyclerview.decoration.DividerDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.functions.Action1;

/**
 * Created by lfh on 2016/9/18.
 */
public class ReadActivity extends BaseActivity implements BookReadContract.View, View.OnClickListener, PlatformActionListener {

    @Bind(R.id.ivBack)
    ImageView mIvBack;
    @Bind(R.id.tvBookReadReading)
    TextView mTvBookReadReading;
    @Bind(R.id.tvBookReadCommunity)
    TextView mTvBookReadCommunity;
    @Bind(R.id.tvBookReadIntroduce)
    TextView mTvBookReadChangeSource;
    @Bind(R.id.tvBookReadSource)
    TextView mTvBookReadSource;
    @Bind(R.id.flReadWidget)
    FrameLayout flReadWidget;
    @Bind(R.id.llBookReadTop)
    LinearLayout mLlBookReadTop;
    @Bind(R.id.tvBookReadTocTitle)
    TextView mTvBookReadTocTitle;
    @Bind(R.id.tvBookReadMode)
    TextView mTvBookReadMode;
    @Bind(R.id.tvBookReadSettings)
    TextView mTvBookReadSettings;
    @Bind(R.id.tvBookReadDownload)
    TextView mTvBookReadDownload;
    @Bind(R.id.tvBookReadToc)
    TextView mTvBookReadToc;
    @Bind(R.id.llBookReadBottom)
    LinearLayout mLlBookReadBottom;
    @Bind(R.id.rlBookReadRoot)
    RelativeLayout mRlBookReadRoot;
    @Bind(R.id.tvDownloadProgress)
    TextView mTvDownloadProgress;
    @Bind(R.id.rlReadAaSet)
    LinearLayout rlReadAaSet;
    @Bind(R.id.ivBrightnessMinus)
    ImageView ivBrightnessMinus;
    @Bind(R.id.seekbarLightness)
    SeekBar seekbarLightness;
    @Bind(R.id.ivBrightnessPlus)
    ImageView ivBrightnessPlus;
    @Bind(R.id.tvFontsizeMinus)
    TextView tvFontsizeMinus;
    @Bind(R.id.seekbarFontSize)
    SeekBar seekbarFontSize;
    @Bind(R.id.tvFontsizePlus)
    TextView tvFontsizePlus;
    @Bind(R.id.rlReadMark)
    LinearLayout rlReadMark;
    @Bind(R.id.tvAddMark)
    TextView tvAddMark;
    @Bind(R.id.lvMark)
    ListView lvMark;
    @Bind(R.id.cbVolume)
    CheckBox cbVolume;
    @Bind(R.id.cbAutoBrightness)
    CheckBox cbAutoBrightness;
    @Bind(R.id.gvTheme)
    GridView gvTheme;
    @Bind(R.id.tv_over)
    TextView tvOver;
    @Bind(R.id.tv_no)
    TextView tvNo;
    @Bind(R.id.tv_page)
    TextView tvPage;
    @Bind(R.id.tv_source)
    TextView tv_source;
    @Bind(R.id.tvMore)
    TextView tvMore;
    @Bind(R.id.tv_auto_light)
    TextView tvAutoLight;
    @Bind(R.id.tvFontSize)
    TextView tvFontSize;

    private View decodeView;
    private SharedPreferences sp;

    @Inject
    BookReadPresenter mPresenter;

    private List<ChapterListBean> mChapterList = new ArrayList<>();
    private ListPopupWindow mTocListPopupWindow;
    private TocListAdapter mTocListAdapter;
    private List<BookMark> mMarkList;
    private BookMarkAdapter mMarkAdapter;
    private int currentChapter = 0;
    private int currentPosition = 0;

    /**
     * 是否开始阅读章节
     **/
    private boolean startRead = false;
    private BaseReadView mPageWidget;
    private int curTheme = -1;
    private List<ReadTheme> themes;
    private ReadThemeAdapter gvAdapter;
    private Receiver receiver = new Receiver();
    private IntentFilter intentFilter = new IntentFilter();
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public static final String INTENT_BEAN = "recommendBooksBean";
    public static final String INTENT_SD = "isFromSD";

    private String title, des, author, bookId;

    private boolean isAutoLightness = false; // 记录其他页面是否自动调整亮度
    private boolean isFromSD = false;
    private boolean isShelf = false;
    public static TtsUtis ttsUtis;
    private String pic;//书籍图片
    private HistoryBeanDao historyBeanDao;
    private List<TtsThemeBean> list = new ArrayList<>();
    private boolean ttsIsChange = false;
    private PopupWindow window;
    private PopupWindow timeWindow;
    private int time;//定时器
    private Disposable disposable, readDisposable;
    private TextView tvTime;
    private SeekBar speedSeekBar;
    private RecyclerView ttsRecycler;
    private TextView tvCloseTts;
    private TtsAdapter ttsAdapter;
    private AlertDialog shareDialog;
    private PopupWindow sharePopWindow;

    private String currentDate;
    private ChapterListDao chapterListDao;
    private BookSourceBeanDao bookSourceBeanDao;
    private String source, source_url;
    private PopupWindow morePopWindow;
    private AdView adView;
    private PopupWindow sharePopWindow2;
    private int notify;
    private Disposable notifyDisposable;

    //添加收藏需要，所以跳转的时候传递整个实体类
    public static void startActivity(Context context, String title, String novelId, boolean isShelf, String pic, String author, String des) {
        startActivity(context, title, novelId, isShelf, false, pic, author, des);
    }

    public static void startActivity(Context context, String title, String novelId, boolean isShelf, boolean isFromSD, String pic, String author, String des) {
        context.startActivity(new Intent(context, ReadActivity.class)
                .putExtra("tile", title)
                .putExtra("novel_id", novelId)
                .putExtra("isShelf", isShelf)
                .putExtra("pic", pic)
                .putExtra("des", des)
                .putExtra("author", author)
                .putExtra(INTENT_SD, isFromSD));
    }

    @Override
    public int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        statusBarColor = ContextCompat.getColor(this, R.color.reader_menu_bg_color);
        return R.layout.activity_read;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBookComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {
        //阅读休息提醒
        notify = SharedPreferencesUtil.getInstance().getInt(Constant.NOTIFYPOSITION);
        notifytTime();

        sp = getSharedPreferences("tts", MODE_PRIVATE);

        if (ReaderApplication.days > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = format.format(new Date());
            String oldDate = sp.getString(currentDate, "");
            if (TextUtils.isEmpty(oldDate) || !currentDate.equals(oldDate)) { //如果为空，今天没有分享，开始阅读计时
                readTime();
            }
        }

        addTtsList();
        historyBeanDao = ReaderApplication.getDaoInstant().getHistoryBeanDao();
        chapterListDao = ReaderApplication.getDaoInstant().getChapterListDao();
        bookSourceBeanDao = ReaderApplication.getDaoInstant().getBookSourceBeanDao();

        isFromSD = getIntent().getBooleanExtra(INTENT_SD, false);
        bookId = getIntent().getStringExtra("novel_id");
        title = getIntent().getStringExtra("tile");
        isShelf = getIntent().getBooleanExtra("isShelf", false);
        pic = getIntent().getStringExtra("pic");
        des = getIntent().getStringExtra("des");
        author = getIntent().getStringExtra("author");
        updateReadTime();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        showDialog();

        mTvBookReadTocTitle.setText(title);

        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);

        CollectionsManager.getInstance().setRecentReadingTime(bookId);

    }

    @Override
    public void configViews() {
        hideStatusBar();
        decodeView = getWindow().getDecorView();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLlBookReadTop.getLayoutParams();
        params.topMargin = ScreenUtils.getStatusBarHeight(this) - 2;
        mLlBookReadTop.setLayoutParams(params);

        initTocList();

        initAASet();

        initPagerWidget();

        mPresenter.attachView(this);
        // 本地收藏  直接打开
        if (isFromSD) {
            BookMixAToc.mixToc.Chapters chapters = new BookMixAToc.mixToc.Chapters();
            chapters.title = title;
//            mChapterList.add(chapters);
//            showChapterRead(null, currentChapter);
            //本地书籍隐藏社区、简介、缓存按钮
            gone(mTvBookReadCommunity, mTvBookReadChangeSource, mTvBookReadDownload);
            return;
        }
        if (!ReaderApplication.net) {//无网络
            ChapterBean chapterBean = null;
            mChapterList.clear();
            List<ChapterList> list1 = chapterListDao.queryBuilder().where(ChapterListDao.Properties.BookId.eq(bookId)).build().list();
            if (list1 != null && list1.size() > 0) {
                for (int i = 0; i < list1.size(); i++) {
                    chapterBean = new Gson().fromJson(list1.get(i).getJs(), ChapterBean.class);
                }
                if (chapterBean.list == null) {
                    return;
                }
                mChapterList.addAll(chapterBean.list);
                readCurrentChapter();
            } else {
                ToastUtils.showToast("暂无缓存");
            }

        } else {
            mPresenter.getBookMixAToc(bookId, "chapters");
        }
    }

    /**
     * 历史记录
     */
    private void addHistory() {
        HistoryBean bean = new HistoryBean();
        bean.setBookId(bookId);
        bean.setPic(pic);
        bean.setTitle(title);
        bean.setAuthor(mChapterList.get(currentChapter).getChapter());
        bean.setDes(des);
        bean.setIsShelf(isShelf);
        historyBeanDao.insertOrReplace(bean);
    }

    /**
     * 目录
     */
    private void initTocList() {
        mTocListAdapter = new TocListAdapter(this, mChapterList, bookId, currentChapter);
        mTocListPopupWindow = new ListPopupWindow(this);
        mTocListPopupWindow.setAdapter(mTocListAdapter);
        mTocListPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mTocListPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mTocListPopupWindow.setAnchorView(mLlBookReadTop);
        mTocListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("gy", "position：" + position);
                mTocListPopupWindow.dismiss();
                currentChapter = position;
                currentPosition = position;
                mTocListAdapter.setCurrentChapter(position);
                startRead = false;
                showDialog();
                readCurrentChapter();
                hideReadBar();
            }
        });
        mTocListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                gone(mTvBookReadTocTitle);
                visible(mTvBookReadReading, mTvBookReadCommunity, mTvBookReadChangeSource);
            }
        });
    }

    /**
     * 时刻监听系统亮度改变事件
     */
    private ContentObserver Brightness = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //LogUtils.d("BrightnessOnChange:" + ScreenUtils.getScreenBrightnessInt255());
            try {

            } catch (Exception e) {
                if (!ScreenUtils.isAutoBrightness(ReadActivity.this)) {
                    seekbarLightness.setProgress(ScreenUtils.getScreenBrightness());
                }
            }

        }
    };

    /**
     * 字体
     */
    private void initAASet() {
        curTheme = SettingManager.getInstance().getReadTheme();
        ThemeManager.setReaderTheme(curTheme, mRlBookReadRoot);

        seekbarFontSize.setMax(15);
        //int fontSizePx = SettingManager.getInstance().getReadFontSize(bookId);
        int fontSizePx = SettingManager.getInstance().getReadFontSize();
        int progress = (int) ((ScreenUtils.pxToDpInt(fontSizePx) - 12) / 1.7f);
        seekbarFontSize.setProgress(progress);
        tvFontSize.setText((int) ScreenUtils.pxToSp(ScreenUtils.dpToPxInt(12 + 1.7f * progress)) + "");
        seekbarFontSize.setOnSeekBarChangeListener(new SeekBarChangeListener());

        seekbarLightness.setMax(100);
        seekbarLightness.setOnSeekBarChangeListener(new SeekBarChangeListener());
        seekbarLightness.setProgress(ScreenUtils.getScreenBrightness());
        isAutoLightness = ScreenUtils.isAutoBrightness(this);


        this.getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, Brightness);

        if (SettingManager.getInstance().isAutoBrightness()) {
            startAutoLightness();
        } else {
            stopAutoLightness();
        }


        cbVolume.setChecked(SettingManager.getInstance().isVolumeFlipEnable());
        cbVolume.setOnCheckedChangeListener(new ChechBoxChangeListener());

        isAutoLight(SettingManager.getInstance().isAutoBrightness());


        cbAutoBrightness.setChecked(SettingManager.getInstance().isAutoBrightness());
        cbAutoBrightness.setOnCheckedChangeListener(new ChechBoxChangeListener());

        gvAdapter = new ReadThemeAdapter(this, (themes = ThemeManager.getReaderThemeData(curTheme)), curTheme);
        gvTheme.setAdapter(gvAdapter);
        gvTheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < themes.size() - 1) {
                    changedMode(false, position);
                } else {
                    changedMode(true, position);
                }
            }
        });
    }

    /**
     * 初始化阅读器
     */
    private void initPagerWidget() {
        switch (SharedPreferencesUtil.getInstance().getInt(Constant.FLIP_STYLE, 0)) {
            case 0:
                tvPage.setSelected(true);
                tvNo.setSelected(false);
                tvOver.setSelected(false);
                mPageWidget = new PageWidget(this, bookId, mChapterList, new ReadListener());
                break;
            case 1:
                tvPage.setSelected(false);
                tvNo.setSelected(false);
                tvOver.setSelected(true);
                mPageWidget = new OverlappedWidget(this, bookId, mChapterList, new ReadListener());
                break;
            case 2:
                tvPage.setSelected(false);
                tvNo.setSelected(true);
                tvOver.setSelected(false);
                mPageWidget = new NoAimWidget(this, bookId, mChapterList, new ReadListener());
        }

        registerReceiver(receiver, intentFilter);
        if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)) {
            mPageWidget.setTextColor(ContextCompat.getColor(this, R.color.chapter_content_night),
                    ContextCompat.getColor(this, R.color.chapter_title_night));
        }
        flReadWidget.removeAllViews();
        flReadWidget.addView(mPageWidget);
    }

    /**
     * 加载章节列表
     *
     * @param list
     */
    @Override
    public void showBookToc(List<ChapterListBean> list) {
        mChapterList.clear();
        mChapterList.addAll(list);
        readCurrentChapter();

        int pos[] = SettingManager.getInstance().getReadProgress(bookId);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == pos[0]) {
                currentPosition = i;
            }
        }
        ChapterBean bean = new ChapterBean();
        bean.list = list;
        ChapterList chapter = new ChapterList();
        chapter.setBookId(bookId);
        chapter.setJs(new Gson().toJson(bean));
        chapterListDao.insertOrReplace(chapter);
        addHistory();

    }

    /**
     * 获取当前章节。章节文件存在则直接阅读，不存在则请求
     */
    public void readCurrentChapter() {

        File chapterFile = CacheManager.getInstance().getChapterFile(bookId, mChapterList.get(currentChapter).getId());
        if (chapterFile != null && chapterFile.length() > 10) {
            showChapterRead(null, mChapterList.get(currentChapter).getId());
        } else {
            mPresenter.getChapterRead(mChapterList.get(currentChapter).getId() + "", source);
        }
    }

    @Override
    public synchronized void showChapterRead(BookBean bookBean, int chapter) { // 加载章节内容
        int pos[] = SettingManager.getInstance().getReadProgress(bookId);
        if (pos[1] == 0 && pos[2] == 0) {
            SettingManager.getInstance().saveReadProgress(bookId, mChapterList.get(0).getId(), 0, 0);
        }

        if (bookBean != null) {
            Log.e("gy", "bookId：" + bookId + " chapter:" + chapter);
            CacheManager.getInstance().saveChapterFile(bookId, chapter, bookBean);
            BookSourceBean bookSourceBean = new BookSourceBean();
            bookSourceBean.setBookId(bookId);
            bookSourceBean.setNovelId(chapter + "");
            bookSourceBean.setSource(bookBean.getSource_url());
            bookSourceBeanDao.insert(bookSourceBean);
            source_url = bookBean.getSource_url();
        } else {
            List<BookSourceBean> list = bookSourceBeanDao.queryBuilder().where(BookSourceBeanDao.Properties.NovelId.eq(chapter + "")).build().list();
            if (list != null && list.size() > 0) {
                source_url = list.get(0).getSource();
            }
        }
        if (TextUtils.isEmpty(source_url)) {
            tv_source.setVisibility(View.GONE);
        } else {
            tv_source.setVisibility(View.VISIBLE);
            tv_source.setText(source_url + "原网页阅读");
        }

        if (!startRead) {
            startRead = true;
            int current = -1;

            for (int i1 = 0; i1 < mChapterList.size(); i1++) {
                if (mChapterList.get(i1).getId() == chapter) {
                    current = i1;
                }
            }
            currentChapter = current;

            if (!mPageWidget.isPrepared) {
                mPageWidget.init(curTheme);
            } else {
                mPageWidget.jumpToChapter(chapter);
            }
            hideDialog();
        }
    }

    @Override
    public void netError(int chapter) {
        hideDialog();//防止因为网络问题而出现dialog不消失
//        if(chapter==0){
//            isNetError = true;
//            showChapterRead(null,0);
//            ToastUtils.showToast(R.string.net_error);
//            return;
//        }

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError() {
        hideDialog();
    }

    @Override
    public void complete() {
        hideDialog();
    }

    private synchronized void hideReadBar() {
        gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
        hideStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    private synchronized void showReadBar() { // 显示工具栏
        visible(mLlBookReadBottom, mLlBookReadTop);
        showStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private synchronized void toggleReadBar() { // 切换工具栏 隐藏/显示 状态
        if (isVisible(mLlBookReadTop)) {
            hideReadBar();
        } else {
            showReadBar();
        }
    }

    /***************Title Bar*****************/

    @OnClick(R.id.tv_page)
    public void pageWeight() {//仿真
        tvOver.setSelected(false);
        tvNo.setSelected(false);
        tvPage.setSelected(true);
        SharedPreferencesUtil.getInstance().putInt(Constant.FLIP_STYLE, 0);
        finish();
        ReadActivity.startActivity(this, title, bookId, isShelf, pic, author, des);
    }

    @OnClick(R.id.tvMore)
    public void tvMore() {//更多
        showMorePopWindow();
    }

    @OnClick(R.id.tv_source)
    public void tvSource() {//换源
        LogUtils.e("source_ur：" + source_url);
        if (TextUtils.isEmpty(source_url)) return;
        CoomonApi.toBrowser(this, source_url);
    }

    @OnClick(R.id.tv_no)
    public void noWeight() {//简洁
        tvOver.setSelected(false);
        tvNo.setSelected(true);
        tvPage.setSelected(false);
        SharedPreferencesUtil.getInstance().putInt(Constant.FLIP_STYLE, 2);
        finish();
        ReadActivity.startActivity(this, title, bookId, isShelf, pic, author, des);
    }

    @OnClick(R.id.tv_over)
    public void overWeight() {//滑动
        tvOver.setSelected(true);
        tvNo.setSelected(false);
        tvPage.setSelected(false);
        SharedPreferencesUtil.getInstance().putInt(Constant.FLIP_STYLE, 1);
        finish();
        ReadActivity.startActivity(this, title, bookId, isShelf, pic, author, des);
    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        if (mTocListPopupWindow.isShowing()) {
            mTocListPopupWindow.dismiss();
        } else if (!isShelf) {
            showJoinBookShelfDialog();
        } else {
            finish();
        }
    }

    @OnClick(R.id.tvBookReadReading)
    public void readBook() {
        hideStatusBar();
        gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
        ReaderApplication.speed = sp.getInt("speed", 50);
        int position = sp.getInt("voicer", 0);
        ReaderApplication.vocher = list.get(position).voicer;
        if (ttsUtis == null) {
            ttsUtis = TtsUtis.getInstance();
        }
        if (ttsUtis.isPlay() && !mPageWidget.getCurrentText().equals(ttsUtis.getCurrentText())) {//正在播放，播放不同的小说
            ttsUtis.pause();
            ttsUtis.start(mPageWidget.getCurrentText());

        } else if (ttsUtis.isPlay() && mPageWidget.getCurrentText().equals(ttsUtis.getCurrentText())) {//正在播放，播放相同的小说
            showTts();
            setBackgroundAlpha(ReadActivity.this, 0.8f);
        } else if (!ttsUtis.isPlay()) {//没有播放
            ttsUtis.play(mPageWidget.getCurrentText(), mTtsListener);
        }

    }

    @OnClick(R.id.tvBookReadCommunity)
    public void onClickCommunity() {
        gone(rlReadAaSet, rlReadMark);
        CoomonApi.share(this, "", this);
    }

    @OnClick(R.id.tvBookReadIntroduce)
    public void onClickIntroduce() {
        gone(rlReadAaSet, rlReadMark);
        BookDetailActivity.startActivity(mContext, bookId);
    }

    @OnClick(R.id.tvBookReadSource)
    public void onClickSource() {//换源

        BookSourceActivity.start(this, bookId, mChapterList.get(currentChapter).getChapter(), 1);
    }

    @OnClick(R.id.tv_auto_light)
    public void autoLight() {
        isAutoLight(!SettingManager.getInstance().isAutoBrightness());
    }

    /***************Bottom Bar*****************/

    @OnClick(R.id.tvBookReadMode)
    public void onClickChangeMode() { // 日/夜间模式切换
        gone(rlReadAaSet, rlReadMark);

        boolean isNight = !SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false);
        changedMode(isNight, -1);
    }

    /**
     * 切换日间夜间
     *
     * @param isNight
     * @param position
     */
    private void changedMode(boolean isNight, int position) {
        SharedPreferencesUtil.getInstance().putBoolean(Constant.ISNIGHT, isNight);
        AppCompatDelegate.setDefaultNightMode(isNight ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);

        if (position >= 0) {
            curTheme = position;
        } else {
            curTheme = SettingManager.getInstance().getReadTheme();
        }
        gvAdapter.select(curTheme);

        mPageWidget.setTheme(isNight ? ThemeManager.NIGHT : curTheme);

        mPageWidget.setTextColor(ContextCompat.getColor(mContext, isNight ? R.color.chapter_content_night : R.color.chapter_content_day),
                ContextCompat.getColor(mContext, isNight ? R.color.chapter_title_night : R.color.chapter_title_day));

        mTvBookReadMode.setText(getString(isNight ? R.string.book_read_mode_day_manual_setting
                : R.string.book_read_mode_night_manual_setting));
        Drawable drawable = ContextCompat.getDrawable(this, isNight ? R.drawable.ic_menu_mode_day_manual
                : R.drawable.ic_menu_mode_night_manual);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTvBookReadMode.setCompoundDrawables(null, drawable, null, null);

        ThemeManager.setReaderTheme(curTheme, mRlBookReadRoot);
    }

    @OnClick(R.id.tvBookReadSettings)
    public void setting() {
        gone(mLlBookReadBottom);
        visible(rlReadAaSet);
//        if (isVisible(mLlBookReadBottom)) {
//            if (isVisible(rlReadAaSet)) {
//                gone(rlReadAaSet);
//            } else {
//                visible(rlReadAaSet);
//                gone(rlReadMark);
//            }
//        }
    }

    @OnClick(R.id.tvBookReadDownload)
    public void downloadBook() {
        gone(rlReadAaSet);

        Log.e("gy", "mChapterList：" + mChapterList.size());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("缓存多少章？")
                .setItems(new String[]{"后面五十章", "后面全部", "全部"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, currentChapter + 1, currentChapter + 50));
                                break;
                            case 1:
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, currentChapter + 1, mChapterList.size()));
                                break;
                            case 2:
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, 1, mChapterList.size()));
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.show();
    }

    @OnClick(R.id.tvBookMark)
    public void onClickMark() {
        if (isVisible(mLlBookReadBottom)) {
            if (isVisible(rlReadMark)) {
                gone(rlReadMark);
            } else {
                gone(rlReadAaSet);

                updateMark();

                visible(rlReadMark);
            }
        }
    }

    @OnClick(R.id.tv_font_type)
    public void setFontType() {
        Intent intent = new Intent(this, FontListActivity.class);
        startActivityForResult(intent, 123);
//        mPageWidget.setTextType("");
    }

    @OnClick(R.id.tvBookReadToc)
    public void onClickToc() {
        gone(rlReadAaSet, rlReadMark);
        if (!mTocListPopupWindow.isShowing()) {
            visible(mTvBookReadTocTitle);
            gone(mTvBookReadReading, mTvBookReadCommunity, mTvBookReadChangeSource);
            mTocListPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mTocListPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mTocListPopupWindow.show();

            mTocListPopupWindow.setSelection(currentChapter);
            mTocListPopupWindow.getListView().setFastScrollEnabled(true);
        }
    }

    /***************Setting Menu*****************/

    @OnClick(R.id.ivBrightnessMinus)
    public void brightnessMinus() {
//        int curBrightness = SettingManager.getInstance().getReadBrightness();
//        if (curBrightness > 5 && !SettingManager.getInstance().isAutoBrightness()) {
//            seekbarLightness.setProgress((curBrightness = curBrightness - 2));
//            ScreenUtils.saveScreenBrightnessInt255(curBrightness, ReadActivity.this);
//        }
    }

    @OnClick(R.id.ivBrightnessPlus)
    public void brightnessPlus() {
//        int curBrightness = SettingManager.getInstance().getReadBrightness();
//        if (!SettingManager.getInstance().isAutoBrightness()) {
//            seekbarLightness.setProgress((curBrightness = curBrightness + 2));
//            ScreenUtils.saveScreenBrightnessInt255(curBrightness, ReadActivity.this);
//        }
    }

    @OnClick(R.id.tvFontsizeMinus)
    public void fontsizeMinus() {
        calcFontSize(seekbarFontSize.getProgress() - 1);
    }

    @OnClick(R.id.tvFontsizePlus)
    public void fontsizePlus() {
        calcFontSize(seekbarFontSize.getProgress() + 1);
    }

    @OnClick(R.id.tvClear)
    public void clearBookMark() {
        SettingManager.getInstance().clearBookMarks(bookId);

        updateMark();
    }

    /***************Book Mark*****************/

    @OnClick(R.id.tvAddMark)
    public void addBookMark() {
        int[] readPos = mPageWidget.getReadPos();
        BookMark mark = new BookMark();
        mark.chapter = readPos[0];
        mark.startPos = readPos[1];
        mark.endPos = readPos[2];
        if (mark.chapter >= 1 && mark.chapter <= mChapterList.size()) {
            mark.title = mChapterList.get(mark.chapter - 1).getChapter();
        }
        mark.desc = mPageWidget.getHeadLine();
        if (SettingManager.getInstance().addBookMark(bookId, mark)) {
            ToastUtils.showSingleToast("添加书签成功");
            updateMark();
        } else {
            ToastUtils.showSingleToast("书签已存在");
        }
    }

    private void updateMark() {
        if (mMarkAdapter == null) {
            mMarkAdapter = new BookMarkAdapter(this, new ArrayList<BookMark>());
            lvMark.setAdapter(mMarkAdapter);
            lvMark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BookMark mark = mMarkAdapter.getData(position);
                    if (mark != null) {
                        mPageWidget.setPosition(new int[]{mark.chapter, mark.startPos, mark.endPos});
                        hideReadBar();
                    } else {
                        ToastUtils.showSingleToast("书签无效");
                    }
                }
            });
        }
        mMarkAdapter.clear();

        mMarkList = SettingManager.getInstance().getBookMarks(bookId);
        if (mMarkList != null && mMarkList.size() > 0) {
            Collections.reverse(mMarkList);
            mMarkAdapter.addAll(mMarkList);
        }
    }

    /***************Event*****************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDownProgress(DownloadProgress progress) {
        if (bookId.equals(progress.bookId)) {
            if (isVisible(mLlBookReadBottom)) { // 如果工具栏显示，则进度条也显示
                visible(mTvDownloadProgress);
                // 如果之前缓存过，就给提示
                mTvDownloadProgress.setText(progress.message);
            } else {
                gone(mTvDownloadProgress);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadMessage(final DownloadMessage msg) {
        if (isVisible(mLlBookReadBottom)) { // 如果工具栏显示，则进度条也显示
            if (bookId.equals(msg.bookId)) {
                visible(mTvDownloadProgress);
                mTvDownloadProgress.setText(msg.message);
                if (msg.isComplete) {
                    mTvDownloadProgress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gone(mTvDownloadProgress);
                        }
                    }, 2500);
                }
            }
        }
    }

    /**
     * 显示加入书架对话框
     */
    private void showJoinBookShelfDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.book_read_add_book))
                .setMessage(getString(R.string.book_read_would_you_like_to_add_this_to_the_book_shelf))
                .setPositiveButton(getString(R.string.book_read_join_the_book_shelf), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        addNovel();
                    }
                })
                .setNegativeButton(getString(R.string.book_read_not), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                source = data.getStringExtra("source");
                LogUtils.e("源id：" + source);
                mPresenter.getChapterRead(bookId, source);
                break;
            case 123:
                LogUtils.e("字体选择");
                if (mPageWidget != null) {
                    mPageWidget.setTextType(getFontType());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (ttsUtis != null && ttsUtis.isPlay()) {
                    ttsUtis.stop();


                    ToastUtils.showToast("已退出语音朗读");
                    return true;
                }

                if (mTocListPopupWindow != null && mTocListPopupWindow.isShowing()) {
                    mTocListPopupWindow.dismiss();
                    gone(mTvBookReadTocTitle);
                    visible(mTvBookReadReading, mTvBookReadCommunity, mTvBookReadChangeSource);
                    return true;
                } else if (isVisible(rlReadAaSet)) {
                    gone(rlReadAaSet);
                    return true;
                } else if (isVisible(mLlBookReadBottom)) {
                    hideReadBar();
                    return true;
                } else if (!isShelf) {
                    showJoinBookShelfDialog();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                toggleReadBar();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (SettingManager.getInstance().isVolumeFlipEnable()) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (SettingManager.getInstance().isVolumeFlipEnable()) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (SettingManager.getInstance().isVolumeFlipEnable()) {
                if (ttsUtis.isPlay()) return true;
                mPageWidget.nextPage();
                return true;// 防止翻页有声音
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (SettingManager.getInstance().isVolumeFlipEnable()) {
                if (ttsUtis.isPlay()) return true;
                mPageWidget.prePage();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void isAutoLight(boolean isAutoLightness) {

        if (isAutoLightness) {
            startAutoLightness();
            tvAutoLight.setBackground(getResources().getDrawable(R.drawable.coner_orange_light));
        } else {
            stopAutoLightness();
            ScreenUtils.saveScreenBrightnessInt255(ScreenUtils.getScreenBrightnessInt255(), AppUtils.getAppContext());
            tvAutoLight.setBackground(getResources().getDrawable(R.drawable.shape_gray));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
            ;
        }
        EventManager.refreshCollectionIcon();
        EventManager.refreshCollectionList();
        EventBus.getDefault().unregister(this);
        this.getContentResolver().unregisterContentObserver(Brightness);

        try {
            unregisterReceiver(receiver);
            receiver = null;
            title = null;
            des = null;
            author = null;
            bookId = null;
            ttsUtis.destroy();
            ttsUtis = null;
            decodeView = null;
            gvAdapter = null;
            pic = null;
            handler.removeMessages(1);
            handler = null;
            historyBeanDao = null;
            list.clear();
            list = null;
            mChapterList.clear();
            mChapterList = null;
            mPageWidget = null;
            mTocListAdapter = null;
            mTocListPopupWindow = null;
            mTtsListener = null;
            sdf = null;
            sp = null;
            themes.clear();
            themes = null;

        } catch (Exception e) {
            LogUtils.e("Receiver not registered");
        }

        if (isAutoLightness) {
            ScreenUtils.startAutoBrightness(ReadActivity.this);
        } else {
            ScreenUtils.stopAutoBrightness(ReadActivity.this);
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mPresenter = null;
        Brightness = null;


        if (readDisposable != null && !readDisposable.isDisposed()) {
            readDisposable.dispose();
        }

        System.gc();

        // 观察内存泄漏情况
        ReaderApplication.getRefWatcher(this).watch(this);

    }

    private class ReadListener implements OnReadStateChangeListener {
        @Override
        public void onChapterChanged(int chapter) {

            LogUtils.i("onChapterChanged:" + chapter);

            for (int i1 = 0; i1 < mChapterList.size(); i1++) {
                if (mChapterList.get(i1).getId() == chapter) {
                    currentChapter = i1;
                }
            }
            mTocListAdapter.setCurrentChapter(currentChapter);
            //TODO  加载前一节 与 后三节
            try {
                for (int i = currentChapter - 1; i <= currentChapter + 3 && i <= mChapterList.size(); i++) {
                    if (i > 0 && i != currentChapter && CacheManager.getInstance().getChapterFile(bookId, mChapterList.get(i).getId()) == null) {
                        //TODO 加载章节列表
                        mPresenter.getChapterRead(mChapterList.get(i).getId() + "", source);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPageChanged(int chapter, int page) {
            LogUtils.e("是否是最后一页：" + mPageWidget.hasLastPage());

            if (mPageWidget.hasLastPage()) {
                getAd();
            } else {
                if (adView != null) {
                    adView.destroy();
                }
            }
            int[] readPos = mPageWidget.getReadPos();

            LogUtils.i("onPageChanged:" + readPos[0] + "-" + readPos[1] + "-" + readPos[2] + "-" + page);
        }

        @Override
        public void onLoadChapterFailure(int chapter) {
            LogUtils.i("onLoadChapterFailure:" + chapter);
            try {
                int i1 = 0;
                for (int i = 0; i < mChapterList.size(); i++) {
                    if (mChapterList.get(i).getId() == chapter) {
                        i1 = i;
                    }
                }
                startRead = false;
                if (CacheManager.getInstance().getChapterFile(bookId, chapter) == null) {

                    mPresenter.getChapterRead(mChapterList.get(i1).getId() + "", source);
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void onCenterClick() {
            LogUtils.i("onCenterClick");
            toggleReadBar();
        }

        @Override
        public void onFlip() {
            hideReadBar();
        }
    }

    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getId() == seekbarFontSize.getId() && fromUser) {
                calcFontSize(progress);
            } else if (seekBar.getId() == seekbarLightness.getId() && fromUser
                    && !SettingManager.getInstance().isAutoBrightness()) { // 非自动调节模式下 才可调整屏幕亮度
                ScreenUtils.saveScreenBrightnessInt100(progress, ReadActivity.this);
                //SettingManager.getInstance().saveReadBrightness(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class ChechBoxChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == cbVolume.getId()) {
                SettingManager.getInstance().saveVolumeFlipEnable(isChecked);
            } else if (buttonView.getId() == cbAutoBrightness.getId()) {
                if (isChecked) {
                    startAutoLightness();
                } else {
                    stopAutoLightness();
                    ScreenUtils.saveScreenBrightnessInt255(ScreenUtils.getScreenBrightnessInt255(), AppUtils.getAppContext());
                }
            }
        }
    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPageWidget != null) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    int level = intent.getIntExtra("level", 0);
                    mPageWidget.setBattery(100 - level);
                } else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                    mPageWidget.setTime(sdf.format(new Date()));
                }
            }
        }
    }

    private void startAutoLightness() {
        SettingManager.getInstance().saveAutoBrightness(true);
        ScreenUtils.startAutoBrightness(ReadActivity.this);
        seekbarLightness.setEnabled(false);
    }

    private void stopAutoLightness() {
        SettingManager.getInstance().saveAutoBrightness(false);
        ScreenUtils.stopAutoBrightness(ReadActivity.this);
        seekbarLightness.setProgress((int) (ScreenUtils.getScreenBrightnessInt255() / 255.0F * 100));
        seekbarLightness.setEnabled(true);
    }

    private void calcFontSize(int progress) {
        // progress range 1 - 10
        if (progress >= 0 && progress <= 10) {
            seekbarFontSize.setProgress(progress);
            tvFontSize.setText((int) ScreenUtils.pxToSp(ScreenUtils.dpToPxInt(12 + 1.7f * progress)) + "");
            mPageWidget.setFontSize(ScreenUtils.dpToPxInt(12 + 1.7f * progress));
        }
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            ToastUtils.showToast("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            ToastUtils.showToast("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            ToastUtils.showToast("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                mPageWidget.nextPage();
                if (!ttsUtis.isPlay()) {
                    ttsUtis.star(mPageWidget.getCurrentText(), mTtsListener);
                    return;
                }
            } else if (error != null) {
                ToastUtils.showToast(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };

    /**
     * 添加书架
     */
    private void addNovel() {
        RetrofitClient.getInstance().createApi().addNovel(ReaderApplication.token, bookId)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "添加中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showToast("添加成功");
                        finish();
                    }
                });
    }

    /**
     * 弹出朗读的对话框
     */
    private void showTts() {
        ttsUtis.pause();
        View view = View.inflate(ReadActivity.this, R.layout.layout_tts, null);

        if (window == null) {
            window = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            speedSeekBar = (SeekBar) view.findViewById(R.id.seekbarSpeed);
            ttsRecycler = (RecyclerView) view.findViewById(R.id.recyclerview);
            tvCloseTts = (TextView) view.findViewById(R.id.tv_open);
            tvTime = (TextView) view.findViewById(R.id.tv_set_time);
            ttsAdapter = new TtsAdapter(R.layout.item_tts, list);
            ttsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.reader_menu_bg_color), 30, 0, 0);
            itemDecoration.setDrawLastItem(false);
            ttsRecycler.addItemDecoration(itemDecoration);
            ttsRecycler.setAdapter(ttsAdapter);
            window.setTouchable(true);
            window.setOutsideTouchable(true);
            window.setBackgroundDrawable(new ColorDrawable());
            window.setFocusable(true);
        }

        if (time == 0) {
            tvTime.setText("定时");
        }

        speedSeekBar.setProgress(ReaderApplication.speed);
        int position = sp.getInt("voicer", 0);
        ttsAdapter.setSelect(position);

        window.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM, 0, 0);

        ttsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter2, View view, int position) {
                ReaderApplication.vocher = list.get(position).voicer;
                ttsAdapter.setSelect(position);
                sp.edit().putInt("voicer", position).commit();
                ttsIsChange = true;
            }
        });

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ReaderApplication.speed = progress;
                sp.edit().putInt("speed", progress).commit();
                ttsIsChange = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvCloseTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsUtis.stop();
                window.dismiss();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeWindow();
            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (this != null) {
                    setBackgroundAlpha(ReadActivity.this, 1f);
                }

                if (ttsIsChange) {
                    ttsUtis.setParam();
                    ttsIsChange = false;
                } else {
                    ttsUtis.reStart();
                    ttsIsChange = false;
                }
                hideStatusBar();
                gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
            }
        });

    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 初始化语音播报的功能
     */
    private void addTtsList() {
        String[] types = getResources().getStringArray(R.array.voicer_cloud_entries);
        String[] values = getResources().getStringArray(R.array.voicer_cloud_values);

        for (int i = 0; i < types.length; i++) {
            TtsThemeBean bean = new TtsThemeBean();
            bean.type = types[i];
            bean.voicer = values[i];
            list.add(bean);
        }
    }

    /**
     * 定时时间
     */
    private void timeWindow() {
        View view = View.inflate(ReadActivity.this, R.layout.pop_time, null);
        timeWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        view.findViewById(R.id.tv_time_5).setOnClickListener(this);
        view.findViewById(R.id.tv_time_15).setOnClickListener(this);
        view.findViewById(R.id.tv_time_30).setOnClickListener(this);
        view.findViewById(R.id.tv_time_60).setOnClickListener(this);
        view.findViewById(R.id.tv_time_90).setOnClickListener(this);
        view.findViewById(R.id.tv_close).setOnClickListener(this);

        timeWindow.setTouchable(true);
        timeWindow.setOutsideTouchable(true);
        timeWindow.setBackgroundDrawable(new ColorDrawable());
        timeWindow.setFocusable(true);
        timeWindow.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM, 0, 0);

        view.findViewById(R.id.tv_time_5).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        time = -1;
        switch (v.getId()) {
            case R.id.tv_time_5:
                time = 5;
                break;
            case R.id.tv_time_15:
                time = 15;
                break;
            case R.id.tv_time_30:
                time = 30;
                break;
            case R.id.tv_time_60:
                time = 60;
                break;
            case R.id.tv_time_90:
                time = 90;
                break;
            case R.id.tv_close:
                time = 0;
                timeWindow.dismiss();
                disposable.dispose();
                break;
            case R.id.ll_live_shar_qq:
                CoomonApi.share(this, QQ.NAME, this);
                break;
            case R.id.ll_live_shar_qqzone:
                CoomonApi.share(this, QZone.NAME, this);
                break;
            case R.id.ll_live_shar_wechat:
                CoomonApi.share(this, Wechat.NAME, this);
                break;
            case R.id.ll_live_shar_pyq://朋友圈
                CoomonApi.share(this, WechatMoments.NAME, this);
                break;
            case R.id.ll_live_share_qq:
                CoomonApi.share(this, QQ.NAME, this);
                break;
            case R.id.ll_live_share_qqzone:
                CoomonApi.share(this, QZone.NAME, this);
                break;
            case R.id.ll_live_share_wechat:
                CoomonApi.share(this, Wechat.NAME, this);
                break;
            case R.id.ll_live_share_sinna:
                CoomonApi.share(this, SinaWeibo.NAME, this);
                break;
            case R.id.ll_live_share_pyq://朋友圈
                CoomonApi.share(this, WechatMoments.NAME, this);
                break;
            case R.id.tv_share_copy://复制
                CoomonApi.copy(this, ReaderApplication.shareUrl);
                sharePopWindow2.dismiss();
                break;
            case R.id.tvBookShare://分享
                morePopWindow.dismiss();
                hideReadBar();
                showSharePopWindow2();
//                CoomonApi.share(this, "", this);
                break;
            case R.id.tvBookDetail://小说详情
                morePopWindow.dismiss();
                gone(rlReadAaSet, rlReadMark);
                BookDetailActivity.startActivity(mContext, bookId);
                break;
        }
        if (time == -1) {
            return;
        }

        if (time == 0) {
            tvTime.setText("定时");
        } else {
            tvTime.setText(time + "分钟");
            startTime();
        }
        if (timeWindow.isShowing()) {
            timeWindow.dismiss();
        }
    }

    /**
     * 朗读开始计时
     */
    private void startTime() {
        if (disposable != null) {
            disposable.dispose();
        }

        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.e("gy", "value：" + value);
                        if (value > 0 && value % 60 == 0) {//一分钟
                            time--;
                            if (time == 0) {
                                tvTime.setText("定时");
                                disposable.dispose();
                                ttsUtis.stop();
                            }
                            tvTime.setText(time + "分钟");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 阅读计时
     */
    private void readTime() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        readDisposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.e("gy", "value：" + value);
                        if (value == 60) {//阅读了一分钟了
                            readDisposable.dispose();
                            handler.sendEmptyMessage(1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        readDisposable.dispose();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                showShareDialog();
            } else if (msg.what == 2) {
                notifyDialog(msg.arg1);
            }

        }
    };

    /**
     * 阅读休息计时
     */
    private void notifytTime() {
        int time = 0;
        if (notify == 0) {
            return;
        } else if (notify == 1) {
            LogUtils.e("阅读15分钟休息");
            time = 15;
        } else if (notify == 2) {
            LogUtils.e("阅读30分钟休息");
            time = 30;
        } else if (notify == 3) {
            LogUtils.e("阅读60分钟休息");
            time = 60;
        }

        final int finalTime = time;
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        notifyDisposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.e("gy", "value：" + value);
                        if (value == finalTime * 60) {//阅读了一分钟了,该休息了
                            notifyDisposable.dispose();
                            Message message = new Message();
                            message.what = 2;
                            message.arg1 = finalTime;
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        notifyDisposable.dispose();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 阅读休息提示
     */
    private void notifyDialog(int arg1) {
        shareDialog = new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("亲 您已阅读" + arg1 + "分钟了哦!是否休息一下再看？")
                .setCancelable(false)
                .setPositiveButton("继续看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        notifytTime();
                    }
                })
                .setNegativeButton("休息一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })

                .create();
        shareDialog.show();
    }


    /**
     * 显示分享提示对话框
     */
    private void showShareDialog() {
        shareDialog = new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("亲 您已阅读一个小时了哦!好东西要分享，分享后可以继续阅读哦!")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showSharePopWindow();
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            dialog.dismiss();
                            finish();
                        }
                        return false;
                    }
                })
                .create();
        shareDialog.show();
    }


    /**
     * 分享pop弹窗
     */
    public void showSharePopWindow() {
        View view = LayoutInflater.from(ReadActivity.this).inflate(R.layout.pop_view_share, null);
        view.findViewById(R.id.ll_live_shar_qqzone).setOnClickListener(this);
        view.findViewById(R.id.ll_live_shar_qq).setOnClickListener(this);
        view.findViewById(R.id.ll_live_shar_pyq).setOnClickListener(this);
        view.findViewById(R.id.ll_live_shar_wechat).setOnClickListener(this);
        sharePopWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        sharePopWindow.setBackgroundDrawable(new BitmapDrawable());
        sharePopWindow.setFocusable(false);
        sharePopWindow.setOutsideTouchable(false);
        sharePopWindow.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    /**
     * 右上角的分享
     */
    public void showSharePopWindow2() {
        View view = LayoutInflater.from(ReadActivity.this).inflate(R.layout.pop_share, null);
        view.findViewById(R.id.ll_live_share_qqzone).setOnClickListener(this);
        view.findViewById(R.id.ll_live_share_qq).setOnClickListener(this);
        view.findViewById(R.id.ll_live_share_pyq).setOnClickListener(this);
        view.findViewById(R.id.ll_live_share_wechat).setOnClickListener(this);
        view.findViewById(R.id.ll_live_share_sinna).setOnClickListener(this);
        view.findViewById(R.id.tv_share_copy).setOnClickListener(this);
        sharePopWindow2 = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        sharePopWindow2.setBackgroundDrawable(new BitmapDrawable());
        sharePopWindow2.setFocusable(false);
        sharePopWindow2.setOutsideTouchable(true);
        sharePopWindow2.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (sharePopWindow != null && sharePopWindow.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 分享回掉
     *
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.showToast("分享成功");
        sp.edit().putString(currentDate, currentDate).commit();
        if (readDisposable != null) {
            readDisposable.dispose();
        }
        if (sharePopWindow != null && sharePopWindow.isShowing()) {
            sharePopWindow.dismiss();

        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.showToast("分享出错");
        finish();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.showToast("取消分享");
    }


    @Subscribe
    public void isPlay(String play) {//语音正在播放 停止播放
        if (play.equals("play")) {
            gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
            if (window != null && window.isShowing()) {
                window.dismiss();
                hideStatusBar();
            } else {
                showTts();
                showStatusBar();
            }
        }
    }

    /**
     * 更新阅读时间
     */
    private void updateReadTime() {
        RetrofitClient.getInstance().createApi().updateReadTime(ReaderApplication.token, bookId)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {

                    }
                });
    }

    /**
     * 显示更多的pop
     */
    private void showMorePopWindow() {
        View view = LayoutInflater.from(ReadActivity.this).inflate(R.layout.pop_read_more, null);
        view.findViewById(R.id.tvBookShare).setOnClickListener(this);
        view.findViewById(R.id.tvBookDetail).setOnClickListener(this);
        morePopWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        morePopWindow.setBackgroundDrawable(new BitmapDrawable());
        morePopWindow.setFocusable(true);
        int windowPos[] = CoomonApi.calculatePopWindowPos(tvMore, view);
        int xOff = 50;// 可以自己调整偏移
        windowPos[0] -= xOff;
        windowPos[1] += 20;
        morePopWindow.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }


    private void getAd() {
        // 创建广告View
        String adPlaceId = "2015351"; //  重要：请填上您的广告位ID，代码位错误会导致无法请求到广告
        adView = new AdView(this, adPlaceId);
        // 设置监听器
        adView.setListener(new AdViewListener() {
            public void onAdSwitch() {
                Log.w("", "onAdSwitch");
            }

            public void onAdShow(JSONObject info) {
                // 广告已经渲染出来
                Log.w("", "onAdShow " + info.toString());
            }

            public void onAdReady(AdView adView) {
                // 资源已经缓存完毕，还没有渲染出来
                Log.w("", "onAdReady " + adView);
            }

            public void onAdFailed(String reason) {
                Log.w("", "onAdFailed " + reason);
            }

            public void onAdClick(JSONObject info) {
                // Log.w("", "onAdClick " + info.toString());

            }

            @Override
            public void onAdClose(JSONObject arg0) {
                Log.w("", "onAdClose");
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        int winW = dm.widthPixels;
        int winH = dm.heightPixels;
        int width = Math.min(winW, winH);
        int height = width * 3 / 20;
        // 将adView添加到父控件中(注：该父控件不一定为您的根控件，只要该控件能通过addView能添加广告视图即可)
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(width, height);
        rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mRlBookReadRoot.addView(adView, rllp);
    }


    public String getFontType() { //搜索目录，扩展名，是否进入子文件夹
        LogUtils.e("小说字体类型：" + SharedPreferencesUtil.getInstance().getString("font"));

        return SharedPreferencesUtil.getInstance().getString("font");
    }

}
