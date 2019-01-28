package com.pindelia.android.vigilsoft.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.pindelia.android.vigilsoft.repository.VigilRepository;
import com.pindelia.android.vigilsoft.viewmodel.VisitorViewModel;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private VigilRepository repository;

    @Inject
    public ViewModelFactory(VigilRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VisitorViewModel.class)) {
            return (T) new VisitorViewModel(repository);
        }
        throw new IllegalArgumentException("Unkown class name");
    }
}
