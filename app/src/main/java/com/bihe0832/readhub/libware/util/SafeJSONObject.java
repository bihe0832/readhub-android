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
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return false;
        }
        return super.getBoolean(name);
    }

    @Override
    public double getDouble(String name) throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return -1;
        }
        return super.getDouble(name);
    }

    @Override
    public int getInt(String name) throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return -1;
        }
        return super.getInt(name);
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
    public long getLong(String name) throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return -1;
        }
        return super.getLong(name);
    }

    @Override
    public String getString(String name) throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return "";
        }
        return super.getString(name);
    }

    public boolean getBoolean(String name, boolean defaultValue)
            throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return defaultValue;
        }
        return super.getBoolean(name);
    }

    public double getDouble(String name, double defaultValue)
            throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return defaultValue;
        }
        return super.getDouble(name);
    }

    public int getInt(String name, int defaultValue) throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return defaultValue;
        }
        return super.getInt(name);
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

    public long getLong(String name, Long defaultValue) throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return defaultValue;
        }
        return super.getLong(name);
    }

    public String getString(String name, String defaultValue)
            throws JSONException {
        if (!has(name)) {
            // Logger.w("json no key: " + name);
            return defaultValue;
        }
        return super.getString(name);
    }
}
