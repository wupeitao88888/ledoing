package cn.ledoing.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class LCDbHelper extends
		android.database.sqlite.SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String mDbName =  SDBHelper.DB_DIR + File.separator + "ledoing.db";
    public LCDbHelper(Context context) {
		super(context, mDbName, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table ispraise(groupsid intager primary key,prise varchar(50),userid varchar(50))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
