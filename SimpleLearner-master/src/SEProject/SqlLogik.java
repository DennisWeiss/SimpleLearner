/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SEProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Marcel
 */
public class SqlLogik {

    Properties userInfo;
    ArrayList<String> faecher;
    ArrayList<String> kategorien;
    ArrayList<String> aufgabenbloecke;
    ArrayList<String> aufgabenbloeckeTemp;
    ArrayList<String> fragen;
    ArrayList<String> antwortenTemp;
    ArrayList<String> absolvierteSchueler;
    ArrayList<AntwortPdfObjekt> tempAntwortenSchueler;
    String loginLehrer;
    String loginSchueler;
    private String currentUser;
    boolean isFertig;

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public SqlLogik() {
        userInfo = new Properties();
        userInfo.put("user", "root"); //"root" für stefan
        userInfo.put("password", "databasemarcel"); //"stefan" für stefan
        faecher = new ArrayList<>();
        kategorien = new ArrayList<>();
        aufgabenbloecke = new ArrayList<>();
        aufgabenbloeckeTemp = new ArrayList<>();
        fragen = new ArrayList<>();
        antwortenTemp = new ArrayList<>();
        absolvierteSchueler = new ArrayList<>();
        tempAntwortenSchueler = new ArrayList<>();
        loginLehrer = null;
        loginSchueler = null;
        currentUser = null;
    }

    public void startBlock(String blockBez, String aSchueler) throws SQLException {
        String startString = "insert into schuelerloestblock(schueler, block) values(?, ?);";
        String suchString = "select block, schueler from schuelerloestblock;";
        ResultSet rsSuche = null;
        boolean confirm = true;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtStart = myConn.prepareStatement(startString);
                Statement stmtSuche = myConn.createStatement()) {

            rsSuche = stmtSuche.executeQuery(suchString);
            while (rsSuche.next()) {
                if (rsSuche.getString("block").equals(blockBez)) {
                    if (rsSuche.getString("schueler").equals(aSchueler)) {
                        confirm = false;
                    }
                }
            }
            if (confirm) {
                stmtStart.setString(1, aSchueler);
                stmtStart.setString(2, blockBez);

                stmtStart.executeUpdate();
            }
        } finally {
            if (rsSuche != null) {
                rsSuche.close();
            }
        }
    }

    /*
    public void endBlock(String blockBez, String aSchueler) throws SQLException {
        String endString = "update schuelerloestblock set fertig = ? where schueler = ? and block = ?;";

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtStart = myConn.prepareStatement(endString)) {

            stmtStart.setBoolean(1, true);
            stmtStart.setString(2, aSchueler);
            stmtStart.setString(3, blockBez);

            stmtStart.executeUpdate();
        }
    }
     */
    public boolean checkAntwort(String blockBez, String aSchueler, String aFrage, String aAntwort) throws SQLException {

        String antwortString = "insert into schuelerloestaufgabe(aufgabe, schueler, antwortS) values((select aufgabe.aid from aufgabe "
                + "where aufgabe.block = ? and aufgabe.frage = ?), ?, ?);";

        String stmtString = "select schuelerloestaufgabe.schueler, schuelerloestaufgabe.antwortS, antwort.isTrue from schuelerloestaufgabe "
                + "join aufgabe on schuelerloestaufgabe.aufgabe = aufgabe.aid "
                + "join schueler on schuelerloestaufgabe.schueler = schueler.SID "
                + "join antwort on aufgabe.aid = antwort.aufgabe "
                + "where aufgabe.frage = ? and antwort.antworttext = schuelerloestaufgabe.antwortS and schuelerloestaufgabe.schueler = ?;";

        /*"select isTrue from antwort join aufgabe on antwort.aufgabe = aufgabe.aid"
                + " join block on aufgabe.block = block.bid where block.bid = ? and aufgabe.frage = ? and antwort.antworttext = ?";*/
        ResultSet rsAntwort = null;

        boolean check = false;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement setAntwort = myConn.prepareStatement(antwortString);
                PreparedStatement stmtAntwort = myConn.prepareStatement(stmtString)) {

            setAntwort.setString(1, blockBez);
            setAntwort.setString(2, aFrage);
            setAntwort.setString(3, aSchueler);
            setAntwort.setString(4, aAntwort);
            setAntwort.execute();

            stmtAntwort.setString(1, aFrage);
            stmtAntwort.setString(2, aSchueler);
            rsAntwort = stmtAntwort.executeQuery();

            while (rsAntwort.next()) {
                if (rsAntwort.getString("antwort.isTrue").equals("1")) {
                    System.out.println(rsAntwort.getString("antwort.isTrue"));
                    check = true;
                }
            }
        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsAntwort != null) {
                rsAntwort.close();
            }
        }
        return check;
    }

    public boolean[] checkLogin(String user, String password) throws SQLException {

        String stringCheck = "select * from lehrer, schueler";
        boolean[] checkPassword = new boolean[2];
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                Statement stmtCheck = myConn.createStatement();
                ResultSet rsCheck = stmtCheck.executeQuery(stringCheck)) {

            while (rsCheck.next()) {
                if (rsCheck.getString("lid").equals(user)) {
                    checkPassword[0] = rsCheck.getString("lehrer.passwort").equals(password);
                    if (checkPassword[0] == true) {
                        checkPassword[1] = true; //1 für Lehrer
                        currentUser = rsCheck.getString("lehrer.vorname") + " " + rsCheck.getString("lehrer.nachname");
                    }
                } else if (rsCheck.getString("sid").equals(user)) {
                    checkPassword[0] = rsCheck.getString("schueler.passwort").equals(password);
                    if (checkPassword[0] == true) {
                        checkPassword[1] = false;//0 für Schüler
                        currentUser = rsCheck.getString("schueler.vorname") + " " + rsCheck.getString("schueler.nachname");
                    }
                }
            }

            return checkPassword;

        } catch (SQLException exc) {
            throw exc;
        }
    }

    public void loadLehrer(String lid) throws SQLException {
        String stringLehrer = "select vorname, nachname from lehrer where lid = ?";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtLehrer = myConn.prepareStatement(stringLehrer);
                ResultSet rsLehrer = stmtLehrer.executeQuery()) {

            while (rsLehrer.next()) {
                loginLehrer = rsLehrer.getString("vorname") + " " + rsLehrer.getString("nachname");
            }

        } catch (SQLException exc) {
            throw exc;
        }
    }

    public void loadSchueler(String sid) throws SQLException {
        String stringSchüler = "select vorname, nachname from schueler where sid = ?";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtSchueler = myConn.prepareStatement(stringSchüler);
                ResultSet rsSchueler = stmtSchueler.executeQuery()) {

            while (rsSchueler.next()) {
                loginSchueler = rsSchueler.getString("vorname") + " " + rsSchueler.getString("nachname");
            }

        } catch (SQLException exc) {
            throw exc;
        }
    }

    public void loadFaecher() throws SQLException {
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                Statement stmtFach = myConn.createStatement();
                ResultSet rsFach = stmtFach.executeQuery("select fid from fach;")) {

            if (faecher != null) {
                faecher.clear();
            }

            while (rsFach.next()) {
                faecher.add(rsFach.getString("fid"));
            }

        } catch (SQLException exc) {
            throw exc;
        }
    }

    public void loadFaecher(String lehrer) throws SQLException {
        String faecherString = "select fach from lehrerunterrichtet where lehrer = ?";
        ResultSet rsFach = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtFach = myConn.prepareStatement(faecherString)) {

            stmtFach.setString(1, lehrer);
            rsFach = stmtFach.executeQuery();

            if (faecher != null) {
                faecher.clear();
            }

            while (rsFach.next()) {
                faecher.add(rsFach.getString("fach"));
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsFach != null) {
                rsFach.close();
            }
        }
    }

    public void loadFilteredFaecher(String filter) throws SQLException {
        String faecherString = "select fid from fach where fid like ?";
        ResultSet rsFach = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtFach = myConn.prepareStatement(faecherString)) {

            stmtFach.setString(1, "%" + filter + "%");
            rsFach = stmtFach.executeQuery();

            if (faecher != null) {
                faecher.clear();
            }

            while (rsFach.next()) {
                faecher.add(rsFach.getString("fid"));
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsFach != null) {
                rsFach.close();
            }
        }
    }

    public void loadFilteredFaecher(String lehrer, String filter) throws SQLException {
        String faecherString = "select fach from lehrerunterrichtet where lehrer = ? and fach like ?;";
        ResultSet rsFach = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtFach = myConn.prepareStatement(faecherString)) {

            stmtFach.setString(1, lehrer);
            stmtFach.setString(2, "%" + filter + "%");
            rsFach = stmtFach.executeQuery();

            if (faecher != null) {
                faecher.clear();
            }

            while (rsFach.next()) {
                faecher.add(rsFach.getString("fach"));
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsFach != null) {
                rsFach.close();
            }
        }
    }

    public void loadKategorien(String fach) throws SQLException {
        String kategorieString = "select kid from kategorie where fach = ?";
        ResultSet rsFach = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtFach = myConn.prepareStatement(kategorieString)) {

            stmtFach.setString(1, fach);
            rsFach = stmtFach.executeQuery();

            if (kategorien != null) {
                kategorien.clear();
            }

            while (rsFach.next()) {
                kategorien.add(rsFach.getString("kid"));
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsFach != null) {
                rsFach.close();
            }
        }
    }

    public void loadFilteredKategorien(String fach, String filter) throws SQLException {
        String kategorieString = "select kid from kategorie where fach = ? and kid like ?";
        ResultSet rsFach = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtFach = myConn.prepareStatement(kategorieString)) {

            stmtFach.setString(1, fach);
            stmtFach.setString(2, "%" + filter + "%");
            rsFach = stmtFach.executeQuery();

            if (kategorien != null) {
                kategorien.clear();
            }

            while (rsFach.next()) {
                kategorien.add(rsFach.getString("kid"));
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsFach != null) {
                rsFach.close();
            }
        }
    }

    public void loadBloeckeLehrer(String kategorie, String lehrer) throws SQLException {
        String blockString = "select bid from block where kategorie = ? and lehrer = ?;";
        ResultSet rsBlock = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtBlock = myConn.prepareStatement(blockString)) {

            stmtBlock.setString(1, kategorie);
            stmtBlock.setString(2, lehrer);
            rsBlock = stmtBlock.executeQuery();
            if (aufgabenbloecke != null) {
                aufgabenbloecke.clear();
            }

            while (rsBlock.next()) {
                aufgabenbloecke.add(rsBlock.getString("bid"));
            }
        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsBlock != null) {
                rsBlock.close();
            }
        }
    }

    public void loadBloeckeSchueler(String kategorie, String schueler) throws SQLException {
        String blockString = "select bid from block where kategorie = ?";
        String blockStringTwo = "select block.bid, schuelerloestblock.block from block "
                + "left join schuelerloestblock on block.bid = schuelerloestblock.block "
                + "where block.kategorie = ? and schuelerloestblock.schueler = ?;";

        ResultSet rsBlock = null;
        ResultSet rsBlockTwo = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtBlock = myConn.prepareStatement(blockString);
                PreparedStatement stmtBlockTwo = myConn.prepareStatement(blockStringTwo);) {

            //alle Kategorien werden geladen
            stmtBlock.setString(1, kategorie);
            rsBlock = stmtBlock.executeQuery();
            if (aufgabenbloecke != null) {
                aufgabenbloecke.clear();
            }

            while (rsBlock.next()) {
                aufgabenbloecke.add(rsBlock.getString("bid"));
            }

            //nur eroeffnete Kategorien werden geladen
            stmtBlockTwo.setString(1, kategorie);
            stmtBlockTwo.setString(2, schueler);
            rsBlockTwo = stmtBlockTwo.executeQuery();
            if (aufgabenbloeckeTemp != null) {
                aufgabenbloeckeTemp.clear();
            }
            while (rsBlockTwo.next()) {
                aufgabenbloeckeTemp.add(rsBlockTwo.getString("block.bid"));
            }

            //eroffnete Kategorien werden allen Kategorien entfernt, damit fuer den jeweiligen Schueler nur die Bloecke erscheinen, die er noch nicht bearbeitet hat
            for (int i = 0; i < aufgabenbloecke.size(); i++) {
                if (!aufgabenbloeckeTemp.isEmpty()) {
                    for (int j = 0; j < aufgabenbloeckeTemp.size(); j++) {
                        if (aufgabenbloecke.get(i).equals(aufgabenbloeckeTemp.get(j))) {
                            aufgabenbloecke.remove(i);
                        }
                    }
                }
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsBlock != null) {
                rsBlock.close();
            }
            if (rsBlockTwo != null) {
                rsBlockTwo.close();
            }
        }
    }

    public void loadFilteredBloeckeLehrer(String kategorie, String lehrer, String filter) throws SQLException {
        String blockString = "select bid from block where kategorie = ? and lehrer = ? and bid like ?;";
        ResultSet rsBlock = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtBlock = myConn.prepareStatement(blockString)) {

            stmtBlock.setString(1, kategorie);
            stmtBlock.setString(2, lehrer);
            stmtBlock.setString(3, "%" + filter + "%");
            rsBlock = stmtBlock.executeQuery();
            if (aufgabenbloecke != null) {
                aufgabenbloecke.clear();
            }

            while (rsBlock.next()) {
                aufgabenbloecke.add(rsBlock.getString("bid"));
            }
        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsBlock != null) {
                rsBlock.close();
            }
        }
    }

    public void loadFilteredBloeckeSchueler(String kategorie, String schueler, String filter) throws SQLException {
        /*String blockString = "select bid from block where kategorie = ? and bid like ?;";
        ResultSet rsBlock = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtBlock = myConn.prepareStatement(blockString)) {

            stmtBlock.setString(1, kategorie);
            stmtBlock.setString(2, "%" + filter + "%");
            rsBlock = stmtBlock.executeQuery();
            if (aufgabenbloecke != null) {
                aufgabenbloecke.clear();
            }

            while (rsBlock.next()) {
                aufgabenbloecke.add(rsBlock.getString("bid"));
            }
        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsBlock != null) {
                rsBlock.close();
            }
        }*/
        String blockString = "select bid from block where kategorie = ?";
        String blockStringTwo = "select block.bid, schuelerloestblock.block from block "
                + "left join schuelerloestblock on block.bid = schuelerloestblock.block "
                + "where block.kategorie = ? and schuelerloestblock.schueler = ?;";

        ResultSet rsBlock = null;
        ResultSet rsBlockTwo = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtBlock = myConn.prepareStatement(blockString);
                PreparedStatement stmtBlockTwo = myConn.prepareStatement(blockStringTwo);) {

            //alle Kategorien werden geladen
            stmtBlock.setString(1, kategorie);
            rsBlock = stmtBlock.executeQuery();
            if (aufgabenbloecke != null) {
                aufgabenbloecke.clear();
            }

            while (rsBlock.next()) {
                aufgabenbloecke.add(rsBlock.getString("bid"));
            }

            //nur eroeffnete Kategorien werden geladen
            stmtBlockTwo.setString(1, kategorie);
            stmtBlockTwo.setString(2, schueler);
            rsBlockTwo = stmtBlockTwo.executeQuery();
            if (aufgabenbloeckeTemp != null) {
                aufgabenbloeckeTemp.clear();
            }
            while (rsBlockTwo.next()) {
                aufgabenbloeckeTemp.add(rsBlockTwo.getString("block.bid"));
            }

            //eroffnete Kategorien werden allen Kategorien entfernt, damit fuer den jeweiligen Schueler nur die Bloecke erscheinen, die er noch nicht bearbeitet hat
            for (int i = 0; i < aufgabenbloecke.size(); i++) {
                if (!aufgabenbloeckeTemp.isEmpty()) {
                    for (int j = 0; j < aufgabenbloeckeTemp.size(); j++) {
                        if (aufgabenbloecke.get(i).equals(aufgabenbloeckeTemp.get(j))) {
                            aufgabenbloecke.remove(i);
                        }
                    }
                }
            }
            for (int i = 0; i < aufgabenbloecke.size(); i++) {
                System.out.println(aufgabenbloecke.get(i) + "in Logik ArrayList davor");
            }
            aufgabenbloecke = filterList(aufgabenbloecke, filter);
            for (int i = 0; i < aufgabenbloecke.size(); i++) {
                System.out.println(aufgabenbloecke.get(i) + "in Logik ArrayList danach");
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsBlock != null) {
                rsBlock.close();
            }
            if (rsBlockTwo != null) {
                rsBlockTwo.close();
            }
        }
    }

    public void loadFragen(String block) throws SQLException {
        String stringFrage = "select frage from aufgabe join block on aufgabe.block = block.bid where block.bid = ?";
        ResultSet rsFrage = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtFrage = myConn.prepareStatement(stringFrage)) {

            stmtFrage.setString(1, block);
            rsFrage = stmtFrage.executeQuery();

            fragen.clear();

            while (rsFrage.next()) {
                fragen.add(rsFrage.getString("frage"));
            }
            for (int i = 0; i < fragen.size(); i++) {
                System.out.println(fragen.get(i) + " in Logik");
            }
        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsFrage != null) {
                rsFrage.close();
            }
        }
    }

    public void loadAntworten(String block, String frage) throws SQLException {
        String stringAntworten = "select antworttext from antwort join aufgabe on antwort.aufgabe = aufgabe.aid "
                + "join block on aufgabe.block = block.bid where block.bid = ? and aufgabe.frage = ?";
        ResultSet rsAntworten = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtAntworten = myConn.prepareStatement(stringAntworten)) {

            stmtAntworten.setString(1, block);
            stmtAntworten.setString(2, frage);
            rsAntworten = stmtAntworten.executeQuery();

            antwortenTemp.clear();

            while (rsAntworten.next()) {
                antwortenTemp.add(rsAntworten.getString("antworttext"));
            }
        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsAntworten != null) {
                rsAntworten.close();
            }
        }
    }

    public void deleteBlock(String blockname, String lehrer, String kategorie) throws SQLException {
        String loeschenString = "delete from block where bid = ? and lehrer = ? and kategorie = ?;";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtLoeschen = myConn.prepareStatement(loeschenString)) {

            stmtLoeschen.setString(1, blockname);
            stmtLoeschen.setString(2, lehrer);
            stmtLoeschen.setString(3, kategorie);

            stmtLoeschen.executeUpdate();

        } catch (SQLException exc) {
            throw exc;
        }
    }

    /*public void deleteAufgabe(String blockname, String frage) throws SQLException {
        String loeschenString = "delete from aufgabe where block = ? and frage = ?;";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtLoeschen = myConn.prepareStatement(loeschenString)) {
            
            stmtLoeschen.setString(1, blockname);
            stmtLoeschen.setString(2, frage);
            
            stmtLoeschen.executeUpdate();

        } catch (SQLException exc) {
            throw exc;
        }
    }*/
    public void updateAntworttext(String block, String frage, String alteAntwort, String neueAntwort) throws SQLException {
        String updateString = "update antwort set antworttext = ? "
                + "join aufgabe on antwort.aufgabe = aufgabe.aid"
                + "join block on aufgabe.block = block.bid"
                + "where aufgabe.frage = ? and antwort.antworttext = ?";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtUpdate = myConn.prepareStatement(updateString)) {

            stmtUpdate.setString(1, neueAntwort);
            stmtUpdate.setString(2, frage);
            stmtUpdate.setString(3, alteAntwort);

            stmtUpdate.executeUpdate();
        }
    }

    public void createBlock(String block, String lehrer, String kategorie) throws SQLException {
        String createString = "insert into block(bid, lehrer, kategorie) values(?, ?, ?);";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtUpdate = myConn.prepareStatement(createString)) {

            stmtUpdate.setString(1, block);
            stmtUpdate.setString(2, lehrer);
            stmtUpdate.setString(3, kategorie);

            stmtUpdate.executeUpdate();

        }

    }

    public void createKategorie(String katName, String fachName) throws SQLException {
        String createString = "insert into kategorie(kid, fach) values(?,?);";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtUpdate = myConn.prepareStatement(createString)) {

            stmtUpdate.setString(1, katName);
            stmtUpdate.setString(2, fachName);

            stmtUpdate.executeUpdate();
        }
    }

    public void createAufgabeInBlock(String block, String frage, VBox vb) throws SQLException {
        String insertString = "insert into aufabe(block, frage) values(?, ?);";

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtNewAufgabe = myConn.prepareStatement(insertString)) {

            stmtNewAufgabe.setString(1, block);
            stmtNewAufgabe.setString(2, frage);

            stmtNewAufgabe.executeUpdate();

        }

    }

    private void createAntwortenInAufgabe(String block, String frage, VBox vb) throws SQLException {
        for (int i = 0; i < vb.getChildren().size(); i++) {
            HBox hb = (HBox) vb.getChildren().get(i);
            for (int j = 0; j < hb.getChildren().size(); j++) {
                RadioButton rb = (RadioButton) hb.getChildren().get(j);
                j++;
                TextField tf = (TextField) hb.getChildren().get(j);
                if (rb.isSelected()) {
                    System.out.println("createAntwortenInAufgabe true");
                    createAntwort(tf.getText(), true, block, frage);
                } else {
                    System.out.println("createAntwortenInAufgabe false");
                    createAntwort(tf.getText(), false, block, frage);
                }
            }
        }
    }

    private void createAntwort(String antworttext, boolean isRichtig, String block, String frage) throws SQLException {
        String insertAntwort = "insert into antwort(antworttext, istrue, aufgabe) values(?, ?, (select aufgabe.aid from aufgabe where "
                + "aufgabe.block = ? and aufgabe.frage = ?));";

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtNewAntwort = myConn.prepareStatement(insertAntwort)) {

            stmtNewAntwort.setString(1, antworttext);
            stmtNewAntwort.setBoolean(2, isRichtig);
            stmtNewAntwort.setString(3, block);
            stmtNewAntwort.setString(4, frage);

            System.out.println("createAntwort davor");
            stmtNewAntwort.executeUpdate();
        }

    }

    public void updateQuiz(String blockAlt, String lehrer, String blockNeu) throws SQLException {
        String updateString = "update block set block.bid = ? where block.bid = ? and block.lehrer = ?;";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtNewName = myConn.prepareStatement(updateString)) {
            stmtNewName.setString(1, blockNeu);
            stmtNewName.setString(2, blockAlt);
            stmtNewName.setString(3, lehrer);

            stmtNewName.executeUpdate();
        }
    }

    public void updateTask(String block, String frageAlt, String frageNeu) throws SQLException {
        String updateString;
        if (frageAlt != null) {
            updateString = "update aufgabe set aufgabe.frage = ? where aufgabe.block = ? and aufgabe.frage = ?;";
        } else {
            updateString = "insert into aufgabe(block, frage) values(?, ?);";
        }
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtNewQuestion = myConn.prepareStatement(updateString)) {
            if (frageAlt != null) {
                stmtNewQuestion.setString(1, frageNeu);
                stmtNewQuestion.setString(2, block);
                stmtNewQuestion.setString(3, frageAlt);
            } else{
                stmtNewQuestion.setString(1, block);
                stmtNewQuestion.setString(2, frageNeu);
            }

            stmtNewQuestion.executeUpdate();
        }
    }

    public void updateAnswers(String block, String frage, String lehrer, VBox neueAntworten) throws SQLException {
        String deleteString = "delete from antwort where antwort.aufgabe = (select aufgabe.aid from aufgabe where aufgabe.frage = ? "
                + "and aufgabe.block = (select block.bid from block where block.bid = ? and block.lehrer = ?));";

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtDeleteAntwort = myConn.prepareStatement(deleteString)) {
            stmtDeleteAntwort.setString(1, frage);
            stmtDeleteAntwort.setString(2, block);
            stmtDeleteAntwort.setString(3, lehrer);

            System.out.println("updateAnswer");
            stmtDeleteAntwort.executeUpdate();

            createAntwortenInAufgabe(block, frage, neueAntworten);
        }
    }

    public void loadAbsolvierteSchueler(String blockName, String lehrer) throws SQLException {
        String searchString = "select * from schueler join schuelerloestblock on schueler.sid = schuelerloestblock.schueler "
                + "join block on schuelerloestblock.block = block.bid "
                + "join lehrer on block.lehrer = lehrer.lid "
                + "where block.bid = ? "
                + "and lehrer.lid = ?;";
        ResultSet rsSearchSchueler = null;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtSearchSchueler = myConn.prepareStatement(searchString)) {

            stmtSearchSchueler.setString(1, blockName);
            stmtSearchSchueler.setString(2, lehrer);

            rsSearchSchueler = stmtSearchSchueler.executeQuery();

            absolvierteSchueler.clear();
            while (rsSearchSchueler.next()) {
                absolvierteSchueler.add(rsSearchSchueler.getString("sid"));
            }
        }
    }

    public void loadAbsolvierteAntworten(String block, String lehrer, String schueler) throws SQLException {
        String loadString = "select lehrer.vorname, lehrer.nachname, schueler.vorname, schueler.nachname, aufgabe.frage, schuelerloestaufgabe.antwortS, antwort.antworttext, fach.kuerzel, fach.fid, kategorie.kid "
                + "from schueler "
                + "join schuelerloestaufgabe on schueler.sid = schuelerloestaufgabe.schueler "
                + "join aufgabe on schuelerloestaufgabe.aufgabe = aufgabe.aid "
                + "join block on aufgabe.block = block.bid "
                + "join kategorie on block.kategorie = kategorie.kid "
                + "join fach on kategorie.fach = fach.fid "
                + "join lehrer on block.lehrer = lehrer.lid "
                + "join antwort on antwort.aufgabe = aufgabe.aid "
                + "where antwort.istrue = true "
                + "and block.bid = ? "
                + "and lehrer.lid = ? "
                + "and schueler.sid = ?;";
        ResultSet rsSearchAntworten = null;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtSearchAntworten = myConn.prepareStatement(loadString)) {

            stmtSearchAntworten.setString(1, block);
            stmtSearchAntworten.setString(2, lehrer);
            stmtSearchAntworten.setString(3, schueler);

            rsSearchAntworten = stmtSearchAntworten.executeQuery();

            tempAntwortenSchueler.clear();
            while (rsSearchAntworten.next()) {
                tempAntwortenSchueler.add(new AntwortPdfObjekt(rsSearchAntworten.getString("schueler.vorname"),
                        rsSearchAntworten.getString("schueler.nachname"),
                        rsSearchAntworten.getString("aufgabe.frage"),
                        rsSearchAntworten.getString("schuelerloestaufgabe.antwortS"),
                        rsSearchAntworten.getString("antwort.antworttext"),
                        rsSearchAntworten.getString("fach.kuerzel"),
                        rsSearchAntworten.getString("fach.fid"),
                        rsSearchAntworten.getString("kategorie.kid"),
                        rsSearchAntworten.getString("lehrer.vorname"),
                        rsSearchAntworten.getString("lehrer.nachname")));
            }
        } finally {
            if (rsSearchAntworten != null) {
                rsSearchAntworten.close();
            }
        }
    }

    public ArrayList<String> filterList(ArrayList<String> inputList, String filter) {
        ArrayList<String> outputList = new ArrayList<>();
        for (int index = 0; index < inputList.size(); index++) {
            String filteredString = inputList.get(index);
            if (filteredString.contains(filter)) {
                outputList.add(filteredString);
            }
        }
        return outputList;
    }
    /*TODO: 
    Lehrer muessen ihre Aufgaben loeschen koennen
    Lehrer muessen Schueler sehen koennen, die ihre Bloecke geloest haben mit Anzahl richtig geloester Fragen
    Lehrer muss seine Bloecke veraendern koennen (loeschen und aendern der Aufgaben)
    Lehrer muessen Bloecke erstellen koennen
    Adminoberflaeche mit Nutzernamen und Passwoertern
     */
}
