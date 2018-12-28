package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Drzava {
    private SimpleIntegerProperty id=new SimpleIntegerProperty(0);
    private SimpleStringProperty naziv=new SimpleStringProperty("");
    private Grad glavniGrad;

    public Drzava(){}

    public Drzava( int id, String naziv, Grad gg) {
        this.id.setValue(id);
        this.naziv.setValue(naziv);
        glavniGrad=gg;
    }

    public Drzava( String naziv, Grad gg) {
        id.setValue(0);
        this.naziv.setValue(naziv);
        glavniGrad=gg;
    }

    public SimpleIntegerProperty getIdProperty(){
        return id;
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

    public Grad getGlavniGrad() {
        return glavniGrad;
    }

    public void setGlavniGrad(Grad gg) {
        glavniGrad = gg;
    }
}
