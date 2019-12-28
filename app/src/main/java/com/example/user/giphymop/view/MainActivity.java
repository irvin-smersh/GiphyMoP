package com.example.user.giphymop.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.user.giphymop.interfaces.AdapterCommunicator;
import com.example.user.giphymop.network.RetrofitClientUpload;
import com.example.user.giphymop.Model.Data;
import com.example.user.giphymop.R;
import com.example.user.giphymop.network.GiphyApi;
import com.example.user.giphymop.viewModels.TrendingViewModel;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FragmentTrending.OnFragmentInteractionListener, FragmentPreview.OnFragmentInteractionListener, FragmentSearch.OnFragmentInteractionListener {

    Toolbar mToolbar;
    MaterialSearchView mSearchView;
    FloatingActionButton btnChooseVideo;
    GiphyApi fileService;
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChooseVideo = findViewById(R.id.choose_video);
        mToolbar = findViewById(R.id.toolbar);
        mSearchView = findViewById(R.id.searchView);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Fragment fragment_trending = new FragmentTrending();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment_trending, "Trending");
        fragmentTransaction.commit();

        fileService = RetrofitClientUpload.getFileService();
        btnChooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermissions();
                }
            }
        });

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("Irvin", "onQueryTextChange");

                Fragment fragment_search = new FragmentSearch();
                Bundle args = new Bundle();
                args.putString("search_text", newText);
                fragment_search.setArguments(args);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment_search, "Search");
                fragmentTransaction.commit();

                return true;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.main_menu_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    public void openPreviewFragment(String url){

        Log.e("Irvin", "ajde ga otvori");
        Log.i("Irvin", "sa url-om = " + url);
        Fragment fragmentPreview = new FragmentPreview();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragmentPreview.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragmentPreview, "Preview");
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(this, "Unable to choose video!", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri videoUri = data.getData();
            videoPath = getRealPathFromUri(videoUri);
            uploadFile(videoPath);
        }
        else{
            Toast.makeText(this, "Request code: " + requestCode, Toast.LENGTH_LONG).show();
        }
    }

    private String getRealPathFromUri(Uri uri){
        String[] projection = {MediaStore.Video.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_idx = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
    }

    private void uploadFile(String filePath){
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Call<ResponseBody> call = fileService.uploadVideo(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Video uploaded successfully", Toast.LENGTH_LONG).show();
                    Log.i("UPLOAD", "Video uploaded successfully");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("UPLOAD", "ERROR: " + t.getMessage());
            }
        });
    }

    private void checkPermissions(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    },
                    1052);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, 0);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1052: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted.
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("video/*");
                    startActivityForResult(intent, 0);

                } else {
                    // Permission denied - Show a message to inform the user that this app only works
                    // with these permissions granted
                    Toast.makeText(this, "You need Read Internal Storage premission", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }
}
