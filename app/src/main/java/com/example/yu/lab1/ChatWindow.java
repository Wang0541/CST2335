package com.example.yu.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.id.list;

public class ChatWindow extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "Query";
    public static final String SQL_MESSAGE="SQL MESSAGE:";
    public static final String COLUMN_COUNT = "Cursor\'s  column count= ";

    SQLiteDatabase db;
    Cursor cursor;

    ListView listView;
    EditText editText;
    Button sendButton;
    final ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(this);
        //open database
        db = chatDatabaseHelper.getWritableDatabase();
        String[] allColumns = { ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE };
        cursor = db.query(chatDatabaseHelper.TABLE_NAME,allColumns, null, null, null, null, null);

        while(!cursor.isAfterLast() )
            Log.i(ACTIVITY_NAME, SQL_MESSAGE + cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );

        Log.i(ACTIVITY_NAME,  COLUMN_COUNT+ cursor.getColumnCount() );
        for(int i = 0; i < cursor.getCount(); i++){
            System.out.println("Column "+ i +" name is "+cursor.getColumnName(i));
            cursor.moveToNext();
        }


        listView = (ListView)findViewById(R.id.list_item);
        editText = (EditText)findViewById(R.id.edit_message);
        sendButton = (Button)findViewById(R.id.send_button);

        //inner class
        //ArrayAdapter:A concrete BaseAdapter that is backed by an array of arbitrary objects. By default this class expects that the provided resource id references a single TextView
       class ChatAdapter extends ArrayAdapter<String> {
           // ctx represents the current context, 0 is resource ID
            public ChatAdapter(Context ctx) {
                super(ctx, 0);
            }
            public int getCount(){
                return list.size();
            }
            public String getItem(int position){
                return list.get(position);
            }

            public View getView(int position, View convertView, ViewGroup parent){
                LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
                View result = null ;
                if(position%2 == 0)
                    // you can make different layout under res/layout
                    result = inflater.inflate(R.layout.chat_row_incoming, null);
                else
                    result = inflater.inflate(R.layout.chat_row_outgoing, null);

                TextView message = (TextView)result.findViewById(R.id.message_text);
                message.setText(   getItem(position)  ); // get the string at position
                return result;
            }
        }

        //in this case, “this” is the ChatWindow, which is-A Context object ChatAdapter
        final ChatAdapter messageAdapter =new ChatAdapter( this );

        //sets data behind this listView???
        listView.setAdapter (messageAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.add(editText.getText().toString());
                ContentValues cValues = new ContentValues();
                cValues.put(chatDatabaseHelper.KEY_MESSAGE,editText.getText().toString());
                //this restarts the process of getCount()/ getView()
                messageAdapter.notifyDataSetChanged();
                editText.setText("");

            }
        });


    }
    @Override
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
        cursor.close();
        db.close();
    }




}
