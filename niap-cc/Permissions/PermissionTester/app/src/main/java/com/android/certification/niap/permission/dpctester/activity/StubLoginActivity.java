/*
 * Copyright (C) 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.certification.niap.permission.dpctester.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.certification.niap.permission.dpctester.R;

public class StubLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        login("dummy","password");
    }

    // ログイン処理
    public void login(final String name, final String password) {
        loginSuccess(name, password);
    }

    // ログイン処理のコールバック
    public void loginSuccess(final String name, final String password) {

        Account account = new Account(name, "com.dpctester.stub");
        AccountManager am = AccountManager.get(this);
        am.addAccountExplicitly(account, password, null);
        setResult(RESULT_OK);
        finish();
    }
}