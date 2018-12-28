package ba.unsa.etf.rpr;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Controller implements Initializable {
    private GeografijaDAO geografijaDAO;

    public TableView tabela;
    public TableColumn kolGrad;
    public TableColumn kolDrzava;
    public TableColumn kolBrojStanovnika;
    public TableColumn kolGlavniGrad;
    public Button izvjestaj;
    private ObservableList<Grad> gradovi = FXCollections.observableArrayList();
    public TextField nazivGrada;
    public TextField nazivDrzave;
    public TextField brojStanovnika;
    public TextField glavniGrad;
    public TextField brojStanovnikaGlavnog;
    ObservableList<Grad> pamti = FXCollections.observableArrayList();
    public BorderPane main;

    Controller(GeografijaDAO geografijaDAO){
        this.geografijaDAO=geografijaDAO;
        gradovi=geografijaDAO.getGradovi();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*tabela.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                model.setTrenutnaKnjiga((Knjiga) newValue);
            }
        });*/

        tabela.setItems(gradovi);
        kolGrad.setCellValueFactory(new PropertyValueFactory<Grad, String>("naziv"));
        kolDrzava.setCellValueFactory(new PropertyValueFactory<Grad, String>("drzava"));
        kolBrojStanovnika.setCellValueFactory(new PropertyValueFactory<Grad, Integer>("brojStanovnika"));
        kolGlavniGrad.setCellValueFactory(new PropertyValueFactory<Grad, String>("glavniGrad"));


    }


    public void izvjestaj (ActionEvent actionEvent) {
        GradoviReport report = new GradoviReport();
        try {
            report.showReport(GeografijaDAO.getConn());
        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    public void obrisiDrzavu(ActionEvent actionEvent){
        if(nazivDrzave.getLength()==0){
            nazivDrzave.getStyleClass().add("poljeNijeIspravno");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Za brisanje države morate unijeti naziv države!");
            alert.setTitle("Greška");
            Optional<ButtonType> result= alert.showAndWait();
        }
        else {
            nazivDrzave.getStyleClass().removeAll("poljeNijeIspravno");
            nazivDrzave.getStyleClass().add("poljeIspravno");
            if(geografijaDAO.nadjiDrzavu(nazivDrzave.getText())==null){
                nazivDrzave.getStyleClass().removeAll("poljeIspravno");
                nazivDrzave.getStyleClass().add("poljeNijeIspravno");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Nepostojeća država!");
                alert.setTitle("Greška");
                Optional<ButtonType> result= alert.showAndWait();
            }
            else {
                nazivDrzave.getStyleClass().removeAll("poljeNijeIspravno");
                nazivDrzave.getStyleClass().add("poljeIspravno");
                geografijaDAO.obrisiDrzavu(nazivDrzave.getText());
                tabela.getItems().clear();
                tabela.refresh();
                gradovi = geografijaDAO.getGradovi();
            }
        }
    }
    public void obrisiGrad(ActionEvent actionEvent){
        if(nazivGrada.getLength()==0){
            nazivGrada.getStyleClass().add("poljeNijeIspravno");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Za brisanje dgrada morate unijeti naziv grada!");
            alert.setTitle("Greška");
            Optional<ButtonType> result= alert.showAndWait();
        }
        else {
            nazivGrada.getStyleClass().removeAll("poljeNijeIspravno");
            nazivGrada.getStyleClass().add("poljeIspravno");
            if(geografijaDAO.nadjiGrad(nazivGrada.getText())==null){
                nazivGrada.getStyleClass().removeAll("poljeIspravno");
                nazivGrada.getStyleClass().add("poljeNijeIspravno");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Nepostojeći grad!");
                alert.setTitle("Greška");
                Optional<ButtonType> result= alert.showAndWait();
            }
            else {
                nazivGrada.getStyleClass().removeAll("poljeNijeIspravno");
                nazivGrada.getStyleClass().add("poljeIspravno");
                geografijaDAO.obrisiGrad(nazivGrada.getText());
                tabela.getItems().clear();
                tabela.refresh();
                gradovi = geografijaDAO.getGradovi();
            }
        }
    }

    public int maxIdGradova(ObservableList<Grad> gradovi){
        int max=gradovi.get(0).getId();
        for(Grad grad:gradovi){
            if(grad.getId()>max) max=grad.getId();
        }
        return max;
    }

    public int maxIdDrzava(ObservableList<Grad> gradovi){
        int max=gradovi.get(0).getDrzava().getId();
        for(Grad grad:gradovi){
            if(grad.getDrzava().getId()>max) max=grad.getDrzava().getId();
        }
        return max;
    }

    public  void dodajGrad(ActionEvent actionEvent){
        if(nazivGrada.getLength()==0 ||nazivDrzave.getLength()==0 || brojStanovnika.getLength()==0 || glavniGrad.getLength()==0 ||brojStanovnikaGlavnog.getLength()==0){
            nazivGrada.getStyleClass().add("poljeNijeIspravno");
            nazivDrzave.getStyleClass().add("poljeNijeIspravno");
            brojStanovnikaGlavnog.getStyleClass().add("poljeNijeIspravno");
            brojStanovnika.getStyleClass().add("poljeNijeIspravno");
            glavniGrad.getStyleClass().add("poljeNijeIspravno");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Za dodavanje grada neophodno je unijeti sve podatke!");
            alert.setTitle("Greška");
            Optional<ButtonType> result= alert.showAndWait();
        }
        else{
            nazivGrada.getStyleClass().removeAll("poljeNijeIspravno");
            nazivGrada.getStyleClass().add("poljeIspravno");
            nazivDrzave.getStyleClass().removeAll("poljeNijeIspravno");
            nazivDrzave.getStyleClass().add("poljeIspravno");
            brojStanovnika.getStyleClass().removeAll("poljeNijeIspravno");
            brojStanovnika.getStyleClass().add("poljeIspravno");
            brojStanovnikaGlavnog.getStyleClass().removeAll("poljeNijeIspravno");
            brojStanovnikaGlavnog.getStyleClass().add("poljeIspravno");
            glavniGrad.getStyleClass().removeAll("poljeNijeIspravno");
            glavniGrad.getStyleClass().add("poljeIspravno");
            int IdZaNoviGrad = maxIdGradova(gradovi) + 1;
            int IdZaGlavni = maxIdGradova(gradovi) +2;
            int IdZaDrzavu = maxIdDrzava(gradovi) +1;
            int brojStanovnikaInt = Integer.parseInt(brojStanovnika.getText());
            int brojStanovnikaGlavnogInt = Integer.parseInt(brojStanovnikaGlavnog.getText());
            Drzava drzava=new Drzava(IdZaDrzavu, nazivDrzave.getText());
            Grad grad=new Grad(IdZaNoviGrad, nazivGrada.getText(), brojStanovnikaInt, drzava);
            if(nazivGrada.getText().equals(glavniGrad.getText())){
                drzava.setGlavniGrad(grad);
            }
            else{
                Grad glavni = new Grad(IdZaGlavni, glavniGrad.getText(), brojStanovnikaGlavnogInt, drzava);
                drzava.setGlavniGrad(glavni);}

            // geografijaDAO.dodajDrzavu(drzava); //glavni ce dodatiu fji

            geografijaDAO.dodajGrad(grad);
            tabela.getItems().clear();
            tabela.refresh();
            gradovi = geografijaDAO.getGradovi();
        }
    }

    public  void traziGrad(ActionEvent actionEvent){
        if(nazivGrada.getText()==null){
            nazivGrada.getStyleClass().add("poljeNijeIspravno");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Pretraga po nazivu grada!");
            alert.setTitle("Greška");
            Optional<ButtonType> result= alert.showAndWait();
        }
        else {
            nazivGrada.getStyleClass().removeAll("poljeNijeIspravno");
            nazivGrada.getStyleClass().add("poljeIspravno");

            pamti=gradovi;
            for(Grad g:gradovi){
                if(!g.getNaziv().equals(nazivGrada.getText())){
                    gradovi.remove(g);
                    tabela.refresh();
                }
            }

        }

    }
    private void doSave(File datoteka) {
        try {
            new GradoviReport().save(datoteka.getAbsolutePath(), geografijaDAO.getConn());
        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }
    }
    public void snimi(ActionEvent actionEvent){
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter xslmExtenizija = new FileChooser.ExtensionFilter("XSLX", "*.xslx");
        fc.getExtensionFilters().add( xslmExtenizija );
        FileChooser.ExtensionFilter docxExtenzija = new FileChooser.ExtensionFilter("DOCX", "*.docx");
        fc.getExtensionFilters().add( docxExtenzija );
        FileChooser.ExtensionFilter pdfExtenzija = new FileChooser.ExtensionFilter("PDF", "*.pdf");
        fc.getExtensionFilters().add( pdfExtenzija );
        fc.setTitle("Saving a file");
        File selectedFile = fc.showSaveDialog(null);

        //Ako ne odaberemo nista, nista se i ne desi.
        if (selectedFile != null)
            doSave(selectedFile);
    }

    public  void traziDrzavu (ActionEvent actionEvent){
        if(nazivDrzave.getText()==null){
            nazivDrzave.getStyleClass().add("poljeNijeIspravno");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Pretraga po nazivu drzave!");
            alert.setTitle("Greška");
            Optional<ButtonType> result= alert.showAndWait();
        }
        else {
            nazivDrzave.getStyleClass().removeAll("poljeNijeIspravno");
            nazivDrzave.getStyleClass().add("poljeIspravno");

            pamti=gradovi;
            for(Grad g:gradovi){
                if(!g.getDrzava().getNaziv().equals(nazivDrzave.getText())){
                    gradovi.remove(g);
                    tabela.refresh();
                }
            }

        }

    }
    private void PromijeniJezik(Locale jezik) {
        Stage primaryStage = (Stage) main.getScene().getWindow();
        Locale.setDefault(jezik);
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Baza.fxml"), bundle);
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
    }

    public void bos(ActionEvent actionEvent) {
        PromijeniJezik(new Locale("bs","BA"));
    }

    public void eng(ActionEvent actionEvent) {
        PromijeniJezik(new Locale("en","US"));
    }

    public void franc(ActionEvent actionEvent) {
        PromijeniJezik(new Locale("fr","FR"));
    }

    public void njem(ActionEvent actionEvent) {
        PromijeniJezik(new Locale("de", "DE"));
    }


}