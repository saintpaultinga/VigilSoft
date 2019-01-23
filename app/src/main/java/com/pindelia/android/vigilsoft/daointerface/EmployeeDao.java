package com.pindelia.android.vigilsoft.daointerface;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.pindelia.android.vigilsoft.entity.Employee;

@Dao
public interface EmployeeDao {
    @Insert
    void insert(Employee employee);
    @Update
    void update(Employee employee);
    @Delete
    void delete(Employee employee);
}
