package com.rey.mvp;

/**
 * A base Presenter class.
 * @param <V>
 */
public abstract class BasePresenter<V, S extends ViewState> implements Presenter<V, S> {

    protected V mView;
    protected S mViewState;
    protected boolean mViewVisible = false;

    @Override public void onCreate(S viewState) {
        mViewState = viewState;
    }

    @Override public void onAttachView(V view) {
        mView = view;
    }

    @Override
    public void onViewVisible() {
        mViewVisible = true;
    }

    @Override
    public void onViewHide() {
        mViewVisible = false;
    }

    @Override public void onDetachView() {
        this.mView = null;
    }

    @Override public void onDestroy() {
    }

}
