package ir.roojano.sematec.session6;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;

    int pageCount;
    int currentPageNumber;
    String searchKey;
    EditText edtSearch;
    Button btnNext;
    Button btnBack;
    TextView txtResultsCount;
    RecyclerView recycler;
    RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Find movie");

        drawer = findViewById(R.id.drawer);

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        txtResultsCount = findViewById(R.id.txtResultsCount);
        recycler = findViewById(R.id.recycler);
        Button btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);

        edtSearch.requestFocus();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKey = edtSearch.getText().toString().trim();
                if (searchKey.matches(""))
                    Toast.makeText(MainActivity.this, "Name please!", Toast.LENGTH_SHORT).show();
                else {
                    currentPageNumber = 1;
                    recyclerLoader(searchKey, currentPageNumber, "Search");
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPageNumber++;
                recyclerLoader(searchKey, currentPageNumber, "Next");
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPageNumber--;
                recyclerLoader(searchKey, currentPageNumber, "Back");
            }
        });

        Button btnFavActivity= findViewById(R.id.btnFavActivity);
        btnFavActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteMovies.class);
                startActivity(intent);
            }
        });

        Button btnMainActivity = findViewById(R.id.btnMainActivity);
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.RIGHT);
            }
        });
    }

    public void recyclerLoader (String searchKey, final int pageNumber, final String buttonName) {

        final String url = "https://www.omdbapi.com/?s="+searchKey+"&page="+pageNumber+"&apikey=b2dc48cc";

        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    Gson gson = new Gson();
                    Searchimdb searchResult =gson.fromJson(response.toString(), Searchimdb.class);

                    if (searchResult.getResponse().matches("True"))
                    {
                        screenSetting(Integer.parseInt(searchResult.getTotalResults()), pageNumber, buttonName);

                        List<Search> moviesList = searchResult.getSearch();
                        adapter = new RecyclerAdapter(moviesList, "MainActivity");
                        recycler.setAdapter(adapter);
                        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
                    }
                    else {
                        screenSetting(0, 0, "Notfound");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.d("MYTAG", "an exception occurred! WE ARE IN CATCH BLOCK >> in recyclerLoader() Method");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void screenSetting (int totalResultsCount, int pageNumber, String buttonName){

        int p=pageNumber*10;

        switch (buttonName) {
            case "Search":
                btnBack.setVisibility(View.GONE);
                if (totalResultsCount > 10)
                {
                    txtResultsCount.setVisibility(View.VISIBLE);
                    txtResultsCount.setText(totalResultsCount+" Results ||"+" Showing "+(p-9)+" to "+p+" || Page "+pageNumber);
                    btnNext.setVisibility(View.VISIBLE);

                    pageCount = totalResultsCount/10;
                    if ((totalResultsCount%10)>0) {
                        pageCount++;
                    }
                }
                else {
                    btnNext.setVisibility(View.GONE);
                    txtResultsCount.setVisibility(View.VISIBLE);
                    txtResultsCount.setText(totalResultsCount+" Results ||"+" Showing "+(p-9)+" to "+totalResultsCount);
                    pageCount = 1;
                }
                break;

            case "Next":
                btnBack.setVisibility(View.VISIBLE);
                if (pageNumber==pageCount) {
                    txtResultsCount.setText(totalResultsCount+" Results ||"+" Showing "+(p-9)+" to "+totalResultsCount+" || Page "+pageNumber);
                    btnNext.setVisibility(View.GONE);
                }
                else
                    txtResultsCount.setText(totalResultsCount+" Results ||"+" Showing "+(p-9)+" to "+p+" || Page "+pageNumber);
                break;

            case "Back":
                if (pageNumber==1)
                    btnBack.setVisibility(View.GONE);
                else if (pageNumber == pageCount-1)
                    btnNext.setVisibility(View.VISIBLE);

                txtResultsCount.setText(totalResultsCount+" Results ||"+" Showing "+(p-9)+" to "+p+" || Page "+pageNumber);
                break;

            default:
                Toast.makeText(MainActivity.this, "Ops Not Found!", Toast.LENGTH_LONG).show();

                if (recycler.getChildCount()!=0)
                    adapter.clear();

                btnBack.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                txtResultsCount.setVisibility(View.GONE);
        }
    }
}