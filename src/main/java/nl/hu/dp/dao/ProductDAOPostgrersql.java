package nl.hu.dp.dao;

import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class ProductDAOPostgrersql implements ProductDAO {

    private Connection connection = null;

    public ProductDAOPostgrersql(Connection inConnection) throws SQLException {
        this.connection = inConnection;
    }

    @Override
    public boolean save(Product inProduct) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "INSERT INTO product (product_nummer, naam, beschrijving, prijs)" +
                            " VALUES (?,?,?,?);");
            statement.setInt(1, inProduct.getProduct_nummer());
            statement.setString(2, inProduct.getNaam());
            statement.setString(3, inProduct.getBeschrijving());
            statement.setDouble(4, inProduct.getPrijs());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            System.err.println("SQLException save: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Product inProduct) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE ONLY product SET naam = ?," +
                            "beschrijving = ?," +
                            "prijs = ?" +
                            "WHERE product_nummer = ?;");
            statement.setString(1, inProduct.getNaam());
            statement.setString(2, inProduct.getBeschrijving());
            statement.setDouble(3, inProduct.getPrijs());
            statement.setInt(4, inProduct.getProduct_nummer());
            return Statement(statement);
        } catch (SQLException e){
            System.err.println("SQLException update: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Product inProduct) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "DELETE FROM product WHERE product_nummer = ?;");
            statement.setInt(1, inProduct.getProduct_nummer());
            statement.execute();

           statement.close();
            return true;
        } catch (SQLException e){
            System.err.println("SQLException delete: " + e.getMessage());
        }
        return false;
    }

    @Override
    public ArrayList<Product> findAll() {
        ArrayList<Product> producten = new ArrayList<Product>();
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "SELECT * FROM product;");
            ResultSet results = statement.executeQuery();
            while (results.next()){
                Product a = new Product();
                a.setProduct_nummer(results.getInt("product_nummer"));
                a.setNaam(results.getString("naam"));
                a.setBeschrijving(results.getString("beschrijving"));
                a.setPrijs(results.getDouble("prijs"));
                producten.add(a);
            }
            results.close();
            statement.close();
        } catch (SQLException e){
            System.err.println("SQLException find all: " + e.getMessage());
        }
        return producten;
    }

    @Override
    public ArrayList<Product> findProductByOv(OVChipkaart ovChipkaart) {
        ArrayList<Product> productsReturn = new ArrayList<Product>();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "SELECT * FROM ov_chipkaart_product WHERE kaart_nummer = ?;");

            statement.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Product p = new Product();
                p.setProduct_nummer(results.getInt("product_nummer"));
                products.add(p);
            }
            results.close();

            for (Product p: products) {
                statement = this.connection.prepareStatement(
                        "SELECT * FROM product WHERE product_nummer = ?;");
                statement.setInt(1, p.getProduct_nummer());
                ResultSet results2 = statement.executeQuery();
                if(results2.next()) {
                    Product newProduct = new Product();
                    newProduct.setProduct_nummer(results2.getInt("product_nummer"));
                    newProduct.setBeschrijving(results2.getString("beschrijving"));
                    newProduct.setNaam(results2.getString("naam"));
                    newProduct.setPrijs(results2.getDouble("prijs"));
                    productsReturn.add(newProduct);
                }
                results2.close();
            }
            statement.close();
        } catch (SQLException e){
            System.err.println("SQLException find by: " + e.getMessage());
        }

        return productsReturn;
    }

    private boolean Statement(PreparedStatement statement) throws SQLException{
        statement.executeUpdate();
        statement.close();
        return true;
    }
}
