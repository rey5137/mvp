package com.rey.mvp.impl;

import android.os.Bundle;

import com.rey.mvp.Presenter;
import com.rey.mvp.PresenterCache;
import com.rey.mvp.PresenterFactory;
import com.rey.mvp.ViewState;
import com.rey.mvp.ViewStateCache;
import com.rey.mvp.ViewStateFactory;

import java.lang.ref.WeakReference;

public class MvpDelegate<V, P extends Presenter<V, S>, S extends ViewState> {

    private static final String KEY_PRESENTER_ID = "com.rey.mvp.presenter_id";
    private static final String KEY_VIEWSTATE_TAG = "com.rey.mvp.viewstate_tag";

    private String mViewStateTag;
    private int mPresenterId;

    private WeakReference<PresenterCache> mPresenterCacheRef;
    private WeakReference<ViewStateCache> mViewStateCacheRef;

    private P mPresenter;
    private S mViewState;

    private boolean mIsDestroyedBySystem;

    public void onCreate(PresenterCache presenterCache, ViewStateCache viewStateCache, Bundle savedInstanceState, PresenterFactory<P> presenterFactory, ViewStateFactory<S> viewStateFactory) {
        if(mPresenter != null && mViewState != null)
            return;

        mPresenterCacheRef = new WeakReference<>(presenterCache);
        mViewStateCacheRef = new WeakReference<>(viewStateCache);

        mViewStateTag = (savedInstanceState == null) ? viewStateFactory.getViewStateTag() : savedInstanceState.getString(KEY_VIEWSTATE_TAG);
        mViewState = viewStateCache.getViewState(mViewStateTag);
        if(mViewState == null) {
            mViewState = viewStateFactory.createViewState();
            mViewState.setTag(mViewStateTag);
            viewStateCache.restoreViewStateData(mViewState);
            viewStateCache.addViewState(mViewState);
        }

        mPresenterId = (savedInstanceState == null) ? presenterCache.generatePresenterId() : savedInstanceState.getInt(KEY_PRESENTER_ID);
        mPresenter = presenterCache.getPresenter(mPresenterId);
        if (mPresenter == null) {
            mPresenter = presenterFactory.createPresenter();
            presenterCache.setPresenter(mPresenterId, mPresenter);
            mPresenter.onCreate(mViewState);
        }

        mViewState.onBind(mPresenter);
    }

    public void onViewCreated(V view) {
        mPresenter.onAttachView(view);
    }

    public void onDestroyView() {
        mPresenter.onDetachView();
    }

    public void onResume() {
        mIsDestroyedBySystem = false;
        mPresenter.onViewVisible();
    }

    public void onPause(){
        mPresenter.onViewHide();
    }

    public void onSaveInstanceState(Bundle outState) {
        mIsDestroyedBySystem = true;
        outState.putInt(KEY_PRESENTER_ID, mPresenterId);
        outState.putString(KEY_VIEWSTATE_TAG, mViewStateTag);
        ViewStateCache cache = mViewStateCacheRef.get();
        if(cache != null)
            cache.saveViewStateData(mViewState);
    }

    public void onDestroy() {
        if (!mIsDestroyedBySystem) {
            // User is exiting this view, remove mPresenter from the presenterCache.
            PresenterCache presenterCache = mPresenterCacheRef.get();
            if(presenterCache != null)
                presenterCache.setPresenter(mPresenterId, null);

            mPresenter.onDestroy();

            //Unbind mPresenter from mViewState. If mViewState isn't bound with any Presenter, then remove it from viewStateCache.
            if(mViewState.onUnbind(mPresenter)) {
                ViewStateCache cache = mViewStateCacheRef.get();
                if(cache != null)
                    cache.removeViewState(mViewState);
            }
        }
    }

    public P getPresenter() {
        return mPresenter;
    }

    public boolean isDestroyedBySystem(){
        return mIsDestroyedBySystem;
    }

}
