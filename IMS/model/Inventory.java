package IMS.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory class definition.
 * 
 * @author dgo
 */
public class Inventory {
   
    private ObservableList<Part> allParts = FXCollections.observableArrayList(); 
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
       
    public void addPart(Part part)              { allParts.add(part); }
    public void addProduct(Product product)     { allProducts.add(product); }
    
    // For changing a part from one type to another (InHouse to Outsourced or vice versa)
    // while keeping the same reference (pointer). This updates all copies that are 
    // associated with products in the process. 
    
    public void changePart(Part oldPart, Part newPart) {
        int index = allParts.indexOf(oldPart);
        allParts.set(index, newPart);
    
        Part part = null;
        ObservableList<Part> productParts = null;
        
        for (Product product : allProducts) {
            productParts = product.getAllParts();
            
            for (int pt = 0; pt < productParts.size(); pt++) {
                part = productParts.get(pt);
                if (part == oldPart)
                    product.changePart(oldPart, newPart);
            }
        }
    }
        
    public void deletePart(Part part)           { allParts.remove(part); }
    public void deleteProduct(Product product)  { allProducts.remove(product); }
        
    public ObservableList<Part> getAllParts()       { return allParts; }
    public ObservableList<Product> getAllProducts() { return allProducts; }
    
    public Part lookUpPart(int id) { 
        for (Part part : allParts) {
            if (part.getId() == id)
                return part;
        }
        return null;
    }
    
    public Part lookUpPart(String name) { 
         for (Part part : allParts) {
            if (part.getName().equals(name))
                return part;
        }
        return null;
    }
   
    public Product lookUpProduct(int id) {
        for (Product product : allProducts) {
            if (product.getId() == id)
                return product;
        }
        return null;
    }
    
    public Product lookUpProduct(String name) {
        for (Product product : allProducts) {
            if (product.getName().equals(name))
                return product;
        }
        return null;
    }
    
    private Part selectedPart = null;
    private Product selectedProduct = null;
    
    public void selectPart(Part part)            { selectedPart = part; }
    public void selectProduct(Product product)   { selectedProduct = product; }
    
    public void deselectPart() {
        if (selectedPart != null)
            selectedPart = null;
    }
    public void deselectProduct() {
        if (selectedProduct != null)
            selectedProduct = null;
    }
    
    public Part getSelectedPart()       { return selectedPart; }
    public Product getSelectedProduct() { return selectedProduct; }
   
}
