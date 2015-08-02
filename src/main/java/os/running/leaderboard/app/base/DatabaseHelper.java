package os.running.leaderboard.app.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public abstract class DatabaseHelper extends SQLiteOpenHelper
{
    // DB constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "leaderboard";
    
    // DB class
    protected SQLiteDatabase DB = null;
    
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public synchronized void open() throws SQLException
    {
        // create DB class
        if (DB == null || !DB.isOpen()) {
            DB = getWritableDatabase();
        }
    }
    
    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        
        if (db.isDbLockedByOtherThreads()) {
            Log.d("app", "DB is locked by other thread");
        }
    }
    
    @Override
    public synchronized void close()
    {
        super.close();
        this.DB = null;
    }
    
    public abstract void onCreate(SQLiteDatabase db);
    
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
