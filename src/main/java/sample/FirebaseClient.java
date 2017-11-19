package sample;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;


/**
 * Created by Tom on 17.11.2017.
 */
public class FirebaseClient {

    public FirebaseClient() throws IOException {
        initFirebaseClient();
    }

    private void initFirebaseClient() throws IOException {

        File googleCredentials = new File("./src/main/java/sample/tb-firebase.json");
        FileInputStream serviceAccount = new FileInputStream(googleCredentials);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://telegrambot-15be3.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);

    }

    public void createUser(String username, String password) throws ExecutionException, InterruptedException, RuntimeException {
        String hwid = getHWID();
        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(username)
                .setPassword(password)
                .setDisplayName(password)
                .setUid(hwid)
                .setDisabled(true);

        FirebaseAuth.getInstance().createUserAsync(createRequest).get();

    }

    public boolean logIn(String username, String password) throws InterruptedException, AuthException {
        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUserByEmailAsync(username).get();
            String hwid;
            String pass;

            if (!(hwid = userRecord.getUid()).equals(getHWID()))
                throw new AuthException("Вы не можете зайти с другого PC");
            if (!(pass = userRecord.getDisplayName()).equals(password))
                throw new AuthException("Неверный пароль");
            if (userRecord.isDisabled())
                throw new AuthException("Аккаунт не активирован");

            return hwid.equals(getHWID()) && password.equals(pass);
        } catch (ExecutionException e) {
            throw new AuthException("Неверный e-mail.");
        }
    }

    public String getHWID() {

        try {
            InetAddress ip = InetAddress.getLocalHost();

            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();

                if (mac != null) {
                    System.out.print("Current MAC address : ");


                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    System.out.println(sb.toString());
                }
            }
            return sb.toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkInternetConnection() {
        Boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL("https://firebase.google.com/").openConnection();
            con.setRequestMethod("HEAD");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try {
            FirebaseClient firebaseClient = new FirebaseClient();
            firebaseClient.createUser("p@GMAIL.COM", "ASda231asf");
            System.out.println(firebaseClient.logIn("p@GMAIL.COM", "ASda231asf"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthException e) {
            e.printStackTrace();
        }
    }

}
