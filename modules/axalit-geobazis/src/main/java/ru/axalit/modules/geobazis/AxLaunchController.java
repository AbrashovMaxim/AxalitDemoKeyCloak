package ru.axalit.modules.geobazis;

public class AxLaunchController {

    public static void main(String[] args) {
        try {
            AxApplicationController.launch(AxApplicationController.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
