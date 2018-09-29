package duti.com.droidtool.dtlib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.handlers.DatabaseHandler;

import static duti.com.droidtool.config.Constants.mDatabaseName;
import static duti.com.droidtool.config.Constants.mDatabaseVersion;


/**
 * Created by imrose on 6/16/2018.
 */

public class Db extends SQLiteOpenHelper {

    Context context;

    public Db(Context context) {
        super(context, new DroidTool(context).pref.getString(mDatabaseName), null, new DroidTool(context).pref.getInt(mDatabaseVersion));
        this.context = context;
    }

    public void executeQuery(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        new DatabaseHandler(context).alterTable(db, oldVersion, newVersion);
    }
}
