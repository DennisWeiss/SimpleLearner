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

/**
 *
 * @author Marcel
 */
public class SqlLogik implements ISqlLogik {

    Properties userInfo;
    ArrayList<String> faecher;
    ArrayList<String> kategorien;
    ArrayList<String> aufgabenbloecke;
    ArrayList<String> fragen;
    ArrayList<String> antwortenTemp;
    String loginLehrer;
    String loginSchueler;
    private String currentUser;

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
        fragen = new ArrayList<>();
        antwortenTemp = new ArrayList<>();
        loginLehrer = null;
        loginSchueler = null;
        currentUser = null;
    }

    @Override
    public boolean checkAntwort(String blockBez, String aSchueler, String aFrage, String aAntwort) throws SQLException {

        String antwortString = "update schuelerloestaufgabe set antwortS = ? "
                + "where aufgabe = (select aid from aufgabe where frage = ?) "
                + "and aktBlock = (select slbid from schuelerloestblock where block = ? and schueler = ?);";

        String stmtString = "select schuelerloestaufgabe.antwortS , antwort.isTrue from schuelerloestaufgabe "
                + "join schuelerloestblock on schuelerloestaufgabe.aktBlock = schuelerloestblock.SLBID "
                + "join aufgabe on schuelerloestaufgabe.Aufgabe = aufgabe.aid "
                + "join antwort on aufgabe.aid = antwort.aufgabe "
                + "where aufgabe.frage = ?"
                + "and schuelerloestBlock.schueler = ? "
                + "and antwort.antworttext = ?;";

        /*"select isTrue from antwort join aufgabe on antwort.aufgabe = aufgabe.aid"
                + " join block on aufgabe.block = block.bid where block.bid = ? and aufgabe.frage = ? and antwort.antworttext = ?";*/
        ResultSet rsAntwort = null;

        boolean check = false;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement setAntwort = myConn.prepareStatement(antwortString);
                PreparedStatement stmtAntwort = myConn.prepareStatement(stmtString)) {

            setAntwort.setString(1, aAntwort);
            setAntwort.setString(2, aFrage);
            setAntwort.setString(3, blockBez);
            setAntwort.setString(4, aSchueler);
            setAntwort.execute();

            stmtAntwort.setString(1, aFrage);
            stmtAntwort.setString(2, aSchueler);
            stmtAntwort.setString(3, aAntwort);
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void loadBloecke(String kategorie, String lehrer) throws SQLException {
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
        } finally{
            if(rsBlock != null) rsBlock.close();
        }
    }

    @Override
    public void loadBloecke(String kategorie) throws SQLException {
        String blockString = "select bid from block where kategorie = ?";
        ResultSet rsBlock = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtBlock = myConn.prepareStatement(blockString)) {

            stmtBlock.setString(1, kategorie);
            rsBlock = stmtBlock.executeQuery();
            if (aufgabenbloecke != null) {
                aufgabenbloecke.clear();
            }

            while (rsBlock.next()) {
                aufgabenbloecke.add(rsBlock.getString("bid"));
            }
        } catch (SQLException exc) {
            throw exc;
        } finally{
            if(rsBlock != null) rsBlock.close();
        }
    }

    @Override
    public void loadFragen(String block) throws SQLException {
        String stringFrage = "select frage from aufgabe join block on aufgabe.block = block.bid where block.bid = ?";
        ResultSet rsFrage = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtFrage = myConn.prepareStatement(stringFrage)) {

            stmtFrage.setString(1, block);
            rsFrage = stmtFrage.executeQuery();

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

    @Override
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
}
