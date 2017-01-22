package hu.tgergo.hteam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] todos = new String[] {
                "Collect underpants",
                "?",
                "Profit"
        };

        ArrayAdapter<String> forecastAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_expandable_list_item_1,
                        todos
                );

        ListView list = (ListView) findViewById(R.id.todo_list);
        list.setAdapter(forecastAdapter);
    }
}
