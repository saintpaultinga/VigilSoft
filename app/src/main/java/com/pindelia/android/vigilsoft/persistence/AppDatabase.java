package com.pindelia.android.vigilsoft.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.pindelia.android.vigilsoft.daointerface.EmployeeDao;
import com.pindelia.android.vigilsoft.daointerface.UserDao;
import com.pindelia.android.vigilsoft.daointerface.VisitorDao;
import com.pindelia.android.vigilsoft.entity.Employee;
import com.pindelia.android.vigilsoft.entity.User;
import com.pindelia.android.vigilsoft.entity.Visitor;

@Database(entities = {User.class, Visitor.class, Employee.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    abstract VisitorDao visitorDao();
    abstract UserDao userDao();
    abstract EmployeeDao employeeDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "note_database")
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
