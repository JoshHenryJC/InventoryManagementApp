/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareoneproject.Model;

import java.util.*;

/**
 *
 * @author joshh
 */
public class Product {
    
    public ArrayList<Part> associatedParts = new ArrayList<>();
    
    public int productID;
    public String name;
    public double price;
    public int inStock;
    public int min;
    public int max;    

    public Product(int productID, String name, double price, int inStock, int min, int max, ArrayList<Part> parts) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.min = min;
        this.max = max;
        this.associatedParts = parts;
    }
    
    public void addAssociatedPart(Part p){
        associatedParts.add(p);
    }
    
    public void removeAssociatedPart(Part p){
        associatedParts.remove(p);
    }
    
    public ArrayList<Part> getAssociatedParts(){
        return associatedParts;
    }
    
    public Part lookupAssociatedPart(int p){
        return null;
    }
    

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    

}
