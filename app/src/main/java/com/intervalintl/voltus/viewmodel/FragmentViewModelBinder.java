package com.intervalintl.voltus.viewmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


public final class FragmentViewModelBinder<VM extends BaseViewModel>
        extends FragmentManager.FragmentLifecycleCallbacks {

    private final FragmentManager fragmentManager;
    private final Callback<VM> callback;
    private final Class<VM> viewModelClass;
    private final String viewModelId;
    private final Fragment fragment;

    public FragmentViewModelBinder(FragmentManager fragmentManager, Callback<VM> callback
            , Fragment fragment, Class<VM> viewModelClass, String viewModelId) {

        this.fragmentManager = fragmentManager;
        this.callback = callback;
        this.fragment = fragment;
        this.viewModelClass = viewModelClass;
        this.viewModelId = viewModelId;

        fragmentManager.registerFragmentLifecycleCallbacks(this, false);
    }

    @Override
    public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
        if (f == fragment) {
            fragmentManager.unregisterFragmentLifecycleCallbacks(this);
            VM viewModel = ViewModelProviders.of(fragment).get(viewModelId, viewModelClass);
            viewModel.onFragmentCreated();
            callback.onViewModelBound(viewModel);
        }
    }


    public interface Callback<U extends BaseViewModel> {
        void onViewModelBound(U viewModel);
    }

}
