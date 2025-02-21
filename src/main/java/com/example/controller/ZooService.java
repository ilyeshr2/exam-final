package com.example.controller;

import com.example.dao.ZooDatabaseManager;
import com.example.model.Animal;
import com.example.model.Enclos;

import java.util.List;

public class ZooService {
    private final ZooDatabaseManager dbManager;
    
    public ZooService() {
        this.dbManager = ZooDatabaseManager.getInstance();
    }
    
    // Méthodes pour les animaux
    public boolean ajouterAnimal(String nom, String espece, int age, String regimeAlimentaire) {
        if (nom == null || nom.trim().isEmpty() || 
            espece == null || espece.trim().isEmpty() || 
            age < 0 || 
            regimeAlimentaire == null || regimeAlimentaire.trim().isEmpty()) {
            return false;
        }
        
        Animal animal = new Animal(nom, espece, age, regimeAlimentaire);
        int id = dbManager.createAnimal(animal);
        return id > 0;
    }
    
    public List<Animal> getTousLesAnimaux() {
        return dbManager.readAllAnimals();
    }
    
    public Animal getAnimalParId(int id) {
        return dbManager.readAnimalById(id);
    }
    
    public boolean mettreAJourAnimal(Animal animal) {
        if (animal == null || animal.getId() <= 0 || 
            animal.getNom() == null || animal.getNom().trim().isEmpty() || 
            animal.getEspece() == null || animal.getEspece().trim().isEmpty() || 
            animal.getAge() < 0 || 
            animal.getRegimeAlimentaire() == null || animal.getRegimeAlimentaire().trim().isEmpty()) {
            return false;
        }
        
        return dbManager.updateAnimal(animal);
    }
    
    public boolean supprimerAnimal(int id) {
        if (id <= 0) {
            return false;
        }
        
        return dbManager.deleteAnimal(id);
    }
    
    // Méthodes pour les enclos
    public List<Enclos> getTousLesEnclos() {
        return dbManager.readAllEnclos();
    }
    
    public boolean ajouterEnclos(String nom, int capacite, String typeHabitat) {
        if (nom == null || nom.trim().isEmpty() || 
            capacite <= 0 || 
            typeHabitat == null || typeHabitat.trim().isEmpty()) {
            return false;
        }
        
        Enclos enclos = new Enclos(nom, capacite, typeHabitat);
        int id = dbManager.createEnclos(enclos);
        return id > 0;
    }
    
    // Méthode pour rechercher des animaux
    public List<Animal> rechercherAnimaux(String terme) {
        if (terme == null || terme.trim().isEmpty()) {
            return getTousLesAnimaux();
        }
        
        String termeLower = terme.toLowerCase();
        return getTousLesAnimaux().stream()
                .filter(animal -> 
                    animal.getNom().toLowerCase().contains(termeLower) ||
                    animal.getEspece().toLowerCase().contains(termeLower) ||
                    animal.getRegimeAlimentaire().toLowerCase().contains(termeLower))
                .collect(java.util.stream.Collectors.toList());
    }
}