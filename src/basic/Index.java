/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package basic;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public abstract class Index implements Initializable {

    /**
     * **** FXML Controls ********
     */
    @FXML
    private Pane head;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Label date_lbl, user_lbl, last_login_lbl, title_lbl, timeClock;

    @FXML
    private MenuBar menu_bar;

    @FXML
    private MenuItem close_app, about_app;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button clear_btn, save_btn;

    @FXML
    private ArrayList<TextField> fields;

    /**
     * ** Private Variables ********
     */
    private int year;
    private User user;
    private AppState state;
    private static final org.apache.logging.log4j.Logger LOGR = LogManager.getLogger(AppInfo.class);

    /**
     * ******************* MAIN ********************************
     */
    public void buildView() {
        this.buildHeader();
        this.buildChild();
        this.addOnClose();
    }

    /**
     * ************* BUILD APPLICATION HEADER **************************
     */
    public void buildHeader() {
        runClock(this.timeClock);
        LocalDate last_login = user.getLastLogin();
        LocalDate today = LocalDate.now(ZoneId.of("America/Port_of_Spain"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        this.date_lbl.setText("Date: " + today.format(formatter));
        this.user_lbl.setText("User: " + user.getEmpname());
        this.last_login_lbl.setText("Last Login: " + last_login.format(formatter));
        this.title_lbl.setText("CGA Blank App");
        this.state = new AppState();
        
        LOGR.info("Header succesfully created");
    }

    /**
     * ******************* BUILD APPLICATION BODY *****************************
     */
    public abstract void buildChild();

    public abstract void saveData();

    public abstract void loadData();

    /**
     * ******************* TASKS ********************************
     */
    public void runClock(Label clock) {
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    DateFormat temp = new SimpleDateFormat("hh:mm aaa");
                    clock.setText("Time: " + temp.format(new Date()));
                });
            }
        };
        t.schedule(tt, 1000);
    }

    public void addOnClose() {
        try {
            Task t = new Task() {
                @Override
                protected Object call() throws Exception {
                    Platform.runLater(() -> {
                        Stage stage = (Stage) last_login_lbl.getScene().getWindow();
                        stage.setOnCloseRequest(event -> {
                            event.consume();
                            endProgram(state);
                        });
                    });
                    return null;
                }
            };

            t.run();
        } catch (Exception ex) {
            LOGR.error("Failed to load onClose Event");
        }
    }

    /**
     * ***************** FXML ACTIONS ******************************
     */
    @FXML
    public void clearBttn(ActionEvent ae) throws Exception {
        AppInfo info = AppInfo.info();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(info.getApp_name());
        alert.setHeaderText("Clear Data");
        ButtonType buttonTypeOne = new ButtonType("Save");
        ButtonType buttonTypeTwo = new ButtonType("Clear");
        ButtonType buttonTypeThree = new ButtonType("Cancel");
        if (this.state.getAppState().equalsIgnoreCase("FAIL") || this.state.getAppState().equalsIgnoreCase("WIP")) {
            alert.setContentText("You have unsaved data. "
                    + "Do you want to save before clearing data?");
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
        } else {
            alert.setContentText("Clear all data?");
            alert.getButtonTypes().setAll(buttonTypeTwo, buttonTypeThree);
        }
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeTwo) {
            clearItems();
            alert.close();
        } else if (result.get() == buttonTypeThree) {
            alert.close();
        } else {
            saveData();
        }

    }

    @FXML
    private void about() {
        AppInfo.showAbout();
    }

    /**
     * ** Getter and Setters ********
     */
    public void setYear(int year) {
        this.year = year;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getYear() {
        return year;
    }

    public ScrollPane getScroll() {
        return scroll;
    }

    public AnchorPane getAnchor() {
        return anchor;
    }

    /**
     * ** Very Long Functions *********
     */
    //Create a dialoge when closing application
    private void endProgram(AppState state) {

        AppInfo info = AppInfo.info();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(info.getApp_name());

        alert.setHeaderText("Close Application");
        ButtonType buttonTypeOne = new ButtonType("Save");
        ButtonType buttonTypeTwo = new ButtonType("Exit");
        ButtonType buttonTypeThree = new ButtonType("Cancel");

        switch (state.getAppState()) {
            case "WIP":
                alert.setContentText("You have unsaved Data. "
                        + "Are you sure you want to close the application");
                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
                break;
            case "FAIL":
                alert.setContentText("You have not saved your Data. "
                        + "Are you sure you want to close the application");
                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
                break;
            default:
                alert.setContentText("Do you wish to exit the application?");
                alert.getButtonTypes().setAll(buttonTypeTwo, buttonTypeThree);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonTypeTwo) {
                    Stage stage = (Stage) menu_bar.getScene().getWindow();
                    stage.close();
                } else if (result.get() == buttonTypeThree) {
                    alert.close();
                } else {
                    saveData();
                }
        }
    }
// Iterates through anchor object setting all form objects to empty

    private void clearItems() {
        for (Node node : anchor.getChildren()) {
            if (node instanceof Group) {
                Group group = (Group) node;
                for (Node group_node : group.getChildren()) {
                    if (group_node instanceof TextField) {
                        TextField temp = (TextField) group_node;
                        temp.setText("");
                    }
                    if (group_node instanceof DatePicker) {
                        DatePicker temp = (DatePicker) group_node;
                        temp.setValue(null);
                    }
                    if (group_node instanceof VBox) {
                        VBox vbox = (VBox) group_node;
                        for (Node vbox_node : vbox.getChildren()) {
                            if (vbox_node instanceof TextField) {
                                TextField temp = (TextField) vbox_node;
                                temp.setText("");
                            }
                        }
                    }
                }
            }
            if (node instanceof VBox) {
                VBox temp_vbox = (VBox) node;
                for (Node vbox_node : temp_vbox.getChildren()) {
                    if (vbox_node instanceof TextField) {
                        TextField temp = (TextField) vbox_node;
                        temp.setText("");
                    }
                }
            }
            if (node instanceof HBox) {
                HBox temp_hbox = (HBox) node;
                for (Node hbox_node : temp_hbox.getChildren()) {
                    if (hbox_node instanceof TextField) {
                        TextField temp = (TextField) hbox_node;
                        temp.setText("");
                    }
                }
            }
            if (node instanceof RadioButton) {
                RadioButton temp = (RadioButton) node;
                if (temp.isSelected()) {
                    temp.setSelected(false);
                }
            }
            if (node instanceof ChoiceBox) {
                ChoiceBox temp = (ChoiceBox) node;
                temp.valueProperty().set(null);
            }
            if (node instanceof DatePicker) {
                DatePicker temp = (DatePicker) node;
                temp.setValue(null);
            }
            if (node instanceof TextField) {
                TextField temp = (TextField) node;
                temp.setText("");
            }
        }
    }

    //Scene intialize
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

//End of class
}
