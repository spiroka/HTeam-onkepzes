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

        String[] forecasts = new String[] {
                "Hétfő - 20",
                "Kedd - 22",
                "Szerda - 21",
                "Csütörtök - 24",
                "Péntek - 18",
                "Szombat - 16",
                "Vasárnap - 20",
        };

        ArrayAdapter<String> forecastAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_expandable_list_item_1,
                        forecasts
                );

        ListView list = (ListView) findViewById(R.id.forecast_list);
        list.setAdapter(forecastAdapter);
    }
}
