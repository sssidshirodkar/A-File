package com.collge.afile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity {

    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    /**
     * false : gridLayoutManager
     * true : linearLayoutManager
     */
    public boolean viewManagerType = false;

    public boolean isViewManagerType() {
        return viewManagerType;
    }

    // Stores names of traversed directories
    ArrayList<String> str = new ArrayList<String>();

    // Check if the first level of the directory structure is the one showing
    private Boolean firstLevel = true;
    boolean doubleBackToExitPressedOnce = false;

    private static final String TAG = "F_PATH";

    public List<Item> fileList;
    private File path = new File(Environment.getExternalStorageDirectory() + "");
    RecyclerView rView;
    FileAdapter rcAdapter;
    String title = "root";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
//        topToolBar.setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(title);
        topToolBar.setLogoDescription(getResources().getString(R.string.logo_desc));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fileList = new ArrayList<>();
        loadFileList();
        gridLayoutManager = new GridLayoutManager(FileActivity.this, 3);
        linearLayoutManager = new LinearLayoutManager(FileActivity.this);

        rView = (RecyclerView) findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);

        rcAdapter = new FileAdapter(FileActivity.this, fileList);
        rView.setAdapter(rcAdapter);
        rView.addOnItemTouchListener(new RecyclerItemClickListener(this, rView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // do whatever
                onClick(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // do whatever
            }
        }));
    }

    private void updateList() {
        rcAdapter.notifyDataSetChanged();
        title = path.getAbsolutePath().substring(path.toString().lastIndexOf("/") + 1);
        if (title.equalsIgnoreCase("0")) title = "root";
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (id == R.id.action_refresh) {

            viewManagerType = !viewManagerType;
            supportInvalidateOptionsMenu();
            rView.setLayoutManager(viewManagerType ? linearLayoutManager : gridLayoutManager);
            rView.setAdapter(rcAdapter);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                this.invalidateOptionsMenu();
            }
        }
//        if(id == R.id.action_new){
//            Toast.makeText(MainActivity.this, "Create Text", Toast.LENGTH_LONG).show();
//        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_refresh).setIcon(getResources().getDrawable(viewManagerType ? R.drawable.view_grid : R.drawable.view_list));
        return super.onPrepareOptionsMenu(menu);
    }

    private List<Item> loadFileList() {
        try {
            path.mkdirs();
        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card ");
        }

        // Checks whether path exists
        if (path.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    // Filters based on whether the file is hidden or not
                    return (sel.isFile() || sel.isDirectory())
                            && !sel.isHidden();

                }
            };

            String[] fList = path.list(filter);
//            fileList = new ArrayList<>(fList.length);
            for (int i = 0; i < fList.length; i++) {
                fileList.add(i, new Item(fList[i], R.mipmap.file));
                // Convert into file path
                File sel = new File(path, fList[i]);

                // Set drawables
                if (sel.isDirectory()) {
                    fileList.get(i).icon = R.mipmap.folder_empty;
                    Log.d("DIRECTORY", fileList.get(i).file);
                } else {
                    Log.d("FILE", fileList.get(i).file);
                }
            }

//            if (!firstLevel) {
//                /**
//                 * this indicates its not the first level so we show up button
//                 */
//                fileList.add(0, new Item("Up", R.drawable.directory_up));
//            }
        } else {
            Log.e(TAG, "path does not exist");
        }
        return fileList;
    }

    public void onClick(int which) {
        String chosenFile = fileList.get(which).file;
        File sel = new File(path + "/" + chosenFile);
        if (sel.isDirectory()) {
            firstLevel = false;

            // Adds chosen directory to list
            str.add(chosenFile);
            fileList.clear();
            path = new File(sel + "");

            loadFileList();
            updateList();
            Log.d(TAG, path.getAbsolutePath());
        }
        // File picked
        else {
            // Perform action with file picked

            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            String mimeType = myMime.getMimeTypeFromExtension(fileExt(sel.getAbsolutePath()));
            newIntent.setDataAndType(Uri.fromFile(sel), mimeType);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void gotoPrevDirectory() {

        // present directory removed from list
        String s = str.remove(str.size() - 1);

        // path modified to exclude present directory
        path = new File(path.toString().substring(0,
                path.toString().lastIndexOf(s)));
        fileList.clear();

        // if there are no more directories in the list, then
        // its the first level
        if (str.isEmpty()) {
            firstLevel = true;
        }
        loadFileList();
        updateList();
        Log.d(TAG, path.getAbsolutePath());
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    @Override
    public void onBackPressed() {
        /**
         * if its not the first level then 0th position is up button
         */
        if (!firstLevel)
            gotoPrevDirectory();
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            ToastUtil.showShortToast(this, "Please click back again to exit");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }
}
