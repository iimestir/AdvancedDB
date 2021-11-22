package model;

public enum Panel {
    // Panels
    MAIN("mainview.fxml");

    private final String fxmlPath;

    Panel(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getPath() {
        return fxmlPath;
    }
}
