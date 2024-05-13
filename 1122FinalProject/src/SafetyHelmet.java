class SafetyHelmet implements Helmet {
    private int id;
    private String model;
    private String size;

    public SafetyHelmet(int id, String model, String size) {
        this.id = id;
        this.model = model;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getSize() {
        return size;
    }

    public String toString() {
        return model + " - " + size;
    }
}