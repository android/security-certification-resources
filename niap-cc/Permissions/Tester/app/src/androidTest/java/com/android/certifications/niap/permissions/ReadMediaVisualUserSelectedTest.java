/*
 * Copyright 2023 The Android Open Source Project
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

package com.android.certifications.niap.permissions;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.utils.PermissionUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumentation test to verify READ_MEDIA_VISUAL_USER_SELECTED permission.
 * 1. To run this test you should put at least one media image to the ContentResolver.
 * 2. Try this test against both normal and no-perm variant apk files.
 * The script is written for the latest version of the android os,
 * If you'd like to run with former versions please try previous versions.
 */
@RunWith(AndroidJUnit4.class)
public class ReadMediaVisualUserSelectedTest {
    /**
     *
     * A list of permissions that can be granted to the shell identity.
     */
    @Rule
    public
    ErrorCollector errs = new ErrorCollector();
    @Rule
    public TestName name= new TestName();
    TestAssertLogger a = new TestAssertLogger(name);
    private UiAutomation mUiAutomation;
    private Context mContext;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false,
            true);
    @Before
    public void setUp() {
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mUiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDevice = UiDevice.getInstance(mInstrumentation);
    }


    @After
    public void tearDown() {
        mUiAutomation.dropShellPermissionIdentity();
    }


    private Activity getActivity() {
        Activity activity = null;

        Context context = mInstrumentation.getContext();
        Instrumentation.ActivityMonitor monitor =
                mInstrumentation.addMonitor(
                        "com.android.certifications.niap.permissions.activities.MainActivity",
                        null, false);

        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage("com.android.certifications.niap.permissions");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        activity = monitor.waitForActivityWithTimeout(2000);

        return activity;
    }
    @Test
    public void runPermissionTests(){

        boolean hasPermissionInManifest = PermissionUtils.ensureRequiredPermissions(
                new String[]{android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED},mInstrumentation.getTargetContext()
        );
        boolean ok = true;
        try {
            MainActivity activity = (MainActivity) getActivity();

            activity.addLogLine(
                    "[READ_MEDIA_VISUAL_USER_SELECTED Test case]. Please run this test case with no-perm/normal apk variants.");

            //Eval variant and determine expected result//
            List<String> not_granted = new ArrayList<String>();
            if (checkSelfPermission(mContext,android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                    != PackageManager.PERMISSION_GRANTED) {
                not_granted.add(android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED);
                not_granted.add(Manifest.permission.READ_MEDIA_IMAGES);
            }

            //* Set System Language Setting as 'English (United States)' to find
            //* an ui object.
            //Wait dialogue shown and click "Select phtos and videos"
            //1-1.Wait chooser dialogue and then choose first one
            //1-2.Wait chooser dialgoue and then choose nothing
            //2. close chooser
            String[] perms = not_granted.toArray(new String[0]);
            if(perms.length>0)
                ActivityCompat.requestPermissions(activity, perms,
                        Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS);
            else
                Log.d("tag","the permission is already granted.");
            mDevice.waitForIdle();
            //mDevice.wait(10000);
            //Find a button with text (the line expects language setting as 'English')w(5000);

            //Fix the test case for Android 15. Showing Approval Dialog for READ_MEDIA_VISUAL_USER_SELECTED
            //Plus Media ADialogue
            mDevice.wait(Until.findObject(By.textStartsWith("Allow limited")),1000);
            UiObject2 btnUserSelected = mDevice.findObject(By.textStartsWith("Allow limited"));
            Log.d("ui",btnUserSelected.toString());
            if(btnUserSelected==null || !btnUserSelected.isClickable()){
                throw new RuntimeException("Can not find expected ui (Allow limited access)");
            }

            btnUserSelected.clickAndWait(Until.newWindow(),500);
            mDevice.waitForIdle();
            //Change for Android 15 : The text line 'Select photos and videos ...' is now unclickable
            mDevice.wait(Until.findObject(By.textStartsWith("Select photos and videos")),5000);
            btnUserSelected = mDevice.findObject(By.textStartsWith("Select photos and videos"));
            if(btnUserSelected==null){
                throw new RuntimeException("Can not find expected ui");
            }
            activity.addLogLine(btnUserSelected.toString());
            btnUserSelected.clickAndWait(Until.newWindow(),500);
            List<UiObject2> cards =
                    mDevice.findObjects(By.clazz(CardView.class));
            if(cards.size() == 0){
                throw new RuntimeException("Can not find expected ui (Cards)");
            }
            cards.get(0).click();

            mDevice.wait(Until.findObject( By.textStartsWith("Allow"))
                   ,1000);

            mDevice.waitForIdle();
            UiObject2 btnAllow = mDevice.findObject(By.textStartsWith("Allow"));
            if(btnAllow==null || !btnAllow.isClickable()){
                throw new RuntimeException("Can not find expected ui (Allow Button)");
            }
            btnAllow.clickAndWait(Until.newWindow(),500);
            mDevice.waitForIdle();

            ContentResolver contentResolver = mContext.getContentResolver();
            String[] PROJECTION_BUCKET = new String[] { "bucket_id", "bucket_display_name", "datetaken", "_data" };

            String[] thumbColumns = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
            String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN};
            String orderBy = MediaStore.Images.Media.DATE_TAKEN+ " DESC";

            @SuppressLint("Recycle") Cursor cursor = contentResolver.query
                    (MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns, "", new String[]{}, orderBy);

            if (cursor == null) {
                throw new SecurityException("Unable to obtain an image to test READ_MEDIA_IMAGES");
            } else if (!cursor.moveToFirst()) {
                throw new SecurityException("Failed to load media files:READ_MEDIA_IMAGES." +
                        "Pleaes ensure to execute the companion app before testing.");
            } else {
                if(cursor.getCount()>=1) {
                    Log.d(TAG, "Count=" + cursor.getCount());
                }
            }
            activity.addLogLine("Count="+cursor.getColumnCount());

        } catch (RuntimeException ex){
            ok = false;
            //Log.d("tag", ex.getMessage());
            errs.checkThat(a.Msg("grant: "+ex.getMessage()),
                    false,org.hamcrest.CoreMatchers.is(true));
            ex.printStackTrace();
        }

        if((ok && hasPermissionInManifest) || (!ok && !hasPermissionInManifest)){
            errs.checkThat(a.Msg("grant: "+hasPermissionInManifest+" result:"+ok),
                    true,org.hamcrest.CoreMatchers.is(true));
        } else {
            errs.checkThat(a.Msg("grant: "+hasPermissionInManifest+" result:"+ok),
                    false,org.hamcrest.CoreMatchers.is(true));
        }
    }


}