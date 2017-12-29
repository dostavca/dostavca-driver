package si.dostavca.driver;

import java.io.Serializable;
import java.util.UUID;

public class Notification implements Serializable {

    private String message;
    private String uid;

    public Notification() {
        generateUid();
    }

    public Notification(String message) {
        this.message = message;
        generateUid();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    private void generateUid() {
        this.uid = UUID.randomUUID().toString();
    }

}
