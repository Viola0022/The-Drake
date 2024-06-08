package thedrake.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
        @FXML
        private Button closeButton;

        public void closeButton(ActionEvent actionEvent)
        {
            ((Stage) closeButton.getScene().getWindow()).close();
        }

        public void initialize(URL location, ResourceBundle resources) {

        }
}
