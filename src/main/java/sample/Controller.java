package sample;

import com.google.firebase.auth.FirebaseAuthException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;

public class Controller {

    Preferences prefs = Preferences.userNodeForPackage(Controller.class);
    final String prefPass = "PREF_PASSWORD";
    final String prefMail = "PREF_EMAIL";

    @FXML
    private Label msgLabel;
    @FXML
    private PasswordField passField;
    @FXML
    private TextField emailField;

    private FirebaseClient fb;

    public Controller() {
        try {
            fb = new FirebaseClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        String email;
        String pass;

        if((email = prefs.get(prefMail,"")) != null)
            emailField.setText(email);
        if((pass = prefs.get(prefPass,"")) != null)
            passField.setText(pass);

    }


    @FXML
    private void registerOnClick() {

        try {
            if (FirebaseClient.checkInternetConnection()) {
                fb.createUser(emailField.getText(), passField.getText());
                msgLabel.setText("Регистрация прошла успешно");
                prefs.put(prefPass,passField.getText());
                prefs.put(prefMail,emailField.getText());
            } else msgLabel.setText("Проверьте соединение с интернетом");
        } catch (
                ExecutionException e)

        {
            msgLabel.setText("Ошибка регистрации.");
        } catch (
                InterruptedException e)

        {
            e.printStackTrace();
        } catch (
                RuntimeException e)

        {
            msgLabel.setText("Ошибка регистрации.");
        } catch (Exception e) {
            msgLabel.setText(e.getMessage());
        }

    }

    @FXML
    private void logInOnClick() {
        try {
            if (FirebaseClient.checkInternetConnection())
                if (fb.logIn(emailField.getText(), passField.getText())) {


                    msgLabel.setText("Loggined");// ВОТ ОТСЮДА НАДО ПЕРЕЙТИ НА НОВУЮ АКТИВНОСТЬ
                    prefs.put(prefPass,passField.getText());
                    prefs.put(prefMail,emailField.getText());

                } else msgLabel.setText("Проверьте соединение с интернетом");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AuthException e) {
            msgLabel.setText(e.getMessage());
        } catch (Exception e) {
            msgLabel.setText(e.getMessage());
        }
    }


}
