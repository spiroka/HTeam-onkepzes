package hu.tgergo.hteam.dal;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import hu.tgergo.hteam.dal.database.DatabaseOpenHelper;
import hu.tgergo.hteam.dal.database.DatabaseSchema;

/**
 * Created by takacsgergo on 2017. 01. 28..
 */

public class HteamContentProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "hu.tgergo.hteam.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/todo");

    private static final int TODOS = 1;
    private static final int SINGLE_TODO = 2;

    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(PROVIDER_NAME, "todo", TODOS);
        mUriMatcher.addURI(PROVIDER_NAME, "todo/#", SINGLE_TODO);
    }

    private DatabaseOpenHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseOpenHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DatabaseSchema.TABLE_NAME);

        if(mUriMatcher.match(uri) == SINGLE_TODO) {
            String rowId = uri.getPathSegments().get(1);
            qb.appendWhere(DatabaseSchema.ID_COLUMN_NAME + "=" + rowId);
        }

        return qb.query(
                mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mDbHelper.getWritableDatabase().insertWithOnConflict(
                DatabaseSchema.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
        );

        if(id > -1) {
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);

            getContext().getContentResolver().notifyChange(insertedId, null);

            return insertedId;
        } else {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if(mUriMatcher.match(uri) == SINGLE_TODO) {
            String rowId = uri.getPathSegments().get(1);
            selection = DatabaseSchema.ID_COLUMN_NAME + "=" + rowId +
                    (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
        } else if(selection == null) {
            selection = "1";
        }

        int count = mDbHelper.getWritableDatabase().delete(
                DatabaseSchema.TABLE_NAME,
                selection,
                selectionArgs
        );

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(mUriMatcher.match(uri) == SINGLE_TODO) {
            String rowId = uri.getPathSegments().get(1);
            selection = DatabaseSchema.ID_COLUMN_NAME + "=" + rowId +
                    (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
        }

        int count = mDbHelper.getWritableDatabase().update(
                DatabaseSchema.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case TODOS:
                return "vnd.android.cursor.dir/vnd.hteam.todo";
            case SINGLE_TODO:
                return "vnd.android.cursor.item/vnd.hteam.todo";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
