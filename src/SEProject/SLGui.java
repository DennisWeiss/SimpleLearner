package SEProject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Marcel
 */
public class SLGui extends Application {

    int count = 0;
    @Override
    public void start(Stage primaryStage) {

        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(10);

        TextField name = new TextField();
        name.setPromptText("Name");
        TextField password = new TextField();
        password.setPromptText("Passwort");
        Button login = new Button("Anmelden");

        GridPane.setColumnSpan(name, GridPane.REMAINING);
        GridPane.setColumnSpan(password, GridPane.REMAINING);
        gp.add(name, 0, 0);
        gp.add(password, 0, 1);
        gp.add(login, 0, 2);

        login.setOnAction((ActionEvent ae) -> {
            gp.getChildren().clear();

            BorderPane bp = new BorderPane();
            for (int i = 0; i < 5; i++) {
                Button b = new Button("Fach" + i);
                b.setPrefSize(100, 40); //Länge, Breite
                gp.add(b, 0, i);
                count = i + 1;
            }
            Button bAdd = new Button("Add");
            gp.add(bAdd, 0, count);
            bAdd.setOnAction((ActionEvent ae2) -> {
               gp.add(new Button("Fach"), 0, count);
               count++;
            });
        });

        Scene scene = new Scene(gp);

        primaryStage.setScene(scene);
        primaryStage.setTitle("SimpleLearner");
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
