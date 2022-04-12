package nl.hu.dp.dao;

import nl.hu.dp.domain.Adres;
import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Product;
import nl.hu.dp.domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class OVChipkaartDAOPostgresql implements OVChipkaartDAO{
    private Connection connection = null;
    private ProductDAO productDAO = null;

    public OVChipkaartDAOPostgresql(Connection inConnection) throws SQLException{
        this.connection = inConnection;
    }

    public OVChipkaartDAOPostgresql(Connection inConnection, ProductDAO productDAO) throws SQLException{
        this.connection = inConnection;
        this.productDAO = productDAO;
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
            statement.executeUpdate();
            statement.close();


            if(this.productDAO != null){
                for (Product p: inOv.getProductArrayList()){
                    this.productDAO.save(p);
                    statement = this.connection.prepareStatement(
                            "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update)" +
                                    "VALUES (?,?,?,?);");
                    statement.setInt(1, inOv.getKaart_nummer());
                    statement.setInt(2, p.getProduct_nummer());
                    statement.setString(3,"actief");
                    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                    statement.setDate(4, date);
                    statement.execute();
                }
            }

            return true;
        } catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(OVChipkaart inOv) {
        try {
            if(this.productDAO != null){
                ArrayList<OVChipkaart> kaarten = new ArrayList<OVChipkaart>();
                PreparedStatement statement = this.connection.prepareStatement(
                        "SELECT * FROM ov_chipkaart_product WHERE kaart_nummer = ?;");

                statement.setInt(1, inOv.getKaart_nummer());
                ResultSet results = statement.executeQuery();

                if(results.next()) {
                    OVChipkaart a = new OVChipkaart();
                    a.setKaart_nummer(results.getInt("kaart_nummer"));
                    statement = this.connection.prepareStatement(
                            "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?;");
                    statement.setInt(1, inOv.getKaart_nummer());
                    ResultSet resultser = statement.executeQuery();
                    if(resultser.next()){
                        a.setGeldig_tot(resultser.getDate("geldig_tot"));
                        a.setReiziger_id(resultser.getInt("reiziger_id"));
                    }
                    Product p = new Product();
                    statement = this.connection.prepareStatement(
                            "SELECT * FROM product WHERE product_nummer = ?;");
                    statement.setInt(1, results.getInt("product_nummer"));
                    ResultSet presult = statement.executeQuery();
                    if(presult.next()){
                        p.setNaam(presult.getString("naam"));
                        p.setBeschrijving("BESCHRIJVING");
                    }
                    p.setProduct_nummer(results.getInt("product_nummer"));
                    a.addProductToArrayList(p);
                    p.setOvChipkaart(a);
                    kaarten.add(a);
                }
                results.close();

                for(OVChipkaart ov: kaarten){
                    this.delete(ov);
                }

                for (OVChipkaart ov: kaarten){
                    this.save(ov);
                }

                for(Product p: inOv.getProductArrayList()){
                    this.productDAO.update(p);
                }
            }

            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE ONLY ov_chipkaart SET geldig_tot = ?," +
                            "klasse = ?," +
                            "saldo = ?" +
                            "WHERE kaart_nummer = ?;");
            statement.setDate(1, inOv.getGeldig_tot());
            statement.setInt(2, inOv.getKlasse());
            statement.setDouble(3, inOv.getSaldo());
            statement.setInt(4, inOv.getKaart_nummer());
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e){
            System.err.println("SQLException update: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(OVChipkaart inOv) {
        try {
            if(this.productDAO != null){
                for (Product p: inOv.getProductArrayList()){

                    PreparedStatement statement = this.connection.prepareStatement("DELETE FROM ov_chipkaart_product " +
                            "WHERE product_nummer = ? AND kaart_nummer = ?;");
                    statement.setInt(1, p.getProduct_nummer());
                    System.out.println(p.getOvChipkaart().getKaart_nummer());
                    statement.setInt(2, inOv.getKaart_nummer());
                    statement.execute();
                    statement.close();
                    this.productDAO.delete(p);
                }
            }

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
