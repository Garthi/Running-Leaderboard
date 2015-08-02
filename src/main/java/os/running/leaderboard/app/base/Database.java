package os.running.leaderboard.app.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Database extends DatabaseHelper
{
    // database tables
    final private String TABLE_ACCOUNT = "account";

    final private String ACCOUNT_FIELD_KEY = "key";
    final private String ACCOUNT_FIELD_DATA = "data";
    
    private static Database self;
    public static Database getInstance(Context context)
    {
        if (self == null) {
            self = new Database(context);
        }
        
        return self;
    }
    
    public Database(Context context)
    {
        super(context);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ACCOUNT + "" +
                   "(" + ACCOUNT_FIELD_KEY + " varchar(20) primary key, " +
                    ACCOUNT_FIELD_DATA + " varchar(150));");
        } catch (Exception e) {
            Log.e("app", "Database.onCreate account", e.fillInStackTrace());
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // no updates
    }
    
    public void addAccounData(String key, String data)
    {
        try {
            this.open();
        } catch (Exception e) {
            return;
        }
        
        DB.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            values.put(ACCOUNT_FIELD_KEY, key);
            values.put(ACCOUNT_FIELD_DATA, data);

            this.DB.replace(TABLE_ACCOUNT, null, values);
            
            DB.setTransactionSuccessful();
            
        } catch (Exception e) {
            Log.e("app", "error on SQL insert: " + e.getMessage());
        } finally {
            DB.endTransaction();
        }
    }
    
    public void addAccounData(List<Parameter> data)
    {
        try {
            this.open();
        } catch (Exception e) {
            return;
        }
        
        DB.beginTransaction();
        try {

            for (Parameter parameter : data) {
                ContentValues values = new ContentValues();

                values.put(ACCOUNT_FIELD_KEY, parameter.getKey());
                values.put(ACCOUNT_FIELD_DATA, parameter.getValue());

                this.DB.replace(TABLE_ACCOUNT, null, values);
            }
            
            DB.setTransactionSuccessful();
            
        } catch (Exception e) {
            Log.e("app", "error on SQL insert: " + e.getMessage());
        } finally {
            DB.endTransaction();
        }
    }
    
    public String getAccountData(String key)
    {
        try {
            this.open();
        } catch (Exception e) {
            return null;
        }
        
        Cursor cursor = DB.query(
                TABLE_ACCOUNT,
                new String[]{ACCOUNT_FIELD_DATA},
                ACCOUNT_FIELD_KEY + "=?",
                new String[]{String.valueOf(key)},
                null,
                null,
                null,
                null
        );
        
        if (cursor == null) {
            return null;
        }
        
        cursor.moveToFirst();
        String data = "";
        try {
            data = cursor.getString(0);
        } catch (CursorIndexOutOfBoundsException e) {
            // no error
        }
        cursor.close();
        
        if (data.isEmpty()) {
            return null;
        }
        
        return data;
    }
}
