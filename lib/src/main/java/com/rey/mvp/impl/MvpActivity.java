package com.rey.mvp.impl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rey.mvp.BuildConfig;
import com.rey.mvp.CacheFactory;
import com.rey.mvp.Presenter;
import com.rey.mvp.PresenterCache;
import com.rey.mvp.ViewStateCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An Activity that implement CacheFactory
 */
public class MvpActivity extends AppCompatActivity implements CacheFactory {

    private MvpActivityDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDelegate = new MvpActivityDelegate();
        mDelegate.onCreate(savedInstanceState, getLastCustomNonConfigurationInstance());

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mDelegate.getPersistentObject();
    }

    @Override
    public PresenterCache getPresenterCache() {
        return mDelegate;
    }

    @Override
    public ViewStateCache getViewStateCache() {
        return mDelegate;
    }
}
