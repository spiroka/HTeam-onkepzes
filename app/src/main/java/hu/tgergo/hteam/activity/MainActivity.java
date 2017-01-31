package hu.tgergo.hteam.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import hu.tgergo.hteam.R;
import hu.tgergo.hteam.dal.HteamContentProvider;
import hu.tgergo.hteam.dal.database.DatabaseSchema;
import hu.tgergo.hteam.rest.RestManager;
import hu.tgergo.hteam.rest.callback.GetTodosCallback;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private TodoCursorAdapter mAdapter;
    private ListView mTodoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new TodoCursorAdapter(this, null);
        mTodoList = (ListView) findViewById(R.id.todo_list);

        RestManager restManager = new RestManager();
        restManager.getTodos(new GetTodosCallback() {
            @Override
            public void onResult(String[] todos) {
                getContentResolver().delete(
                        HteamContentProvider.CONTENT_URI,
                        null,
                        null
                );

                for (String todo : todos) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseSchema.TITLE_COLUMN_NAME, todo);
                    getContentResolver().insert(HteamContentProvider.CONTENT_URI, values);
                }

                getSupportLoaderManager().restartLoader(1, null, MainActivity.this);
            }

            @Override
            public void onError(String message) {
                getSupportLoaderManager().restartLoader(1, null, MainActivity.this);
            }

            @Override
            public void onDataUnavailable() {
                getContentResolver().delete(
                        HteamContentProvider.CONTENT_URI,
                        null,
                        null
                );

                Toast.makeText(MainActivity.this, "Nothing to do", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                HteamContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mTodoList.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public class TodoCursorAdapter extends CursorAdapter {
        TodoCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(
                    android.R.layout.simple_list_item_1,
                    parent,
                    false
            );
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            if(cursor != null) {
                TextView titleView = (TextView) view.findViewById(android.R.id.text1);
                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseSchema.TITLE_COLUMN_NAME)
                );

                titleView.setText(title);
            }
        }
    }
}
