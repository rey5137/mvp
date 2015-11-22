package com.rey.mvp.impl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.rey.mvp.CacheFactory;
import com.rey.mvp.Presenter;
import com.rey.mvp.PresenterFactory;
import com.rey.mvp.ViewState;
import com.rey.mvp.ViewStateFactory;

/**
 * A base Fragment class that handle presenter construction, calling and caching.
 */
public abstract class MvpFragment<V, P extends Presenter<V, S>, S extends ViewState> extends Fragment {

    private MvpDelegate<V, P, S> mMvpDelegate = new MvpDelegate<>();

    private PresenterFactory<P> mPresenterFactory = new PresenterFactory<P>() {
        @NonNull
        @Override public P createPresenter() {
            return onCreatePresenter();
        }
    };

    protected ViewStateFactory<S> mViewStateFactory = new ViewStateFactory<S>() {

        @NonNull
        @Override
        public S createViewState() {
            return onCreateViewState();
        }

        @Override
        public String getViewStateTag() {
            return getViewStateTag();
        }

    };

    /**
     * The default implementation try cast the Activity to CacheFactory.
     * @return The CacheFactory object.
     */
    protected CacheFactory getCacheFactory(){
        Activity activity = getActivity();

        if(activity instanceof CacheFactory)
            return (CacheFactory)activity;
        else
            throw new RuntimeException(getClass() + " must be attached to " +
                    "an Activity that implements " + CacheFactory.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CacheFactory cacheFactory = getCacheFactory();
        mMvpDelegate.onCreate(cacheFactory.getPresenterCache(), cacheFactory.getViewStateCache(), savedInstanceState, mPresenterFactory, mViewStateFactory);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void onDestroyView() {
        super.onDestroyView();
        mMvpDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMvpDelegate.onDestroy();
    }

    /**
     * @return The Presenter that will attach this view.
     */
    @NonNull
    public abstract P onCreatePresenter();

    /**
     * @return The ViewState will be bound with the Presenter.
     */
    @NonNull
    public abstract S onCreateViewState();

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
