package cf.kashif.booksapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cf.kashif.booksapp.AppSingleton;
import cf.kashif.booksapp.Constants;
import cf.kashif.booksapp.adapters.DataAdapter;
import cf.kashif.booksapp.R;
import cf.kashif.booksapp.pojo.Book;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MyLog " + this.getClass().getSimpleName();

    ProgressBar progressBar;
    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    LinearLayout infoView;
    RelativeLayout rootView;
    List<Book> dataList;
    String URL = Constants.URL;

    private SearchView searchView;
    private MenuItem searchMenuItem;
    SearchView.OnQueryTextListener listener;
    private int startIndex;
    private LinearLayoutManager layoutManager;
    private boolean isLoading, isLastPage;
    private String lastKeywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //all things of this activity initialized here
    void init() {

        startIndex = 0;
        lastKeywords = "";

        //views
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        //view shown on launch
        infoView = (LinearLayout) findViewById(R.id.infoView);

        rootView = (RelativeLayout) findViewById(R.id.relativeLayout);

        //search listener from action bar
        listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                try {

                    Log.d(TAG, "query =" +query);
                    //replaces spaces
                    lastKeywords = query.replaceAll("\\s", "+");
                    showLoading();
                    requestData(lastKeywords, true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };

    }

    //pagination
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= Constants.PAGE_SIZE) {
                    startIndex += 10;
                    requestData( lastKeywords, false );
                }
            }
        }
    };


    void requestData(String keywords, boolean clear) {

        String URL = Constants.URL +"?q="+ keywords +"&startIndex="+ startIndex +"&maxResults="+ Constants.MAX_RESULTS +"&key="+ Constants.GOOGLE_API_KEY ;

        //clear data
        if ( clear && dataList!=null && !dataList.isEmpty())
            dataList.clear();

        //request data from api using volley library
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "onResponse= " + response.toString());
                new parseTask(response).execute();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                Log.e(TAG, "onErrorResponse= " + error.getMessage());
                handleNetworkError(error);
            }
        }
        );

        AppSingleton.getInstance().addToRequestQueue(jsonObjectRequest);

    }//getData

    private void handleNetworkError(VolleyError error) {

        if( error!=null && error.getMessage()!=null ) {
            if (error.getMessage().contains("UnknownHostException")) {
                Snackbar.make(rootView, "No Network", Snackbar.LENGTH_LONG).show();
                findViewById(R.id.tvNoNetwork).setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(rootView, "Error - " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }else{
            Snackbar.make(rootView, "Something went wrong.", Snackbar.LENGTH_LONG).show();
        }

    }


    //background task for parsing data
    class parseTask extends AsyncTask<Void, Void, Void> {

        JSONObject jsonObject;

        public parseTask(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... args) {

            try {

                if (jsonObject.has("items")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    if (jsonArray.length() > 0) {

                        //gson library for faster and easier serialization
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Book>>() {
                        }.getType();
                        List<Book> dataListNew = gson.fromJson(jsonArray.toString(), listType);

                        if ( dataListNew!=null && !dataListNew.isEmpty()) {

                            if( dataList == null ){
                                dataList = new ArrayList<>();
                            }

                            Log.d("serData", "dataList.size=" + dataListNew.size());
                            Snackbar.make(rootView, "Loading books", Snackbar.LENGTH_SHORT).show();
                            dataList.addAll( dataListNew );

                        }
                    } else {
                        Snackbar.make(rootView, "Found no results", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(rootView, "Found no results", Snackbar.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar.make(rootView, "Error - " + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!dataList.isEmpty()) {
                if( dataAdapter == null ) {
                    dataAdapter = new DataAdapter(MainActivity.this, dataList);
                    recyclerView.setAdapter(dataAdapter);
                }else{
                    dataAdapter.notifyDataSetChanged();
                }

            }
            hideLoading();
        }

    }//parseTask


    void showLoading() {
        infoView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideLoading() {
        infoView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(listener);
        return true;
    }


}

