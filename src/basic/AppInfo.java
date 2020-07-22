/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *Class accesses and creates an About dialog box providing information about
 * the application. 
 * 
 * @author takeb
 */
public final class AppInfo {

    private static final org.apache.logging.log4j.Logger LOGR = LogManager.getLogger(AppInfo.class);
    private final String app_name, app_version, app_creator, app_desc;

    //Application Information Object
    private AppInfo(String app_name, String app_version, String app_creator, String app_desc) {
        this.app_name = app_name;
        this.app_version = app_version;
        this.app_creator = app_creator;
        this.app_desc = app_desc;
    }

    public final String getApp_name() {
        return app_name;
    }

    public final String getApp_version() {
        return app_version;
    }

    public final String getApp_creator() {
        return app_creator;
    }

    public final String getApp_desc() {
        return app_desc;
    }

    //Reads application information from json file
    public final static AppInfo info() {
        AppInfo temp = null;
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("src/resources/appinfo.json"));
            JSONObject jsonObject = (JSONObject) obj;
            temp = new AppInfo((String) jsonObject.get("app_name"),
                    (String) jsonObject.get("app_version"),
                    (String) jsonObject.get("app_creator"),
                    (String) jsonObject.get("app_desc"));
        } catch (Exception ex) {
            LOGR.error("Failed to load appdata from json file /n" + ex.getMessage());
        }
        return temp;
    }

    //Reads applicaiton information from text file
    private static String version() {
        String temp = "";
        try {
            String file = "src/resources/version.txt";
            Stream<String> lines = Files.lines(Paths.get(file));
            temp = lines.collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            LOGR.error("Unable load file\n" + e.getMessage());
        }
        return temp;
    }

    public static final void showAbout() {
        AppInfo temp = AppInfo.info();
        String contents = AppInfo.version();

        if (temp != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About " + temp.getApp_name());
            alert.setHeaderText(temp.getApp_name());
            alert.setContentText(temp.getApp_desc()  
                    +"\n\n\n Created By: " + temp.app_creator
                    +"\n Version: "  + temp.app_version);

            Label label = new Label("Version History:");
            TextArea textArea = new TextArea(contents);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();

        }
    }
    
}
