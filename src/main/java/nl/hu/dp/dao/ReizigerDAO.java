package nl.hu.dp.dao;
import nl.hu.dp.domain.Reiziger;
import java.sql.*;
import java.util.ArrayList;


public interface ReizigerDAO {
    boolean save(Reiziger inReiziger) throws SQLException;
    boolean update(Reiziger inReiziger) throws SQLException;
    boolean delete(Reiziger inReiziger) throws SQLException;
    ArrayList<Reiziger> findAll() throws SQLException;
}
