package ru.axalit.modules.geobazis.display;

import org.json.JSONObject;

import java.util.Map;

public interface AxDisplayCallback {

    void changeToAuth();
    void changeToAction();

    void saveResultAuth(JSONObject resultAuth);

    void openKinest();
    void logout();

}
