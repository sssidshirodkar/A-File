package com.collge.afile.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
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

import com.collge.afile.R;
import com.collge.afile.presenter.FilePresenter;
import com.collge.afile.util.FileType;
import com.collge.afile.util.StorageUtil;
import com.collge.afile.util.ToastUtil;
import com.collge.afile.pojo.Item;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity implements FilePresenter.View{

    Toolbar topToolBar;
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

    private final String TAG = "F_PATH";

    public List<Item> fileList;

    private File path = new File(Environment.getExternalStorageDirectory() + "");
    public File getPath() {
        return path;
    }

    RecyclerView rView;
    FileAdapter rcAdapter;
    String title = "root";
    /**
     * used to identify the view is ready for update
     */
    boolean isReady = false;
    FilePresenter presenter;

    /**
     * indicates whether files are in selection mode.
     */
    boolean selectionMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        isReady = true;

        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
//        topToolBar.setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(title);
        topToolBar.setLogoDescription(getResources().getString(R.string.logo_desc));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fileList = new ArrayList<>();

        presenter = new FilePresenter();
        presenter.setView(this);
        presenter.setLevel(firstLevel);
        presenter.loadFileList(path, fileList);

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
                selectionMode = true;
                topToolBar.setBackgroundColor(FileActivity.this.getResources().getColor(R.color.dark_gray));
                toggleSelection(position);
            }
        }));
    }

    @Override
    protected void onResume() {
        isReady = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isReady = false;
        super.onPause();
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

    public void onClick(int position) {
        /**
         * if in selection mode, we do not process onClick()
         */
        if(selectionMode){
            toggleSelection(position);
        }else {
            String chosenFile = fileList.get(position).file;
            File sel = firstLevel ? new File(chosenFile) : new File(path + "/" + chosenFile);
            if (sel.isDirectory()) {
                firstLevel = false;

                // Adds chosen directory to list
                str.add(chosenFile);
                fileList.clear();
                path = new File(sel + "");

                presenter.setLevel(firstLevel);
                presenter.loadFileList(path, fileList);
                updateList();
            }
            // File picked
            else {
                // Perform action with file picked

                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(fileExt(sel.getAbsolutePath()));
                /**
                 * following line commented since If your targetSdkVersion is 24 or higher, we have to use FileProvider
                 * class to give access to the particular file or folder to make them accessible for other apps.
                 * and add extra flag "FLAG_GRANT_READ_URI_PERMISSION".
                 */
//            newIntent.setDataAndType(Uri.fromFile(sel), mimeType);
                Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", sel.getAbsoluteFile());
                newIntent.setDataAndType(photoURI, mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if(newIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(newIntent);
                else
                    Toast.makeText(this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void toggleSelection(int position){
        Item file = fileList.get(position);
        file.setSelected(!file.isSelected());
        rcAdapter.notifyDataSetChanged();
    }

    private void gotoPrevDirectory() {

        // present directory removed from list
        String s = str.remove(str.size() - 1);

        // path modified to exclude present directory
        if(!s.contains(path.getAbsolutePath())) { // this check is done to avid conflict when 'str' array list is made empty and nothing available to make new file Path
            path = new File(path.toString().substring(0,
                    path.toString().lastIndexOf(s)));
        }
        fileList.clear();

        // if there are no more directories in the list, then
        // its the first level
        if (str.isEmpty()) {
            firstLevel = true;
        }
        presenter.setLevel(firstLevel);
        presenter.loadFileList(path, fileList);
        updateList();
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

    public void clearList(){
        for (Item item: fileList) {
            item.setSelected(false);
        }
        rcAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        /**
         * on back press clear selection mode first if is in
         */
        if(selectionMode){
            selectionMode = false;
            topToolBar.setBackgroundColor(FileActivity.this.getResources().getColor(R.color.color_primary));
            clearList();
        }else {
            /**
             * if its not the first level then 0th position is up button
             */
            if (!firstLevel)
                gotoPrevDirectory();
            else {
                if (doubleBackToExitPressedOnce) {
                    isReady = false;
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

    @Override
    public void update(List<?> list, File currentPath) {
        fileList = (List<Item>)list;
        path = currentPath;
        updateList();
    }

    @Override
    public boolean isReady() {
        return isReady;
    }
}
