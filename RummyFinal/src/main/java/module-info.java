module ar.unlu.rummyfinal {
    requires javafx.controls;
    requires javafx.fxml;


    opens ar.unlu.rummyfinal to javafx.fxml;
    exports ar.unlu.rummyfinal;
}