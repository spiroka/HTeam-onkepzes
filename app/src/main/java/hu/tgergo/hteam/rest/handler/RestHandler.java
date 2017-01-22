package hu.tgergo.hteam.rest.handler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by takacsgergo on 2017. 01. 22..
 */

public class RestHandler {
    public String get(URL url) {
        HttpURLConnection urlConnection = null;
        String result = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder out = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

            in.close();
            reader.close();

            result =  out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null) urlConnection.disconnect();
        }

        return result;
    }
}
