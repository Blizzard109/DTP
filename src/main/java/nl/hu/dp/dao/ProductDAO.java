package nl.hu.dp.dao;

import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductDAO {
    boolean save(Product inProduct) throws SQLException;
    boolean update(Product inProduct) throws SQLException;
    boolean delete(Product inProduct) throws SQLException;
    ArrayList<Product> findAll() throws SQLException;
    ArrayList<Product> findProductByOv(OVChipkaart ovChipkaart);
}
