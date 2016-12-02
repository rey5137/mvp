package com.rey.mvp.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;

import com.rey.mvp.CacheFactory;
import com.rey.mvp.Presenter;
import com.rey.mvp.PresenterFactory;
import com.rey.mvp.ViewState;
import com.rey.mvp.ViewStateFactory;

/**
 * A base DialogFragment class that handle presenter construction, calling and caching.
 */
public abstract class MvpDialogFragment<V, P extends Presenter<V, S>, S extends ViewState> extends AppCompatDialogFragment {

    protected Context mContext;
    protected MvpDelegate<V, P, S> mMvpDelegate = new MvpDelegate<>();

    protected PresenterFactory<P> mPresenterFactory = new PresenterFactory<P>() {
        @NonNull
        @Override public P createPresenter() {
            return onCreatePresenter(mContext);
        }
    };

    protected ViewStateFactory<S> mViewStateFactory = new ViewStateFactory<S>() {

        @NonNull
        @Override
        public S createViewState() {
            return onCreateViewState(mContext);
        }

        @Override
        public String getViewStateTag() {
            return MvpDialogFragment.this.getViewStateTag();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
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
