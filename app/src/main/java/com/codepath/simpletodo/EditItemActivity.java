package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private EditText etEditItem;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditItem = (EditText)findViewById(R.id.etEditItem);
        position = getIntent().getIntExtra("position", -1);
        String itemText = getIntent().getStringExtra("itemText");
        etEditItem.setText(itemText);

        etEditItem.requestFocus();
    }

    public void onSave(View view) {
        Intent editedItemIntent = new Intent();
        editedItemIntent.putExtra("position", position);
        editedItemIntent.putExtra("itemText",
                                  etEditItem.getText().toString());
        setResult(RESULT_OK, editedItemIntent);
        finish();
    }
}
