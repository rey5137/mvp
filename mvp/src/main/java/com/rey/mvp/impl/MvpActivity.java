package com.rey.mvp.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rey.mvp.Presenter;
import com.rey.mvp.PresenterFactory;
import com.rey.mvp.ViewState;
import com.rey.mvp.ViewStateFactory;

/**
 * Created by Rey on 9/7/2016.
 */
public abstract class MvpActivity<V, P extends Presenter<V, S>, S extends ViewState> extends PersistentActivity {

    protected MvpDelegate<V, P, S> mMvpDelegate = new MvpDelegate<>();

    protected PresenterFactory<P> mPresenterFactory = new PresenterFactory<P>() {
        @NonNull
        @Override public P createPresenter() {
            return onCreatePresenter(MvpActivity.this);
        }
    };

    protected ViewStateFactory<S> mViewStateFactory = new ViewStateFactory<S>() {

        @NonNull
        @Override
        public S createViewState() {
            return onCreateViewState(MvpActivity.this);
        }

        @Override
        public String getViewStateTag() {
            return MvpActivity.this.getViewStateTag();
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mMvpDelegate.onCreate(getPresenterCache(), getViewStateCache(), savedInstanceState, mPresenterFactory, mViewStateFactory);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mMvpDelegate.onViewCreated(getViewImpl());
    }

    @Override
    public void onResume() {
        super.onResume();
        mMvpDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMvpDelegate.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMvpDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMvpDelegate.onDestroyView();
        mMvpDelegate.onDestroy();
    }

    /**
     * @return The Presenter that will attach this view.
     */
    @NonNull
    public abstract P onCreatePresenter(Context context);

    /**
     * @return The ViewState will be bound with the Presenter.
     */
    @NonNull
    public abstract S onCreateViewState(Context context);

    /**
     * @return An unique tag for the ViewState.
     */
    @NonNull
    public abstract String getViewStateTag();

    public P getPresenter() {
        return mMvpDelegate.getPresenter();
    }

    protected boolean isDestroyedBySystem(){
        return mMvpDelegate.isDestroyedBySystem();
    }

    @SuppressWarnings("unchecked")
    public V getViewImpl() {
        V v;
        try {
            v = (V) this;
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
        return v;
    }
}
