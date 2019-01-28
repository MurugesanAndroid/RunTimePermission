package mis.tvscs.com.runtimepermission.ui;

import android.os.Bundle;
import android.widget.Toast;

import mis.tvscs.com.runtimepermission.R;
import mis.tvscs.com.runtimepermission.utility.CommonLog;

public class HomeActivity extends BaseActivity {
    private final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void goNext() {
        CommonLog.createLog(CommonLog.LOG_TYPE_ERROR, TAG, mActivity.getResources().getString(R.string.toast_permission_granted));
        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_permission_granted),
                Toast.LENGTH_LONG).show();
    }
}
