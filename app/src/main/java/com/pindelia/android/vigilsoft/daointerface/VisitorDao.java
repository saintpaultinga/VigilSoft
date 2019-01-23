package com.pindelia.android.vigilsoft.daointerface;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pindelia.android.vigilsoft.entity.Visitor;

import java.util.List;

@Dao
public interface VisitorDao {
    @Insert
    void insert(Visitor visitor);
    @Update
    void update(Visitor visitor);
    @Delete
    void delete(Visitor visitor);
    @Query("DELETE FROM visitor_table")
    void deleteAllDetails();
    @Query("SELECT * FROM visitor_table ORDER BY entered_date DESC")
    LiveData<List<Visitor>> getAllVisitors();
}
