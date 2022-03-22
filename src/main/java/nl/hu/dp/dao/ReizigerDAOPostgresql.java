package nl.hu.dp.dao;

import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Reiziger;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReizigerDAOPostgresql implements ReizigerDAO {

    private Connection connection = null;
    private AdresDAO adresDAO;
    private OVChipkaartDAO ovChipkaartDAO;

    public ReizigerDAOPostgresql(Connection inConnection) throws SQLException {
        this.connection = inConnection;
    }

    public ReizigerDAOPostgresql(Connection inConnection, AdresDAO adresDAO){
        this.adresDAO = adresDAO;
        this.connection = inConnection;
    }

    public ReizigerDAOPostgresql(Connection inConnection, AdresDAO adresDAO, OVChipkaartDAO ovChipkaartDAO){
        this.adresDAO = adresDAO;
        this.connection = inConnection;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    @Override
    public boolean save(Reiziger inReiziger) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum)" +
                            " VALUES (?, ?, ?, ?, ?);");
            statement.setInt(1, inReiziger.getId());
            statement.setString(2, inReiziger.getVoorletters());
            statement.setString(3, inReiziger.getTussenvoegsel());
            statement.setString(4, inReiziger.getAchternaam());
            statement.setDate(5, inReiziger.getGeboortedatum());
            statement.executeUpdate();
            statement.close();

            if(this.adresDAO != null){
                this.adresDAO.save(inReiziger.getAdres());
            }

            if(this.ovChipkaartDAO != null){
                for (OVChipkaart a: inReiziger.getOvChipkaartArrayList()
                     ) {
                    this.ovChipkaartDAO.save(a);
                }
            }

            return true;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Reiziger inReiziger) {
        try{
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE ONLY reiziger SET voorletters = ?," +
                            "tussenvoegsel = ?," +
                            "achternaam = ?," +
                            "geboortedatum = ?" +
                            "WHERE reiziger_id = ?;");

            statement.setString(1, inReiziger.getVoorletters());
            statement.setString(2, inReiziger.getTussenvoegsel());
            statement.setString(3, inReiziger.getAchternaam());
            statement.setDate(4, inReiziger.getGeboortedatum());
            statement.setInt(5, inReiziger.getId());
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Reiziger inReiziger){
        try{
            if(this.adresDAO != null){
                this.adresDAO.delete(inReiziger.getAdres());
            }

            if(this.ovChipkaartDAO != null){
                for (OVChipkaart a: inReiziger.getOvChipkaartArrayList()
                ) {
                    this.ovChipkaartDAO.delete(a);
                }
            }

            PreparedStatement statement = this.connection.prepareStatement(
                    "DELETE FROM reiziger WHERE reiziger_id = ?;");

            statement.setInt(1, inReiziger.getId());
            statement.executeUpdate();
            statement.close();
            return true;

        } catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public ArrayList<Reiziger> findAll(){
        ArrayList<Reiziger> reizigers = new ArrayList<Reiziger>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reiziger;");
            ResultSet theSet = statement.executeQuery();
            while (theSet.next()) {
                Reiziger r = new Reiziger();
                r.setId(theSet.getInt("reiziger_id"));
                r.setVoorletters(theSet.getString("voorletters"));
                r.setTussenvoegsel(theSet.getString("tussenvoegsel"));
                r.setAchternaam(theSet.getString("achternaam"));
                r.setGeboortedatum(theSet.getDate("geboortedatum"));
                if (adresDAO != null){
                    r.setAdres(adresDAO.findByReiziger(r));
                }
                reizigers.add(r);
            }
            theSet.close();
            statement.close();
        } catch (SQLException sqlex) {
            System.err.println("SQLException: " + sqlex.getMessage());
        }
        return reizigers;
    }
}
