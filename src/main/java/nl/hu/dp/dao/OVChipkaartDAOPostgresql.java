package nl.hu.dp.dao;

import nl.hu.dp.domain.Adres;
import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OVChipkaartDAOPostgresql implements OVChipkaartDAO{
    private Connection connection = null;

    public OVChipkaartDAOPostgresql(Connection inConnection) throws SQLException{
        this.connection = inConnection;
    }

    @Override
    public boolean save(OVChipkaart inOv) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id)" +
                            " VALUES (?,?,?,?,?);");
            statement.setInt(1, inOv.getKaart_nummer());
            statement.setDate(2, inOv.getGeldig_tot());
            statement.setInt(3, inOv.getKlasse());
            statement.setDouble(4, inOv.getSaldo());
            statement.setInt(5, inOv.getReiziger_id());
            return Statement(statement);
        } catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(OVChipkaart inOv) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE ONLY ov_chipkaart SET geldig_tot = ?," +
                            "klasse = ?," +
                            "saldo = ?" +
                            "WHERE kaart_nummer = ?;");
            statement.setDate(1, inOv.getGeldig_tot());
            statement.setInt(2, inOv.getKlasse());
            statement.setDouble(3, inOv.getSaldo());
            statement.setInt(4, inOv.getKaart_nummer());
            return Statement(statement);
        } catch (SQLException e){
            System.err.println("SQLException update: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(OVChipkaart inOv) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?;");
            statement.setInt(1, inOv.getKaart_nummer());
            return Statement(statement);
        } catch (SQLException e){
            System.err.println("SQLException delete: " + e.getMessage());
        }
        return false;
    }

    @Override
    public ArrayList<OVChipkaart> findAll() {
        ArrayList<OVChipkaart> kaarten = new ArrayList<OVChipkaart>();
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "SELECT * FROM ov_chipkaart;");
            ResultSet results = statement.executeQuery();
            while (results.next()){
                OVChipkaart a = new OVChipkaart();
                a.setKaart_nummer(results.getInt("kaart_nummer"));
                a.setGeldig_tot(results.getDate("geldig_tot"));
                a.setKlasse(results.getInt("klasse"));
                a.setSaldo(results.getDouble("saldo"));
                a.setReiziger_id(results.getInt("reiziger_id"));
                kaarten.add(a);
            }
            results.close();
            statement.close();
        } catch (SQLException e){
            System.err.println("SQLException find all: " + e.getMessage());
        }
        return kaarten;
    }

    @Override
    public OVChipkaart findByReiziger(Reiziger reiziger) {
        OVChipkaart a = new OVChipkaart();
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?;");
            statement.setInt(1, reiziger.getId());
            ResultSet results = statement.executeQuery();
            if(results.next()) {
                a.setKaart_nummer(results.getInt("kaart_nummer"));
                a.setGeldig_tot(results.getDate("geldig_tot"));
                a.setKlasse(results.getInt("klasse"));
                a.setSaldo(results.getDouble("saldo"));
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
