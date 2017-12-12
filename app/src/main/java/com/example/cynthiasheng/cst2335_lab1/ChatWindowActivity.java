package com.example.cynthiasheng.cst2335_lab1;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.cynthiasheng.cst2335_lab1.ChatDatabaseHelper.name;

public class ChatWindowActivity extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    ListView contentList;
    EditText userEdit;
    Button userSend;
    ArrayList<String> userList = new ArrayList<String>();
    String input;
    ChatAdapter messageAdapter;
    SQLiteDatabase db;
    Cursor curs;
    private Boolean isLandscape;
    private FrameLayout landscapeFrameLayout;
    private int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        contentList = (ListView) findViewById(R.id.chatView);
        userEdit = (EditText) findViewById(R.id.userEdit);
        userSend = (Button) findViewById(R.id.sendButton);


        landscapeFrameLayout = (FrameLayout) findViewById(R.id.landscapeFrameLayout);

        if(landscapeFrameLayout == null){
            isLandscape = false;
            Log.i(ACTIVITY_NAME, "The phone is on portrait layout.");

        }
        else {
            isLandscape = true;
            Log.i(ACTIVITY_NAME, "The phone is on landscape layout.");
        }

        messageAdapter=new ChatAdapter(this);
        contentList.setAdapter(messageAdapter);


        ChatDatabaseHelper aHelperObject = new ChatDatabaseHelper(this);
         db = aHelperObject.getWritableDatabase();

        curs= db.rawQuery("select * from "+ name,null);

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
                newData.put(ChatDatabaseHelper.KEY_MESSAGE, input);
                db.insert(name, "" , newData);
                refreshActivity();
            }
        });
        final Intent intent = new Intent(this, MessageDetailActivity.class);

               contentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                   @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String message = messageAdapter.getItem(position);
                long idInDb =  messageAdapter.getItemId(position);

                Bundle bundle = new Bundle();
                bundle.putLong("id",idInDb);
                bundle.putString("message", message);
                bundle.putBoolean("isLandscape", isLandscape);

                if(isLandscape == true){
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setArguments(bundle);
//                    FragmentManager fragmentManager =getFragmentManager();
//                    //remove previous fragment
//                    if (fragmentManager.getBackStackEntryCount() > 0) {
//                        FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
//                        fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    }

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.landscapeFrameLayout,messageFragment);
                   fragmentTransaction.addToBackStack(null);
                   fragmentTransaction.commit();
                    // fragmentTransaction.add(R.id.landscapeFrameLayout, messageFragment).addToBackStack(null).commit();
                }
                else{
                    intent.putExtra("bundle", bundle);
                    startActivityForResult(intent, requestCode);
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && data != null) {
            Long id = data.getLongExtra("id", -1);
            db.delete(name, ChatDatabaseHelper.KEY_ID + "=" + id, null);
            refreshActivity();
        }
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
        public long getItemId(int position){
            curs.moveToPosition(position);
            return curs.getLong(curs.getColumnIndex(ChatDatabaseHelper.KEY_ID));
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
        db.close();
        curs.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
    public void refreshActivity(){
        finish();
        Intent intent = getIntent();
        startActivity(intent);
    }

}

