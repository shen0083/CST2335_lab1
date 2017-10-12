package com.example.cynthiasheng.cst2335_lab1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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


public class ChatWindowActivity extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    ListView contentList;
    EditText userEdit;
    Button userSend;
    ArrayList<String> userList = new ArrayList<String>();
    String input;
    ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        contentList = (ListView) findViewById(R.id.chatView);
        userEdit = (EditText) findViewById(R.id.userEdit);
        userSend = (Button) findViewById(R.id.sendButton);

        //in this case, “this” is the ChatWindow, which is-A Context object
        messageAdapter=new ChatAdapter(this);
        contentList.setAdapter(messageAdapter);

        ChatDatabaseHelper aHelperObject = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = aHelperObject.getWritableDatabase();

        Cursor curs= db.rawQuery("select * from MyTable",null);

        int messageIndex = curs.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        if(curs.moveToFirst()) {

            while (!curs.isAfterLast()){
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + curs.getString(messageIndex));
                userList.add(curs.getString(messageIndex));
                curs.moveToNext();
            }
        }

        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + curs.getColumnCount());

        for(int i = 0; i <curs.getColumnCount();i++){
            System.out.println(curs.getColumnName(i));
        }


        userSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                input = userEdit.getText().toString();
                userList.add(input);
                messageAdapter.notifyDataSetChanged();
                //this restarts the process of getCount()/ getview()
                userEdit.setText("");

                ContentValues newData = new ContentValues();
                newData.put("MESSAGE", input);
                db.insert("MyTable", "" , newData);
            }
        });
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount() {
            return userList.size();
        }

        public String getItem(int position) {
            return userList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindowActivity.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}

