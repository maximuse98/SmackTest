package simple;

import connections.Connect;
import models.Msg;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;

import java.io.IOException;

public class Client1 {

    public static void main(String[] args) throws InterruptedException, XMPPException, IOException, SmackException {

        Connect connect = new Connect("user3", "user3");
        Msg msg = new Msg("user1",
                "sqlt", "13:20:00");
        connect.connect();
        connect.send("user1", msg);
        Thread.sleep(Long.MAX_VALUE);
    }
}
