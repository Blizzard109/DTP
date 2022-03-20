package nl.hu.dp.dao;

import nl.hu.dp.domain.Adres;
import nl.hu.dp.domain.Reiziger;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AdresDAO {
    boolean save(Adres inAdres) throws SQLException;
    boolean update(Adres inAdres) throws SQLException;
    boolean delete(Adres inAdres) throws SQLException;
    ArrayList<Adres> findAll() throws SQLException;
    Adres findByReiziger(Reiziger reiziger) throws SQLException;
}
