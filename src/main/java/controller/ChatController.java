package controller;

import com.jfoenix.controls.JFXDatePicker;
import connections.ConnectDb;
import dao.UserDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Msg;
import connections.Connect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import models.UserNote;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ChatController implements Initializable {
    private Connect user;
    private Image image;
    private Image incognitoImage = new Image(getClass().getResource("/pictures/incognito.png").toString(), 512, 512, true, true);;
    private List<Connect> users;
    private List<String> newMsg = new LinkedList();
    private String chosenUser = "";
    private String chosenMsg ="";
    private boolean incognito = false;
    private boolean isMsg = false;

    @FXML
    private List<String> foundUsers;
    @FXML
    public BorderPane borderPane;
    @FXML
    public VBox vb;
    @FXML
    public TextField searchField;
    @FXML
    public Label statusField;
    @FXML
    public ImageView incognitoMode;
    @FXML
    public ImageView refresh;
    @FXML
    public ImageView chosenLogo;
    @FXML
    public ImageView userImage;
    @FXML
    private TextArea msgInput;
    @FXML
    private ListView userListView;
    @FXML
    private ListView chatListView;
    @FXML
    private ImageView newMessage;
    @FXML
    private ImageView exit;
    @FXML
    private Label userNameLabel;
    @FXML
    private JFXDatePicker msgTimeInput;
    @FXML
    private Button addMsgButton;
    @FXML
    private Button removeMsgButton;

    public ChatController() {
        this.users = new ArrayList();
        this.foundUsers = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            image = new Image(getClass().getResource("/pictures/" + user.getUsername() + ".png").toString(), 512, 512, true, true);
            try {
                user.connect(newMessage,newMsg);
            }
            catch (Exception e){
            }

            Set<RosterEntry> rosterEntries = user.listContacts(user.getConnect());

            for (RosterEntry rosterEntry : rosterEntries) {
                users.add(new Connect(rosterEntry.getName()));
            }

            Roster roster = Roster.getInstanceFor(user.getConnect());

            ObservableList<Connect> observableList = FXCollections.observableList(users);
            userListView.setItems(observableList);
            userListView.setCellFactory(new CellRendererUserList(roster));

            if (!incognito) {
                incognito=true;
                this.onIncognitoModeClick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMainClick() throws SQLException, IOException {
        ResultSet rs = new UserDao(new ConnectDb())
                .get(user);
        List ll = new LinkedList();

        while (rs.next()) {
            int i=0;
            String action = rs.getString("action");
            String time = rs.getString("time");

            ll.add(i++,time.concat(" - " + action));
        }
        Collections.sort(ll);

        userNameLabel.setText(user.getUsername());
        if (incognito) {
            chosenLogo.setImage(image);
        } else {
            chosenLogo.setImage(incognitoImage);
        }
        ObservableList<Msg> observableList = FXCollections.observableList(ll);
        chatListView.setItems(observableList);
        chosenUser=user.getUsername();

        addMsgButton.setVisible(false);
        removeMsgButton.setVisible(false);
        isMsg=false;
    }

    public void onSendMsgClick() throws Exception {
        System.out.println("click !!!");
        if(!msgInput.getText().isEmpty() && msgTimeInput.getTime()!=null && !chosenUser.isEmpty()) {
            if (chosenUser == user.getUsername()) {
                new UserDao(new ConnectDb()).add(new UserNote(user.getUsername(), msgInput.getText(), msgTimeInput.getTime().toString()));
                this.onMainClick();
            }
            user.send(chosenUser, new Msg(user.getUsername(), msgInput.getText(), msgTimeInput.getTime().toString()));
            addMsgButton.setVisible(false);
            removeMsgButton.setVisible(false);
        }
    }

    public void onUserListClick() throws IOException, SQLException {
        Connect selectedItem = (Connect) userListView.getSelectionModel().getSelectedItem();
        if(selectedItem==null) return;
        chosenUser = selectedItem.getUsername();

        ResultSet rs = new UserDao(new ConnectDb())
                .get(selectedItem);
        List ll = new LinkedList();

        while (rs.next()) {
            String time = rs.getString("time");

            ll.add(time);
        }
        Collections.sort(ll);

        chosenLogo.setImage(new Image(getClass().getResource("/pictures/" + chosenUser + ".png").toString(), 512, 512, true, true));
        userNameLabel.setText(chosenUser);
        ObservableList<Msg> observableList = FXCollections.observableList(ll);
        chatListView.setItems(observableList);
        addMsgButton.setVisible(false);
        removeMsgButton.setVisible(false);
        isMsg=false;
    }

    public void onChatListClick() {
        if(chosenUser==user.getUsername() && !chatListView.getItems().isEmpty()){
            removeMsgButton.setVisible(true);
        }
        if(isMsg) {
            addMsgButton.setVisible(true);
            removeMsgButton.setVisible(true);
        }
        chosenMsg = (String) chatListView.getSelectionModel().getSelectedItem();
    }

    public void onExit() throws IOException {
        Stage stage = (Stage) exit.getScene().getWindow();
        user.disconnect();
        stage.close();

        Stage st = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fronts/login.fxml"));
        stage.setTitle("Login");
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.setResizable(false);
        st.show();
    }

    public void onNewMessageClick() {
        if(newMsg.size()!=0) {
            chosenLogo.setImage(new Image(getClass().getResource("/pictures/newMessageChoose.png").toString(), 512, 512, true, true));
            userNameLabel.setText("New messages");
            ObservableList<String> observableList = FXCollections.observableList(user.getCh().getNewMsg());
            chatListView.setItems(observableList);
            chosenUser="";
            isMsg=true;
        }
        addMsgButton.setVisible(false);
        removeMsgButton.setVisible(false);
    }

    public void addMsg() throws IOException, SQLException {
        UserNote ns = new UserNote(user.getUsername(),this.actionReader(chosenMsg),this.timeReader(chosenMsg));

        if(addMsgButton.isVisible()) {
            new UserDao(new ConnectDb()).add(ns);
        }

        this.removeMsg();

        addMsgButton.setVisible(false);
        removeMsgButton.setVisible(false);
        isMsg=false;
    }

    public void removeMsg() throws IOException, SQLException {
        if(chosenUser==user.getUsername()) {
            UserNote ns = new UserNote(user.getUsername(),this.actionReader(chosenMsg),this.usernameReader(chosenMsg));
            new UserDao(new ConnectDb()).remove(ns);
            this.onMainClick();
            removeMsgButton.setVisible(false);
            isMsg=false;
            return;
        }
        newMsg.remove(chatListView.getSelectionModel().getSelectedItem());

        ObservableList<String> observableList = FXCollections.observableList(user.getCh().getNewMsg());
        chatListView.setItems(observableList);

        if(chatListView.getItems().isEmpty()) {
            newMessage.setImage(null);
        }
        addMsgButton.setVisible(false);
        removeMsgButton.setVisible(false);
        isMsg=false;
    }

    public void refresh() throws Exception {
        Roster roster = Roster.getInstanceFor(user.getConnect());

        ObservableList<Connect> observableList = FXCollections.observableList(users);
        userListView.setItems(observableList);
        userListView.setCellFactory(new CellRendererUserList(roster));

        addMsgButton.setVisible(false);
        removeMsgButton.setVisible(false);
    }

    public void onIncognitoModeClick() {
        if (incognito) {
            incognitoMode.setImage(new Image(getClass().getResource("/pictures/polzHover.png").toString()));
            userImage.setImage(incognitoImage);
            statusField.setText("Offline");
            if(chosenLogo.getImage()==image) chosenLogo.setImage(incognitoImage);
            user.disconnect();
        } else {
            incognitoMode.setImage(new Image(getClass().getResource("/pictures/polzunok.png").toString()));
            userImage.setImage(image);
            statusField.setText("Online");
            if(chosenLogo.getImage()==incognitoImage) chosenLogo.setImage(image);
            try {
                user.connect(newMessage,newMsg);
            } catch (Exception e) {
            }
        }
        incognito = !incognito;

        addMsgButton.setVisible(false);
        removeMsgButton.setVisible(false);
    }

    public void onSearchField() throws IOException, SQLException {
        chosenLogo.setImage(new Image(getClass().getResource("/pictures/search.png").toString(), 512, 512, true, true));
        userNameLabel.setText("Search");
        ResultSet rs = new UserDao(new ConnectDb())
                .getNote(user,searchField.getText());
        List ll = new LinkedList();

        while (rs.next()) {
            String action = rs.getString("action");
            String time = rs.getString("time");

            ll.add(time.concat(" - " + action));
        }
        Collections.sort(ll);
        ObservableList<Msg> observableList = FXCollections.observableList(ll);
        chatListView.setItems(observableList);
        chosenUser=user.getUsername();
        addMsgButton.setVisible(false);
        removeMsgButton.setVisible(false);
    }

    public void setUser(Connect user) {
        this.user = user;
    }
    public void setIncognito(boolean incognito) {
        this.incognito = incognito;
    }

    public String usernameReader(String str) throws IOException {
        BufferedReader br =  new BufferedReader(new StringReader(str));
        int character;
        StringBuilder sb = new StringBuilder();
        while ((character = br.read()) != -1) {
            char ch = (char) character;
            if (character == ' ') break;
            sb.append(ch);
        }
        return sb.toString();
    }
    public String actionReader(String str) throws IOException {
        BufferedReader br =  new BufferedReader(new StringReader(str));
        int character;
        StringBuilder sb = new StringBuilder();
        while ((character = br.read()) != -1) {
            if (character == '-') break;
        }
        br.read();
        while ((character = br.read()) != -1) {
            char ch = (char) character;
            if (character == '-') break;
            sb.append(ch);
        }
        return sb.toString();
    }
    public String timeReader(String str) throws IOException {
        BufferedReader br =  new BufferedReader(new StringReader(str));
        int character;
        StringBuilder sb = new StringBuilder();
        while ((character = br.read()) != -1) {
            if (character == '-') break;
        }
        while ((character = br.read()) != -1) {
            if (character == '-') break;
        }
        while ((character = br.read()) != -1) {
            char ch = (char) character;
            sb.append(ch);
        }
        return sb.toString();
    }
}
