package ir.roojano.sematec.session6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class FavoriteMovies extends AppCompatActivity {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setVisibility(View.GONE);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setVisibility(View.GONE);
        TextView txtResultsCount = findViewById(R.id.txtResultsCount);
        txtResultsCount.setVisibility(View.GONE);
        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        EditText edtSearch = findViewById(R.id.edtSearch);
        edtSearch.setVisibility(View.GONE);
        LinearLayout line = findViewById(R.id.line);
        line.setVisibility(View.GONE);

        getSupportActionBar().setTitle("Offline Search");

        drawer =findViewById(R.id.drawer);

        Button btnMainActivity = findViewById(R.id.btnMainActivity);
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnFavActivity = findViewById(R.id.btnFavActivity);
        btnFavActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.RIGHT);
            }
        });

        SQLiteHelper helper = new SQLiteHelper(this, "IMDB_Movies", null, 1);
        List<Search> moviesList = helper.getMovies();
        helper.close();

        RecyclerView recycler = findViewById(R.id.recycler);
        RecyclerAdapter adapter = new RecyclerAdapter(moviesList, "FavoriteMovies");
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.drawerMenu)
        {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
            } else drawer.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }
}