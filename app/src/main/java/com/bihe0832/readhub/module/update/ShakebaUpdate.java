package com.bihe0832.readhub.module.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.common.BaseRet;
import com.bihe0832.readhub.framework.common.eFlag;
import com.bihe0832.readhub.framework.request.HttpResponseHandler;
import com.bihe0832.readhub.framework.request.RequestServer;
import com.bihe0832.readhub.framework.Shakeba;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.ui.ToastUtil;

/**
 * Created by code@bihe0832.com
 */
public class ShakebaUpdate {
    private static volatile ShakebaUpdate instance;

    public static ShakebaUpdate getInstance () {
        if (instance == null) {
            synchronized (ShakebaUpdate.class) {
                if (instance == null) {
                    instance = new ShakebaUpdate();
                }
            }
        }
        return instance;
    }

    public void checkUpdate(final Activity activity, boolean isAuto){
        CheckUpdateRequestHandle request = new CheckUpdateRequestHandle();
        request.requestStartTime = System.currentTimeMillis() / 1000;
        request.mActivity = activity;
        request.mIsAuto = isAuto;

        CheckUpdateResponseHandle handler = new CheckUpdateResponseHandle(request);
        CheckUpdateRequest checkRequest = new CheckUpdateRequest( handler);
        checkRequest.data = checkRequest.getBody().toString().getBytes();
        RequestServer.getInstance().doRequest(checkRequest);

    }
    private class CheckUpdateRequestHandle {
        public long requestStartTime;
        public Activity mActivity;
        public boolean mIsAuto;
    }


    private class CheckUpdateResponseHandle implements
            HttpResponseHandler<CheckUpdateResponse> {

        private CheckUpdateRequestHandle mRequest;

        public CheckUpdateResponseHandle(CheckUpdateRequestHandle request) {
            this.mRequest = request;
        }

        @Override
        public void onResponse(final CheckUpdateResponse response) {
            Logger.d(response.toString());
            if (response.ret == BaseRet.RET_SUCC && eFlag.UPDATE_HAS_NEW ==  response.flag){
                mRequest.mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mRequest.mActivity);
                        builder.setTitle(
                                Shakeba.getInstance().getStringById(R.string.game_update_notnew_Title)
                        );
                        builder.setMessage(
                                String.format(Shakeba.getInstance().getStringById(R.string.game_update_notnew_desc)
                                        ,response.mPackageUpdateInfo.pckVersion));
                        builder.setPositiveButton(
                                Shakeba.getInstance().getStringById(R.string.game_update_notnew_update_btn), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        String url = response.mPackageUpdateInfo.url;
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(url));
                                        mRequest.mActivity.startActivity(intent);
                                    }
                                });
                        builder.setNegativeButton(
                                Shakeba.getInstance().getStringById(R.string.game_update_notnew_cancle_btn), new DialogInterface.OnClickListener() {
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
            if (!mRequest.mIsAuto) {
                String text =
                        String.format(Shakeba.getInstance().getStringById(R.string.game_update_isnew_toast),Shakeba.getInstance().getVersionName() + "." + Shakeba.getInstance().getVersionCode());
                ToastUtil.show(mRequest.mActivity, text, Toast.LENGTH_SHORT);
                return ;
            }

        }
    }
}
