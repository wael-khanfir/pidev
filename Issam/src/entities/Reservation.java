package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Reservation {


    private final SimpleIntegerProperty id;
    private final SimpleStringProperty first_name;
    private final SimpleStringProperty last_name;
    private final SimpleStringProperty email;
    private final SimpleIntegerProperty appointed_num;


    public Reservation(int id, String first_name, String last_name, String email, int appointed_num) {
        this.id = new SimpleIntegerProperty(id);
        this.first_name = new SimpleStringProperty(first_name);
        this.last_name = new SimpleStringProperty(last_name);
        this.email = new SimpleStringProperty(email);
        this.appointed_num = new SimpleIntegerProperty(appointed_num);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirst_name() {
        return first_name.get();
    }

    public SimpleStringProperty first_nameProperty() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name.set(first_name);
    }

    public String getLast_name() {
        return last_name.get();
    }

    public SimpleStringProperty last_nameProperty() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name.set(last_name);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public int getAppointed_num() {
        return appointed_num.get();
    }

    public SimpleIntegerProperty appointed_numProperty() {
        return appointed_num;
    }

    public void setAppointed_num(int appointed_num) {
        this.appointed_num.set(appointed_num);
    }
}
