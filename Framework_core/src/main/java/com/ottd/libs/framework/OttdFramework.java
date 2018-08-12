package com.ottd.libs.framework;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.ottd.libs.config.Config;
import com.ottd.libs.framework.common.eFlag;
import com.ottd.libs.framework.model.ApplicationInfo;
import com.ottd.libs.framework.network.ReadHubApi;
import com.ottd.libs.logger.OttdLog;
import com.ottd.libs.thread.ThreadManager;
import com.ottd.libs.ui.ToastUtil;
import com.ottd.libs.utils.APKUtils;
import com.ottd.libs.utils.TextUtils;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by hardyshi on 2017/6/27.
 *
 */

public class OttdFramework {

    private Context mApplicationContext = null;

    //是否为调试版本，主要用于开发中打一些提供给游戏定位问题
    private static final boolean IS_TEST_VERSION = true;
    private static volatile OttdFramework instance = null;
    private OttdFramework(){}
    public static OttdFramework getInstance() {
        if (instance == null) {
            synchronized (OttdFramework.class) {
                if (instance == null) {
                    instance = new OttdFramework();
                }
            }
        }
        return instance;
    }

    // 全局变量的初始化
    public void init(final Context ctx) {
        mApplicationContext = ctx;
        OttdLog.init(getApplicationContext(),isDebug());

        OttdFramework.getInstance().showTips();

        final String CONFIG_FILE = "conf.ini";
        Config.init(getApplicationContext(),CONFIG_FILE);

        ReadHubApi.INSTANCE.init(ctx, true);
    }

    public Context getApplicationContext(){
        return mApplicationContext;
    }

    public boolean isDebug(){
        return IS_TEST_VERSION;
    }

    public void showWaitting(){
        ToastUtil.showShort(getApplicationContext(),"功能开发中，敬请期待~");
    }

    public void showTips(){
        String tips = "";
        if(isDebug()){
            tips = "测试版本，体验问题请及时联系子勰";
        }else{
            //TODO tipsList
        }

        if(!TextUtils.ckIsEmpty(tips)){
            ToastUtil.showShort(getApplicationContext(),tips);
        }
    }

    public void checkUpdate(final Activity activity, final boolean isAuto){
        RequestBody data = RequestBody.create(
                MediaType.parse(
                        "Content-Type, application/json"),
                "{\"cmd\":\"101001\",\"versionCode\":\""+ APKUtils.getAppVersionCode(getApplicationContext()) + "\"}");
        ReadHubApi.aboutService.checkUpdate(data).enqueue(new Callback<ApplicationInfo>() {
            @Override
            public void onResponse(Call<ApplicationInfo> call, final Response<ApplicationInfo> response) {
                if (response.body().getRet() == eFlag.Succ && eFlag.UPDATE_HAS_NEW ==  response.body().getEcode()){
                    ThreadManager.getInstance().runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(
                                    getApplicationContext().getResources().getString(R.string.game_update_notnew_Title)
                            );
                            builder.setMessage(
                                    String.format(getApplicationContext().getResources().getString(R.string.game_update_notnew_desc)
                                            ,response.body().getPackageinfo().getVersion()));
                            builder.setPositiveButton(
                                    getApplicationContext().getResources().getString(
                                            R.string.game_update_notnew_update_btn), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startDownload(response.body().getPackageinfo().getVersion(), response.body().getPackageinfo().getUrl());
                                        }
                                    });
                            builder.setNegativeButton(
                                    getApplicationContext().getResources().getString(
                                            R.string.game_update_notnew_cancle_btn), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.create().show();
                        }
                    });
                    return ;
                }
                if (!isAuto) {
                    String text =
                            String.format(
                                    getApplicationContext().getResources().getString(
                                            R.string.game_update_isnew_toast),
                                    APKUtils.getAppVersionName(getApplicationContext())
                                            + "." + APKUtils.getAppVersionCode(getApplicationContext()));
                    ToastUtil.show(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    return ;
                }
            }

            @Override
            public void onFailure(Call<ApplicationInfo> call, Throwable t) {
                String text =
                        String.format(
                                getApplicationContext().getResources().getString(
                                        R.string.game_update_isnew_toast),
                                APKUtils.getAppVersionName(getApplicationContext())
                                        + "." + APKUtils.getAppVersionCode(getApplicationContext()));
                ToastUtil.show(getApplicationContext(), text, Toast.LENGTH_SHORT);
            }
        });

    }

    public final void startDownload(@NotNull String versionName, @NotNull String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(3);
        request.setNotificationVisibility(1);
        request.setTitle(getApplicationContext().getResources().getString(R.string.game_update_notnew_Title) + "更新: v" + versionName);
        DownloadManager downloadManager= (DownloadManager) OttdFramework.getInstance().getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }

    }
}
