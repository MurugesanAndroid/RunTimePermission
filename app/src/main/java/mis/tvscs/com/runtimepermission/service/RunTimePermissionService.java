package mis.tvscs.com.runtimepermission.service;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class RunTimePermissionService {
    private AppCompatActivity mActivity;
    private ArrayList<String> permissionList;

    public RunTimePermissionService(AppCompatActivity activity) {
        mActivity = activity;
        permissionList = new ArrayList<>();
    }

    public void getPermissionList(String permissions[]) {
        for (int i = 0; i < permissions.length; i++) {
            permissionList.add(permissions[i]);
        }
    }

    public boolean checkSelfPermission() {
        boolean checkSelfPermissionFlag = false;
        for (int i = 0; i < permissionList.size(); i++) {
            if (ActivityCompat.checkSelfPermission(mActivity, permissionList.get(i)) != PackageManager.PERMISSION_GRANTED)
                checkSelfPermissionFlag = true;
            else
                checkSelfPermissionFlag = false;
        }
        return checkSelfPermissionFlag;
    }

    public boolean checkSelfPermissionOnActivityResult() {
        boolean checkSelfPermissionFlag = false;
        for (int i = 0; i < permissionList.size(); i++) {
            if (ActivityCompat.checkSelfPermission(mActivity, permissionList.get(i)) == PackageManager.PERMISSION_GRANTED)
                checkSelfPermissionFlag = true;
            else
                checkSelfPermissionFlag = false;
        }
        return checkSelfPermissionFlag;
    }

    public boolean shouldShowRequestPermissionRationale() {
        boolean rationaleFlag = false;

        for (int i = 0; i < permissionList.size(); i++) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionList.get(i)))
                rationaleFlag = true;
            else
                rationaleFlag = false;
        }

        return rationaleFlag;
    }

    public boolean grantResults(int[] grantResults) {
        boolean resultFlag = false;
        for (int value : grantResults) {
            if (value == PackageManager.PERMISSION_GRANTED) {
                resultFlag = true;
            } else {
                resultFlag = false;
            }
        }
        return resultFlag;
    }
}
