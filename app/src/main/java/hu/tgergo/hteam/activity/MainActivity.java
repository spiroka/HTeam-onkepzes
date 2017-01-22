package hu.tgergo.hteam.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import hu.tgergo.hteam.R;
import hu.tgergo.hteam.rest.RestManager;
import hu.tgergo.hteam.rest.callback.GetTodosCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestManager restManager = new RestManager();
        restManager.getTodos(new GetTodosCallback() {
            @Override
            public void onResult(String[] todos) {
                ArrayAdapter<String> todoAdapter =
                        new ArrayAdapter<>(
                                MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1,
                                todos
                        );

                ListView list = (ListView) findViewById(R.id.todo_list);
                list.setAdapter(todoAdapter);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDataUnavailable() {
                Toast.makeText(MainActivity.this, "Nothing to do", Toast.LENGTH_LONG).show();
            }
        });
    }
}
