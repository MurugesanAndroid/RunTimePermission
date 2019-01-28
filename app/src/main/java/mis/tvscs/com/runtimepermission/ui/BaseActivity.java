package mis.tvscs.com.runtimepermission.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import mis.tvscs.com.runtimepermission.R;
import mis.tvscs.com.runtimepermission.service.RunTimePermissionService;
import mis.tvscs.com.runtimepermission.utility.CommonLog;
import mis.tvscs.com.runtimepermission.utility.ProjectPreference;

public class BaseActivity extends AppCompatActivity {
    private final String TAG = BaseActivity.class.getSimpleName();

    protected AppCompatActivity mActivity;

    private RunTimePermissionService mRunTimePermissionService;

    private final int PERMISSION_REQUEST_CODE = 100;
    private final int SETTINGS_REQUEST_CODE = 200;

    private final int PERMISSION_FROM_START = 1;
    private final int PERMISSION_FROM_PERMISSION_RESULT = 2;

    private static final String SCHEME = "package";

    private static String permissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initData();
        checkRuntimePermission();
    }

    private void initData() {
        mActivity = this;
        mRunTimePermissionService = new RunTimePermissionService(this);
        mRunTimePermissionService.getPermissionList(permissions);
    }

    private void checkRuntimePermission() {
        if (mRunTimePermissionService.checkSelfPermission()) {
            if (mRunTimePermissionService.shouldShowRequestPermissionRationale()) {
                showPermissionDialog(PERMISSION_FROM_START);
            } else if (ProjectPreference.getBooleanPref(mActivity,
                    ProjectPreference.PREF_PERMISSION_STATUS, false)) {
                showSettingsDialog();
            } else {
                requestPermission();
            }

            ProjectPreference.setBooleanPref(mActivity, ProjectPreference.PREF_PERMISSION_STATUS, true);

        } else {
            goNext();
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(mActivity.getResources().getString(R.string.dialog_title));
        builder.setMessage(mActivity.getResources().getString(R.string.dialog_message));
        builder.setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_pos),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts(SCHEME, getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
                    }
                });

        builder.setNegativeButton(mActivity.getResources().getString(R.string.dialog_btn_neg),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_unable_to_get_permission),
                                Toast.LENGTH_LONG).show();
                    }
                });

        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SETTINGS_REQUEST_CODE:
                if (mRunTimePermissionService.checkSelfPermissionOnActivityResult())
                    goNext();
                else
                    checkRuntimePermission();
                break;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(mActivity, permissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && mRunTimePermissionService.grantResults(grantResults)) {
                    goNext();
                } else {
                    if (mRunTimePermissionService.shouldShowRequestPermissionRationale()) {
                        showPermissionDialog(PERMISSION_FROM_PERMISSION_RESULT);
                    } else {
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_unable_to_get_permission),
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void showPermissionDialog(final int permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(mActivity.getResources().getString(R.string.dialog_title));
        builder.setMessage(mActivity.getResources().getString(R.string.dialog_message));
        builder.setPositiveButton(mActivity.getResources().getString(R.string.dialog_btn_pos),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        requestPermission();
                    }
                });

        builder.setNegativeButton(mActivity.getResources().getString(R.string.dialog_btn_neg),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        switch (permission) {
                            case PERMISSION_FROM_START:
                                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_unable_to_get_permission),
                                        Toast.LENGTH_LONG).show();
                                break;
                            case PERMISSION_FROM_PERMISSION_RESULT:
                                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_unable_to_get_permission),
                                        Toast.LENGTH_LONG).show();
                                mActivity.finish();
                                break;
                        }
                    }
                });

        builder.show();
    }

    protected void goNext() {
        CommonLog.createLog(CommonLog.LOG_TYPE_ERROR, TAG, mActivity.getResources().getString(R.string.toast_permission_granted));
    }
}