package ir.roojano.sematec.session6;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    String table_name = "favMovies";

    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_table_query = "CREATE TABLE " + table_name + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "type TEXT," +
                "year TEXT," +
                "poster TEXT," +
                "imdbID TEXT" +
                ")" +
                "";
        db.execSQL(create_table_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertMovie(String title, String type, String year, String poster, String imdbID) {

        String insert_query = "INSERT INTO " + table_name + "(" +
                "title," +
                "type," +
                "year," +
                "poster," +
                "imdbID" +
                ") " +
                "VALUES(" +
                "'" + title + "'" + "," +
                "'" + type + "'" + "," +
                "'" + year + "'" + "," +
                "'" + poster + "'" + "," +
                "'" + imdbID + "'" +
                ")" +
                "";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insert_query);
        db.close();
    }

    public void deleteMovie (String imdbID) {
        String delete_query = "DELETE FROM " + table_name + " WHERE imdbID=" + "'" + imdbID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(delete_query);
        db.close();
    }

    public List<Search> getMovies () {

        List<Search> result = new ArrayList<>();
        String get_movie_query = "SELECT * FROM " + table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(get_movie_query, null);

        int i = 0;
        while (cursor.moveToNext())
        {
            Search movie = new Search();
            movie.setTitle(cursor.getString(1));
            movie.setType(cursor.getString(2));
            movie.setYear(cursor.getString(3));
            movie.setPoster(cursor.getString(4));
            movie.setImdbID(cursor.getString(5));
            result.add(i, movie);
            i++;
        }
        db.close();

        return result;
    }
}