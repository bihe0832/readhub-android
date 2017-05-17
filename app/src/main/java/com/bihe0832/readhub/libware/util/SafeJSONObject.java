package com.bihe0832.readhub.libware.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SafeJSONObject extends JSONObject {
    public SafeJSONObject(String content) throws JSONException {
        super(content);
    }

    public SafeJSONObject() {
        super();
    }

    public SafeJSONObject(JSONObject src) throws JSONException {
        super(src.toString());
    }

    @Override
    public boolean getBoolean(String name) throws JSONException {
        return getBoolean(name, false);
    }

    @Override
    public double getDouble(String name) throws JSONException {
        return getDouble(name, -1);
    }

    @Override
    public int getInt(String name) {
        return getInt(name, -1);
    }

    @Override
    public JSONArray getJSONArray(String name) throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return new JSONArray();
        }
        return super.getJSONArray(name);
    }

    @Override
    public JSONObject getJSONObject(String name) throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return new SafeJSONObject();
        }
        return super.getJSONObject(name);
    }

    @Override
    public long getLong(String name) {
        return getLong(name, -1L);
    }

    @Override
    public String getString(String name) {

        return getString(name, "");
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        if (!has(name)) {
            return defaultValue;
        }
        try {
            return super.getBoolean(name);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public double getDouble(String name, double defaultValue) {
        if (!has(name)) {
            return defaultValue;
        }
        try {
            return super.getDouble(name);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public int getInt(String name, int defaultValue) {
        if (!has(name)) {
            return defaultValue;
        }
        try {
            return super.getInt(name);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public JSONArray getJSONArray(String name, JSONArray defaultValue)
            throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return defaultValue;
        }
        return super.getJSONArray(name);
    }

    public JSONObject getJSONObject(String name, JSONObject defaultValue)
            throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return defaultValue;
        }
        return super.getJSONObject(name);
    }

    public long getLong(String name, Long defaultValue) {
        if (!has(name)) {
            return defaultValue;
        }
        try {
            return super.getLong(name);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public String getString(String name, String defaultValue) {
        if (!has(name)) {
            return defaultValue;
        }
        try {
            return super.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
