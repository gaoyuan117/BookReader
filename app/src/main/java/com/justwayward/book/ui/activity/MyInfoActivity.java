package com.justwayward.book.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justwayward.book.AppConfig;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.CommonBean;
import com.justwayward.book.bean.UserBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.yuyh.easyadapter.glide.GlideCircleTransform;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 个人信息
 */
public class MyInfoActivity extends BaseActivity {

    @Bind(R.id.img_my_info_avatar)
    ImageView imgMyInfoAvatar;
    @Bind(R.id.tv_my_info_name)
    TextView tvMyInfoName;
    @Bind(R.id.tv_my_info_sex)
    TextView tvMyInfoSex;
    @Bind(R.id.tv_my_info_phone)
    TextView tvMyInfoPhone;
    @Bind(R.id.tv_my_info_id)
    TextView tvMyInfoId;
    private String photoByCameraPath;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("个人信息");
    }

    @Override
    public void initDatas() {
        getUser();
    }

    @Override
    public void configViews() {

    }

    @OnClick({R.id.ll_my_info_name, R.id.ll_my_info_sex, R.id.ll_my_info_phone, R.id.tv_my_info_id, R.id.ll_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_my_info_name:
                showInputDialog();
                break;
            case R.id.ll_my_info_sex:
                String[] items = {"男", "女"};
                showListDialog(items, "请选择性别");
                break;
            case R.id.ll_my_info_phone:
                break;
            case R.id.ll_avatar:
                String[] item = {"拍照", "从相册选择"};
                showListDialog(item, "更换头像");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == AppConfig.REQUEST_IMAGE) {//选择的图片地址回掉
            List<String> list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            Log.e("gy", list.get(0));
            compress(new File(list.get(0)));
        }

        if (requestCode == AppConfig.REQUEST_CAMERA) {//拍照
            compress(new File(photoByCameraPath));
        }

    }

    /**
     * 获取用户信息
     */
    private void getUser() {
        RetrofitClient.getInstance().createApi().getUser(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<UserBean>>io_main())
                .subscribe(new BaseObjObserver<UserBean>(this) {
                    @Override
                    protected void onHandleSuccess(UserBean userBean) {
                        setUserInfo(userBean);
                    }
                });
    }

    /**
     * 设置用户信息
     *
     * @param userBean
     */
    private void setUserInfo(UserBean userBean) {
        Glide.with(MyInfoActivity.this).load(userBean.getAvatar())
                .error(R.drawable.avatar_default)
                .transform(new GlideCircleTransform(MyInfoActivity.this)).into(imgMyInfoAvatar);

        tvMyInfoName.setText(userBean.getUser_nickname());
        tvMyInfoId.setText(userBean.getId() + "");
        tvMyInfoPhone.setText(userBean.getMobile());
        int sex = userBean.getSex();
        if (sex == 0) {
            tvMyInfoSex.setText("保密");
        } else if (sex == 1) {
            tvMyInfoSex.setText("男");

        } else if (sex == 2) {
            tvMyInfoSex.setText("女");
        }
    }

    /**
     * 列表对话框
     *
     * @param items 列表内容
     * @param title 标题
     */
    private void showListDialog(final String[] items, String title) {
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        listDialog.setTitle(title);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // which 下标从0开始
                if (items[which].equals("男")) {
                    updateUserInfo("sex", "1");

                } else if (items[which].equals("女")) {
                    updateUserInfo("sex", "2");

                } else if (items[which].equals("拍照")) {
                    photoByCameraPath = getPhotoByCamera();

                } else if (items[which].equals("从相册选择")) {
                    selectImg();
                }
            }
        });
        listDialog.show();
    }

    /**
     * 更改昵称对话框
     */
    private void showInputDialog() {
        final EditText editText = new EditText(this);
        editText.setPadding(16, 100, 100, 16);
        editText.setLines(1);
        InputFilter[] filters = {new InputFilter.LengthFilter(8)};
        editText.setFilters(filters);
        new AlertDialog.Builder(this)
                .setTitle("修改昵称")
                .setView(editText)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = editText.getText().toString();
                        if (TextUtils.isEmpty(s)) {
                            showToastMsg("请输入昵称");
                            return;
                        }
                        updateUserInfo("user_nickname", s);
                    }
                }).show();
    }


    /**
     * 更新用户信息
     *
     * @param field
     * @param value
     */
    private void updateUserInfo(String field, String value) {

        RetrofitClient.getInstance().createApi().updateUserInfo(ReaderApplication.token, field, value)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "更新中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("修改成功");
                        getUser();
                    }
                });
    }

    /**
     * 调用系统相机拍一张照片
     *
     * @return 图片地址
     */
    public String getPhotoByCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "avatar.png");
        Uri imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, AppConfig.REQUEST_CAMERA);
        return file.getAbsolutePath();
    }

    /**
     * 选择图片
     */
    public void selectImg() {
        MultiImageSelector.create()
                .showCamera(false)
                .count(1)
                .multi()
                .origin(new ArrayList<String>())
                .start(this, AppConfig.REQUEST_IMAGE);

    }

    /**
     * 图片压缩
     *
     * @param file
     */
    private void compress(File file) {
        Luban.get(this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.e("gy", "file" + file.getAbsolutePath());
                        updateAvatar(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.toString();
                    }
                }).launch();
    }

    /**
     * 修改头像
     *
     * @param file
     */
    private void updateAvatar(final File file) {

        OkHttpUtils.post()
                .addFile("avatar", "avatar.jpg", file)
                .addParams("token", ReaderApplication.token)
                .url(AppConfig.BaseUrl + "/api/User/updateAvatar")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Glide.with(MyInfoActivity.this).load(file.getAbsolutePath())
                                .error(R.drawable.avatar_default)
                                .transform(new GlideCircleTransform(MyInfoActivity.this)).into(imgMyInfoAvatar);
                    }
                });
    }

}
