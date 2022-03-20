package nl.hu.dp.dao;

import nl.hu.dp.domain.Adres;
import nl.hu.dp.domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdresDAOPostgresql implements AdresDAO {
    private Connection connection = null;

    public AdresDAOPostgresql(Connection inConnection) throws SQLException{
        this.connection = inConnection;
    }

    @Override
    public boolean save(Adres inAdres) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id)" +
                            " VALUES (?,?,?,?,?,?);");
            statement.setInt(1, inAdres.getAdres_id());
            statement.setString(2, inAdres.getPostcode());
            statement.setString(3, inAdres.getHuisnummer());
            statement.setString(4, inAdres.getStraat());
            statement.setString(5, inAdres.getWoonplaats());
            statement.setInt(6, inAdres.getReiziger_id());
            return Statement(statement);
        } catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Adres inAdres)  {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE ONLY adres SET postcode = ?," +
                            "huisnummer = ?," +
                            "straat = ?," +
                            "woonplaats = ? " +
                            "WHERE adres_id = ?;");
            statement.setString(1, inAdres.getPostcode());
            statement.setString(2, inAdres.getHuisnummer());
            statement.setString(3, inAdres.getStraat());
            statement.setString(4, inAdres.getWoonplaats());
            statement.setInt(5, inAdres.getAdres_id());
            return Statement(statement);
        } catch (SQLException e){
            System.err.println("SQLException update: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Adres inAdres) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "DELETE FROM adres WHERE adres_id = ?;");
            statement.setInt(1, inAdres.getAdres_id());
            return Statement(statement);
        } catch (SQLException e){
            System.err.println("SQLException delete: " + e.getMessage());
        }
        return false;
    }

    @Override
    public ArrayList<Adres> findAll() {
        ArrayList<Adres> adressen = new ArrayList<Adres>();
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "SELECT * FROM adres;");
            ResultSet results = statement.executeQuery();
            while (results.next()){
                Adres a = new Adres();
                a.setAdres_id(results.getInt("adres_id"));
                a.setHuisnummer(results.getString("huisnummer"));
                a.setPostcode(results.getString("postcode"));
                a.setStraat(results.getString("straat"));
                a.setWoonplaats(results.getString("woonplaats"));
                a.setReiziger_id(results.getInt("reiziger_id"));
                adressen.add(a);
            }
            results.close();
            statement.close();
        } catch (SQLException e){
            System.err.println("SQLException find all: " + e.getMessage());
        }
        return adressen;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        Adres a = new Adres();
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "SELECT * FROM adres WHERE reiziger_id = ?;");
            statement.setInt(1, reiziger.getId());
            ResultSet results = statement.executeQuery();
            if(results.next()) {
                a.setAdres_id(results.getInt("adres_id"));
                a.setHuisnummer(results.getString("huisnummer"));
                a.setPostcode(results.getString("postcode"));
                a.setStraat(results.getString("straat"));
                a.setWoonplaats(results.getString("woonplaats"));
                a.setReiziger_id(results.getInt("reiziger_id"));
            }
            results.close();
            statement.close();
        } catch (SQLException e){
            System.err.println("SQLException find by: " + e.getMessage());
        }
        return a;
    }

    private boolean Statement(PreparedStatement statement) throws SQLException{
        statement.executeUpdate();
        statement.close();
        return true;
    }
}
