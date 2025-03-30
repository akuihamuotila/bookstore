package fi.haagahelia.bookstore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "käyttäjätunnus", unique = true)
    private String käyttäjätunnus;

    private String salasana;
    private String rooli;

    public User() {
    }

    public User(String käyttäjätunnus, String salasana, String rooli) {
        this.käyttäjätunnus = käyttäjätunnus;
        this.salasana = salasana;
        this.rooli = rooli;
    }

    public Long getId() {
        return id;
    }

    public String getKäyttäjätunnus() {
        return käyttäjätunnus;
    }

    public void setKäyttäjätunnus(String käyttäjätunnus) {
        this.käyttäjätunnus = käyttäjätunnus;
    }

    public String getSalasana() {
        return salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
    }

    public String getRooli() {
        return rooli;
    }

    public void setRooli(String rooli) {
        this.rooli = rooli;
    }
}