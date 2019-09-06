package controller;

import connections.Connect;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StartController {
    private Callback<Class<?>, Object> callback;
    private Connect user;

    @FXML
    private TextField loginLabel;
    @FXML
    private PasswordField passwordLabel;
    @FXML
    private CheckBox checkIncognito;

    public StartController() {
        callback = new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> controllerClass) {
                if (controllerClass == ChatController.class) {
                    ChatController controller = new ChatController();
                    controller.setUser(user);
                    controller.setIncognito(!checkIncognito.isSelected());
                    return controller;
                } else {
                    try {
                        return controllerClass.newInstance();
                    } catch (Exception exc) {
                        throw new RuntimeException(exc); // just bail
                    }
                }
            }
        };
    }

    public void onLogin(ActionEvent actionEvent) throws Exception {

        user = new Connect(loginLabel.getText(), passwordLabel.getText());
        //System.out.println(user);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fronts/chat.fxml"));

        fxmlLoader.setControllerFactory(callback);
        fxmlLoader.load();

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/main.css");
        stage.setScene(scene);
        stage.setMinHeight(560);
        stage.setMinWidth(550);

        stage.show();

        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
