package com.shang.noticeuefa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.shang.noticeuefa.model2.*;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "notice.sqlite";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<Team, Integer> teamDao = null;
    private Dao<Tour, Integer> tourDao = null;
    private Dao<Match, Integer> matchDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Team.class);
            TableUtils.createTable(connectionSource, Group.class);
            TableUtils.createTable(connectionSource, Match.class);
            TableUtils.createTable(connectionSource, Tour.class);
            TableUtils.createTable(connectionSource, TeamGroup.class);
            TableUtils.createTable(connectionSource, Notification.class);
//			// here we try inserting data in the on-create as a test
//
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Team, Integer> getTeamDao() throws SQLException {
        if (teamDao == null) {
            teamDao = getDao(Team.class);
        }
        return teamDao;
    }

    public Dao<Tour, Integer> getTourDao() throws SQLException {
        if (tourDao == null) {
            tourDao = getDao(Tour.class);
        }
        return tourDao;
    }

    public Dao<Match, Integer> getMatchDao() throws SQLException {
        if (matchDao == null) {
            matchDao = getDao(Match.class);
        }
        return matchDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        teamDao = null;
        tourDao = null;
        matchDao = null;
    }

    public void truncate(Class clz) {
        try {
            Dao dao = getDao(clz);
            dao.delete(dao.queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void createAll(Collection c) {
        if (c.size() == 0)
            return;

        Class clz = c.iterator().next().getClass();
        try {
            Dao dao = getDao(clz);
            for (Object obj : c) {
                dao.create(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
