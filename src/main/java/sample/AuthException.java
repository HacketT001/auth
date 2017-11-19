package sample;

/**
 * Created by Tom on 19.11.2017.
 */
public class AuthException extends Exception {
    public AuthException(String reason){
        super(reason);
    }
}
