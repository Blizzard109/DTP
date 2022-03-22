package nl.hu.dp.domain;

import java.sql.Date;
import java.util.ArrayList;

public class Reiziger {
    private int id = 0;
    private String voorletters = "";
    private String tussenvoegsel = "";
    private String achternaam = "";
    private Date geboortedatum = new Date(0);
    private Adres adres;
    private ArrayList<OVChipkaart> ovChipkaartArrayList = new ArrayList<OVChipkaart>();

    public Reiziger(){};

    public Reiziger(int id, String vl, String tv, String an, Date gb, Adres adres){
        this.id = id;
        this.voorletters = vl;
        this.tussenvoegsel = tv;
        this.achternaam = an;
        this.geboortedatum = gb;
        this.adres = adres;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        if (voorletters != null) {
            if (voorletters.length() > 10) {
                voorletters = voorletters.substring(0, 10);
            }
            this.voorletters = voorletters;
        }
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public ArrayList<OVChipkaart> getOvChipkaartArrayList() {
        return ovChipkaartArrayList;
    }

    public void setOvChipkaartArrayList(ArrayList<OVChipkaart> ovChipkaartArrayList) {
        this.ovChipkaartArrayList = ovChipkaartArrayList;
    }

    public void addToOvChipkaartArryList(OVChipkaart ovChipkaart){
        this.ovChipkaartArrayList.add(ovChipkaart);
    }

    @Override
    public String toString() {
        return "Reiziger{" +
                "id=" + id +
                ", voorletters='" + voorletters + '\'' +
                ", tussenvoegsel='" + tussenvoegsel + '\'' +
                ", achternaam='" + achternaam + '\'' +
                ", geboortedatum=" + geboortedatum +
                ", adres=" + adres +
                ", ovChipkaartArrayList=" + ovChipkaartArrayList +
                '}';
    }
}
