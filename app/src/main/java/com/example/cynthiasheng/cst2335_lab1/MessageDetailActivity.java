package com.example.cynthiasheng.cst2335_lab1;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        messageFragment.setArguments(bundle);

//        FragmentManager fragmentManager =getFragmentManager();
//        //remove previous fragment
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
//            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        }
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.portraitFrameLayout, messageFragment).addToBackStack(null).commit();
//    }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.portraitFrameLayout, messageFragment);
        ft.commit();
    }
}