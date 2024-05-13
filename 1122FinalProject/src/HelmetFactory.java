class HelmetFactory {
    private static int nextId = 1;

    public static Helmet createHelmet(String model, String size) {
        return new SafetyHelmet(nextId++, model, size);
    }
}