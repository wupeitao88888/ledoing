/*******************************************************************************
 * Copyright (c) 2014 by ehoo Corporation all right reserved.
 * 2014-10-13 
 * 
 *******************************************************************************/
package cn.ledoing.db;

import java.util.concurrent.atomic.AtomicInteger;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseManager {

	private AtomicInteger mOpenCounter = new AtomicInteger();

	private static DatabaseManager instance;
	private static SQLiteOpenHelper mDatabaseHelper;
	private SQLiteDatabase mDatabase;

	public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
		if (instance == null) {
			instance = new DatabaseManager();
			mDatabaseHelper = helper;
		}
	}

	public static synchronized DatabaseManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					DatabaseManager.class.getSimpleName()
							+ " is not initialized, call initializeInstance(..) method first.");
		}

		return instance;
	}

	public synchronized SQLiteDatabase openDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			// Opening new database
			mDatabase = mDatabaseHelper.getWritableDatabase();
		}

		return mDatabase;
	}

	public synchronized SQLiteDatabase readDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			// Opening new database
			mDatabase = mDatabaseHelper.getReadableDatabase();
		}
		return mDatabase;
	}

	public synchronized void closeDatabase() {
		if (mOpenCounter.decrementAndGet() == 0) {
			// Closing database
			if (mDatabase != null) {
				mDatabase.close();
			}

		}
	}
}
