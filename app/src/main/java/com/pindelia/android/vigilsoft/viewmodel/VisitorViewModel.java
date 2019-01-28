package com.pindelia.android.vigilsoft.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.pindelia.android.vigilsoft.entity.Visitor;
import com.pindelia.android.vigilsoft.net.tools.ApiResponse;
import com.pindelia.android.vigilsoft.repository.VigilRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VisitorViewModel extends ViewModel {

    private VigilRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();

    public VisitorViewModel(VigilRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> createVisitorResponse() {
        return responseLiveData;
    }

    public void executeVisitorCreation(Visitor visitor) {
        disposable.add(repository.sendVisitorData(visitor)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
        .subscribe(
                result -> responseLiveData.setValue(ApiResponse.success(result)),
                throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
        ));
    }

    @Override
    protected void onCleared() {
        disposable.clear();
    }
}
