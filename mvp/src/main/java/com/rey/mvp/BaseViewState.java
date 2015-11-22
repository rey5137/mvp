package com.rey.mvp;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rey on 11/16/2015.
 */
public abstract class BaseViewState implements ViewState {

    protected String mTag;
    protected List<WeakReference<Presenter>> mPresenters = new ArrayList<>();

    @Override
    public String getTag() {
        return mTag;
    }

    @Override
    public void setTag(String tag) {
        mTag = tag;
    }

    @Override
    public void onBind(Presenter presenter) {
        boolean exist = false;
        for(int i = mPresenters.size() - 1; i >= 0; i--){
            WeakReference<Presenter> ref = mPresenters.get(i);
            if(ref.get() == null)
                mPresenters.remove(i);
            else if(ref.get() == presenter)
                exist = true;
        }

        if(!exist)
            mPresenters.add(new WeakReference<>(presenter));
    }

    @Override
    public boolean onUnbind(Presenter presenter) {
        for(int i = mPresenters.size() - 1; i >= 0; i--){
            WeakReference<Presenter> ref = mPresenters.get(i);
            if(ref.get() == null || ref.get() == presenter)
                mPresenters.remove(i);
        }

        return mPresenters.isEmpty();
    }
}
