import java.util.ArrayList;
// 建立helmet的class 讓他的id不會重複

class HelmetFactory<HelmetDataBase> {
    private static int currentMaxId;
    private ArrayList<SafetyHelmet> helmetList;
    private HelmetDataBase database;  // 建立好helmet之後要回傳到helmet 的database裡(要增加)
    private SafetyHelmet helmet;
    
    public void getCurrentMaxId() {
    	
    }

    public Helmet createHelmet(String model, String size) {
    	
    	helmet = new SafetyHelmet(currentMaxId ++, model, size);
		
    	
        return helmet;
    }
}