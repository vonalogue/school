package IMS.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product class definition.
 * 
 * @author dgo
 */
public class Product {
    
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Part selectedPart = null;
    private int id;
    private String name;
    private int stock;
    private double price;
    private int min;
    private int max;
    
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = Math.round(price * 100d) / 100d;
        this.min = min;
        this.max = max;
    }
    
    public void setId(int id)           { this.id = id; }
    public void setName(String name)    { this.name = name; }
    public void setPrice(int price)     { this.price = price; }
    public void setPrice(double price)  { this.price = price; }
    public void setStock(int stock)     { this.stock = stock; }
    public void setMin(int min)         { this.min = min; }
    public void setMax(int max)         { this.max = max; }
        
    public int getId()                  { return id; }
    public String getName()             { return name; }
    public double getPrice()            { return price; }
    public String getFormattedPrice()   { return "$" + String.format("%.2f", price); }
    public int getStock()               { return stock; }
    public int getMin()                 { return min; }
    public int getMax()                 { return max; }

    public void addPart(Part part)         { associatedParts.add(part); }
    public void deletePart(Part part)      { associatedParts.remove(part); }
    
    public void changePart(Part oldPart, Part newPart) {
        int index = associatedParts.indexOf(oldPart);
        associatedParts.set(index, newPart);
    }
    
    public ObservableList<Part> getAllParts() { return associatedParts; }
   
    public Part getSelectedPart()       { return selectedPart; }
    public void selectPart(Part part)   { selectedPart = part; }
    public void deselectPart() {
        if (selectedPart != null)
            selectedPart = null;
    }
    
}
