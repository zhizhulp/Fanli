package com.ascba.rebate.activities.base;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ascba.rebate.R;

/**
 * Created by 李鹏 on 2017/04/20 0020.
 */

public class BaseActivity extends AppCompatActivity {

    private PermissionCallback requestPermissionAndBack;

    protected void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    public interface PermissionCallback {
        void requestPermissionAndBack(boolean isOk);
    }

    /**
     * 申请权限
     *
     * @param permissions
     */
    protected void checkAndRequestAllPermission(String[] permissions, PermissionCallback requestPermissionAndBack) {
        this.requestPermissionAndBack = requestPermissionAndBack;
        if (permissions == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, 1);
        }
    }

    public void setRequestPermissionAndBack(PermissionCallback requestPermissionAndBack) {
        this.requestPermissionAndBack = requestPermissionAndBack;
    }

    /**
     * 申请权限
     *
     * @param permissions
     */
    protected void checkAndRequestAllPermission(String[] permissions) {
        if (permissions == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, 1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] per,
                                           @NonNull int[] grantResults) {
        boolean isAll = true;
        for (int i = 0; i < per.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAll = false;
                break;
            }
        }
        if (!isAll) {
            showToast(getResources().getString(R.string.no_permission));
        }
        if (requestPermissionAndBack != null) {
            requestPermissionAndBack.requestPermissionAndBack(isAll);//isAll 用户是否拥有所有权限
        }
        super.onRequestPermissionsResult(requestCode, per, grantResults);
    }
}
