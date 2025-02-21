package com.example.model;

public class Soigneur {
    private int id;
    private String nom;
    private String specialite;
    
    // Constructeur par défaut
    public Soigneur() {
    }
    
    // Constructeur sans ID (pour création)
    public Soigneur(String nom, String specialite) {
        this.nom = nom;
        this.specialite = specialite;
    }
    
    // Constructeur complet
    public Soigneur(int id, String nom, String specialite) {
        this.id = id;
        this.nom = nom;
        this.specialite = specialite;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    
    @Override
    public String toString() {
        return "Soigneur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", specialite='" + specialite + '\'' +
                '}';
    }
}