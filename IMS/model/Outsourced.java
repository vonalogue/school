package IMS.model;

/**
 * Outsourced part class definition.
 * 
 * @author dgo
 */
public class Outsourced extends Part {
    private String companyName;
    
    public Outsourced(int id, String name, int stock, double price, int min, int max, String companyName) {
        super(id, name, stock, price, min, max);
        this.companyName = companyName;
    }
    
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyName()                 { return companyName; }
}
