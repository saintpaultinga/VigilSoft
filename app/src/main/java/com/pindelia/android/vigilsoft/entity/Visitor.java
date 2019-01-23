package com.pindelia.android.vigilsoft.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
@Entity(tableName = "visitor_table", foreignKeys = {@ForeignKey(
        entity = Employee.class, parentColumns = "id",
        childColumns = "emp_visited"
), @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id")})
public class Visitor {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "user_id")
    private Long user_id;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String company;
    private String reason;
    @ColumnInfo(name = "entered_date")
    private Date enteredDate;
    private Date leftDate;
    @ColumnInfo(name = "emp_visited")
    private Long empId;
    private String departement;
    @ColumnInfo(name = "id_image")
    private String idImage;
    private String signature;
    // status = { 1:"data is sent", 0:"data is not sent"}
    private int status;

    public Visitor() {

    }

    public Long getUser_id() {
        return user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(Date enteredDate) {
        this.enteredDate = enteredDate;
    }

    public Date getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(Date leftDate) {
        this.leftDate = leftDate;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
