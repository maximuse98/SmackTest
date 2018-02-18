package simple;

import connections.Connect;
import models.Msg;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public class Client2 {

    public static void main(String[] args) throws InterruptedException, XMPPException, IOException, SmackException {

        Connect connect = new Connect("user2", "user2");
        Msg msg = new Msg("user2",
                "privet2", "");
        connect.connect();
        //connect.send("user1", msg);
        Thread.sleep(Long.MAX_VALUE);
    }
}
