 module DatabaseConnector {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.desktop;
    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires java.naming;
    requires java.sql;

    opens Control to javafx.fxml;
    opens Entities;

    exports Control;
    exports Main;
    exports Db;
    exports Entities;
    exports Singleton;
    
}
