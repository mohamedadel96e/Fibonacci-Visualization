module org.example.fibonaccivisualization {
  requires javafx.controls;
  requires javafx.fxml;


  opens org.example.fibonaccivisualization to javafx.fxml;
  exports org.example.fibonaccivisualization;
}