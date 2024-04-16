module com.tool.lyrics {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires static lombok;
    requires juniversalchardet;

    opens com.tool.lyrics to javafx.fxml;
    opens com.tool.lyrics.controller to javafx.fxml;
    exports com.tool.lyrics;
    exports com.tool.lyrics.model;
}
