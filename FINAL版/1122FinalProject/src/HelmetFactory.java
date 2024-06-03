import java.util.ArrayList;
// 建立helmet的class 讓他的id不會重複

class HelmetFactory {
    private static int nextId = 1;

    public static SafetyHelmet createHelmet(String model, String size,int available) {
        return new SafetyHelmet(nextId++, model, size,available);
    }
}
