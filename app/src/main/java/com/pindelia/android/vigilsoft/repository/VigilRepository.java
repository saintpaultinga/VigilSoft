package com.pindelia.android.vigilsoft.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.google.gson.JsonElement;
import com.pindelia.android.vigilsoft.daointerface.VisitorDao;
import com.pindelia.android.vigilsoft.entity.Visitor;
import com.pindelia.android.vigilsoft.net.service.VigilSoftService;
import com.pindelia.android.vigilsoft.persistence.AppDatabase;

import java.util.List;

import io.reactivex.Observable;

public class VigilRepository {
    private VisitorDao visitorDao;
    private LiveData<List<Visitor>> allVisitors;
    // network interface for REST API
    private VigilSoftService apiCallInterface;

    public VigilRepository(Application application, VigilSoftService vigilSoftService) {
        AppDatabase vigilDatabase = AppDatabase.getInstance(application);
        visitorDao = vigilDatabase.visitorDao();
        apiCallInterface = vigilSoftService;

    }

    public Observable<JsonElement> sendVisitorData(Visitor visitor) {
        return apiCallInterface.createVisitor(visitor);
    }
}
