package hr.math.pemarti.popravni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by pemarti on 2/23/18.
 */

public class DBAdapter extends BaseAdapter{
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "ime";
    static final String KEY_SURNAME = "prezime";
    static final String KEY_ROWID_SPORT = "_id";
    static final String KEY_NAZIV = "naziv";
    static final String KEY_VRSTA = "vrsta";
    static final String KEY_OLIMP = "olimp";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB4";

    static final String DATABASE_TABLE1 = "sportas";
    static final String DATABASE_TABLE2 = "sport";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE_SPORTAS =
            "create table sportas (_id integer primary key autoincrement, "
                    + "ime text not null, prezime text not null);";

    static final String DATABASE_CREATE_SPORT =
            "create table sport (_id integer primary key autoincrement, "
                    + "naziv text not null, vrsta text not null,olimp text not null);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE_SPORTAS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                db.execSQL(DATABASE_CREATE_SPORT);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading db from" + oldVersion + "to"
                    + newVersion );
            db.execSQL("DROP TABLE IF EXISTS sportas");
            db.execSQL("DROP TABLE IF EXISTS sport");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //za sport

    //---insert a sport into the database---
    public long insertSport(String naziv, String vrsta,String olimp)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAZIV,naziv);
        initialValues.put(KEY_VRSTA,vrsta);
        initialValues.put(KEY_OLIMP, olimp);
        return db.insert(DATABASE_TABLE2, null, initialValues);
    }

    //---deletes a particular sport---
    public boolean deleteSport(long rowId)
    {
        return db.delete(DATABASE_TABLE2, KEY_ROWID_SPORT + "=" + rowId, null) > 0;
    }

    //---retrieves all the sport---
    public Cursor getAllSport()
    {
        Log.w("myapp","cursor");
        return db.query(DATABASE_TABLE2, new String[] {KEY_ROWID_SPORT, KEY_NAZIV,
                KEY_VRSTA,KEY_OLIMP}, null, null, null, null, null);
    }

    //---retrieves a particular sport---
    public Cursor getSport(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE2, new String[] {KEY_ROWID_SPORT,
                                KEY_NAZIV,
                                KEY_VRSTA,KEY_OLIMP}, KEY_ROWID_SPORT + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a sport---
    public boolean updateSport(long rowId, String naziv, String vrsta,String olimp)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAZIV, naziv);
        args.put(KEY_VRSTA, vrsta);
        args.put(KEY_OLIMP, olimp);
        return db.update(DATABASE_TABLE2, args, KEY_ROWID_SPORT + "=" + rowId, null) > 0;
    }

    //za sportasa
    //---insert a sportas into the database---
    public long insertSportas(String ime, String prezime)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, ime);
        initialValues.put(KEY_SURNAME, prezime);
        return db.insert(DATABASE_TABLE1, null, initialValues);
    }

    //---deletes a particular sportas---
    public boolean deleteSportas(long rowId)
    {
        return db.delete(DATABASE_TABLE1, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the sportas---
    public Cursor getAllSportas()
    {
        Log.w("myapp","cursor");
        return db.query(DATABASE_TABLE1, new String[] {KEY_ROWID, KEY_NAME,
                KEY_SURNAME}, null, null, null, null, null);
    }

    //---retrieves a particular sportas---
    public Cursor getSportas(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE1, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_SURNAME}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a sportas---
    public boolean updateSportas(long rowId, String ime, String prezime)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, ime);
        args.put(KEY_SURNAME, prezime);
        return db.update(DATABASE_TABLE1, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
