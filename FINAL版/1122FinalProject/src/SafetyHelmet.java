public class SafetyHelmet implements Helmet {
    private int id;
    private String model;
    private String size;
    private int available;
    private String username;

    public SafetyHelmet(int id, String model, String size, int available) {
        this.id = id;
        this.model = model;
        this.size = size;
        this.available = available;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getSize() {
        return size;
    }

    public int isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(int available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Helmet [id=" + id + ", model=" + model + ", size=" + size + ", available=" + available + "]";
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
}
