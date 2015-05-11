package com.example.tmagiera.j2objc;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.common.eventbus.Subscribe;

import External.Book;
import External.BookManagerFacade;
import External.BooksRefreshedEvent;
import External.EventBusSingleton;
import External.LoginEvent;
import External.LoginManagerFacade;

public class MainActivity extends ActionBarActivity {

    class EventBusLoginRecorder {
        @Subscribe public void recordCustomerChange(LoginEvent loginEvent) {
            if (loginEvent.loginSuccess) {
                Log.d("Event", "Refreshing book list");
                BookManagerFacade.refreshBookList();
            }
        }
    }

    class EventBusBookListRecorder {
        @Subscribe public void recordCustomerChange(BooksRefreshedEvent booksRefreshedEvent) {
            for(Book book : BookManagerFacade.getBookList()) {
                Log.d("BookTitle", book.title);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBusSingleton.register(new EventBusLoginRecorder());
        EventBusSingleton.register(new EventBusBookListRecorder());

        LoginManagerFacade.login();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
