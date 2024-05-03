module Thlproject2 {
	requires javafx.controls;
	requires org.controlsfx.controls;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
}
