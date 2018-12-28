package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.event.ActionEvent;
import java.io.*;
import java.lang.reflect.GenericArrayType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;




public class GeografijaDAO {

    private static GeografijaDAO geografija = null;
    private PreparedStatement  upitGlavniGrad, upitDrzavaId, upitGradId, upitBrisanjeGrada, upitBrisanjeGrada2, upitBrisanjeDrzave, upitSortiraniGradovi, upitDrzavaGlavniGrad, upitInsertGrad,
            upitInsertDrzava, upitUpdateDrzava, upitUpdateGrad, upitGaradIzDrzave, upitTraziDrzavu, upitTraziGrad, upitPuniUpdateGrada, upitTraziDrzavuPoIdu, upitTraziGradPoNazivu, upitTraziDrzavuPoGlavnom;
    private static Connection conn;
    private ObservableList<Grad> gradovi = FXCollections.observableArrayList();
    private ObservableList<String> glavniGradovi = FXCollections.observableArrayList();

    public static Connection getConn(){
        return conn;
    }
    private GeografijaDAO() {
        conn = null;
        try {
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            //String url = "jdbc:oracle:thin:@ora.db.lab.ri.etf.unsa.ba:1521:ETFLAB";
            String url = "jdbc:sqlite:baza.db";
            conn = DriverManager.getConnection(url/*, "SP18145", "Iqrjkisw"*/);
            kreirajTabele();
            pripremiUpite();
            popuniTabele();



        }  /*catch (ClassNotFoundException e) {
            System.out.println("Greska");
         } */catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void kreirajTabele() {
        Statement statement= null;
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.execute(new String(Files.readAllBytes(Paths.get("src\\grad.sql")), StandardCharsets.UTF_8));
            statement.execute(new String(Files.readAllBytes(Paths.get("src\\drzava.sql")), StandardCharsets.UTF_8));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pripremiUpite(){
        try {
            upitGlavniGrad = conn.prepareStatement("SELECT grad.id, grad.naziv, grad.brojStanovnika, drzava.naziv FROM grad, drzava WHERE grad.id = drzava.glavniGrad AND drzava.naziv = ?");
            upitDrzavaId = conn.prepareStatement("SELECT drzava.id FROM drzava WHERE drzava.naziv = ?");
            upitBrisanjeGrada=conn.prepareStatement("DELETE FROM grad WHERE drzava = ?");
            upitBrisanjeGrada2 = conn.prepareStatement("DELETE FROM grad WHERE naziv = ?");
            upitBrisanjeDrzave = conn.prepareStatement("DELETE FROM drzava WHERE naziv = ?");
            upitSortiraniGradovi = conn.prepareStatement("SELECT id, naziv, brojStanovnika, drzava FROM grad ORDER BY brojStanovnika DESC");
            upitDrzavaGlavniGrad = conn.prepareStatement("SELECT drzava.naziv, grad.naziv, grad.brojStanovnika FROM drzava, grad WHERE drzava.glavniGrad = grad.id AND drzava.id = ?" );
            upitGaradIzDrzave =conn.prepareStatement("SELECT grad.id, grad.naziv, grad.brojStanovnika, grad.drzava FROM drzava, grad WHERE grad.drzava=drzava.id AND drzava.naziv = ?");
            upitGradId = conn.prepareStatement("SELECT grad.id FROM grad WHERE grad.naziv = ?");
            upitInsertGrad = conn.prepareStatement("INSERT INTO grad VALUES (?, ?, ?, NULL )");
            upitInsertDrzava = conn.prepareStatement("INSERT INTO drzava VALUES (?, ?, ?)");
            upitUpdateDrzava = conn.prepareStatement("UPDATE drzava SET naziv=?, glavniGrad=? WHERE id=?");
            upitUpdateGrad = conn.prepareStatement("UPDATE grad SET drzava=? WHERE grad.id = ?");
            upitPuniUpdateGrada = conn.prepareStatement("UPDATE grad SET naziv=?, brojStanovnika=?, drzava=? WHERE grad.id = ?");
            upitTraziDrzavu = conn.prepareStatement("SELECT id, naziv, glavniGrad from drzava where naziv= ?");
            upitTraziDrzavuPoIdu = conn.prepareStatement("SELECT id, naziv, glavniGrad from drzava where id= ?");
            upitTraziGrad = conn.prepareStatement("SELECT naziv, brojStanovnika, drzava FROM grad WHERE id=?");
            upitTraziGradPoNazivu = conn.prepareStatement("SELECT id, naziv, brojStanovnika, drzava FROM grad WHERE naziv=?");
            upitTraziDrzavuPoGlavnom = conn.prepareStatement("SELECT id, naziv, glavniGrad from drzava WHERE glavniGrad=?");
            //upitTraziGradPoNazivu1= conn.prepareStatement("SELECT id, brojStanovnika, drzava FROM grad WHERE naziv=?");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void popuniTabele() throws SQLException {

        try {
            // insert gradova
            upitInsertGrad.setInt(1, 1);
            upitInsertGrad.setString(2, "Pariz");
            upitInsertGrad.setInt(3, 2206488);
            upitInsertGrad.executeUpdate();

            upitInsertGrad.setInt(1, 2);
            upitInsertGrad.setString(2, "London");
            upitInsertGrad.setInt(3, 8825000);
            upitInsertGrad.executeUpdate();

            upitInsertGrad.setInt(1, 3);
            upitInsertGrad.setString(2, "Beč");
            upitInsertGrad.setInt(3, 1899055);
            upitInsertGrad.executeUpdate();

            upitInsertGrad.setInt(1, 4);
            upitInsertGrad.setString(2, "Manchester");
            upitInsertGrad.setInt(3, 545500);
            upitInsertGrad.executeUpdate();

            upitInsertGrad.setInt(1, 5);
            upitInsertGrad.setString(2, "Graz");
            upitInsertGrad.setInt(3, 280200);
            upitInsertGrad.executeUpdate();

            //insert drzava
            upitInsertDrzava.setInt(1, 1);
            upitInsertDrzava.setString(2, "Francuska");
            upitInsertDrzava.setInt(3, 1);
            upitInsertDrzava.executeUpdate();

            upitInsertDrzava.setInt(1, 2);
            upitInsertDrzava.setString(2, "Velika Britanija");
            upitInsertDrzava.setInt(3, 2);
            upitInsertDrzava.executeUpdate();

            upitInsertDrzava.setInt(1, 3);
            upitInsertDrzava.setString(2, "Austrija");
            upitInsertDrzava.setInt(3, 3);
            upitInsertDrzava.executeUpdate();

            upitUpdateGrad.setInt(1, 1);
            upitUpdateGrad.setInt(2, 1);
            upitUpdateGrad.executeUpdate();

            upitUpdateGrad.setInt(1, 2);
            upitUpdateGrad.setInt(2, 2);
            upitUpdateGrad.executeUpdate();

            upitUpdateGrad.setInt(1, 3);
            upitUpdateGrad.setInt(2, 3);
            upitUpdateGrad.executeUpdate();

            upitUpdateGrad.setInt(1, 2);
            upitUpdateGrad.setInt(2, 4);
            upitUpdateGrad.executeUpdate();

            upitUpdateGrad.setInt(1, 3);
            upitUpdateGrad.setInt(2, 5);
            upitUpdateGrad.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Grad glavniGrad(String drzava){
        try {
            upitTraziDrzavu.setString(1, drzava);
            ResultSet rs = upitTraziDrzavu.executeQuery();
            if(!rs.next()) return null; //ako nije pronadjena drzava
            int idGradaIzDrzave = rs.getInt(3);
            upitTraziGrad.setInt(1,idGradaIzDrzave);
            ResultSet rs2 = upitTraziGrad.executeQuery();
            Drzava d=new Drzava(rs.getInt(1), drzava);
            Grad g= new Grad(idGradaIzDrzave, rs2.getString(1), rs2.getInt(2), d);
            d.setGlavniGrad(g);
            g.setDrzava(d);
            return g;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void obrisiDrzavu(String drzava){
        try {
            upitTraziDrzavu.setString(1, drzava);
            ResultSet rs = upitTraziDrzavu.executeQuery();
            if(!rs.next()) return; //ako nije pronadjena drzava
            //prvo obrisati sve gradove iz drzave
            int idDrzave=rs.getInt(1);
            upitGaradIzDrzave.setString(1, drzava);
            ResultSet rs2 = upitGaradIzDrzave.executeQuery();
            while(rs2.next()){
                upitBrisanjeGrada.setInt(1,idDrzave);
                upitBrisanjeGrada.executeUpdate();
            }
            upitBrisanjeDrzave.setString(1, drzava);
            upitBrisanjeDrzave.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Drzava nadjiDrzavu(String drzava){
        try {
            upitTraziDrzavu.setString(1, drzava);
            ResultSet rs = upitTraziDrzavu.executeQuery();
            if (!rs.next()) return null;
            int idGlavnogGrada = rs.getInt(3);
            upitTraziGrad.setInt(1,idGlavnogGrada);
            ResultSet rs2 = upitTraziGrad.executeQuery();
            Drzava d=new Drzava(rs.getInt(1), drzava);
            Grad glavniGrad= new Grad(idGlavnogGrada, rs2.getString(1), rs2.getInt(2), d);
            d.setGlavniGrad(glavniGrad);
            glavniGrad.setDrzava(d);
            return d;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void obrisiGrad(String grad){
        try {
            upitTraziGradPoNazivu.setString(1, grad);
            ResultSet rs = upitTraziGradPoNazivu.executeQuery();
            upitTraziDrzavuPoGlavnom.setInt(1, rs.getInt(1));
            ResultSet rs2 = upitTraziDrzavuPoGlavnom.executeQuery();
            if(rs2.next()){
                upitTraziDrzavuPoIdu.setInt(1, rs2.getInt(1));
                ResultSet rs3 = upitTraziDrzavuPoIdu.executeQuery();
                obrisiDrzavu(rs3.getString(2));
            }
            upitBrisanjeGrada2.setString(1,grad);
            upitBrisanjeGrada2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Grad nadjiGrad(String grad){
        try {
            upitGradId.setString(1, grad);
            ResultSet a = upitGradId.executeQuery();
            int id=a.getInt(1);
            upitTraziGrad.setInt(1, id);
            ResultSet rs = upitTraziGrad.executeQuery();
            if(!rs.next())return null;
            Grad g= new Grad();
            g.setId(id);
            g.setNaziv(rs.getString(1));
            g.setBroj_Stanovnika(rs.getInt(2));
            Drzava drzava = new Drzava();
            upitTraziDrzavuPoIdu.setInt(1, rs.getInt(3));
            ResultSet rs2 = upitTraziDrzavuPoIdu.executeQuery();
            drzava.setId(rs2.getInt(1));
            drzava.setNaziv(rs2.getString(2));
            upitTraziGrad.setInt(1,rs2.getInt(3));
            ResultSet rs3 = upitTraziGrad.executeQuery();
            Grad glavni=new Grad(rs3.getInt(1), rs3.getString(2), rs3.getInt(3), drzava);
            drzava.setGlavniGrad(glavni);
            g.setDrzava(drzava);
            return g;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }



    private static void initialize() {
        geografija = new GeografijaDAO();
    }

    public static void removeInstance()  {
        try {
            if(conn!=null) conn.close();
            conn=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        geografija=null;
    }

    public static GeografijaDAO getInstance() {
        if (geografija == null) initialize();
        return geografija;
    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> vrati = new ArrayList<>();
        try {
            ResultSet rs = upitSortiraniGradovi.executeQuery();
            while(rs.next()){
                Grad g=new Grad();
                g.setId(rs.getInt(1));
                g.setNaziv(rs.getString(2));
                g.setBroj_Stanovnika(rs.getInt(3));
                upitTraziDrzavuPoIdu.setInt(1, rs.getInt(4));
                ResultSet rs2 = upitTraziDrzavuPoIdu.executeQuery();
                Drzava d=new Drzava();
                d.setId(rs2.getInt(1));
                d.setNaziv(rs2.getString(2));
                upitTraziGrad.setInt(1, rs2.getInt(3));
                ResultSet rs3 = upitTraziGrad.executeQuery();
                Grad glavni=new Grad(rs2.getInt(3), rs3.getString(1), rs3.getInt(2), d);
                d.setGlavniGrad(glavni);
                g.setDrzava(d);
                vrati.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vrati;

    }
    public  ObservableList<Grad> getGradovi(){
        ArrayList<Grad> lista=gradovi();
        for(Grad g: lista) gradovi.add(g);
        return gradovi;
    }

    public ObservableList<String> getGlavniGradovi(){

        ArrayList<Grad> lista=gradovi();
        for(Grad g: lista) {
            glavniGradovi.add(g.getDrzava().getGlavniGrad().getNaziv());
        }
        return glavniGradovi;
    }



    public void dodajGrad(Grad grad) {
        try {
            upitTraziGrad.setInt(1, grad.getId());
            ResultSet rs = upitTraziGrad.executeQuery();
            if(!rs.next()){ //grad vec postoji
                upitInsertGrad.setInt(1, grad.getId());
                upitInsertGrad.setString(2, grad.getNaziv());
                upitInsertGrad.setInt(3, grad.getBroj_Stanovnika());
                upitInsertGrad.executeUpdate();
                upitTraziDrzavu.setString(1, grad.getDrzava().getNaziv());
                ResultSet rs2=upitTraziDrzavu.executeQuery();
                if(!rs2.next()){
                    dodajDrzavu(grad.getDrzava());
                }
                upitTraziDrzavu.setString(1, grad.getDrzava().getNaziv());
                rs2=upitTraziDrzavu.executeQuery();
                int idDrzave = rs2.getInt(1);
                upitUpdateGrad.setInt(1,idDrzave);
                upitUpdateGrad.setInt(2, grad.getId());
                upitUpdateGrad.executeUpdate();}

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            upitTraziDrzavu.setString(1, drzava.getNaziv());
            ResultSet rs=upitTraziDrzavu.executeQuery();
            if(!rs.next()){
                Grad glavni=new Grad(drzava.getGlavniGrad());
                dodajGrad(glavni); //dodat ce ako ga nema  inace nece
                upitInsertDrzava.setInt(1, drzava.getId());
                upitInsertDrzava.setString(2, drzava.getNaziv());
                upitInsertDrzava.setInt(3, glavni.getId());
                upitInsertDrzava.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void izmijeniGrad(Grad grad) {
        try {
            upitTraziGradPoNazivu.setString(1, grad.getNaziv());
            ResultSet rs = upitTraziGrad.executeQuery();
            if(!rs.next()) dodajGrad(grad);
            else{
                upitPuniUpdateGrada.setString(1, grad.getNaziv());
                upitPuniUpdateGrada.setInt(2, grad.getBroj_Stanovnika());
                upitPuniUpdateGrada.setInt(3, grad.getDrzava().getId());
                upitPuniUpdateGrada.setInt(4, grad.getId());
                upitPuniUpdateGrada.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
