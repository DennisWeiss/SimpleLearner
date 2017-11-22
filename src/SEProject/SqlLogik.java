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

    public SqlLogik() {
        userInfo = new Properties();
        userInfo.put("user", "root");
        userInfo.put("password", "databasemarcel");
        aufgabenblöcke = new ArrayList<>();
        fragen = new ArrayList<>();
        antwortenTemp = new ArrayList<>();
    }

    @Override
    public boolean checkAntwort(String blockBez, String aFrage, String aAntwort) throws SQLException {
        String stmtString = "select isTrue from antwort join aufgabe on antwort.aufgabe = aufgabe.aid"
                + " join block on aufgabe.block = block.bid where block.bid = ? and aufgabe.frage = ? and antwort.antworttext = ?";

        boolean check = false;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtAntwort = myConn.prepareStatement(stmtString);
                ResultSet rsAntwort = stmtAntwort.executeQuery()) {

            stmtAntwort.setString(1, blockBez);
            stmtAntwort.setString(2, aFrage);
            stmtAntwort.setString(3, aAntwort);

            while (rsAntwort.next()) {
                if (rsAntwort.getBoolean("isTrue") == true) {
                    check = false;
                }
            }
        } catch (SQLException exc) {
            throw exc;
        }
        return check;
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
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtFrage = myConn.prepareStatement(stringFrage);
                ResultSet rsFrage = stmtFrage.executeQuery()) {

            stmtFrage.setString(1, block);

            while (rsFrage.next()) {
                fragen.add(rsFrage.getString("frage"));
            }
        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void loadAntworten(String block, String frage) throws SQLException {
        String stringAntworten = "select antworttext from antwort join aufgabe on antwort.aufgabe = aufgabe.aid "
                + "join block on aufgabe.block = block.bid where block.bid = ? and aufgabe.frage = ?";
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SimpleLearner?useSSL=true", userInfo);
                PreparedStatement stmtAntworten = myConn.prepareStatement(stringAntworten);
                ResultSet rsAntworten = stmtAntworten.executeQuery()) {

            stmtAntworten.setString(1, block);
            stmtAntworten.setString(2, frage);

            antwortenTemp.clear();

            while (rsAntworten.next()) {
                antwortenTemp.add(rsAntworten.getString("antworttext"));
            }
        } catch (SQLException exc) {
            throw exc;
        }
    }
}
