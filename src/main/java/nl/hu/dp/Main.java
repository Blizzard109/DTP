package nl.hu.dp;

import nl.hu.dp.dao.*;
import nl.hu.dp.domain.Adres;
import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Reiziger;

import java.sql.*;
import java.util.List;

import static java.lang.String.format;

public class Main {

    private static Connection connection = null;

    private static Connection getConnection() throws SQLException {

        if(connection == null){
            String databaseName = "ovchip";
            String userName = "postgres";
            String password = "Musholm22";

            String url = format(
                    "jdbc:postgresql://localhost/%s?user=%s&password=%s",
                    databaseName, userName, password);

            connection = DriverManager.getConnection(url);
        }

        return connection;
    }

    private static void closeConnection() {
        try {
            if(connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testConnection() throws SQLException {
        getConnection();
        String query = "SELECT * FROM reiziger;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet set = statement.executeQuery();
        while (set != null && set.next()) {
            System.out.println(set.getString("achternaam"));
        }
        closeConnection();
    }

    public static void main(String[] args) {
        System.out.println("Alle reizigers: ");
        try {
            getConnection();
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOPostgresql(connection);
            AdresDAO dao = new AdresDAOPostgresql(connection);
            //testAdresDAO(dao);
            ReizigerDAO rdao = new ReizigerDAOPostgresql(connection, dao, ovChipkaartDAO);
            //testReizigerDAO(rdao);
            testOVDAO(rdao, ovChipkaartDAO, dao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    private static void testOVDAO(ReizigerDAO reizigerDAO, OVChipkaartDAO ovChipkaartDAO, AdresDAO adresDAO) throws SQLException{
        Adres adres = new Adres(1, "3511LX", "37", "Visschersplein", "Utrecht", 1);
        Reiziger reiziger = new Reiziger(1, "G", "van", "Rijn", java.sql.Date.valueOf("2002-09-17"), adres);
        OVChipkaart ov1 = new OVChipkaart(1, java.sql.Date.valueOf("2002-09-17"), 2, 10, 1);
        OVChipkaart ov2 = new OVChipkaart(6, java.sql.Date.valueOf("2002-09-17"), 2, 10, 1);

        reiziger.addToOvChipkaartArryList(ov1);
        reiziger.addToOvChipkaartArryList(ov2);

        reiziger.setAdres(adres);

        System.out.println(reiziger);

        List<Reiziger> reizigers = reizigerDAO.findAll();
        List<OVChipkaart> ovs = ovChipkaartDAO.findAll();

        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        System.out.println("[Test] ovChipkaartDAO.findAll() geeft de volgende ov's:");
        for (OVChipkaart o : ovs) {
            System.out.println(o);
        }
        System.out.println();
        // Delete CRUD-operation
        System.out.println("Aantal reizigers voor delete: " + reizigers.size());
        System.out.println("Aantal ovs voor delete: " + ovs.size());
        reizigerDAO.delete(reiziger);
        reizigers = reizigerDAO.findAll();
        ovs = ovChipkaartDAO.findAll();
        System.out.println("Aantal reizigers na delete: " + reizigers.size());
        System.out.println("Aantal ovs na delete: " + ovs.size());

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        reizigerDAO.save(reiziger);
        reizigers = reizigerDAO.findAll();
        ovs = ovChipkaartDAO.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
        System.out.println(ovs.size() + " ov's\n");
    }

    private static void testAdresDAO(AdresDAO adresDAO) throws SQLException{
        System.out.println("\n---------- Test AdresDAO -------------");


        Adres adres = new Adres(1, "3511LX", "37", "Visschersplein", "Utrecht", 1);
        Reiziger reiziger = new Reiziger(1, "G", "van", "Rijn", java.sql.Date.valueOf("2002-09-17"), adres);
        // Haal alle adressen op uit de database
        List<Adres> adressen = adresDAO.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();
        System.out.println("Aantal adressen: " + adressen.size());

        // Update CRUD-operation
        System.out.println("Find adres met reiziger_id 1 + dat adres voor update: " + adresDAO.findByReiziger(reiziger));
        Adres adres2 = new Adres(1, "3511LX", "42", "Visschersplein", "Utrecht", 1);
        adresDAO.update(adres2);
        System.out.println("Find adres met reiziger_id 1 + dat adres na update: " +adresDAO.findByReiziger(reiziger));

        // Delete CRUD-operation
        System.out.println("Aantal adressen voor delete: " + adressen.size());
        adresDAO.delete(adres);
        adressen = adresDAO.findAll();
        System.out.println("Aantal adressen na delete: " + adressen.size());

        // Maak een nieuw adres aan en persisteer deze in de database
        adresDAO.save(adres);
        adressen = adresDAO.findAll();
        System.out.println("Aantal adressen na save: " + adressen.size());
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");
        Adres adres = new Adres(1, "3511LX", "37", "Visschersplein", "Utrecht", 1);

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum), adres);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.

        // Update CRUD-operation.
        sietske.setVoorletters("U");
        rdao.update(sietske);
        System.out.println("Updated\n");


        // Delete CRUD-operation.
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

    }
}
