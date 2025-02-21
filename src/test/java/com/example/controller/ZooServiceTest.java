package com.example.controller;

import com.example.model.Animal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ZooServiceTest {
    
    @Test
    void testValidationAjouterAnimal() {
        ZooService service = new ZooService();
        
        // Test avec des valeurs nulles
        assertFalse(service.ajouterAnimal(null, "Espèce", 5, "Herbivore"));
        assertFalse(service.ajouterAnimal("Nom", null, 5, "Herbivore"));
        assertFalse(service.ajouterAnimal("Nom", "Espèce", 5, null));
        
        // Test avec des chaînes vides
        assertFalse(service.ajouterAnimal("", "Espèce", 5, "Herbivore"));
        assertFalse(service.ajouterAnimal("Nom", "", 5, "Herbivore"));
        assertFalse(service.ajouterAnimal("Nom", "Espèce", 5, ""));
        
        // Test avec des espaces blancs
        assertFalse(service.ajouterAnimal("   ", "Espèce", 5, "Herbivore"));
        assertFalse(service.ajouterAnimal("Nom", "   ", 5, "Herbivore"));
        assertFalse(service.ajouterAnimal("Nom", "Espèce", 5, "   "));
        
        // Test avec un âge négatif
        assertFalse(service.ajouterAnimal("Nom", "Espèce", -1, "Herbivore"));
    }
    
    @Test
    void testValidationMettreAJourAnimal() {
        ZooService service = new ZooService();
        
        // Test avec un animal null
        assertFalse(service.mettreAJourAnimal(null));
        
        // Test avec un ID invalide
        Animal animal = new Animal("Simba", "Lion", 5, "Carnivore");
        animal.setId(0); // ID invalide
        assertFalse(service.mettreAJourAnimal(animal));
        
        animal.setId(-1); // ID invalide
        assertFalse(service.mettreAJourAnimal(animal));
        
        // Test avec des valeurs nulles
        animal.setId(1); // ID valide
        animal.setNom(null);
        assertFalse(service.mettreAJourAnimal(animal));
        
        animal.setNom("Simba");
        animal.setEspece(null);
        assertFalse(service.mettreAJourAnimal(animal));
        
        animal.setEspece("Lion");
        animal.setRegimeAlimentaire(null);
        assertFalse(service.mettreAJourAnimal(animal));
        
        // Test avec des chaînes vides
        animal.setRegimeAlimentaire("Carnivore");
        animal.setNom("");
        assertFalse(service.mettreAJourAnimal(animal));
        
        animal.setNom("Simba");
        animal.setEspece("");
        assertFalse(service.mettreAJourAnimal(animal));
        
        animal.setEspece("Lion");
        animal.setRegimeAlimentaire("");
        assertFalse(service.mettreAJourAnimal(animal));
        
        // Test avec un âge négatif
        animal.setRegimeAlimentaire("Carnivore");
        animal.setAge(-1);
        assertFalse(service.mettreAJourAnimal(animal));
    }
}