package com.pindelia.android.vigilsoft.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
@Entity(tableName = "visitor_table")
public class Visitor implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "user_id")
    private String user_id;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    private String company;
    private String reason;
    @ColumnInfo(name = "entered_date")
    private Date enteredDate;
    private Date leftDate;
    @ColumnInfo(name = "emp_visited")
    private String empId;
    private String departement;
    @ColumnInfo(name = "id_front_image")
    private String idFrontImage;
    @ColumnInfo(name = "id_back_image")
    private String idBackImage;
    private String signature;
    @ColumnInfo(name = "birth_date")
    private String birthDate;
    @ColumnInfo(name = "id_exp_date")
    private String idExpiryDate;
    private String gender;
    private String nationality;
    @ColumnInfo(name = "id_number")
    private String idNumber;
    private String idCode;
    // status = { 1:"data is sent", 0:"data is not sent"}
    private int status;

    public Visitor() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIdFrontImage() {
        return idFrontImage;
    }

    public void setIdFrontImage(String idFrontImage) {
        this.idFrontImage = idFrontImage;
    }

    public String getIdBackImage() {
        return idBackImage;
    }

    public void setIdBackImage(String idBackImage) {
        this.idBackImage = idBackImage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last_name) {
        this.lastName = last_name;
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

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdExpiryDate() {
        return idExpiryDate;
    }

    public void setIdExpiryDate(String idExpiryDate) {
        this.idExpiryDate = idExpiryDate;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    protected Visitor(Parcel in) {
        id = in.readLong();
        user_id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
        company = in.readString();
        reason = in.readString();
        enteredDate = new Date(in.readLong());
        leftDate = new Date(in.readLong());
        empId = in.readString();
        departement = in.readString();
        idFrontImage = in.readString();
        idBackImage = in.readString();
        signature = in.readString();
        birthDate = in.readString();
        idExpiryDate = in.readString();
        gender = in.readString();
        nationality = in.readString();
        idNumber = in.readString();
        idCode = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(user_id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNumber);
        dest.writeString(company);
        dest.writeString(reason);
        dest.writeLong(enteredDate.getTime());
        dest.writeLong(leftDate.getTime());
        dest.writeString(empId);
        dest.writeString(departement);
        dest.writeString(idFrontImage);
        dest.writeString(signature);
        dest.writeString(birthDate);
        dest.writeString(idExpiryDate);
        dest.writeString(gender);
        dest.writeString(nationality);
        dest.writeString(idNumber);
        dest.writeString(idCode);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Visitor> CREATOR = new Creator<Visitor>() {
        @Override
        public Visitor createFromParcel(Parcel in) {
            return new Visitor(in);
        }

        @Override
        public Visitor[] newArray(int size) {
            return new Visitor[size];
        }
    };
}
