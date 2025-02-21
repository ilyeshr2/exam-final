package com.example.dao;

import com.example.model.Animal;
import java.util.List;

public interface IZooDataAccess {
    // Créer un animal
    int createAnimal(Animal animal);
    
    // Lire un animal par son ID
    Animal readAnimalById(int id);
    
    // Lire tous les animaux
    List<Animal> readAllAnimals();
    
    // Mettre à jour un animal
    boolean updateAnimal(Animal animal);
    
    // Supprimer un animal
    boolean deleteAnimal(int id);
}