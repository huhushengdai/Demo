package com.windy.im.ui;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.windy.im.R;
import com.windy.im.application.BaseApp;
import com.windy.tool.utils.LoginSaveManager;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private LoginSaveManager loginSaveManager;
    private BaseApp baseApp;

    //---      view       -----
    @ViewInject(R.id.login_user)
    private EditText userEdit;
    @ViewInject(R.id.login_password)
    private EditText passwordEdit;
    @ViewInject(R.id.login_remember_user)
    private CheckBox rememberBox;
    @ViewInject(R.id.login_auto_login)
    private CheckBox autoLogin;

    @Override
    protected void init() {
        baseApp = BaseApp.getInstance();
        loginSaveManager = new LoginSaveManager(this,
                userEdit, passwordEdit,
                rememberBox, autoLogin);
        if (autoLogin.isChecked()) {
//            BaseApp.toast("自动登录");
        }
    }

    @OnClick(R.id.login_submit)
    public void httpLogin(View view) {
        loginSaveManager.saveLoginPreferences();//保存登录信息

        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY,"LHQhacKsVdBn5wHIi25DCfmv");
    }
}
