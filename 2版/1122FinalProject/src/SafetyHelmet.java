class SafetyHelmet implements Helmet {
	
	// 取得helmet資訊的地方
    private int id;
    private String model;
    private String size;

   
    public SafetyHelmet(int id, String model, String size) { //要釐清這裡要input的id, model, size跟helmet factory class之間的差異跟分別怎麼用
        this.id = id;
        this.model = model;
        this.size = size;
    }

    public int getId() { //helmet的id
        return id;
    }

    public String getModel() { // full face, jet, half helmet
        return model;
    }

    public String getSize() { //S,M,L
        return size;
    }

    public String toString() {
        return model + " - " + size;
    }
}