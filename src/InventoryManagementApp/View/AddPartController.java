/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareoneproject.View;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import softwareoneproject.Model.InHouse;
import softwareoneproject.Model.Inventory;
import softwareoneproject.Model.Outsourced;
import softwareoneproject.Model.Part;

/**
 * FXML Controller class
 *
 * @author joshh
 */
public class AddPartController implements Initializable {
    
    private boolean isNewPart = true;
    private boolean isInHouse = true;    
    
    @FXML
    private Button saveButton;
    @FXML
    private RadioButton inhouseButton;
    @FXML
    private RadioButton outsourcedButton;
    @FXML
    private Label partCorMLabel;
    @FXML
    private TextField partIDText;
    @FXML
    private TextField partNameText;
    @FXML
    private TextField partInStockText;
    @FXML
    private TextField partPriceText;
    @FXML
    private TextField partCorMText;
    @FXML
    private TextField partMaxText;
    @FXML
    private TextField partMinText;
    
    public ToggleGroup cmTG;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        cmTG = new ToggleGroup();
        this.inhouseButton.setToggleGroup(cmTG);
        this.outsourcedButton.setToggleGroup(cmTG);
        
        partCorMLabel.setText(" ");
        
        if(MainController.existingPart == true){
            saveButton.setDisable(false);
            isNewPart = false;
        } else{
            partIDText.setText("Auto Generated: " + Integer.toString(Inventory.createPartID()));
        }
        fillPart(MainController.selectedPart);
    }
    
    @FXML
    private void radioButtonAction() {
        
        //Check which radio button is selected and change the appropriate values
        if(this.cmTG.getSelectedToggle() == this.inhouseButton){
            partCorMLabel.setText("Machine ID");
            isInHouse = true;
        }       
        if(this.cmTG.getSelectedToggle() == this.outsourcedButton){
            partCorMLabel.setText("Company Name");
            isInHouse = false;
        }
        
        saveButton.setDisable(false);
    }
    
    @FXML
    private void addButtonAction(ActionEvent event) throws IOException {
        
        if(checkPartInfo()){
            if(isNewPart)
                newPart();
            else
                updatePart();
        
            //Load the Main window into a new scene
            Parent root = FXMLLoader.load(getClass().getResource("MainFXML.fxml"));
            Scene mainViewScene = new Scene(root);
            //Show the Main window instead of current window
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();        
            window.setScene(mainViewScene);
            window.show();
        }
    }
    
    @FXML
    private void cancelButtonAction(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText("Canceling will clear all un-saved part info");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            //Load the Main window into a new scene
            Parent root = FXMLLoader.load(getClass().getResource("MainFXML.fxml"));
            Scene mainViewScene = new Scene(root);
            //Show the Main window instead of current window
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();        
            window.setScene(mainViewScene);
            window.show();
        }
    }
    
    //Fill text fields with proper part info
    private void fillPart(Part part){
        
        if(!isNewPart){
            partIDText.setText(Integer.toString(part.getPartID()));
            partNameText.setText(part.getName());
            partInStockText.setText(Integer.toString(part.getInStock()));
            partPriceText.setText(Double.toString(part.getPrice()));
            partMinText.setText(Integer.toString(part.getMin()));
            partMaxText.setText(Integer.toString(part.getMax()));
            
            if(MainController.selectedPart instanceof InHouse){
                InHouse p = (InHouse) part;
                partCorMLabel.setText("Machine ID");
                inhouseButton.selectedProperty().set(true);
                partCorMText.setText(Integer.toString(p.machineID));
                isInHouse = true;
            } else {
                Outsourced p = (Outsourced) part;
                partCorMLabel.setText("Company Name");
                outsourcedButton.selectedProperty().set(true);
                partCorMText.setText(p.companyName);
                isInHouse = false;
            }
        } else {
            partIDText.setPromptText("Part ID");
            partNameText.setPromptText("Part Name");
            partInStockText.setPromptText("Inventory Amount");
            partPriceText.setPromptText("Price");
            partMinText.setPromptText("Minimum");
            partMaxText.setPromptText("Maximum");
        }
            
    }
    
    
    private boolean checkPartInfo(){
        String idText = partIDText.getText();
        String nameText = partNameText.getText();
        String inStockText = partInStockText.getText();
        String priceText = partPriceText.getText();
        String minText = partMinText.getText();
        String maxText = partMaxText.getText();
        String cmText = partCorMText.getText();
        
        String errorMessage = "";
               
        if(nameText.length() == 0){
            errorMessage = "Invalid Part Name. (Can't be empty)";
        }
        if(cmText.length() == 0) {
                if(isInHouse)
                    errorMessage = "Invalid Machine ID. (Can't be left empty)";
                else
                    errorMessage = "Invalid Company Name. (Can't be left empty)";
        } else {
            try{
                Integer machineID = Integer.parseInt(cmText);
            }
            catch(NumberFormatException e){
                if(isInHouse)
                    errorMessage = "Invalid Machine ID. (Must be a whole number)";                
            }
        } 
        
        if(minText.length() == 0 || maxText.length() == 0) {
            errorMessage = "Invalid Minimum/Maximum value. (Can't be left empty)";
        } else { 
            try{
                int min = Integer.parseInt(minText);
                int max = Integer.parseInt(maxText);

        }
            catch(NumberFormatException e){
                errorMessage = "Invalid Minimum/Maximum value. (Must be a whole number)";
            }
        }
        
        if(inStockText.length() == 0) {
            errorMessage = "Invalid Inventory value. (Can't be left empty)";
        } else { 
            try{
                int min = Integer.parseInt(minText);
                int max = Integer.parseInt(maxText);       
                
                int inStock = Integer.parseInt(inStockText);
                
                if(inStock > max || inStock < min){
                    errorMessage = "Invalid Inventory value. (Can't be higher than Max or lower than Min)";
                }
                
                if(min > max){
                    errorMessage = "Invalid Minimum/Maximum value. (Minimum can't be higher than Maximum)";           
                }
                
            }
            catch(NumberFormatException e){
                errorMessage = "Invalid Inventory value. (Must be a whole number)";
            }            
        }
        
        if(priceText.length() == 0) {
            errorMessage = "Invalid Price. (Can't be left empty)";
        } else { 
            try{
                Double price = Double.parseDouble(priceText);
            }
            catch(NumberFormatException e){
                errorMessage = "Invalid Price value. (Must be a number)";
            }            
        }
        
        if(errorMessage.length() == 0)
            return true;
        else {
            showAlert(errorMessage);
            return false;
        }
    }
    
    private void showAlert(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);

        alert.showAndWait();
    }    
    

    
    
    private void updatePart(){        
        String nameText = partNameText.getText();
        String inStockText = partInStockText.getText();
        String priceText = partPriceText.getText();
        String minText = partMinText.getText();
        String maxText = partMaxText.getText();
        String cmText = partCorMText.getText();
        
        Integer ID = MainController.selectedPart.getPartID();
        Double price = Double.parseDouble(priceText);
        Integer inStock = Integer.parseInt(inStockText);
        Integer min = Integer.parseInt(minText);
        Integer max = Integer.parseInt(maxText);  
        
        if(isInHouse){
            Integer machineID = Integer.parseInt(cmText);
            Inventory.updatePart(MainController.selectedPartIndex, new InHouse(machineID, ID, nameText, price, inStock, min, max));        
        } else {
            Inventory.updatePart(MainController.selectedPartIndex, new Outsourced(cmText, ID, nameText, price, inStock, min, max));  
        }
    }
    
    private void newPart(){
        String nameText = partNameText.getText();
        String inStockText = partInStockText.getText();
        String priceText = partPriceText.getText();
        String minText = partMinText.getText();
        String maxText = partMaxText.getText();
        String cmText = partCorMText.getText();
        
        Integer ID = Inventory.createPartID();
        Double price = Double.parseDouble(priceText);
        Integer inStock = Integer.parseInt(inStockText);
        Integer min = Integer.parseInt(minText);
        Integer max = Integer.parseInt(maxText);        
        
        if(isInHouse){
            Integer machineID = Integer.parseInt(cmText);
            Inventory.addPart(new InHouse(machineID, ID, nameText, price, inStock, min, max));        
        } else {
            Inventory.addPart(new Outsourced(cmText, ID, nameText, price, inStock, min, max));  
        }
    
    }
}
