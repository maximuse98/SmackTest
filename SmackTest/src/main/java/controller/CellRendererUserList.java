package controller;

import connections.Connect;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CellRendererUserList implements Callback<ListView<Connect>, ListCell<Connect>> {

    List<String> onlineUsers;
    Collection<RosterEntry> entries;

    public CellRendererUserList(Roster roster) {
        this.entries = roster.getEntries();
        onlineUsers=new ArrayList<>();

        Presence presence;
        for (RosterEntry entry : entries) {
            presence = roster.getPresence(entry.getUser());
            String result = presence.getType().name();
            if(result=="available") onlineUsers.add(entry.getUser());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellRendererUserList that = (CellRendererUserList) o;

        if (onlineUsers != null ? !onlineUsers.equals(that.onlineUsers) : that.onlineUsers != null) return false;
        return entries != null ? entries.equals(that.entries) : that.entries == null;
    }

    @Override
    public int hashCode() {
        int result = onlineUsers != null ? onlineUsers.hashCode() : 0;
        result = 31 * result + (entries != null ? entries.hashCode() : 0);
        return result;
    }

    @Override
    public ListCell<Connect> call(ListView<Connect> param) {

        ListCell<Connect> cell = new ListCell<Connect>() {

            @Override
            protected void updateItem(Connect user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    HBox hBox = new HBox();

                    Text username = new Text("\t" + user.getUsername());
                    username.setFill(Color.valueOf("white"));

                    Image image = new Image(getClass().getResource("/pictures/" + user.getUsername() + ".png").toString(), 50, 50, true, true);

                    ImageView pictureImageView = new ImageView();
                    ImageView online = new ImageView();

                    pictureImageView.setImage(image);

                    for(String s: onlineUsers){
                        if(s.contains(user.getUsername())) online.setImage(new Image(getClass().getResource("/pictures/online.png").toString(), 50, 50, true, true));
                    }

                    hBox.getChildren().addAll(pictureImageView, username,online);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(hBox);
                }
            }
        };
        cell.setId("userCellId");
        return cell;
    }
}