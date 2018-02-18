package connections;

import javafx.scene.image.ImageView;
import listeners.ChatMessageListenerImpl;
import models.Msg;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Connect {
    private ChatMessageListenerImpl ch;
    private String username;
    private String password;
    private ChatManager cm;
    private XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
    private AbstractXMPPConnection con;

    public String getUsername() {
        return username;
    }

    public AbstractXMPPConnection getConnect() {
        return con;
    }

    public Connect(String login){
        this.username=login;
    }

    public Connect(String login, String password) throws IOException, InterruptedException, XMPPException, SmackException {
        FileInputStream server=new FileInputStream("src/main/resources/properties/server.prop");
        Properties p = new Properties ();
        p.load(server);

        String host = (String) p.get("host");
        int port = Integer.parseInt((String) p.get("port"));

        this.username=login;
        this.password=password;

        config.setUsernameAndPassword(username, password);
        config.setPort(port);
        config.setHost(host);
        config.setServiceName("localhost");
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setConnectTimeout(100000);

        con= new XMPPTCPConnection(config.build());
    }

    public ChatMessageListenerImpl getCh() {
        return ch;
    }

    public void connect(ImageView image, List newMsg) throws InterruptedException, IOException, XMPPException, SmackException {
        con.connect();
        System.out.println("Connecting to : " + con.getHost() + ":" + con.getPort());
        con.login(this.username, this.password);

        cm = ChatManager.getInstanceFor(con);
        ch=new ChatMessageListenerImpl(image,newMsg);
        cm.addChatListener(ch);
    }

    public void connect() throws IOException, XMPPException, SmackException {
        con.connect();
        System.out.println("Connecting to : " + con.getHost() + ":" + con.getPort());
        con.login(this.username, this.password);

        cm = ChatManager.getInstanceFor(con);
        cm.addChatListener(new ChatMessageListenerImpl());
    }

    public Set<RosterEntry> listContacts(AbstractXMPPConnection connection) throws InterruptedException, SmackException.NotConnectedException, SmackException.NotLoggedInException {
        Roster roster = Roster.getInstanceFor(connection);
        if (!roster.isLoaded()) {
            roster.reloadAndWait();
        }
        return roster.getEntries();
    }

    public void disconnect(){
        con.disconnect();
    }

    public void send(final String receiver, Msg msg) throws XMPPException, SmackException.NotConnectedException {
        Chat chat = cm.createChat(receiver + "@localhost");
        chat.sendMessage(username.concat(" - "+msg.getMessage()+" - "+msg.getTime()));
    }
}
