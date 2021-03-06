package hu.tgergo.hteam.rest;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import hu.tgergo.hteam.rest.callback.GetTodosCallback;
import hu.tgergo.hteam.rest.handler.RestHandler;

/**
 * Created by takacsgergo on 2017. 01. 22..
 */

public class RestManager {
    public void getTodos(final GetTodosCallback callback) {
        new AsyncTask<Void, Void, String[]>() {
            private String errorMsg = null;

            @Override
            protected String[] doInBackground(Void... params) {
                try {
                    RestHandler restHandler = new RestHandler();
                    String json = restHandler.get(new URL("https://jsonplaceholder.typicode.com/todos"));

                    return readTodosFromResponse(json);
                } catch (Exception e) {
                    errorMsg = e.getMessage();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String[] result) {
                if(errorMsg != null) {
                    callback.onError(errorMsg);
                } else if(result != null) {
                    callback.onResult(result);
                } else {
                    callback.onDataUnavailable();
                }
            }
        }.execute();
    }

    private String[] readTodosFromResponse(String json) throws JSONException {
        JSONArray todosArray = new JSONArray(json);
        String[] result = new String[todosArray.length()];

        for(int i = 0; i < todosArray.length(); i++) {
            JSONObject todo = todosArray.getJSONObject(i);
            result[i] = todo.getString("title");
        }

        return result;
    }
}
