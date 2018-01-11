package SEProject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;

/**
 *
 * @author stefan
 */
public class SimpleLearnerGUI extends Application {

    SqlLogik sql;

    public SimpleLearnerGUI() {
        sql = new SqlLogik();
    }

    @Override
    public void start(Stage primaryStage) {
        // lade CSS-Datei(-en)  //Z.511
        loadStyleSheets();
        // initialisiere AnmeldungPane
        buildLoginPane();
        setBtnLogin(TestStage);
        // initialisere MeldungPane
        // initialisiere AufgabenPane
        setBtnConfirmAnswer();
        setBtnNextTask(TestStage);
        setBtnConfirmQuiz(TestStage);
        buildTaskPane();
        setBtnBlockName();
        setBtnBack(TestStage);
        setBtnNewQuestionText();
        setVBoxTaskContainer();
        setBtnLoadAnswerChoice();
        //initialisiere HauptPane  
        setTopMenu();
        //setHauptLeft(); setHauptRight(); setHauptBottom(); //zurzeit nicht beötigt
        setMainContainer();
        //changeHauptCenter(getAufgabenPane()); //Funktion auskommentiert (Z.214)
        //setScene(getHauptPane());
        buildMainPane();

        scene.setRoot(getLoginPane());

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
    TextField loginName = new TextField();
    PasswordField loginPassword = new PasswordField();
    Button btnLogin = new Button("Einloggen");
    BorderPane loginPane = new BorderPane();
    BorderPane loginWindow = new BorderPane();
    GridPane loginContainer = new GridPane();
    boolean isTeacher;
    String currentUser = null;

    String gName = null;

    String getGName() {
        return gName;
    }

    void setGName(String gName) {
        this.gName = gName;
    }

    boolean isTeacher() {
        return isTeacher;
    }

    private void buildLoginPane() {
        BorderPane tempMain = new BorderPane();

        BorderPane temp = new BorderPane();

        BorderPane loginTop = new BorderPane();
        loginTop.setPrefHeight(20);
        loginTop.setLeft(new Label("Anmeldung"));

        loginContainer = new GridPane();
        loginContainer.setHgap(5.0);
        loginContainer.setVgap(5.0);
        loginContainer.add(new Label("Name: "), 0, 0);
        loginContainer.add(this.loginName, 1, 0);
        loginContainer.add(new Label("Passwort: "), 0, 1);
        loginContainer.add(this.loginPassword, 1, 1);
        loginContainer.add(this.btnLogin, 1, 2);

        temp.setTop(loginTop);
        temp.setCenter(loginContainer);

        Pane a1 = new Pane();
        a1.setPrefHeight((scene.getHeight() /* -btnAbmelden.getHeight() */) / 2.5);

        Pane a2 = new Pane();
        a2.setPrefHeight((scene.getHeight() /* -btnAbmelden.getHeight() */) / 2.5);

        Pane b1 = new Pane();
        b1.setPrefWidth(150);

        Pane b2 = new Pane();
        b2.setPrefWidth(150);

        tempMain.setTop(a1);
        tempMain.setBottom(a2);
        tempMain.setLeft(b1);
        tempMain.setRight(b2);
        tempMain.setCenter(temp);
        loginPane = tempMain;
    }

    void setBtnLogin(Stage tempStage) {
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("------------------------------");
                System.out.println("Benutzer wird eingeloggt");
                System.out.println("    Name: " + loginName.getText());
                System.out.println("    Passwort: " + loginPassword.getText());
                //System.out.println("------------------------------");

                boolean[] check = new boolean[2];
                try {
                    check = sql.checkLogin(loginName.getText(), loginPassword.getText());
                    System.out.println(check[1]);
                    if (check[0] == true) {
                        if (check[1] == true) {//LehrerPane erstellen
                            isTeacher = check[1];
                            System.out.println("Anmeldung Lehrer >>> true");
                        } else if (check[1] == false) { //SchülerPane erstellen
                            isTeacher = check[1];
                            System.out.println("Anmeldung Lehrer >>> false");
                        }

                        currentUser = sql.getCurrentUser();
                        System.out.println(currentUser);

                        System.out.println("    Lehrer: " + isTeacher());
                        hString = "Kategorie";
                        scene.setRoot(getHauptPane());
                        tempStage.setScene(scene);
                        tempStage.setTitle("SimpleLearner - Kategorie");
                        tempStage.show();
                    } else {
                        System.out.println("Falsche Eingabe");
                    }
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }

                fillKategorie();

                System.out.println("------------------------------");

            }
        });
    }

    String getName() {
        return loginName.getText();
    }

    String getPasswort() {
        return loginPassword.getText();
    }

    BorderPane getLoginPane() {
        return loginPane;
    }

// HauptPane
    Label label = new Label("");
    TextField filter = new TextField();
    BorderPane hauptTop = new BorderPane();
    Pane hauptLeft = new Pane();
    BorderPane hauptRight = new BorderPane();
    BorderPane hauptBottom = new BorderPane();
    BorderPane hauptCenter = new BorderPane();
    VBox centerListe = new VBox();
    Button btnNeuesElement = new Button("Neues Element");
    BorderPane HauptPane = new BorderPane();

    //SpeicherStrings
    String kategorieString = null;
    String modulString = null;
    String hString = null;

    void setTopMenu() {
        hauptTop.setId("hauptTop");
        filter.setPromptText("Filter");
        filter.setFocusTraversable(false);
        filter.setOnKeyReleased(e -> {
            if (hString.equals("Kategorie")) {
                fillKategorie();
            } else if (hString.equals("Modul")) {
                fillModul(kategorieString);
            } else if (hString.equals("Verzeichnis")) {
                fillVerzeich();
            }
        });
        Button btnAbmelden = new Button("Abmelden");
        Button btnZurueck = new Button("Zurueck");
        btnZurueck.setId("hZurueck");
        HBox hbButtons = new HBox();
        hbButtons.getChildren().addAll(btnZurueck, btnAbmelden);
        hauptTop.setLeft(filter);
        hauptTop.setCenter(label);
        hauptTop.setRight(hbButtons);
        btnAbmelden.setOnAction((ActionEvent e) -> {
            //DislogFenster für Namenseingabe
            //-> Erstellung einer neuen Aufgabe
            System.out.println("-----------------");
            System.out.println("Abmeldung startet");
            System.out.println("-----------------");

            scene.setRoot(getLoginPane());

            Stage tempStage = TestStage;
            tempStage.setTitle("SimpleTest - Anmeldung");
            tempStage.setScene(scene);
            tempStage.show();
        });
        btnZurueck.setOnAction((ActionEvent e) -> {
            if (hString.equals("Modul")) {
                fillKategorie();
            } else if (hString.equals("Verzeichnis")) {
                fillModul(kategorieString);
                hString = "Modul";
            } else {
                System.out.println("ELSE " + hString);
            }

        });
    }

    /*
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
     */
    void setMainContainer() {
        hauptCenter.setId("hauptCenter");
        centerListe.setSpacing(5);
        hauptCenter.setCenter(centerListe);
    }

    class BtnNeuesElement {

        Button btnNeuesElement;

        BtnNeuesElement(String input) {
            btnNeuesElement = new Button();
            setBtnNeuesElement(input);
        }

        void setBtnNeuesElement(String input) {
            btnNeuesElement.setPrefWidth(scene.getWidth());
            btnNeuesElement.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //DislogFenster für Namenseingabe
                    //-> Erstellung einer neuen Aufgabe

                    System.out.println(scene.getWidth());

                    if (input.equals("Kategorie")) {
                        fillKategorie();
                        System.out.println(">>> " + input);
                        btnNeuesElement.setText("Neue Kategorie");
                    } else if (input.equals("Modul")) {
                        fillModul(kategorieString);
                        System.out.println(">>> " + input);
                        btnNeuesElement.setText("Neues Modul");
                    } else if (input.equals("Verzeich")) {
                        fillVerzeich();
                        System.out.println(">>> " + input);
                        btnNeuesElement.setText("Neues Verzeich");
                    }

                    //fillVerzeich();
                    System.out.println("Neues Element wird erstellt");
                }
            });
        }

        Button getBtnNeuesElement() {
            return btnNeuesElement;
        }
    }

    void buildMainPane() {
        HauptPane.setTop(hauptTop);
        HauptPane.setLeft(hauptLeft);
        HauptPane.setRight(hauptRight);
        HauptPane.setBottom(hauptBottom);
        HauptPane.setCenter(hauptCenter);

        fillKategorie();
    }

    void fillKategorie() {
        //tempStage.setTitle("SimpleLearner - Kategorienverzeichnis");

        // Liste leeren
        centerListe.getChildren().setAll();

        // Liste füllen
        if (isTeacher) {
            if (filter.getText().isEmpty()) {
                try {
                    sql.loadFaecher(getName());
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
            } else {
                try {
                    sql.loadFilteredFaecher(getName(), filter.getText());
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
            }
            for (int i = 0; i < sql.faecher.size(); i++) {
                centerListe.getChildren().add(new KategorieButton(sql.faecher.get(i), i).getKategorieButton());
            }
        } else {
            if (this.filter.getText().isEmpty()) {
                try {
                    sql.loadFaecher();
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
            } else {
                try {
                    sql.loadFilteredFaecher(filter.getText());
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
            }
            for (int i = 0; i < sql.faecher.size(); i++) {
                centerListe.getChildren().add(new KategorieButton(sql.faecher.get(i), i).getKategorieButton());
            }
        }

        //centerListe.getChildren().add(new KategorieButton("Kategorie 0", 0).getKategorieButton());
        centerListe.getChildren().add(new BtnNeuesElement("Kategorie").getBtnNeuesElement());
    }

    void fillVerzeich() { //Parameterübergabe für Anzahl der Aufgaben
        // -> Anzahl aus Datenbank
        // Liste leeren (Liste soll sich neu füllen, nicht erweitern)

        centerListe.getChildren().setAll();

        if (isTeacher) {
            if (filter.getText().isEmpty()) {
                try {
                    sql.loadBloecke(getGName(), getName());
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
            } else {
                try {
                    sql.loadFilteredBloecke(getGName(), getName(), filter.getText());
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
            }
            for (int i = 0; i < sql.aufgabenbloecke.size(); i++) {
                centerListe.getChildren().add(new VerzeichnisButton(sql.aufgabenbloecke.get(i), i).getVerzeichnisButton()); // ersetze ("SimpleLearnerGUI "+i) mit Aufgabenname
            }
        } else {
            if (filter.getText().isEmpty()) {
                try {
                    sql.loadBloecke(getGName());
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
            } else{
                try {
                    sql.loadFilteredBloecke(getGName(), filter.getText());
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
            }
            for (int i = 0; i < sql.aufgabenbloecke.size(); i++) {
                centerListe.getChildren().add(new VerzeichnisButton(sql.aufgabenbloecke.get(i), i).getVerzeichnisButton()); // ersetze ("SimpleLearnerGUI "+i) mit Aufgabenname
            }
        }
        centerListe.getChildren().add(new BtnNeuesElement("Verzeich").getBtnNeuesElement());

    }

    void fillModul(String kategorieString) { //Parameterübergabe für Anzahl der Aufgaben
        // -> Anzahl aus Datenbank
        // Liste leeren (Liste soll sich neu füllen, nicht erweitern)

        centerListe.getChildren().setAll();

        if (filter.getText().isEmpty()) {
            try {
                sql.loadKategorien(kategorieString);
                System.out.println(getGName() + "adsgonj " + kategorieString);
            } catch (SQLException exc) {
                System.out.println(exc.getMessage());
            }
        } else {
            try {
                sql.loadFilteredKategorien(kategorieString, filter.getText());
                System.out.println(getGName() + "adsgonj " + kategorieString);
            } catch (SQLException exc) {
                System.out.println(exc.getMessage());
            }
        }
        for (int i = 0; i < sql.kategorien.size(); i++) {
            System.out.println("sdgvhsklgga" + sql.kategorien.get(i) + " " + i);
            centerListe.getChildren().add(new ModulButton(sql.kategorien.get(i), i).getModulButton()); // ersetze ("SimpleLearnerGUI "+i) mit Aufgabenname
        }
        //centerListe.getChildren().add(new ModulButton("Modul 0", 0).getModulButton());
        //centerListe.getChildren().add(new ModulButton("Modul 1", 1).getModulButton());
        //centerListe.getChildren().add(new ModulButton("Modul 2", 2).getModulButton());

        centerListe.getChildren().add(new BtnNeuesElement("Modul").getBtnNeuesElement());
    }

    BorderPane getHauptPane() {
        return HauptPane;

    }

// Verzeichnis-Element
//
//
//
    class VerzeichnisButton {

        String btnLabel;
        Button btnName;
        Button btnLoeschen;
        int aufgabenNummer;

        VerzeichnisButton(String input, int nummer) {
            System.out.println("Verzeichnis: Lehrer angemeldet? " + isTeacher());
            btnLabel = input;
            aufgabenNummer = nummer;
            System.out.println("Aufgabennummer " + aufgabenNummer);
            btnName = new Button(input);
            btnName.getStyleClass().add("VerzeichnisButton");
            System.out.println(btnName.getStyleClass());
            btnName.setPrefWidth(scene.getWidth());
            btnName.setMinWidth(hauptCenter.getWidth()/*-btnLöschen.getPrefWidth()*/);
            setBtnName(TestStage);

            btnLoeschen = new Button("Loeschen");
            btnLoeschen.setStyle("-fx-background-color:rgb(255,50,50)");
            //btnLoeschen.setPrefWidth(100);

            btnLoeschen.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //
                    System.out.println("Element \"" + btnLabel + "\" wird gelöscht");
                    //
                }
            });

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
                    blockName.setText(btnLabel);
                    fillAntwortAuswahl(btnLabel, sql.fragen.get(aufgabenNummer));
                    aufgabeText.setText(sql.fragen.get(aufgabenNummer));

                    //buildAufgabenPane();// Parameter
                    scene.setRoot(getAufgabenPane());
                    tempStage.setScene(scene);
                    tempStage.setTitle("SimpleLearner - Aufgabe-Nr. " + "***");
                    tempStage.show();

                }
            });
        }

        BorderPane getVerzeichnisButton() {
            BorderPane temp = new BorderPane();
            //temp.setLeft(btnName);
            if (isTeacher()) {
                btnName.setPrefWidth(scene.getWidth() - 100);
                temp.setLeft(btnName);
                btnLoeschen.setPrefWidth(100);
                temp.setRight(btnLoeschen);
            } else {
                btnName.setPrefWidth(scene.getWidth());
                temp.setLeft(btnName);
            }

            //temp.setLeft(btnName);
            /*
            if(isStudent){
                btnAbmelden.setLeft(btnName);
            }else{
                btnAbmelden.setLeft(btnName);
                btnAbmelden.setRight(btnLöschen);
            }
             */
            return temp;
        }
    }

//Kategorie-Element
//
//
//
    class KategorieButton {

        String btnLabel;
        Button btnName;
        Button btnLoeschen;
        int aufgabenNummer;

        KategorieButton(String input, int nummer) {
            System.out.println("Modul: Lehrer angemeldet? " + isTeacher());
            btnLabel = input;
            aufgabenNummer = nummer;
            System.out.println("Kategorie " + aufgabenNummer);
            btnName = new Button(input);
            btnName.getStyleClass().add("KategorieButton");
            System.out.println(btnName.getStyleClass());
            setBtnName(TestStage);

            btnLoeschen = new Button("Loeschen");
            btnLoeschen.setStyle("-fx-background-color:rgb(255,50,50)");
            //btnLoeschen.setPrefWidth(100);
            setBtnLoeschen(TestStage);
        }

        void setBtnLoeschen(Stage tempStage) {
            btnLoeschen.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //
                    System.out.println("Element \"" + btnLabel + "\" wird gelöscht");
                    //
                }
            });
        }

        void setBtnName(Stage tempStage) {
            btnName.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Modul wird geöffnet:");
                    System.out.println("    gewählte Einheit: " + btnLabel);
                    System.out.println("------------------------------");

                    aufgabenNummer = 0;
                    setGName(btnLabel);
                    label.setText(btnLabel);
                    kategorieString = btnLabel;
                    hString = "Modul";
                    fillModul(kategorieString);
                    tempStage.setTitle("SimpleLearner - Modul - " + "***");
                    tempStage.show();
                }
            });
        }

        BorderPane getKategorieButton() {
            BorderPane temp = new BorderPane();
            //temp.setLeft(btnName);

            /*if (isTeacher()) {
                btnName.setPrefWidth(scene.getWidth() - 100);
                btnAbmelden.setLeft(btnName);
                btnLoeschen.setPrefWidth(100);
                btnAbmelden.setRight(btnLoeschen);
            } else {*/
            btnName.setPrefWidth(scene.getWidth());
            temp.setLeft(btnName);
            //}
            return temp;
        }
    }

// Kategorie-Element    
//
//
//
    class ModulButton {

        String btnLabel;
        Button btnName;
        Button btnLöschen;
        int aufgabenNummer;

        ModulButton(String input, int nummer) {
            btnLabel = input;
            aufgabenNummer = nummer;
            System.out.println("Modulummer " + aufgabenNummer);
            btnName = new Button(input);
            btnName.getStyleClass().add("ModulButton");
            System.out.println(btnName.getStyleClass());
            btnName.setPrefWidth(scene.getWidth());
            btnName.setMinWidth(hauptCenter.getWidth()/*-btnLöschen.getPrefWidth()*/);
            setBtnName(TestStage);

            //btnLöschen = new Button("Loeschen");
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

        String getBtnName() {
            return btnName.getText();
        }

        void setBtnName(Stage tempStage) {
            btnName.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Modul wird geöffnet:");
                    System.out.println("    gewählte Einheit: " + btnLabel);
                    System.out.println("------------------------------");

                    aufgabenNummer = 0;
                    /*
                    try {
                        sql.loadFragen(btnLabel);
                    } catch (SQLException exc) {
                        System.out.println(exc.getMessage());
                    }

                    
                    setBlockPar(btnLabel);
                    setFragePar(sql.fragen.indexOf(sql.fragen.get(aufgabenNummer))); //aufgabenNummer -> modulNummer
                    fillAntwortAuswahl(btnLabel, sql.fragen.get(aufgabenNummer));
                    aufgabeText.setText(sql.fragen.get(aufgabenNummer));
                     */

                    //buildAufgabenPane();// Parameter
                    setGName(btnLabel);
                    label.setText(btnLabel);
                    modulString = btnLabel;
                    hString = "Verzeichnis";
                    fillVerzeich();
                    System.out.println("label : " + getGName());
                    //setScene(getAufgabenPane());
                    //tempStage.setScene(scene);
                    tempStage.setTitle("SimpleLearner - Aufgabe");
                    tempStage.show();
                }
            });
        }

        BorderPane getModulButton() {
            BorderPane temp = new BorderPane();
            temp.setLeft(btnName);
            /*
            if(isStudent){
                btnAbmelden.setLeft(btnName);
            }else{
                btnAbmelden.setLeft(btnName);
                btnAbmelden.setRight(btnLöschen);
            }
             */
            return temp;
        }
    }

//AufgabenPane
//
//
//
    String blockPar = null;
    int nummerFragePar = 0;

    BorderPane AufgabenKopf = new BorderPane();
    HBox hbBlockName = new HBox();
    Label blockName = new Label("");
    Button btnBlockName = new Button("Blocknamen ändern");
    Button btnBlockZurueck = new Button("Zurueck");

    BorderPane AufgabenPane = new BorderPane();
    BorderPane tempPane = new BorderPane(); // Ausgabe des Aufgabentextes
    Label aufgabeText = new Label();
    Button btnAufgabeText = new Button("Aufgabentext hier");
    VBox vbAufgabeText = new VBox();

    BorderPane AntwortPane = new BorderPane();
    VBox antwortAuswahl = new VBox();
    Button btnAntwortenAuswahl = new Button("Antwortenauswahl hier");
    ToggleGroup AntwortGroup = new ToggleGroup();
    Button btnNeueAntwort = new Button("Neue Aufgabe");
    GridPane navigator = new GridPane();
    //Button btnZurück = new Button("Zurück");
    //Button btnNächste = new Button("Nächste");
    Label auswertungAntwort = new Label();
    Button btnBestaetigen = new Button("Bestätigen");
    Button btnNaechsteAufgabe = new Button("Nächste");
    Button btnBeenden = new Button("Beenden");

    void setVBoxTaskContainer() {
        vbAufgabeText.getChildren().addAll(aufgabeText, btnAufgabeText);
    }

    void setBlockPar(String par) {
        blockPar = par;
    }

    void setFragePar(int par) {
        nummerFragePar = par;
    }

    void buildTaskPane() {
        tempPane.setId("aufgabePane");
        AntwortPane.setId("antwortPane");
        AufgabenKopf.setId("aufgabenKopf");

        hbBlockName.getChildren().addAll(blockName, btnBlockName);
        AufgabenKopf.setLeft(hbBlockName);
        AufgabenKopf.setRight(btnBlockZurueck);

        tempPane.setPrefWidth(scene.getWidth() - 150);

        aufgabeText.setWrapText(true);
        aufgabeText.setFont(Font.font(16));
        aufgabeText.setText("ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        tempPane.setCenter(vbAufgabeText);//aufgabeText

        AufgabenPane.setLeft(tempPane);
        AufgabenPane.setRight(AntwortPane);
        AufgabenPane.setTop(AufgabenKopf);

        //AntwortPane.setStyle("-fx-background-color:rgb(200,200,200);");
        AntwortPane.setPrefWidth(150);
        //fillAntwortAuswahl();
        antwortAuswahl.setSpacing(5);

        //antwortAuswahl.getChildren().add(btnNeueAntwort);
        AntwortPane.setAlignment(antwortAuswahl, Pos.CENTER);
        VBox antworten = new VBox();
        antworten.getChildren().add(antwortAuswahl);
        antworten.getChildren().add(btnAntwortenAuswahl);
        AntwortPane.setCenter(antworten);//antwortAuswahl
        //setBtnNeueAufgabe();    //Funktion auskommentiert (Z.338)
        //antwortAuswahl.setBottom(btnNeueAntwort);
        AntwortPane.setAlignment(navigator, Pos.CENTER);
        AntwortPane.setBottom(navigator);
        auswertungAntwort.setFont(Font.font(20));
        navigator.add(auswertungAntwort, 0, 0);
        navigator.add(btnBestaetigen, 0, 1);
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

    void setBtnLoadAnswerChoice() {

        btnAntwortenAuswahl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                System.out.println("Neue Antwortauswahl");

                Stage tempStage = new Stage();
                tempStage.setTitle("Neuer Aufgabentext");

                ToggleGroup tempToggleGroup = new ToggleGroup();
                VBox vBox = new VBox();
                HBox hBox = new HBox();
                Button btnAbbr = new Button("Abbrechen");
                Button btnBestaet = new Button("Bestätigen");
                // vBox füllen
                //HBox h1 = new HBox();
                //    h1.getChildren().add(new Button());
                //    h1.getChildren().add(new TextField());
                //    vBox.getChildren().add(h1);
                for (int i = 0; i < antwortAuswahl.getChildren().size(); i++) {
                    RadioButton rb = (RadioButton) antwortAuswahl.getChildren().get(i);
                    String text = rb.getText();
                    RadioButton neuRb = new RadioButton();
                    neuRb.setToggleGroup(tempToggleGroup);
                    TextField neuTf = new TextField(text);
                    HBox hb = new HBox();
                    hb.getChildren().addAll(neuRb, neuTf);
                    vBox.getChildren().add(hb);
                }
                VBox vBox2 = new VBox();
                Button btnNeueAnwort = new Button("Neue Antwort");
                vBox2.getChildren().add(vBox);
                vBox2.getChildren().add(btnNeueAntwort);

                hBox.getChildren().add(btnAbbr);
                hBox.getChildren().add(btnBestaet);

                BorderPane tempPane = new BorderPane();
                tempPane.setCenter(vBox2);
                tempPane.setBottom(hBox);

                Scene tempScene = new Scene(tempPane, 300, 300);

                tempStage.setScene(tempScene);
                tempStage.show();

                // btnNeueAntwort definieren
                btnNeueAntwort.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        System.out.println("Neue Antwort wird erstellt");

                        RadioButton radioBtn = new RadioButton();
                        radioBtn.setToggleGroup(tempToggleGroup);

                        HBox h = new HBox();
                        h.getChildren().add(radioBtn);
                        h.getChildren().add(new TextField());
                        vBox.getChildren().add(h);

                    }
                });

                // btnAbbr definieren
                btnAbbr.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {

                        System.out.println("Eingabe abgebrochen");
                        tempStage.close();
                    }
                });

                btnBestaet.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {

                        System.out.println("Eingabe wird gespeichert");

                        /*
                           Anweisungen hier
                         */
                        // Auslesen aller TextFelder
                        for (int i = 0; i < vBox.getChildren().size(); i++) {
                            HBox hb = (HBox) vBox.getChildren().get(i);
                            for (int j = 0; j < hb.getChildren().size(); j++) {
                                if (hb.getChildren().get(j) instanceof TextField) {
                                    TextField tf = (TextField) hb.getChildren().get(j);
                                    String ausgabeString = tf.getText();
                                    System.out.println(ausgabeString);
                                }
                            }
                        }

                        // vergleiche toggles mit gewählten toggle zur feststellung der wie vielte er ist.            
                        int nrSelectedToggle = -1;

                        for (int i = 0; i < tempToggleGroup.getToggles().size(); i++) {
                            if (tempToggleGroup.getSelectedToggle().equals(tempToggleGroup.getToggles().get(i))) {
                                nrSelectedToggle = i;
                                break;
                            }
                        }
                        System.out.println(nrSelectedToggle);

                        //Antworten neuladen und Fenster schliessen
                        //btnLabel und aufgabenNummer sind unbekannt 
                        //fillAntwortAuswahl(*btnLabel*, sql.fragen.get(*aufgabenNummer*));
                        tempStage.close();
                    }
                });

            }
        });
    }

    void setBtnBlockName() {
        btnBlockName.setOnAction(e -> {
            Stage tempStage = new Stage();
            tempStage.setTitle("Neuer Aufgabentext");

            BorderPane borderPane = new BorderPane();
            TextField tf = new TextField();
            tf.setPromptText("Hier Blocknamen eintragen");
            Button btnAbbrechen = new Button("Abbrechen");
            Button btnBestaetigen = new Button("Bestaetigen");

            btnAbbrechen.setOnAction(e2 -> {
                tempStage.close();
            });
            btnBestaetigen.setOnAction(e3 -> {
                blockName.setText(tf.getText());
                tempStage.close();
            });
            borderPane.setTop(tf);
            borderPane.setLeft(btnAbbrechen);
            borderPane.setRight(btnBestaetigen);

            Scene tempScene = new Scene(borderPane, 300, 300);

            tempStage.setScene(tempScene);
            tempStage.show();
        });
    }

    void setBtnBack(Stage tempStage) {//öffnet nächste Aufgabe
        btnBlockZurueck.setOnAction((ActionEvent e) -> {
            System.out.println("aufgabe beendet");
            sql.endAufgabe();
            fillVerzeich();
            scene.setRoot(getHauptPane());
            tempStage.setScene(scene);
            tempStage.setTitle("SimpleLearner - Kategorie");
            tempStage.show();
        });
    }

    void setBtnNewQuestionText() {

        btnAufgabeText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                System.out.println("Neuer Aufgabentext");

                Stage tempStage = new Stage();
                tempStage.setTitle("Neuer Aufgabentext");

                HBox hBox = new HBox();
                Button btnAbbr = new Button("Abbrechen");
                Button btnBestaet = new Button("Bestätigen");
                hBox.getChildren().add(btnAbbr);
                hBox.getChildren().add(btnBestaet);

                TextArea tempTextArea = new TextArea("AufgabenText hier eingeben");
                tempTextArea.setMinWidth(300);
                tempTextArea.setMinHeight(250);
                tempTextArea.setWrapText(true);

                BorderPane tempPane = new BorderPane();
                tempPane.setCenter(tempTextArea);
                tempPane.setBottom(hBox);

                Scene tempScene = new Scene(tempPane, 300, 300);

                tempStage.setScene(tempScene);
                tempStage.show();

                // btnAbbr definieren
                btnAbbr.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {

                        System.out.println("Eingabe abgebrochen");
                        tempStage.close();
                    }
                });

                btnBestaet.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {

                        System.out.println("Eingabe wird gespeichert");

                        /*
            Anweisungen hier
                - TextArea auslesen
                - String übergeben
                         */
                        System.out.println(">> String-Ausgabe >> " + tempTextArea.getText());
                        aufgabeText.setText(tempTextArea.getText());

                        tempStage.close();
                    }
                });

            }
        });
    }

    void setBtnConfirmAnswer() {//wertet den bewählte Button aus und gibt diese aus
        btnBestaetigen.setPrefWidth(75);
        btnBestaetigen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String antwort = AntwortGroup.getSelectedToggle().getUserData().toString();
                System.out.println("Auswahl wurde bestätigt");
                System.out.println("    Gewählte Antwort: " + antwort);

                //checkAntwort(antwort);
                System.out.println(blockPar + sql.fragen.get(nummerFragePar) + antwort);
                System.out.println(nummerFragePar);
                System.out.println(loginName.getText());
                try {
                    if (!sql.isFertig) {
                        sql.startAufgabe(blockPar, loginName.getText(), false); //am Ende dann noch true setzen bei der letzten Aufgabe
                    }
                    if (sql.checkAntwort(blockPar, loginName.getText(), sql.fragen.get(nummerFragePar), antwort) == true) {
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
                    navigator.add(btnNaechsteAufgabe, 0, 1);
                } else {
                    navigator.add(btnBeenden, 0, 1);
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

    void setBtnNextTask(Stage tempStage) {//öffnet nächste Aufgabe
        btnNaechsteAufgabe.setPrefWidth(75);
        btnNaechsteAufgabe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Nächste Aufgabe wird geöffnet");
                System.out.println("    Neue Aufgabe: ");
                System.out.println("------------------------------");

                // exception durch erneutes Einfügen von btnBestätigen
                // -> btnBestätigen löschen und erneut einfügen
                navigator.getChildren().remove(1, 3); // btnBestätigen und btnNächsteAufgabe löschen
                navigator.add(btnBestaetigen, 0, 1); // btnBestätigen ein
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

    void setBtnConfirmQuiz(Stage tempStage) {//öffnet nächste Aufgabe
        btnBeenden.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("aufgabe beendet");
                sql.endAufgabe();
                fillVerzeich();
                scene.setRoot(getHauptPane());
                tempStage.setScene(scene);
                tempStage.setTitle("SimpleLearner - Kategorie");
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
//Scene scene = new Scene(changeMeldungPane(getLoginPane()), 600, 600);
    Scene scene = new Scene(getLoginPane(), 600, 600);

    void loadStyleSheets() {
        scene.getStylesheets().add("SEProject/SimpleLearnerGUI.css");
    }

    Stage TestStage = new Stage();

    void setPrimaryStage() {
        TestStage.setScene(scene);
    }
}
