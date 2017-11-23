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
    ArrayList<String> aufgabenblöcke;
    ArrayList<String> fragen;
    ArrayList<String> antwortenTemp;
    String loginLehrer;
    String loginSchüler;

    public SqlLogik() {
        userInfo = new Properties();
        userInfo.put("user", "root");
        userInfo.put("password", "databasemarcel");
        aufgabenblöcke = new ArrayList<>();
        fragen = new ArrayList<>();
        antwortenTemp = new ArrayList<>();
        loginLehrer = null;
        loginSchüler = null;
    }

    @Override
    public boolean checkAntwort(String blockBez, String aFrage, String aAntwort) throws SQLException {
        String stmtString = "select isTrue from antwort join aufgabe on antwort.aufgabe = aufgabe.aid"
                + " join block on aufgabe.block = block.bid where block.bid = ? and aufgabe.frage = ? and antwort.antworttext = ?";
        ResultSet rsAntwort = null;

        boolean check = false;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtAntwort = myConn.prepareStatement(stmtString)) {

            stmtAntwort.setString(1, blockBez);
            stmtAntwort.setString(2, aFrage);
            stmtAntwort.setString(3, aAntwort);
            
            rsAntwort = stmtAntwort.executeQuery();

            while (rsAntwort.next()) {
                if (rsAntwort.getString("isTrue").equals("1")) {
                    check = true;
                }
            }
        } catch (SQLException exc) {
            throw exc;
        } finally{
            if(rsAntwort != null){
                rsAntwort.close();
            }
        }
        return check;
    }

    @Override
    public boolean checkLogin(String user, String password) throws SQLException {

        String stringCheck = "select lid, lehrer.passwort, sid, schüler.passwort from lehrer, schüler";
        boolean checkPassword = false;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                Statement stmtCheck = myConn.createStatement();
                ResultSet rsCheck = stmtCheck.executeQuery(stringCheck)) {

            while (rsCheck.next()) {
                if (rsCheck.getString("lid").equals(user)) {
                    checkPassword = rsCheck.getString("lehrer.passwort").equals(password);
                } else if (rsCheck.getString("sid").equals(user)) {
                    checkPassword = rsCheck.getString("schüler.passwort").equals(password);
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
    public void loadSchüler(String sid) throws SQLException {
        String stringSchüler = "select vorname, nachname from schüler where sid = ?";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtSchüler = myConn.prepareStatement(stringSchüler);
                ResultSet rsSchüler = stmtSchüler.executeQuery()) {

            while (rsSchüler.next()) {
                loginLehrer = rsSchüler.getString("vorname") + " " + rsSchüler.getString("nachname");
            }

        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void loadBlöcke() throws SQLException {
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                Statement stmtAufgaben = myConn.createStatement();
                ResultSet rsAufgaben = stmtAufgaben.executeQuery("select bid from block")) {

            while (rsAufgaben.next()) {
                aufgabenblöcke.add(rsAufgaben.getString("bid"));
            }
        } catch (SQLException exc) {
            throw exc;
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
            for(int i = 0; i < fragen.size(); i++){
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
        } finally{
            if(rsAntworten != null){
                rsAntworten.close();
            }
        }
    }
}
