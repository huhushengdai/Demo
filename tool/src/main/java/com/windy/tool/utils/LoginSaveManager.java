package com.windy.tool.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * author: wang
 * time: 2015/11/13
 * description:
 * 登录管理工具
 * 关联 账号EditText,密码EditText，记住密码CheckBox，自动登录CheckBox
 * 登录保存账号密码到SharedPreferences
 */
public class LoginSaveManager implements CompoundButton.OnCheckedChangeListener {

    private static final String LOGIN_PREFERENCES = "Preferences_login";
    private static final String USER = "Preferences_user";
    private static final String PASSWORD = "Preferences_password";
    private static final String REMEMBER_USER = "Preferences_remember";//是否记住密码
    private static final String AUTOMATIC_LOGIN = "Preferences_automatic_login";//是否自动登录

    private Context mContext;
    private EditText userEdit;
    private EditText passwordEdit;
    private CheckBox rememberBox;
    private CheckBox automaticLoginBox;


    public LoginSaveManager(Context context,
                            EditText userEdit, EditText passwordEdit,
                            CheckBox rememberBox, CheckBox automaticLoginBox) {
        this.mContext = context;
        this.userEdit = userEdit;
        this.passwordEdit = passwordEdit;
        this.rememberBox = rememberBox;
        this.automaticLoginBox = automaticLoginBox;
        if (mContext != null) {
            init();
        }
    }

    private void init() {
        readLoginPreferences();
        if (rememberBox != null && automaticLoginBox != null) {
            rememberBox.setOnCheckedChangeListener(this);
            automaticLoginBox.setOnCheckedChangeListener(this);
        }
    }


    /**
     * 读取登录配置信息
     */
    private void readLoginPreferences() {
        SharedPreferences sp = mContext.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_APPEND);
        boolean rememberUser = sp.getBoolean(REMEMBER_USER, false);
        if (rememberUser) {
            userEdit.setText(sp.getString(USER, ""));
            passwordEdit.setText(sp.getString(PASSWORD, ""));
            rememberBox.setChecked(true);
            automaticLoginBox.setChecked(sp.getBoolean(AUTOMATIC_LOGIN, false));
        }
    }

    /**
     * 保存登录配置信息
     */
    private void saveLoginPreferences(String user, String password,
                                      boolean isRemember, boolean isAutomaticLogin) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_APPEND).edit();
        editor.putString(USER, user);
        editor.putString(PASSWORD, password);
        editor.putBoolean(REMEMBER_USER, isRemember);
        editor.putBoolean(AUTOMATIC_LOGIN, isAutomaticLogin);
        editor.commit();
    }

    /**
     * 保存登录配置信息
     * 对外接口，当登录成功后使用
     */
    public void saveLoginPreferences() {
        saveLoginPreferences(userEdit.getText().toString().trim(),
                passwordEdit.getText().toString().trim(),
                rememberBox.isChecked(),
                automaticLoginBox.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == rememberBox.getId()) {
            rememberUser(isChecked);
        } else if (buttonView.getId() == automaticLoginBox.getId()) {
            automaticLogin(isChecked);
        }
    }

    /**
     * 点击 记住密码 checkbox 事件
     *
     * @param isChecked
     */
    private void rememberUser(boolean isChecked) {
        if (!isChecked) {//取消记住密码
            automaticLoginBox.setChecked(false);//取消自动登录
            saveLoginPreferences("", "", false, false);
        }
    }

    /**
     * 点击 自动登录 checkbox 事件
     *
     * @param isChecked
     */
    private void automaticLogin(boolean isChecked) {
        if (!isChecked) {//取消自动登录
            saveLoginPreferences(userEdit.getText().toString().trim(),
                    passwordEdit.getText().toString().trim(),
                    rememberBox.isChecked(),
                    false);
        }else{
            rememberBox.setChecked(true);
        }
    }
}
