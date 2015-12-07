package com.rey.mvp;

/**
 * Created by Rey on 11/16/2015.
 */
public interface Presenter<V, S extends ViewState> {
    /**
     * Called when the presenter gets created.
     * Note that because presenters survive configuration changes,
     * onCreate will not get called every time your associated view(fragment) gets created.
     * @param viewState A ViewState associated with this Presenter.
     */
    void onCreate(S viewState);

    /**
     * Attach your view to this presenter.
     * @param view The view will associated with this presenter.
     */
    void onAttachView(V view);

    /**
     * Called when the view go visible (Fragment onResume()).
     */
    void onViewVisible();

    /**
     * Called when the view go hiding (Fragment onPause()).
     */
    void onViewHide();

    /**
     * Detach your view from this presenter.
     */
    void onDetachView();

    /**
     * Called when the presenter is destroyed.
     * Note that because presenters survive configuration changes,
     * onDestroy will not get called every time your associated view(fragment) is destroyed.
     * This method is intended for cleanup, for example, cancelling a useless request.
     */
    void onDestroy();
}
