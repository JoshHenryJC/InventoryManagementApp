
package softwareoneproject.View;

import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import softwareoneproject.Model.Inventory;
import softwareoneproject.Model.Part;
import softwareoneproject.Model.Product;

/**
 *
 * @author joshh
 */
public class MainController {
    
    public static boolean existingPart;
    public static Part selectedPart;
    public static int selectedPartIndex;
    public static boolean existingProd;
    public static Product selectedProd;
    public static int selectedProdIndex;
    
    public ObservableList<Part> searchPartList = FXCollections.observableArrayList();
    public ObservableList<Product> searchProdList = FXCollections.observableArrayList();
    
    
    @FXML
    private TextField searchPartTextField;
        @FXML
    private TextField searchProdTextField;
    
    @FXML 
    private TableView<Part> partsViewTable;
    @FXML 
    private TableColumn<Part, Integer> partIDColumn;
    @FXML 
    private TableColumn<Part, String> partNameColumn;
    @FXML 
    private TableColumn<Part, Double> partInStockColumn;
    @FXML 
    private TableColumn<Part, Double> partPriceColumn;
        
    @FXML 
    private TableView<Product> prodViewTable;
    @FXML 
    private TableColumn<Product, Integer> prodIDColumn;
    @FXML 
    private TableColumn<Product, String> prodNameColumn;
    @FXML 
    private TableColumn<Product, Double> prodInStockColumn;
    @FXML 
    private TableColumn<Product, Double> prodPriceColumn;
    
    
        @FXML
    public void initialize() {
        //Reset these every time main page loads
        existingPart = false;
        selectedPart = null;
        existingProd = false;
        selectedProd = null;
        
        //Set the columns of the Parts table to the appropriate references
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInStockColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        //Set the columns of the Products table to the apprpriate references
        prodIDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        prodNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInStockColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        prodPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
       
        //Fill the Products table with the data from the Products list
        prodViewTable.setItems(Inventory.getAllProducts());
        
        //Fill the Parts table with the data from the Parts list
        partsViewTable.setItems(Inventory.getAllParts());
    }
    
    //This method will run when the Exit button is clicked
    @FXML
    private void exitButtonAction(ActionEvent event){
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.close();
    }
    
    //This method will run when the Add button is clicked
    @FXML
    private void addPartButtonAction(ActionEvent event) throws IOException {
        
        //Load the Part window into a new scene
        Parent AddViewParent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene addViewScene = new Scene(AddViewParent);
        
        //Show the Part window instead of the current window
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();        
        window.setScene(addViewScene);
        window.show();
    }
    
    //This method will run when the add product button is clicked
    @FXML
    private void addProductButtonAction(ActionEvent event) throws IOException {
        
        //Load the Part window into a new scene
        Parent AddViewParent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene addViewScene = new Scene(AddViewParent);
        
        //Show the Part window instead of the current window
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();        
        window.setScene(addViewScene);
        window.show();
    }
    
    //This method will run when the modify part button is clicked
    @FXML
    private void modifyPartButtonAction(ActionEvent event) throws IOException { 
                
        try {
            //Get the selected item in the table and the items index in the Part List
            selectedPart = partsViewTable.getSelectionModel().getSelectedItem();
            selectedPartIndex = Inventory.getPartIndex(selectedPart);

            existingPart = true;

            //Load the Part window into a new scene
            Parent AddViewParent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
            Scene addViewScene = new Scene(AddViewParent);      

            //Show the Part window instead of the current window
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();        
            window.setScene(addViewScene);
            window.show();
        }
        catch(LoadException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Nothing Selected");
            alert.setHeaderText(null);
            alert.setContentText("Make sure you have a part selected!");

            alert.showAndWait();
        }
    }
    
    @FXML
    private void modifyProductButtonAction(ActionEvent event) throws IOException { 
        
        try {
            //Get the selected item in the table and the items index in the Part List
            selectedProd = prodViewTable.getSelectionModel().getSelectedItem();
            selectedProdIndex = Inventory.getProductIndex(selectedProd);

            existingProd = true;

            //Load the Product window into a new scene
            Parent AddViewParent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
            Scene addViewScene = new Scene(AddViewParent);      

            //Show the Part window instead of the current window
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();        
            window.setScene(addViewScene);
            window.show();
        }
        catch(LoadException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Nothing Selected");
            alert.setHeaderText(null);
            alert.setContentText("Make sure you have a product selected!");

            alert.showAndWait();
        }
    }
    
    
    //This method will run when the Delete Part button is clicked
    @FXML
    private void deletePartButtonAction(ActionEvent event){  
        
        partsViewTable.setItems(Inventory.getAllParts());
        
        //Get the selected item in the table
        selectedPart = partsViewTable.getSelectionModel().getSelectedItem();
        
        //Check if selection is not null and remove from list of Parts
        if(selectedPart != null){
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this Part?");
            
            //Check if the user slected OK on the confrimation screen
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                
                searchPartList.remove(selectedPart);
                Inventory.deletePart(selectedPart);
            }            
        }      
        //If no Part was selected 
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Nothing Selected");
            alert.setHeaderText(null);
            alert.setContentText("Make sure you have a part selected!");

            alert.showAndWait();
        }
    }
    
        //This method will run when the Delete Part button is clicked
    @FXML
    private void deleteProductButtonAction(ActionEvent event){  
        
        //Get the selected item in the table
        selectedProd = prodViewTable.getSelectionModel().getSelectedItem();
        
        //Check if selection is not null and remove from list of Parts
        if(selectedProd != null){
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this Product?");
            
            //Check if the user slected OK on the confrimation screen
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                
                searchProdList.remove(selectedProd);
                Inventory.removeProduct(selectedProd);
            }            
        }      
        //If no Part was selected 
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Nothing Selected");
            alert.setHeaderText(null);
            alert.setContentText("Make sure you have a product selected!");

            alert.showAndWait();
        }
    }
    
    //This method will run when the Search Part button is clicked
    @FXML
    private void searchPartButtonAction(ActionEvent event){
        String searchText = searchPartTextField.getText();
        boolean found = false;
        
        //If search is empty, show all parts
        if(searchText.isEmpty()){
            partsViewTable.setItems(Inventory.getAllParts());
        }else {
            //Loop through each of the parts in the allParts list
            try{
                //Try to find through part ID
                for(Part part : Inventory.getAllParts()) {
                    if(part.getPartID() == Integer.parseInt(searchText)){
                        found = true;
                        Part currentPart = Inventory.lookupPart(Integer.parseInt(searchText));
                        searchPartList.clear();
                        searchPartList.add(currentPart);
                        partsViewTable.setItems(searchPartList);
                    }
                }
            }            
            catch(NumberFormatException e){
                //Try to find through part name
                for(Part part : Inventory.getAllParts()) {
                if(part.getName().equalsIgnoreCase(searchText)){
                    found = true;
                    Part currentPart = part;
                    searchPartList.clear();
                    searchPartList.add(currentPart);
                    partsViewTable.setItems(searchPartList);
                    }
                }
            }
            finally{
                //If nothing is found, show the nothing found alert window
                if(!found){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Nothing Found");
                    alert.setHeaderText(null);
                    alert.setContentText("Try searching by exact Part ID or Name");
                    
                    alert.showAndWait();
                    
                    partsViewTable.setItems(Inventory.getAllParts());
                }
            }
        }
    }
    
    @FXML
    private void searchProductButtonAction(ActionEvent event){
        String searchText = searchProdTextField.getText();
        boolean found = false;
        
        //If search is empty, show all parts
        if(searchText.isEmpty()){
            prodViewTable.setItems(Inventory.getAllProducts());
        }else {
            //Loop through each of the products in the allProducts list
            try{
                //Try to find through product ID
                for(Product prod : Inventory.getAllProducts()) {
                    if(prod.getProductID() == Integer.parseInt(searchText)){
                        found = true;
                        Product currentProd = Inventory.lookupProduct(Integer.parseInt(searchText));
                        searchProdList.clear();
                        searchProdList.add(currentProd);
                        prodViewTable.setItems(searchProdList);
                    }
                }
            }            
            catch(NumberFormatException e){
                //Try to find through part name
                for(Product prod : Inventory.getAllProducts()) {
                    if(prod.getProductID() == Integer.parseInt(searchText)){
                        found = true;
                        Product currentProd = prod;
                        searchProdList.clear();
                        searchProdList.add(currentProd);
                        prodViewTable.setItems(searchProdList);
                    }
                }
            }
            finally{
                //If nothing is found, show the nothing found alert window
                if(!found){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Nothing Found");
                    alert.setHeaderText(null);
                    alert.setContentText("Try searching by exact Product ID or Name");
                    
                    alert.showAndWait();
                    
                    prodViewTable.setItems(Inventory.getAllProducts());

                }
            }
        }
    }
        
    
    
}
