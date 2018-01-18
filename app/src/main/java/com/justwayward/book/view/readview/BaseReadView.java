
package com.justwayward.book.view.readview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.bumptech.glide.request.animation.NoAnimation;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.bean.ChapterListBean;
import com.justwayward.book.manager.SettingManager;
import com.justwayward.book.manager.ThemeManager;
import com.justwayward.book.ui.activity.ReadActivity;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.utils.ScreenUtils;
import com.justwayward.book.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author yuyh.
 * @date 2016/10/18.
 */
public abstract class BaseReadView extends View {

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected PointF mTouch = new PointF();
    protected float actiondownX, actiondownY;
    protected float touch_down = 0; // 当前触摸点与按下时的点的差值
    protected float down = 0; // 点击的位置
    protected float ups = 0; // 抬起的位置

    protected Bitmap mCurPageBitmap, mNextPageBitmap;
    protected Canvas mCurrentPageCanvas, mNextPageCanvas;
    protected PageFactory pagefactory = null;

    protected OnReadStateChangeListener listener;
    protected String bookId;
    public boolean isPrepared = false;

    protected boolean nextLeft, NeedCalcu;

    Scroller mScroller;

    public BaseReadView(Context context, String bookId, List<ChapterListBean> chaptersList,
                        OnReadStateChangeListener listener) {
        super(context);
        this.listener = listener;
        this.bookId = bookId;

        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurrentPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
        mScroller = new Scroller(getContext());

        pagefactory = new PageFactory(getContext(), bookId, chaptersList);
        pagefactory.setOnReadStateChangeListener(listener);

    }

    public String getCurrentText() {
        return pagefactory.getCurrentText();
    }


    public synchronized void init(int theme) {
        if (!isPrepared) {
            try {
                pagefactory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
                // 自动跳转到上次阅读位置
                int pos[] = SettingManager.getInstance().getReadProgress(bookId);

                int ret = pagefactory.openBook(pos[0], new int[]{pos[1], pos[2]});
                LogUtils.e("上次阅读位置：chapter=" + pos[0] + " startPos=" + pos[1] + " endPos=" + pos[2] + "ret：" + ret);

                if (ret == 0) {
                    listener.onLoadChapterFailure(pos[0]);
                    return;
                }

                pagefactory.onDraw(mCurrentPageCanvas);
                postInvalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPrepared = true;
        }
    }

    protected int dx, dy;
    protected long et = 0;
    protected boolean cancel = false;
    protected boolean center = false;

    private boolean isNextPage(boolean isnext) {
        BookStatus status;
        if (isnext) {// 从左翻
            nextLeft = true;
            status = pagefactory.prePage();
            if (status == BookStatus.NO_PRE_PAGE) {
                ToastUtils.showSingleToast("没有上一页啦");
                return false;
            } else if (status == BookStatus.LOAD_SUCCESS) {
                abortAnimation();
                pagefactory.onDraw(mNextPageCanvas);
            } else {
                return false;
            }
        } else {// 从右翻
            nextLeft = false;
            status = pagefactory.nextPage();
            if (status == BookStatus.NO_NEXT_PAGE) {
                ToastUtils.showSingleToast("没有下一页啦");
                return false;
            } else if (status == BookStatus.LOAD_SUCCESS) {
                abortAnimation();
                pagefactory.onDraw(mNextPageCanvas);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (ReadActivity.ttsUtis != null && ReadActivity.ttsUtis.isPlay()) {
            EventBus.getDefault().postSticky("play");
            return true;
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                et = System.currentTimeMillis();
                dx = (int) e.getX();
                dy = (int) e.getY();
                down = dx;
                mTouch.x = dx;
                mTouch.y = dy;
                actiondownX = dx;
                actiondownY = dy;
                touch_down = 0;
                pagefactory.onDraw(mCurrentPageCanvas);
                if (actiondownX >= mScreenWidth / 3 && actiondownX <= mScreenWidth * 2 / 3
                        && actiondownY >= mScreenHeight / 3 && actiondownY <= mScreenHeight * 2 / 3) {
                    center = true;
                } else {
                    center = false;
                    NeedCalcu = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (center)
                    break;
                int mx = (int) e.getX();
                int my = (int) e.getY();

                if (NeedCalcu) {
                    calcCornerXY(actiondownX, actiondownY);
                    if (mx > actiondownX + 30) {
                        if (!isNextPage(true)) {
                            return true;
                        }
                    } else if (mx < actiondownX - 30) {
                        if (!isNextPage(false)) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                    NeedCalcu = false;
                    listener.onFlip();
                    setBitmaps(mCurPageBitmap, mNextPageBitmap);
                }
                Log.d("BaseReadView", mx + "---" + my);
                Log.d("BaseReadView", actiondownX + "___" + actiondownY);
                cancel = (nextLeft && mx < actiondownX) || (!nextLeft && mx > actiondownX);

                mTouch.x = mx;
                mTouch.y = my;
                touch_down = mTouch.x - actiondownX;
                this.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                long t = System.currentTimeMillis();
                int ux = (int) e.getX();
                int uy = (int) e.getY();
                ups = ux;
                if (center) { // ACTION_DOWN的位置在中间，则不响应滑动事件
                    resetTouchPoint();
                    if (Math.abs(ux - actiondownX) < 5 && Math.abs(uy - actiondownY) < 5) {
                        listener.onCenterClick();
                        return false;
                    }
                    break;
                }

                if ((Math.abs(ux - actiondownX) < 30) && (Math.abs(uy - actiondownY) < 30)) {
                    if ((t - et < 1000)) { // 单击
                        if (actiondownX < mScreenWidth / 3) {
                            if (!isNextPage(true)) {
                                return true;
                            }
                        } else if (actiondownX > mScreenWidth / 3 * 2) {
                            if (!isNextPage(false)) {
                                return true;
                            }
                        } else {
                            return false;
                        }
                        calcCornerXY(actiondownX, actiondownY);
                        NeedCalcu = false;
                        listener.onFlip();
                        setBitmaps(mCurPageBitmap, mNextPageBitmap);

                        mTouch.x = ux;
                        mTouch.y = uy;
                        touch_down = mTouch.x - actiondownX;

                        startAnimation();
                        if (this instanceof NoAimWidget) {
                            ((NoAimWidget) this).startAnimation(0);
                        } else {
                            startAnimation();
                        }
                    } else { // 长按
                        pagefactory.cancelPage();
                        restoreAnimation();
                    }
                    postInvalidate();
                    return true;
                }
                if (cancel) {
                    pagefactory.cancelPage();
                    restoreAnimation();
                    postInvalidate();
                } else {
                    startAnimation();
                    postInvalidate();
                }
                cancel = false;
                center = false;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calcPoints();
        drawCurrentPageArea(canvas);
        drawNextPageAreaAndShadow(canvas);
        drawCurrentPageShadow(canvas);
        drawCurrentBackArea(canvas);
    }

    protected abstract void drawNextPageAreaAndShadow(Canvas canvas);

    protected abstract void drawCurrentPageShadow(Canvas canvas);

    protected abstract void drawCurrentBackArea(Canvas canvas);

    protected abstract void drawCurrentPageArea(Canvas canvas);

    protected abstract void calcPoints();

    protected abstract void calcCornerXY(float x, float y);

    /**
     * 开启翻页
     */
    protected abstract void startAnimation();

    /**
     * 停止翻页动画（滑到一半调用停止的话  翻页效果会卡住 可调用#{restoreAnimation} 还原效果）
     */
    protected abstract void abortAnimation();

    /**
     * 还原翻页
     */
    protected abstract void restoreAnimation();

    protected abstract void setBitmaps(Bitmap mCurPageBitmap, Bitmap mNextPageBitmap);

    public abstract void setTheme(int theme);

    /**
     * 复位触摸点位
     */
    protected void resetTouchPoint() {
        mTouch.x = 0.1f;
        mTouch.y = 0.1f;
        touch_down = 0;
        calcCornerXY(mTouch.x, mTouch.y);
    }

    public void jumpToChapter(int chapter) {
        resetTouchPoint();
        pagefactory.openBook(chapter, new int[]{0, 0});
        pagefactory.onDraw(mCurrentPageCanvas);
        pagefactory.onDraw(mNextPageCanvas);
        postInvalidate();
    }

    public void nextPage() {
        BookStatus status = pagefactory.nextPage();
        if (status == BookStatus.NO_NEXT_PAGE) {
            ToastUtils.showSingleToast("没有下一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory.onDraw(mCurrentPageCanvas);
                pagefactory.onDraw(mNextPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }

    }

    public void prePage() {
        BookStatus status = pagefactory.prePage();
        if (status == BookStatus.NO_PRE_PAGE) {
            ToastUtils.showSingleToast("没有上一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory.onDraw(mCurrentPageCanvas);
                pagefactory.onDraw(mNextPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }
    }

    public boolean hasLastPage() {
        return pagefactory.hasLastPage();
    }

    public synchronized void setFontSize(final int fontSizePx) {
        resetTouchPoint();
        pagefactory.setTextFont(fontSizePx);
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            pagefactory.onDraw(mNextPageCanvas);
            //SettingManager.getInstance().saveFontSize(bookId, fontSizePx);
            SettingManager.getInstance().saveFontSize(fontSizePx);
            postInvalidate();
        }
    }

    public synchronized void setTextColor(int textColor, int titleColor) {
        resetTouchPoint();
        pagefactory.setTextColor(textColor, titleColor);
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            pagefactory.onDraw(mNextPageCanvas);
            postInvalidate();
        }
    }

    public void setBattery(int battery) {
        pagefactory.setBattery(battery);
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            postInvalidate();
        }
    }


    public void setTime(String time) {
        pagefactory.setTime(time);
    }

    public void setPosition(int[] pos) {
        int ret = pagefactory.openBook(pos[0], new int[]{pos[1], pos[2]});
        if (ret == 0) {
            listener.onLoadChapterFailure(pos[0]);
            return;
        }
        pagefactory.onDraw(mCurrentPageCanvas);
        postInvalidate();
    }

    public int[] getReadPos() {
        return pagefactory.getPosition();
    }

    public String getHeadLine() {
        return pagefactory.getHeadLineStr().replaceAll("@", "");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (pagefactory != null) {
            pagefactory.recycle();
        }

        if (mCurPageBitmap != null && !mCurPageBitmap.isRecycled()) {
            mCurPageBitmap.recycle();
            mCurPageBitmap = null;
            LogUtils.d("mCurPageBitmap recycle");
        }

        if (mNextPageBitmap != null && !mNextPageBitmap.isRecycled()) {
            mNextPageBitmap.recycle();
            mNextPageBitmap = null;
            LogUtils.d("mNextPageBitmap recycle");
        }

        if (ThemeManager.bmp != null && !ThemeManager.bmp.isRecycled()) {
            ThemeManager.bmp.recycle();
            ThemeManager.bmp = null;
            LogUtils.d(" ThemeManager.bmp  ThemeManager.bmp");
        }


        mCurrentPageCanvas = null;
        mNextPageCanvas = null;
        System.gc();
    }
}
