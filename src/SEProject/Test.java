package SEProject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;

/**
 *
 * @author stefan
 */
public class Test extends Application {

    SqlLogik sql;

    public Test() {
        sql = new SqlLogik();
    }

    @Override
    public void start(Stage primaryStage) {
        // lade CSS-Datei(-en)  //Z.511
        // CSS-Referenz für Panes: https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html
        loadStyleSheets();
        // initialisiere AnmeldungPane
        buildAnmeldPane();
        setBtnAnmeldung(TestStage);
        // initialisere MeldungPane
        setMeldungPane(anmeldPane);
        // initialisiere AufgabenPane
        setBtnBestätigen();
        setBtnNächsteAufgabe(TestStage);
        buildAufgabenPane();
        //initialisiere HauptPane  
        setBtnNeuesElement();
        //setHauptTop();
        //setHauptLeft(); setHauptRight(); setHauptBottom(); //zurzeit nicht beötigt
        setHauptCenter();
        //changeHauptCenter(getAufgabenPane()); //Funktion auskommentiert (Z.214)
        //setScene(getHauptPane());
        buildHauptPane();

        primaryStage = TestStage;
        primaryStage.setTitle("SimpleTest - Anmeldung");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

// AnmeldungPane
    TextField AnmeldungName = new TextField();
    PasswordField AnmeldungPasswort = new PasswordField();
    Button btnAnmeld = new Button("Einloggen");
    BorderPane anmeldPane = new BorderPane();
    GridPane eingabeCenter = new GridPane();
    boolean istStudent;
    String currentUser = null;

    private void buildAnmeldPane() {
        BorderPane temp = new BorderPane();
        temp.setId("anmeldPane");

        //eingabeDisplay.setPrefHeight(150);
        //eingabeDisplay.setPrefWidth(400);
        BorderPane eingabeTop = new BorderPane();
        eingabeTop.setId("eingabeTop");
        eingabeTop.setPrefHeight(20);
        eingabeTop.setLeft(new Label("Anmeldung"));

        eingabeCenter = new GridPane();
        eingabeCenter.setHgap(5.0);
        eingabeCenter.setVgap(5.0);
        eingabeCenter.add(new Label("Name: "), 0, 0);
        eingabeCenter.add(this.AnmeldungName, 1, 0);
        eingabeCenter.add(new Label("Passwort: "), 0, 1);
        eingabeCenter.add(this.AnmeldungPasswort, 1, 1);
        eingabeCenter.add(this.btnAnmeld, 1, 2);

        temp.setTop(eingabeTop);
        temp.setCenter(eingabeCenter);
        anmeldPane = temp;
    }

    void setBtnAnmeldung(Stage tempStage) {
        btnAnmeld.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("------------------------------");
                System.out.println("Benutzer wird eingeloggt");
                System.out.println("    Name: " + AnmeldungName.getText());
                System.out.println("    Passwort: " + AnmeldungPasswort.getText());
                System.out.println("------------------------------");

                boolean[] check = new boolean[2];
                try {
                    check = sql.checkLogin(AnmeldungName.getText(), AnmeldungPasswort.getText());
                    if (check[0] == true) {
                        if (check[1] == true) {//LehrerPane erstellen
                            istStudent = check[1];
                        } else if (check[1] == false) { //SchülerPane erstellen
                            istStudent = check[1];
                        }
                        currentUser = sql.getCurrentUser();
                        System.out.println(currentUser);
                        setScene(getHauptPane());
                        tempStage.setScene(scene);
                        tempStage.setTitle("SimpleLearner - Aufgabenverzeichnis");
                        tempStage.show();
                    } else {
                        System.out.println("Falsche Eingabe");
                    }
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }

            }
        });
    }

    String getName() {
        return AnmeldungName.getText();
    }

    String getPasswort() {
        return AnmeldungPasswort.getText();
    }

    BorderPane getAnmeldPane() {
        return anmeldPane;
    }

//meldungPane
    FlowPane meldungPane = new FlowPane();

    void setMeldungPane() {
        meldungPane.setId("meldungPane");
        meldungPane.setAlignment(Pos.CENTER);
        meldungPane.getChildren().add(new BorderPane());
    }

    void setMeldungPane(BorderPane temp) {
        meldungPane.setId("meldungPane");
        meldungPane.setAlignment(Pos.CENTER);
        meldungPane.getChildren().setAll(temp);
    }

    FlowPane changeMeldungPane(BorderPane temp) {
        setMeldungPane(temp);
        return meldungPane;
    }

// HauptPane
    BorderPane hauptTop = new BorderPane();
    Label label = new Label("Test");
    Pane hauptLeft = new Pane();
    BorderPane hauptRight = new BorderPane();
    BorderPane hauptBottom = new BorderPane();
    BorderPane hauptCenter = new BorderPane();
    VBox centerListe = new VBox();
    Button btnNeuesElement = new Button("Neues Element");
    BorderPane HauptPane = new BorderPane();
    StackPane root = new StackPane();
    boolean isStudent = false;

    // Funktion auskommentiert
    void setHauptTop() {
        hauptTop.setId("hauptTop");
        //hauptTop.setStyle("-fx-background-color: rgb(100,100,100);");
        hauptTop.setPrefHeight(30);
        Button temp = new Button("Schüler");
        hauptTop.setCenter(label);
        hauptTop.setRight(temp);
        temp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!isStudent) {
                    temp.setText("Schüler");
                    isStudent = true;
                } else {
                    temp.setText("Lehrer");
                    isStudent = false;
                }
            }
        });
    }

    // Funktion auskommentiert
    void setHauptLeft() {
        hauptLeft.setId("hauptLeft");
        hauptLeft.setPrefWidth(50);
    }

    // Funktion auskommentiert
    void setHauptRight() {
        hauptRight.setId("hauptRight");
        hauptRight.setPrefWidth(50);
    }

    // Funktion auskommentiert
    void setHauptBottom() {
        hauptBottom.setId("hauptBottom");
        hauptBottom.setStyle("-fx-background-color: rgb(100,100,100);");
        hauptBottom.setPrefHeight(50);
    }

    void setHauptCenter() {
        hauptCenter.setId("hauptCenter");
        centerListe.setSpacing(5);
        //centerListe.getChildren().add(btnNeuesElement);
        hauptCenter.setCenter(centerListe);
    }

    void setBtnNeuesElement() {
        btnNeuesElement.setId("btnNeuesElement");
        btnNeuesElement.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //DislogFenster für Namenseingabe
                //

                /*
                int temp = centerListe.getChildren().size();
                // VBox leeren
                centerListe.getChildren().setAll(); 
                // VBox füllen
                for (int i = 0; i < temp; i++) {
                    System.out.println("Neues Element " + i + " wird hinzugefügt");
                    centerListe.getChildren().add(new VerzeichnisButton("String " + i, i).getVerzeichnisButton()); //VBox mit Buttons füllen
                }
                 */
                centerListe.getChildren().add(btnNeuesElement); //btnNeuesElement anhängen
            }
        });
    }

    //void changeHauptCenter(BorderPane temp){
    //    hauptCenter = temp;
    //};
    void buildHauptPane() {
        HauptPane.setTop(hauptTop);
        HauptPane.setLeft(hauptLeft);
        HauptPane.setRight(hauptRight);
        HauptPane.setBottom(hauptBottom);
        HauptPane.setCenter(hauptCenter);

        fillVerzeich();
    }

    void fillVerzeich() { //Parameterübergabe für Anzahl der Aufgaben
        // -> Anzahl aus Datenbank
        // Liste leeren (Liste soll sich neu füllen, nicht erweitern)
        centerListe.getChildren().setAll();

        try {
            sql.loadBlöcke();
        } catch (SQLException exc) {
            System.out.println(exc.getMessage());
        }
        for (int i = 0; i < sql.aufgabenblöcke.size(); i++) {
            centerListe.getChildren().add(new VerzeichnisButton(sql.aufgabenblöcke.get(i), i).getVerzeichnisButton()); // ersetze ("Test "+i) mit Aufgabenname
        }
    }

    BorderPane getHauptPane() {
        return HauptPane;
    }

    class VerzeichnisButton {

        String btnLabel;
        Button btnName;
        Button btnLöschen;
        int aufgabenNummer;

        VerzeichnisButton(String input, int nummer) {
            btnLabel = input;
            aufgabenNummer = nummer;
            System.out.println(aufgabenNummer + " früher");
            btnName = new Button(input);
            btnName.getStyleClass().add("VerzeichnisButton");
            System.out.println(btnName.getStyleClass());
            btnName.setPrefWidth(scene.getWidth());
            btnName.setMinWidth(hauptCenter.getWidth()/*-btnLöschen.getPrefWidth()*/);
            setBtnName(TestStage);

            //btnLöschen = new Button("Löschen");
            //btnLöschen.setStyle("-fx-background-color:rgb(255,50,50)");
            //btnLöschen.setPrefWidth(100);

            /*
            btnLöschen.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e){
                    //
                    
                    //
                }
            });
             */
        }

        void setBtnName(Stage tempStage) {
            btnName.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Aufgabeneinheit wird geöffnet:");
                    System.out.println("    gewählte Einheit: " + btnLabel);
                    System.out.println("------------------------------");

                    aufgabenNummer = 0;
                    try {
                        sql.loadFragen(btnLabel);
                    } catch (SQLException exc) {
                        System.out.println(exc.getMessage());
                    }
                    setBlockPar(btnLabel);
                    setFragePar(sql.fragen.indexOf(sql.fragen.get(aufgabenNummer)));
                    fillAntwortAuswahl(btnLabel, sql.fragen.get(aufgabenNummer));
                    aufgabeText.setText(sql.fragen.get(aufgabenNummer));

                    //buildAufgabenPane();// Parameter
                    setScene(getAufgabenPane());
                    tempStage.setScene(scene);
                    tempStage.setTitle("SimpleLearner - Aufgabe");
                    tempStage.show();

                }
            });
        }

        BorderPane getVerzeichnisButton() {
            BorderPane temp = new BorderPane();
            temp.setLeft(btnName);
            /*
            if(isStudent){
                temp.setLeft(btnName);
            }else{
                temp.setLeft(btnName);
                temp.setRight(btnLöschen);
            }
             */
            return temp;
        }
    }

//AufgabenPane
    String blockPar = null;
    int nummerFragePar = 0;

    BorderPane AufgabenPane = new BorderPane();
    BorderPane tempPane = new BorderPane(); // Ausgabe des Aufgabentextes
    Label aufgabeText = new Label();
    BorderPane AntwortPane = new BorderPane();
    VBox antwortAuswahl = new VBox();
    ToggleGroup AntwortGroup = new ToggleGroup();
    Button btnNeueAntwort = new Button("Neue Aufgabe");
    GridPane navigator = new GridPane();
    //Button btnZurück = new Button("Zurück");
    //Button btnNächste = new Button("Nächste");
    Label auswertungAntwort = new Label();
    Button btnBestätigen = new Button("Bestätigen");
    Button btnNächsteAufgabe = new Button("Nächste");

    void setBlockPar(String par) {
        blockPar = par;
    }

    void setFragePar(int par) {
        nummerFragePar = par;
    }

    void buildAufgabenPane() {
        tempPane.setId("aufgabePane");
        AntwortPane.setId("antwortPane");

        tempPane.setPrefWidth(scene.getWidth() - 150);

        aufgabeText.setWrapText(true);
        aufgabeText.setFont(Font.font(16));
        aufgabeText.setText("ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        tempPane.setCenter(aufgabeText);

        AufgabenPane.setLeft(tempPane);
        AufgabenPane.setRight(AntwortPane);

        //AntwortPane.setStyle("-fx-background-color:rgb(200,200,200);");
        AntwortPane.setPrefWidth(150);
        //fillAntwortAuswahl();
        antwortAuswahl.setSpacing(5);

        //antwortAuswahl.getChildren().add(btnNeueAntwort);
        AntwortPane.setAlignment(antwortAuswahl, Pos.CENTER);
        AntwortPane.setCenter(antwortAuswahl);
        //setBtnNeueAufgabe();    //Funktion auskommentiert (Z.338)
        //antwortAuswahl.setBottom(btnNeueAntwort);
        AntwortPane.setAlignment(navigator, Pos.CENTER);
        AntwortPane.setBottom(navigator);
        auswertungAntwort.setFont(Font.font(20));
        navigator.add(auswertungAntwort, 0, 0);
        navigator.add(btnBestätigen, 0, 1);
    }

    void fillAntwortAuswahl(String block, String frage) {
        AntwortGroup = new ToggleGroup();
        antwortAuswahl.getChildren().setAll();
        try {
            sql.loadAntworten(block, frage);
        } catch (SQLException exc) {
            System.out.println(exc.getMessage());
        }
        for (int i = 0; i < sql.antwortenTemp.size()/*anzahl der Antworten*/; i++) {
            // hole Aufgabenname der i.ten Aufgabeneinheit
            // Übergebe AufgabenName
            antwortAuswahl.getChildren().add(new btnAntwort(sql.antwortenTemp.get(i)).getBtnAntwort());
        }
    }

    void setBtnBestätigen() {//wertet den bewählte Button aus und gibt diese aus
        btnBestätigen.setPrefWidth(75);
        btnBestätigen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String antwort = AntwortGroup.getSelectedToggle().getUserData().toString();
                System.out.println("Auswahl wurde bestätigt");
                System.out.println("    Gewählte Antwort: " + antwort);

                //checkAntwort(antwort);
                System.out.println(blockPar + sql.fragen.get(nummerFragePar) + antwort);
                System.out.println(nummerFragePar);
                try {
                    if (sql.checkAntwort(blockPar, sql.fragen.get(nummerFragePar), antwort) == true) {
                        System.out.println("richtig");
                        auswertungAntwort.setText("richtig");
                    } else {
                        System.out.println("falsch");
                        auswertungAntwort.setText("falsch");
                    }
                    System.out.println("------------------------------");
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
                //ersetze "Bestätigen"-Button mit "Nächste"-Button
                if (nummerFragePar < sql.fragen.size() - 1) {
                    navigator.add(btnNächsteAufgabe, 0, 1);
                }
                /*else {
                    Stage stage = new Stage();
                    Label label = new Label("Sie haben das Quiz vollständig bearbeitet.");
                    Button b = new Button("Zurück zu den Aufgaben");
                    VBox vb = new VBox();
                    vb.getChildren().addAll(label, b);
                    Scene scene = new Scene(vb);
                    stage.setScene(scene);
                    stage.show();
                }*/

            }
        });
    }

    void setBtnNächsteAufgabe(Stage tempStage) {//öffnet nächste Aufgabe
        btnNächsteAufgabe.setPrefWidth(75);
        btnNächsteAufgabe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Nächste Aufgabe wird geöffnet");
                System.out.println("    Neue Aufgabe: ");
                System.out.println("------------------------------");

                // exception durch erneutes Einfügen von btnBestätigen
                // -> btnBestätigen löschen und erneut einfügen
                navigator.getChildren().remove(1, 3); // btnBestätigen und btnNächsteAufgabe löschen
                navigator.add(btnBestätigen, 0, 1); // btnBestätigen ein
                System.out.println("    >Bestätigen-Button eingefügt");
                aufgabeText.setText(sql.fragen.get(nummerFragePar + 1));
                nummerFragePar++;
                System.out.println("    >AufgabenText geändert");
                fillAntwortAuswahl(blockPar, sql.fragen.get(nummerFragePar));
                auswertungAntwort.setText("");
                System.out.println("    >AntwortAuswahl erneuert");
                System.out.println("------------------------------");

                tempStage.show();
            }
        });
    }

    BorderPane getAufgabenPane() {
        return AufgabenPane;
    }

    class btnAntwort {

        RadioButton btn;
        String btnLabel;

        btnAntwort(String input) {
            btnLabel = input;
            btn = new RadioButton(btnLabel);
            btn.setUserData(btnLabel);
            btn.setToggleGroup(AntwortGroup);
        }

        void setBtnAntwort() {
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println(btnLabel);
                }
            });
        }

        RadioButton getBtnAntwort() {
            return btn;
        }
    }
// Scene    
    // getHauptPane();
    // getAufgabenPane();
    Scene scene = new Scene(changeMeldungPane(getAnmeldPane()), 600, 600);

    void loadStyleSheets() {
        scene.getStylesheets().add("SEProject/test.css");
    }

    void setScene(StackPane temp) {
        scene = new Scene(temp, 600, 600);
    }

    void setScene(BorderPane temp) {
        scene = new Scene(temp, 600, 600);
        loadStyleSheets();
    }

    Stage TestStage = new Stage();

    void setPrimaryStage() {
        TestStage.setScene(scene);
    }
}
