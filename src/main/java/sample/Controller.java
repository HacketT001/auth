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

public class Controller {
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
    private void registerOnClick() {

        try {
            if (FirebaseClient.checkInternetConnection()) {
                fb.createUser(emailField.getText(), passField.getText());
                msgLabel.setText("Регистрация прошла успешно");
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
        }

    }

    @FXML
    private void logInOnClick() {
        try {
            if (FirebaseClient.checkInternetConnection())
                if (fb.logIn(emailField.getText(), passField.getText())) {


                    msgLabel.setText("Loggined");// ВОТ ОТСЮДА НАДО ПЕРЕЙТИ НА НОВУЮ АКТИВНОСТЬ


                } else msgLabel.setText("Проверьте соединение с интернетом");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AuthException e) {
            msgLabel.setText(e.getMessage());
        }
    }


}
