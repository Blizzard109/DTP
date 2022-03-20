package nl.hu.dp.dao;
import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;

public interface OVChipkaartDAO {
    boolean save(OVChipkaart inOv) throws SQLException;
    boolean update(OVChipkaart inOv) throws SQLException;
    boolean delete(OVChipkaart inOv) throws SQLException;
    ArrayList<OVChipkaart> findAll() throws SQLException;
    OVChipkaart findByReiziger(Reiziger reiziger) throws SQLException;
}
