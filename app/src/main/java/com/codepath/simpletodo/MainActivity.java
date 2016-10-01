package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lvItems;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;

    private EditText etNewItem;

    private static final String TODO_FILE = "todo.txt";

    private static final int REQUEST_EDIT_ITEM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this,
                                          android.R.layout.simple_list_item_1,
                                          items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListeners();

        etNewItem = (EditText)findViewById(R.id.etNewItem);
    }

    public void onAddItem(View item) {
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListeners() {
        AdapterView.OnItemLongClickListener listViewItemLongClickListener
            = new AdapterView.OnItemLongClickListener() {
                  @Override
                  public boolean onItemLongClick(AdapterView<?> adapter,
                                                 View item, int position,
                                                 long id) {
                      items.remove(position);
                      itemsAdapter.notifyDataSetChanged();
                      writeItems();
                      return true;
                  }
              };
        AdapterView.OnItemClickListener listViewItemClickListener
            = new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> adapter,
                                          View item, int position, long id) {
                      Intent editItemIntent
                          = new Intent(MainActivity.this,
                                       EditItemActivity.class);
                      editItemIntent.putExtra("position", position);
                      editItemIntent.putExtra("itemText",
                                              itemsAdapter.getItem(position));
                      startActivityForResult(editItemIntent,
                              REQUEST_EDIT_ITEM);
                  }
              };

        lvItems.setOnItemLongClickListener(listViewItemLongClickListener);
        lvItems.setOnItemClickListener(listViewItemClickListener);
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE);
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT_ITEM) {
            int position = data.getIntExtra("position", -1);
            String itemText = data.getStringExtra("itemText");
            items.set(position, itemText);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
