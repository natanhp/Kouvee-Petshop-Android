package com.p3lj2.koveepetshop.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.p3lj2.koveepetshop.dao.EmployeeDAO;
import com.p3lj2.koveepetshop.model.EmployeeModel;

@Database(entities = {EmployeeModel.class}, version = 1, exportSchema = false)
public abstract class KouveeDatabase extends RoomDatabase {
    private static KouveeDatabase kouveeDatabase;

    public abstract EmployeeDAO employeeDAO();

    public static synchronized KouveeDatabase getInstance(Context context) {
        if (kouveeDatabase == null) {
            kouveeDatabase = Room.databaseBuilder(context.getApplicationContext(), KouveeDatabase.class, "kouvee_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return kouveeDatabase;
    }
}
