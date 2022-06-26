/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodCalorie;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	int porzioni;
    	try {
    		porzioni = Integer.parseInt(txtPorzioni.getText());
    	} catch(NumberFormatException e) {
    		txtResult.setText("Il campo porzioni deve essere un valore numerico");
    		return;
    	}
    	
    	model.creaGrafo(porzioni);
    	txtResult.appendText("GRAFO CREATO\n");
    	txtResult.appendText(String.format("# VERTICI: %d\n# ARCHI: %d", model.nVertici(), model.nArchi()));
    	
    	boxFood.getItems().clear();
    	boxFood.getItems().addAll(model.getVertici());
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	Food food = boxFood.getValue();
    	if(food==null) {
    		txtResult.setText("Selezionare un cibo");
    		return;
    	}
    	
    	List<FoodCalorie> foodsCalorie = model.getFoodsCalorieMax(food);
    	if(foodsCalorie==null) {
    		txtResult.setText("Nessun risultato, prova a selezionare un nuovo cibo");
    		return;
    	}
    	
    	for(int i=0; i<5 && i<foodsCalorie.size(); i++) {
    		txtResult.appendText(String.format("%s - %.3f\n", foodsCalorie.get(i).getFood().getDisplay_name(),
    				foodsCalorie.get(i).getCalorie()));
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	Food food = boxFood.getValue();
    	if(food==null) {
    		txtResult.setText("Selezionare un cibo");
    		return;
    	}
    	
    	int K;
    	try {
    		K = Integer.parseInt(txtK.getText());
    	} catch(NumberFormatException e) {
    		txtResult.setText("K deve essere un numero intero");
    		return;
    	}
    	
    	if(K<1 || K>10) {
    		txtResult.setText("K deve essere un numero intero compreso tra 1 e 10, inclusi");
    		return;
    	}
    	
    	try {
    		model.simula(K, food);
    	} catch(IllegalArgumentException e) {
    		txtResult.setText("Il cibo selezionato è isolato, prova a selezionare un nuovo cibo");
    		return;
    	}
    	
    	txtResult.appendText("Tempo di preparazione dei cibi: "+model.getDurata()+"\n");
    	txtResult.appendText("Numero di cibi preparati: "+model.getNumCibiPreparati());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
