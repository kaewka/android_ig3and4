package com.codepath.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codepath.instagram.R;
import com.codepath.instagram.networking.InstagramClient;
import com.codepath.oauth.OAuthLoginActivity;

public class LoginActivity extends OAuthLoginActivity<InstagramClient> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View view) {
        getClient().connect();
    }

    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
}
