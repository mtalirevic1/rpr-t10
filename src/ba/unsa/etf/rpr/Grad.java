package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Grad {
    private SimpleIntegerProperty id=new SimpleIntegerProperty(0);
    private SimpleStringProperty naziv=new SimpleStringProperty("");
    private SimpleIntegerProperty broj_Stanovnika=new SimpleIntegerProperty(0);
    private Drzava drzava;

    public Grad(){}

    public Grad (int id, String naziv, int brojStanovnika) {
        this.id.setValue(id);
        this.naziv.setValue(naziv);
        this.broj_Stanovnika.setValue(brojStanovnika);
        drzava=null;
    }

    public Grad( String naziv, int brojStanovnika){
        this.id.setValue(0);
        this.naziv.setValue(naziv);
        this.broj_Stanovnika.setValue(brojStanovnika);
        drzava=null;
    }

    public SimpleIntegerProperty getIdProperty(){
        return id;
    }

    public SimpleIntegerProperty getBroj_StanovnikaProperty(){
        return broj_Stanovnika;
    }

    public SimpleStringProperty getNazivProperty(){
        return naziv;
    }

    public int getId() {
        return id.getValue();
    }

    public void setId(int id) {
        this.id.setValue(id);
    }

    public String getNaziv() {
        return naziv.getValue();
    }

    public void setNaziv(String naziv) {
        this.naziv.setValue(naziv);
    }

    public int getBroj_Stanovnika() {
        return broj_Stanovnika.getValue();
    }

    public void setBroj_Stanovnika(int broj_Stanovnika) {
        this.broj_Stanovnika.setValue(broj_Stanovnika);
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void setDrzava(Drzava drz) {
        drzava = drz;
    }

}
