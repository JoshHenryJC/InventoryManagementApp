/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareoneproject.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author joshh
 */
public class Inventory {

    public static ObservableList<Product> products = FXCollections.observableArrayList();
    public static ObservableList<Part> allParts = FXCollections.observableArrayList();     

    
    public static int getPartCount(){
        System.out.println(allParts.size());
        return allParts.size();
    }
    
    public static int createPartID(){
       int count = getPartCount() + 1;
       return count;
    }
    
    //This method will return the index in the part list of the part passed into it
    public static int getPartIndex(Part part){
        return allParts.indexOf(part);
    }    
    
    public static int getProductCount(){
        return products.size();
    }
    
    public static int createProductID(){
       int count = getProductCount() + 1;
       return count;
    }
    
    //This method will return the index in the product list of the part passed into it
    public static int getProductIndex(Product prod){
        return products.indexOf(prod);
    }    
    
    public static void AddProduct(Product prod){
        products.add(prod);
    }   
    
    public static void removeProduct(Product prod){
        products.remove(prod);
    }
    
    public static  ObservableList<Product> getAllProducts(){
        return products;
    }
    
    public static Product lookupProduct(int n){        
        for(Product prod  : getAllProducts()){
            if(prod.getProductID() ==  n)
                return prod;
        }
                return null;
    }
    
    public static void updateProduct(int n, Product prod) {
        products.set(n, prod);
    }
    
    public static void addPart(Part n){
       allParts.add(n);
    }
        
    public static void updatePart(int n, Part part){
        allParts.set(n, part);
    }
    
    public static  ObservableList<Part> getAllParts(){
        return allParts;
    }
    
    public static void deletePart(Part part){
        allParts.remove(part);
    }
    
    public static Part lookupPart(int n){
        for( Part part : getAllParts()){
            if(part.getPartID() ==  n)
                return part;
        }
                return null;
    }

}  
