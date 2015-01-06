package com.example.khalid.hisnulmuslim;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import adapters.DuaGroupAdapter;
import classes.Dua;
import database.ExternalDbOpenHelper;
import database.mySqliteDatabase;

public class DuaListActivity extends ActionBarActivity {
    private SQLiteDatabase database;
    public ArrayList ArrayListDuas = new ArrayList<Dua>();
    private DuaGroupAdapter mAdapter;

    private mySqliteDatabase myDB = new mySqliteDatabase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dua_list);

        // TextView txtQuery = (TextView) findViewById(R.id.txtQuery);

        ExternalDbOpenHelper dbOpenHelper;
        dbOpenHelper = new ExternalDbOpenHelper(this, myDB.DB_NAME);
        database = dbOpenHelper.openDataBase();

        fromDBtoArrayList(ArrayListDuas);
        fromArrayListToListView();
    }

    public void fromDBtoArrayList(ArrayList<Dua> ArrayListDuas) {
        Cursor duaGroupCursor;
        duaGroupCursor = database.query(myDB.TABLE_DUA_GROUP,
                new String[]{myDB.TABLE_DUA_GROUP_ID,
                        myDB.TABLE_DUA_GROUP_TITLE},
                null,
                null,
                null,
                null,
                myDB.TABLE_DUA_GROUP_ID);

        duaGroupCursor.moveToFirst();

        if (!duaGroupCursor.isAfterLast()) {
            do {
                String dua_group_id = duaGroupCursor.getString(0);
                String dua_group_title = duaGroupCursor.getString(1);
                ArrayListDuas.add(new Dua(Integer.parseInt(dua_group_id), dua_group_title));
            } while (duaGroupCursor.moveToNext());
        }
        duaGroupCursor.close();
    }

    public void fromArrayListToListView() {
        final ListView listView;
        listView = (ListView) findViewById(R.id.duaListView);

        // Search Related (Filter)
        listView.setTextFilterEnabled(true);

        this.mAdapter = new DuaGroupAdapter(this,
                ArrayListDuas);

        listView.setAdapter(this.mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent;
                intent = new Intent(getBaseContext(),
                        DuaDetailActivity.class);

                String dua_id = String.valueOf(position + 1);
                String dua_title = ((Dua) ArrayListDuas.get(position)).getTitle();

                intent.putExtra("dua_id", dua_id);
                intent.putExtra("dua_title", dua_title);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_dualist, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.action_bookmarks) {
            Intent intent = new Intent(this, BookmarksActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            this.startActivity(intent);
        } /*else if (id == R.id.search) {
            onSearchRequested();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}