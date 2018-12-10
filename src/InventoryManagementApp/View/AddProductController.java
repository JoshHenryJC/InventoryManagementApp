/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareoneproject.View;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import softwareoneproject.Model.Inventory;
import softwareoneproject.Model.Part;
import softwareoneproject.Model.Product;

/**
 * FXML Controller class
 *
 * @author joshh
 */
public class AddProductController implements Initializable {    
    
    public ObservableList<Part> searchList = FXCollections.observableArrayList();
    public ObservableList<Part> partsList = FXCollections.observableArrayList();
    
        
    private Part selectedPart;
    private int selectedPartIndex;
    
    //Table and collumns for all parts
    @FXML 
    private TableView<Part> allPartsViewTable;
    @FXML 
    private TableColumn<Part, Integer> allPartIDColumn;
    @FXML 
    private TableColumn<Part, String> allPartNameColumn;
    @FXML 
    private TableColumn<Part, Double> allPartInStockColumn;
    @FXML 
    private TableColumn<Part, Double> allPartPriceColumn;
    
    //Table and collumns for associated parts
    @FXML 
    private TableView<Part> associatedPartsViewTable;
    @FXML 
    private TableColumn<Part, Integer> associatedPartIDColumn;
    @FXML 
    private TableColumn<Part, String> associatedPartNameColumn;
    @FXML 
    private TableColumn<Part, Double> associatedPartInStockColumn;
    @FXML 
    private TableColumn<Part, Double> associatedPartPriceColumn;
    
    @FXML
    private Button saveButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button searchButton;
    
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField prodIDText;
    @FXML
    private TextField prodNameText;
    @FXML
    private TextField prodInStockText;
    @FXML
    private TextField prodPriceText;
    @FXML
    private TextField prodMaxText;
    @FXML
    private TextField prodMinText;
    
    private boolean isNewProd = true;
    
        /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //Set the columns of the all parts table to the appropriate references
        allPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("partID"));
        allPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartInStockColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        allPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        //Set the columns of the associated parts table to the appropriate references
        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("partID"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInStockColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        allPartsViewTable.setItems(Inventory.getAllParts());
        associatedPartsViewTable.setItems(partsList);
        
        if(MainController.existingProd == true){
            partsList.addAll(MainController.selectedProd.getAssociatedParts());
            isNewProd = false;
            associatedPartsViewTable.setItems(partsList);
        } else{
            prodIDText.setText("Auto Generated: " + Integer.toString(Inventory.createProductID()));
        }
        fillPart(MainController.selectedProd);
        
    } 
    
    @FXML
    private void addButtonAction(ActionEvent event){        
        selectedPart = allPartsViewTable.getSelectionModel().getSelectedItem();
        
        partsList.add(selectedPart);
        
        associatedPartsViewTable.setItems(partsList);
    }
    
    @FXML
    private void saveButtonAction(ActionEvent event) throws IOException {
        
        if(checkProdInfo()){
            if(isNewProd)
                newProd();
            else
                updateProd();
        
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
        alert.setContentText("Canceling will clear all un-saved product info");

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
    
        //This method will run when the Delete Part button is clicked
    @FXML
    private void deletePartButtonAction(ActionEvent event){  
        
        //Get the selected item in the table
        selectedPart = associatedPartsViewTable.getSelectionModel().getSelectedItem();
        
        //Check if selection is not null and remove from list of Parts
        if(selectedPart != null){
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this Part?");
            
            //Check if the user slected OK on the confrimation screen
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                
                searchList.remove(selectedPart);
                partsList.remove(selectedPart);
            }            
        }      
        //If no Part was selected 
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nothing Selected");
            alert.setHeaderText(null);
            alert.setContentText("Make sure you have a part selected!");

            alert.showAndWait();
        }
    }
    
//This method will run when the Search Part button is clicked
    @FXML
    private void searchPartButtonAction(ActionEvent event){
        String searchText = searchTextField.getText();
        boolean found = false;
        
        //If search is empty, show all parts
        if(searchText.isEmpty()){
            allPartsViewTable.setItems(Inventory.getAllParts());
        }else {
            //Loop through each of the parts in the allParts list
            try{
                //Try to find through part ID
                for(Part part : Inventory.getAllParts()) {
                    if(part.getPartID() == Integer.parseInt(searchText)){
                        found = true;
                        Part currentPart = Inventory.lookupPart(Integer.parseInt(searchText));
                        searchList.clear();
                        searchList.add(currentPart);
                        allPartsViewTable.setItems(searchList);
                    }
                }
            }            
            catch(NumberFormatException e){
                //Try to find through part name
                for(Part part : Inventory.getAllParts()) {
                if(part.getName().equalsIgnoreCase(searchText)){
                    found = true;
                    Part currentPart = part;
                    searchList.clear();
                    searchList.add(currentPart);
                    allPartsViewTable.setItems(searchList);
                    }
                }
            }
            finally{
                //If nothing is found, show the nothing found alert window
                if(!found){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Nothing Found");
                    alert.setHeaderText(null);
                    alert.setContentText("Try searching by exact Part ID or Name");
                    
                    alert.showAndWait();
                    
                    allPartsViewTable.setItems(Inventory.getAllParts());
                }
            }
        }
    }
   

        //Fill text fields with proper part info
    private void fillPart(Product prod){
        
        if(!isNewProd){
            prodIDText.setText(Integer.toString(prod.getProductID()));
            prodNameText.setText(prod.getName());
            prodInStockText.setText(Integer.toString(prod.getInStock()));
            prodPriceText.setText(Double.toString(prod.getPrice()));
            prodMinText.setText(Integer.toString(prod.getMin()));
            prodMaxText.setText(Integer.toString(prod.getMax()));

        } else {
            prodIDText.setPromptText("Product ID");
            prodNameText.setPromptText("Product Name");
            prodInStockText.setPromptText("Inventory Amount");
            prodPriceText.setPromptText("Price");
            prodMinText.setPromptText("Minimum");
            prodMaxText.setPromptText("Maximum");
        }
            
    }
    
    
    private boolean checkProdInfo(){
        String nameText = prodNameText.getText();
        String inStockText = prodInStockText.getText();
        String priceText = prodPriceText.getText();
        String minText = prodMinText.getText();
        String maxText = prodMaxText.getText();
        
        String errorMessage = "";
               
        if(nameText.length() == 0){
            errorMessage = "Invalid Part Name. (Can't be empty)";
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
    

    
    
    private void updateProd(){        
        String nameText = prodNameText.getText();
        String inStockText = prodInStockText.getText();
        String priceText = prodPriceText.getText();
        String minText = prodMinText.getText();
        String maxText = prodMaxText.getText();
        
        Integer ID = MainController.selectedProd.getProductID();
        Double price = Double.parseDouble(priceText);
        Integer inStock = Integer.parseInt(inStockText);
        Integer min = Integer.parseInt(minText);
        Integer max = Integer.parseInt(maxText);  
        
        ArrayList<Part> parts = new ArrayList<>();
        parts.addAll(partsList);
        
        Inventory.updateProduct(MainController.selectedProdIndex, new Product(ID, nameText, price, inStock, min, max, parts));
    }
    
    private void newProd(){
        String nameText = prodNameText.getText();
        String inStockText = prodInStockText.getText();
        String priceText = prodPriceText.getText();
        String minText = prodMinText.getText();
        String maxText = prodMaxText.getText();
        
        Integer ID = Inventory.createProductID();
        Double price = Double.parseDouble(priceText);
        Integer inStock = Integer.parseInt(inStockText);
        Integer min = Integer.parseInt(minText);
        Integer max = Integer.parseInt(maxText);      
        
        ArrayList<Part> parts = new ArrayList<>();
        parts.addAll(partsList);
        
        Inventory.AddProduct(new Product(ID, nameText, price, inStock, min, max, parts));
    
    }
}

