package IMS.model;

/**
 * In-house part class definition.
 * 
 * @author dgo
 */
public class InHouse extends Part {
    
    private int machineId;
    
    public InHouse(int id, String name, int stock, double price, int min, int max, int machineId) {
        super(id, name, stock, price, min, max);
        this.machineId = machineId;
    }
    
    public void setMachineId(int machineId) { this.machineId = machineId; }
    public int getMachineId()               { return machineId; }
}
