package com.pindelia.android.vigilsoft.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.pindelia.android.vigilsoft.daointerface.EmployeeDao;
import com.pindelia.android.vigilsoft.daointerface.UserDao;
import com.pindelia.android.vigilsoft.daointerface.VisitorDao;
import com.pindelia.android.vigilsoft.entity.Employee;
import com.pindelia.android.vigilsoft.entity.User;
import com.pindelia.android.vigilsoft.entity.Visitor;
import com.pindelia.android.vigilsoft.persistence.util.DateTypeConverter;

@Database(entities = {User.class, Visitor.class, Employee.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract VisitorDao visitorDao();
    public abstract UserDao userDao();
    public abstract EmployeeDao employeeDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();

        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;

        private PopulateDBAsyncTask(AppDatabase appDatabase) {
            this.userDao = appDatabase.userDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User("stinga", "test"));
            return null;
        }
    }

}
