/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import db.Login;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class LoginController implements Initializable {

    private static final org.apache.logging.log4j.Logger LOGR = LogManager.getLogger(LoginController.class);

    @FXML
    private Button bttn_login, bttn_clear;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label error;

    @FXML
    public void onEnter(ActionEvent ae) throws Exception {
        // login();
    }

    //Listen for login button click
    @FXML
    public void onLogin(ActionEvent ae) throws Exception {

        //hardcode test User and Year
        User user = new User("kevin.mahon", "Richard Staxx", "Finance Dept", "Clerk", LocalDate.now());
        int year = LocalDate.now(ZoneId.of("America/Port_of_Spain")).getYear();

        /**
         *
         **** Disabled login for manual login for duration of beta testing
         *
         * login();
         */
        openMain(user, year);
    }

    //Tab Key action listener
    @FXML
    public void onTab(KeyEvent key) throws Exception {
        if (key.getCode() == KeyCode.TAB) {
            if (username.isFocused()) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        password.requestFocus();
                    }
                });
            } else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        username.requestFocus();
                    }
                });
            }
        }
        if (key.getCode() == KeyCode.ENTER) {
            if (password.isFocused()) {
                login();
            }
        }
    }

    /**
     * **********************************************************************
     */
    //trigges the gui actions during login
    public void login() throws Exception {
        //error if a field is empty
        if (!valLogin()) {
            loginError("Error! Please enter Username and Password");
        } else {
            //load gui effects
            loginEntry();
            // configure task - receives data from user model 
            Task<Object[]> task = new Task<Object[]>() {
                @Override
                protected Object[] call() throws InterruptedException {
                    //under review
                    Thread.sleep(1000);
                    //reutrns Login object
                    return Login.getUser(username.getText(), password.getText());
                }
            };
            //task was successfull
            task.setOnSucceeded(
                    new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(final WorkerStateEvent workerStateEvent
                ) {
                    //if login is successfull 
                    if ((Boolean) task.getValue()[0]) {
                        try {
                            //attemps to launch program main gui
                            openMain((User) task.getValue()[1], LocalDate.now(ZoneId.of("America/Port_of_Spain")).getYear());
                        } catch (Exception ex) {
                            LOGR.fatal("Main GUI failed to open. Main.FXML /n" + ex.getMessage());
                        }
                    } else {
                        //triggers Gui error event
                        loginError("Error! Please Re-Enter Username or Password");
                        LOGR.error("User Credentials Failed" );
                    }
                }
            }
            );
            task.setOnFailed(
                    new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(final WorkerStateEvent workerStateEvent
                ) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle(null);
                    alert.setHeaderText("Critical Error");
                    alert.setContentText("                           Network Database Unreachable. "
                            + "\n             Please contact Network Administrator for assistance.");
                    alert.showAndWait();

                    //triggers Gui error event
                    loginError("");
                }
            }
            );

            new Thread(task)
                    .start();
        }
    }

    /**
     * *******************************************************************
     */
    private void openMain(User login_user, int login_year) throws Exception {

        //Panel
        Stage stage = (Stage) username.getScene().getWindow();
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("/main/Main.fxml"));
        Parent root = load.load();

        try{
        //Passes data from LoginController to MainController
        main.MainController mContr = load.getController();
        mContr.setYear(login_year);
        mContr.setUser(login_user);
        mContr.buildView();
        //create new main GUI scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("CGA Ltd Daily Financials");
        stage.setResizable(false);
        stage.show();
        }catch(Exception e){
        LOGR.fatal("Main FXML failed to load");        
        }
    }

    /**
     * ************ GUI TRIGGERS *************
     */
    //Gui error event trigger and message.
    private void loginError(String msg) {

        //enable input
        username.setDisable(false);
        password.setDisable(false);
        bttn_login.setDisable(false);
        bttn_clear.setDisable(false);
        //trigger error message
        error.setGraphic(null);
        error.setStyle("-fx-font-size: 12pt ;");
        error.setText(msg);
    }

    private void loginEntry() {
        //loard gui graphics
        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResource("/resources/spin.gif").toExternalForm());
        ImageView iv1 = new ImageView();
        iv1.setFitHeight(50);
        iv1.setFitWidth(50);
        iv1.setImage(image);

        //disable input
        this.username.setDisable(true);
        this.password.setDisable(true);
        this.bttn_login.setDisable(true);
        this.bttn_clear.setDisable(true);

        //load login graphic
        this.error.setGraphic(iv1);
        this.error.setText("Logging In  ");
        this.error.setStyle("-fx-font-size: 15pt ;");

    }

    //Clears text input fields
    @FXML
    public void clearFields(ActionEvent ae) throws Exception {
        this.username.setText("");
        this.password.setText("");
        error.setText("");
        error.setGraphic(null);
    }

    //validates text input fields
    private Boolean valLogin() {
        return (!this.username.getText().equalsIgnoreCase("") && !this.password.getText().equalsIgnoreCase(""));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
