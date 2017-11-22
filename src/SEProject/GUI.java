/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SEProject;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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


/**
 *
 * @author stefan
 */

public class GUI extends Application{
    
    @Override
    public void start(Stage primaryStage) {
    
    // initialisiere AnmeldungPane
        buildAnmeldPane();
        setBtnAnmeldung(primaryStage);
    // initialisere MeldungPane
        setMeldungPane(anmeldPane);
    // initialisiere AufgabenPane
        buildAufgabenPane();        
    //initialisiere HauptPane    
        setHauptTop();
        setHauptLeft();
        setHauptRight();
        setHauptBottom();
        setHauptCenter();
        //changeHauptCenter(getAufgabenPane()); // Fenster für Aufgaben
        setVerzeichPane();
        Scene scene = new Scene(getRootBase(),600,600); // Fenster für Verzeichnis
        //Scene scene = new Scene(getRootAll(),600,600); // Fenster für Anmeldung
        
        primaryStage.setTitle("SimpleTest");
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
    
    void setBtnAnmeldung(){
        btnAnmeld.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                System.out.println("Benutzer wird eingeloggt");
                System.out.println("Name: "+ AnmeldungName.getText());
                System.out.println("Passwort: " + AnmeldungPasswort.getText());
                
            }
        }); 
    }

    void setBtnAnmeldung(Stage tempStage){ 
        btnAnmeld.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                System.out.println("Benutzer wird eingeloggt");
                System.out.println("Name: "+ AnmeldungName.getText());
                System.out.println("Passwort: " + AnmeldungPasswort.getText());

                Scene temp = new Scene(root,400,600); // -> Exception (keine Veränderung)
                tempStage.setScene(temp);
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
            hauptTop.setPrefHeight(50);
            Button temp = new Button("Schüler");
            hauptTop.setCenter(new Label("Disziplin / Modul / Aufgabenblöcke"));
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
            btnNeuesElement.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e){
                    int temp = centerListe.getChildren().size();
                    centerListe.getChildren().setAll(); // VBox leeren
                    for(int i=0; i<temp; i++){
                        System.out.println("Neues Element "+i+" wird hinzugefügt");
                        centerListe.getChildren().add(new VerzeichnisButton(i,isStudent).getVerzeichnisButton()); //VBox mit Buttons füllen
                    }
                    centerListe.getChildren().add(btnNeuesElement); //btnNeuesElement anhängen
                }
            }); 
            centerListe.setSpacing(5);
            centerListe.getChildren().add(btnNeuesElement);
            hauptCenter.setCenter(centerListe);
        }
        
        void changeHauptCenter(BorderPane temp){
            hauptCenter = temp;
        };
        
        void setVerzeichPane(){
            HauptPane.setTop(hauptTop);
            HauptPane.setLeft(hauptLeft);
            HauptPane.setRight(hauptRight);
            HauptPane.setBottom(hauptBottom);
            HauptPane.setCenter(hauptCenter);
        }
        
        StackPane getRootAll(){
            root.getChildren().setAll(HauptPane,meldungPane); 
            return root;
        }
        
        StackPane getRootBase(){
            root.getChildren().setAll(HauptPane); 
            return root;
        } 


        
    class VerzeichnisButton{
        int numBtn;
        Button btnName;
        Button btnLöschen;
    
        VerzeichnisButton(int i, boolean isStudent){
            numBtn = i;
            btnName = new Button("Name "+numBtn);
            btnName.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e){
                    System.out.println("Element "+numBtn+" wird geöffnet");    
                }
            });
            btnLöschen = new Button("Löschen");
            btnLöschen.setStyle("-fx-background-color:rgb(255,50,50)");
            btnLöschen.setPrefWidth(100);
            btnName.setPrefWidth(hauptCenter.getWidth()-btnLöschen.getPrefWidth());
            btnLöschen.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e){
                    System.out.println("Element "+numBtn+" wird gelöscht");
                    //centerListe.getChildren().remove(numBtn,numBtn+1);
                    System.out.println(numBtn);
                    //centerListe.getChildren().add(btnNeuesElement);
                }
            });
        }
    
        BorderPane getVerzeichnisButton(){
            BorderPane temp = new BorderPane();
            if(isStudent){
                temp.setLeft(btnName);
                btnName.setPrefWidth(hauptCenter.getWidth());
            }else{
                temp.setLeft(btnName);
                temp.setRight(btnLöschen);
            }
            return temp;
        }
    }
    
//AufgabenPane
    BorderPane AufgabenPane = new BorderPane();
        Pane tempPane = new Pane(); // mit TextArea ersetzen
        BorderPane AntwortPane = new BorderPane();
            Label numAufgaben = new Label("Aufgabe Nr.1");
            VBox antwortAuswahl = new VBox();
                Button btnNeueAntwort = new Button("Neue Aufgabe");
            GridPane navigator = new GridPane();
                Button btnZurück = new Button("Zurück");
                Button btnNächste = new Button("Nächste");
                Button btnBestätigen = new Button("Bestätigen");
            
    void buildAufgabenPane(){
        tempPane.setStyle("-fx-background-color:rgb(220,220,220)");
        tempPane.setPrefWidth(350);
        tempPane.setPrefHeight(50);
        AufgabenPane.setLeft(tempPane);
        AufgabenPane.setRight(AntwortPane);
            AntwortPane.setStyle("-fx-background-color:rgb(200,150,200);");
            AntwortPane.setAlignment(numAufgaben, Pos.CENTER);
            AntwortPane.setTop(numAufgaben);
            AntwortPane.setAlignment(antwortAuswahl, Pos.CENTER);
            //AntwortPane.setMargin(antwortAuswahl, new Insets(5,0,5,0));
            AntwortPane.setCenter(antwortAuswahl);
                btnNeueAntwort.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e){
                        System.out.println("Neue Antwort wird erstellt");
                    }
                });
            antwortAuswahl.getChildren().add(btnNeueAntwort);
            AntwortPane.setAlignment(navigator, Pos.CENTER);
            AntwortPane.setBottom(navigator);
                btnZurück.setPrefWidth(75);
                btnNächste.setPrefWidth(75);
                btnBestätigen.setPrefWidth(150);
                navigator.add(btnZurück,0,0);
                navigator.add(btnNächste,1,0);
                navigator.setColumnSpan(btnBestätigen,2);                
                navigator.add(btnBestätigen,0,1);
    }
    
    BorderPane getAufgabenPane(){
        return AufgabenPane;
    }  
    
    class AntwortButton{
        Button btnAntwort;
        boolean isClicked;
        
        AntwortButton(){
            isClicked = false;
            btnAntwort = new Button();
            btnAntwort.setStyle("-fx-background-color:rgb(150,100,200");
            btnAntwort.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e){
                    if(!isClicked){
                        isClicked=true;
                        btnAntwort.setStyle("-fx-background-color:rgb(200,150,100)");
                    }else{
                        isClicked=false;
                        btnAntwort.setStyle("-fx-background-color:rgb(150,100,200)");
                    }
                    
                }
            });
        }
    }
}





    

