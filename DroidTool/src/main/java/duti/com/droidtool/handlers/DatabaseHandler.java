package duti.com.droidtool.handlers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseHandler {

    Context mContext;

    public interface onCreateTable {
        void onCreated();
    }

    private onCreateTable onCreateTable = null;

    public void setCreateTableListener(onCreateTable onCreateTable) {
        this.onCreateTable = onCreateTable;
    }

    public interface onAlterTable {
        void onAltered();
    }

    private onAlterTable onAlterTable = null;

    public void setAlterTableListener(onAlterTable onAlterTable) {
        this.onAlterTable = onAlterTable;
    }

    public DatabaseHandler(Context context) {
        mContext = context;
        createTable();
    }

    public void createTable() {
        if(onCreateTable!=null)onCreateTable.onCreated();
    }

    public void alterTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(onAlterTable!=null)onAlterTable.onAltered();
    }
}
