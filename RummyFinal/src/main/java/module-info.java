module ar.unlu.rummyfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    //requires ar.unlu.rummyfinal;


    opens ar.unlu.rummyfinal to javafx.fxml;
    exports ar.unlu.rummyfinal;
}