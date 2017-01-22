package hu.tgergo.hteam.rest.callback;

/**
 * Created by takacsgergo on 2017. 01. 22..
 */

public interface GetTodosCallback {
    void onResult(String[] todos);
    void onError(String message);
    void onDataUnavailable();
}
