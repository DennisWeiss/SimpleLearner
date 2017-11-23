package SEProject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
 
/**
 *
 * @author stefan
 */

public class Test extends Application{
    
    @Override
    public void start(Stage primaryStage) {
    
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
    TextField AnmeldungPasswort = new TextField();
    Button btnAnmeld = new Button("Einloggen");
    BorderPane anmeldPane = new BorderPane();
    GridPane eingabeCenter = new GridPane();
    
    private void buildAnmeldPane(){
            BorderPane temp = new BorderPane();
                temp.setStyle("-fx-background-color: rgb(255,255,255);");
                //eingabeDisplay.setPrefHeight(150);
                //eingabeDisplay.setPrefWidth(400);
                    BorderPane eingabeTop = new BorderPane();
                        eingabeTop.setStyle("-fx-background-color:rgb(100,100,100);");
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
    void setBtnAnmeldung(Stage tempStage){ 
        btnAnmeld.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                System.out.println("------------------------------");
                System.out.println("Benutzer wird eingeloggt");
                System.out.println("    Name: "+ AnmeldungName.getText());
                System.out.println("    Passwort: " + AnmeldungPasswort.getText());
                System.out.println("------------------------------");
                
                setScene(getHauptPane());                
                tempStage.setScene(scene);
                tempStage.setTitle("SimpleLearner - Aufgabenverzeichnis");
                tempStage.show();

            }
        }); 
    }      
    String getName(){
        return AnmeldungName.getText();
    }  
    String getPasswort(){
        return AnmeldungPasswort.getText();
    }
    BorderPane getAnmeldPane(){
        return anmeldPane;
    }
    
//meldungPane
    FlowPane meldungPane = new FlowPane();
        void setMeldungPane(){
            meldungPane.setStyle("-fx-background-color:rgba(0,0,0,0.5);");
            meldungPane.setAlignment(Pos.CENTER);
            meldungPane.getChildren().add(new BorderPane());
        }
        void setMeldungPane(BorderPane temp){
            meldungPane.setStyle("-fx-background-color:rgba(0,0,0,0.5);");
            meldungPane.setAlignment(Pos.CENTER);
            meldungPane.getChildren().setAll(temp);
        }
        FlowPane changeMeldungPane(BorderPane temp){
            setMeldungPane(temp);
            return meldungPane;
        }       

     
// HauptPane
    BorderPane hauptTop = new BorderPane();
        Label label = new Label("Test");
    Pane hauptLeft = new Pane(); 
    BorderPane hauptRight = new BorderPane();
    BorderPane hauptBottom = new BorderPane();
    BorderPane hauptCenter= new BorderPane();
        VBox centerListe = new VBox();
        Button btnNeuesElement = new Button("Neues Element");
    BorderPane HauptPane = new BorderPane();    
    StackPane root = new StackPane();
    boolean isStudent = false;
    
        void setHauptTop(){
            hauptTop.setStyle("-fx-background-color: rgb(100,100,100);");
            hauptTop.setPrefHeight(30);
            Button temp = new Button("Schüler");
            hauptTop.setCenter(label);
            hauptTop.setRight(temp);
            temp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e){
                    if(!isStudent){
                        temp.setText("Schüler");
                        isStudent=true;
                    }else{
                        temp.setText("Lehrer");
                        isStudent=false;
                    }
                }
            }); 
        }  
        void setHauptLeft(){
            hauptLeft.setStyle("-fx-background-color: rgb(200,200,200);");  
            hauptLeft.setPrefWidth(50);
        }
        void setHauptRight(){
            hauptRight.setStyle("-fx-background-color: rgb(200,200,200);");  
            hauptRight.setPrefWidth(50);
        }
        void setHauptBottom(){
            hauptBottom.setStyle("-fx-background-color: rgb(100,100,100);");
            hauptBottom.setPrefHeight(50);
        }
        void setHauptCenter(){
            centerListe.setSpacing(5);
            //centerListe.getChildren().add(btnNeuesElement);
            hauptCenter.setCenter(centerListe);
        }
        void setBtnNeuesElement(){
            btnNeuesElement.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e){
                    int temp = centerListe.getChildren().size();
                    centerListe.getChildren().setAll(); // VBox leeren
                    for(int i=0; i<temp; i++){
                        System.out.println("Neues Element "+i+" wird hinzugefügt");
                        centerListe.getChildren().add(new VerzeichnisButton("String "+i).getVerzeichnisButton()); //VBox mit Buttons füllen
                    }
                    //centerListe.getChildren().add(btnNeuesElement); //btnNeuesElement anhängen
                }
            }); 
        }
        
        
        //void changeHauptCenter(BorderPane temp){
        //    hauptCenter = temp;
        //};
        void buildHauptPane(){
            HauptPane.setTop(hauptTop);
            HauptPane.setLeft(hauptLeft);
            HauptPane.setRight(hauptRight);
            HauptPane.setBottom(hauptBottom);
            HauptPane.setCenter(hauptCenter);
            
            fillVerzeich(6);
        }
        void fillVerzeich(int zahl){ //Parameterübergabe für Anzahl der Aufgaben
            for( int i=0;i<zahl;i++)
            centerListe.getChildren().add(new VerzeichnisButton("Test "+i).getVerzeichnisButton()); // ersetze ("Test "+i) mit Aufgabenname
        }
        BorderPane getHauptPane(){
            return HauptPane;
        } 
        
    class VerzeichnisButton{
        String btnLabel;
        Button btnName;
        Button btnLöschen;
    
        VerzeichnisButton(String input){
            btnLabel = input;
            btnName = new Button(input);
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
        void setBtnName(Stage tempStage){
            btnName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                System.out.println("Aufgabeneinheit wird geöffnet:");
                System.out.println("    gewählte Einheit: " + btnLabel);
                System.out.println("------------------------------");
                
                //buildAufgabenPane();// Parameter
                
                setScene(getAufgabenPane());                
                tempStage.setScene(scene);
                tempStage.setTitle("SimpleLearner - Aufgabe");
                tempStage.show();

                }
            }); 
        }
    
        BorderPane getVerzeichnisButton(){
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
    BorderPane AufgabenPane = new BorderPane();
        BorderPane tempPane = new BorderPane(); // Ausgabe des Aufgabentextes
            Label aufgabeText = new Label();
        BorderPane AntwortPane = new BorderPane();
            Label numAufgaben = new Label("Aufgabe Nr.1");
                VBox antwortAuswahl = new VBox();
                ToggleGroup AntwortGroup = new ToggleGroup();
                Button btnNeueAntwort = new Button("Neue Aufgabe");
            GridPane navigator = new GridPane();
                //Button btnZurück = new Button("Zurück");
                //Button btnNächste = new Button("Nächste");
                Label auswertungAntwort = new Label();
                Button btnBestätigen = new Button("Bestätigen");
                Button btnNächsteAufgabe = new Button("Nächste");
            
    void buildAufgabenPane(){ // Variablen übergeben für verschiedene Aufgaben
        tempPane.setStyle("-fx-background-color:rgb(220,220,220)");
        tempPane.setPrefWidth(scene.getWidth()-150);        
            aufgabeText.setWrapText(true);
            aufgabeText.setFont(Font.font(16));
            aufgabeText.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        tempPane.setCenter(aufgabeText);
        AufgabenPane.setLeft(tempPane);
        AufgabenPane.setRight(AntwortPane);
            AntwortPane.setStyle("-fx-background-color:rgb(200,150,200);");
            AntwortPane.setPrefWidth(150);
            AntwortPane.setAlignment(numAufgaben, Pos.CENTER);
            AntwortPane.setTop(numAufgaben);
            fillAntwortAuswahl();
            antwortAuswahl.setSpacing(5);
                        
            //antwortAuswahl.getChildren().add(btnNeueAntwort);
            AntwortPane.setAlignment(antwortAuswahl, Pos.CENTER);
            AntwortPane.setCenter(antwortAuswahl);
            //setBtnNeueAufgabe();    //Funktion auskommentiert (Z.338)
            //antwortAuswahl.setBottom(btnNeueAntwort);
            AntwortPane.setAlignment(navigator, Pos.CENTER);
            AntwortPane.setBottom(navigator); 
                auswertungAntwort.setFont(Font.font(20));
                navigator.add(auswertungAntwort,0,0);
                navigator.add(btnBestätigen,0,1);
    }
    void fillAntwortAuswahl(){
        AntwortGroup = new ToggleGroup();
        antwortAuswahl.getChildren().setAll();
        for(int i=0; i<3/*anzahl der Aufgaben*/; i++){
                // hole Aufgabenname der i.ten Aufgabeneinheit
            // Übergebe AufgabenName
                antwortAuswahl.getChildren().add(new btnAntwort("Test "+i).getBtnAntwort());
            }
    }
    void setBtnBestätigen(){//wertet den bewählte Button aus und gibt diese aus
        btnBestätigen.setPrefWidth(75);
        btnBestätigen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                String antwort = AntwortGroup.getSelectedToggle().getUserData().toString();
                System.out.println("Auswahl wurde bestätigt");
                System.out.println("    Gewählte Antwort: "+antwort);
            
            //checkAntwort(antwort);
                if(antwort.equals("Test 1")){
                    System.out.println("        >> die Antwort ist richtig");
                    auswertungAntwort.setText("Richtig");
                }else{
                    System.out.println("        >> die Antwort ist falsch");
                    auswertungAntwort.setText("Falsch");
                }
                System.out.println("------------------------------");
                
                //ersetze "Bestätigen"-Button mit "Nächste"-Button
                navigator.add(btnNächsteAufgabe, 0, 1);

                
            }
        });
    }
    void setBtnNächsteAufgabe(Stage tempStage){//öffnet nächste Aufgabe
        btnNächsteAufgabe.setPrefWidth(75);
        btnNächsteAufgabe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                System.out.println("Nächste Aufgabe wird geöffnet");
                System.out.println("    Neue Aufgabe: " );
                System.out.println("------------------------------");
                
                // exception durch erneutes Einfügen von btnBestätigen
                // -> btnBestätigen löschen und erneut einfügen
                    navigator.getChildren().remove(1,3); // btnBestätigen und btnNächsteAufgabe löschen
                    navigator.add(btnBestätigen, 0, 1); // btnBestätigen ein
                        System.out.println("    >Bestätigen-Button eingefügt");
                    aufgabeText.setText("   >Text der nächsten Aufgabe");
                        System.out.println("    >AufgabenText geändert");
                    fillAntwortAuswahl();
                        System.out.println("    >AntwortAuswahl erneuert");
                        System.out.println("------------------------------");
                    
                tempStage.show();                
            }
        });
    }
    BorderPane getAufgabenPane(){
        return AufgabenPane;
    } 
    
    class btnAntwort{
        RadioButton btn;
        String btnLabel;
        
        btnAntwort(String input){
            btnLabel = input;
            btn = new RadioButton(btnLabel);
            btn.setUserData(btnLabel);
            btn.setToggleGroup(AntwortGroup);
        }
        
        void setBtnAntwort(){
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e){
                    System.out.println(btnLabel);
                }
            });
        }
        
        RadioButton getBtnAntwort(){
            return btn;
        }
    }    
// Scene    
    // getHauptPane();
    // getAufgabenPane();
    Scene scene = new Scene(changeMeldungPane(getAnmeldPane()),600,600);
    
    void setScene(StackPane temp){
        scene = new Scene(temp,600,600);
    }; 
    void setScene(BorderPane temp){
        scene = new Scene(temp,600,600);
    };
    Stage TestStage = new Stage();
    void setPrimaryStage(){
        TestStage.setScene(scene);    
    }
}