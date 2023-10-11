package de.popup.test;

import android.app.AlertDialog;
import android.content.DialogInterface;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class popup extends CordovaPlugin {

    private final String pluginName = "cordova-plugin-popup";

    private void popupMessages(String[] messages, CallbackContext callbackContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cordova.getActivity());
        builder.setMessage(String.join(",", messages));
        builder.setTitle("TEST");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PluginResult result = new PluginResult(PluginResult.Status.OK, "DATEN DARGESTELLT");
                callbackContext.sendPluginResult(result);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR, "ABBRUCH DURCH USER");
                callbackContext.sendPluginResult(result);
            }
        });

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.create().show();
            }
        });

    }


    @Override
    public boolean execute(final String action, final JSONArray data, final CallbackContext callbackContext) {

        if (action.equals("message")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    ArrayList<String> messages = new ArrayList<>();

                    try {
                        for (int n = 0; n < data.length(); n++) {
                            String test = data.getString(n);
                            messages.add(test);
                        }

                        popupMessages(messages.toArray(new String[messages.size()]), callbackContext);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);

        } else if (action.equals("test")) {

            /*
            {
                "test": "inhalt bla keks"
            }


            * */
            try {
                JSONObject obj = data.getJSONObject(0);

                PluginResult result = new PluginResult(PluginResult.Status.OK,obj.has("test")?obj.getString("test"):"test nicht gefunden!");
                callbackContext.sendPluginResult(result);
            } catch (JSONException e) {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                callbackContext.sendPluginResult(result);
                return true;
            }
        } else if (action.equals("error")) {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "FEHLER ABC");
            callbackContext.sendPluginResult(result);
        } else {
            return false;
        }

        return true;
    }
}