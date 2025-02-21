package com.example.dao;

import com.example.model.Animal;
import com.example.model.Enclos;
import com.example.model.Soigneur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZooDatabaseManager implements IZooDataAccess {
    // Instance unique (pattern Singleton)
    private static ZooDatabaseManager instance;
    
    // URL de connexion à la base de données SQLite
    private static final String DB_URL = "jdbc:sqlite:zoo.db";
    
    // Connexion à la base de données
    private Connection connection;
    
    // Constructeur privé (pattern Singleton)
    private ZooDatabaseManager() {
        initializeDatabase();
    }
    
    // Méthode pour obtenir l'instance unique (pattern Singleton)
    public static synchronized ZooDatabaseManager getInstance() {
        if (instance == null) {
            instance = new ZooDatabaseManager();
        }
        return instance;
    }
    
    // Initialisation de la base de données
    private void initializeDatabase() {
        try {
            // Établir la connexion
            connection = DriverManager.getConnection(DB_URL);
            
            // Créer les tables si elles n'existent pas
            createTables();
            
            // Initialiser les données par défaut
            initialiserEnclosParDefaut();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
        }
    }
    
    // Création des tables
    private void createTables() throws SQLException {
        Statement statement = connection.createStatement();
        
        // Table Animal
        statement.execute("CREATE TABLE IF NOT EXISTS animal (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL," +
                "espece TEXT NOT NULL," +
                "age INTEGER NOT NULL," +
                "regime_alimentaire TEXT NOT NULL)");
        
        // Table Enclos
        statement.execute("CREATE TABLE IF NOT EXISTS enclos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL," +
                "capacite INTEGER NOT NULL," +
                "type_habitat TEXT NOT NULL)");
        
        // Table Soigneur
        statement.execute("CREATE TABLE IF NOT EXISTS soigneur (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL," +
                "specialite TEXT NOT NULL)");
        
        statement.close();
    }
    
    // Initialiser les enclos par défaut
    private void initialiserEnclosParDefaut() {
        // Vérifier si des enclos existent déjà
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM enclos")) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                // Aucun enclos n'existe, on les crée
                String[] requetes = {
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('Savane africaine', 10, 'Plaine')",
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('Foret tropicale', 8, 'humide')",
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('arctique', 5, 'Froid')",
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('Desert du Sahara', 6, 'sec')",
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('coral', 15, 'Aquatique')",
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('himalaya', 4, 'Montagne')",
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('Jungle amazonienne', 12, 'Forêt tropicale')",
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('Prairie nord-américaine', 8, 'Prairie')",
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('Marais tropical', 7, 'Marécage')",
                    "INSERT INTO enclos (nom, capacite, type_habitat) VALUES ('Volière tropicale', 20, 'Aérien')"
                };
                
                for (String requete : requetes) {
                    stmt.executeUpdate(requete);
                }
                System.out.println("Enclos par défaut créés avec succès");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation des enclos par défaut : " + e.getMessage());
        }
    }
    
    
    // Implémentation des méthodes CRUD pour Animal
    @Override
    public int createAnimal(Animal animal) {
        String sql = "INSERT INTO animal (nom, espece, age, regime_alimentaire) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, animal.getNom());
            pstmt.setString(2, animal.getEspece());
            pstmt.setInt(3, animal.getAge());
            pstmt.setString(4, animal.getRegimeAlimentaire());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'animal : " + e.getMessage());
        }
        
        return -1; // Échec
    }
    
    @Override
    public Animal readAnimalById(int id) {
        String sql = "SELECT * FROM animal WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Animal(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("espece"),
                        rs.getInt("age"),
                        rs.getString("regime_alimentaire")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture de l'animal : " + e.getMessage());
        }
        
        return null;
    }
    
    @Override
    public List<Animal> readAllAnimals() {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animal";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                animals.add(new Animal(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("espece"),
                        rs.getInt("age"),
                        rs.getString("regime_alimentaire")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture des animaux : " + e.getMessage());
        }
        
        return animals;
    }
    
    @Override
    public boolean updateAnimal(Animal animal) {
        String sql = "UPDATE animal SET nom = ?, espece = ?, age = ?, regime_alimentaire = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, animal.getNom());
            pstmt.setString(2, animal.getEspece());
            pstmt.setInt(3, animal.getAge());
            pstmt.setString(4, animal.getRegimeAlimentaire());
            pstmt.setInt(5, animal.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'animal : " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteAnimal(int id) {
        String sql = "DELETE FROM animal WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'animal : " + e.getMessage());
            return false;
        }
    }
    
    // Méthodes pour les enclos
    public int createEnclos(Enclos enclos) {
        String sql = "INSERT INTO enclos (nom, capacite, type_habitat) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, enclos.getNom());
            pstmt.setInt(2, enclos.getCapacite());
            pstmt.setString(3, enclos.getTypeHabitat());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'enclos : " + e.getMessage());
        }
        
        return -1; // Échec
    }
    
    public Enclos readEnclosById(int id) {
        String sql = "SELECT * FROM enclos WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Enclos(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("capacite"),
                        rs.getString("type_habitat")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture de l'enclos : " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Enclos> readAllEnclos() {
        List<Enclos> enclosList = new ArrayList<>();
        String sql = "SELECT * FROM enclos";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                enclosList.add(new Enclos(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("capacite"),
                        rs.getString("type_habitat")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture des enclos : " + e.getMessage());
        }
        
        return enclosList;
    }
    
    public boolean updateEnclos(Enclos enclos) {
        String sql = "UPDATE enclos SET nom = ?, capacite = ?, type_habitat = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, enclos.getNom());
            pstmt.setInt(2, enclos.getCapacite());
            pstmt.setString(3, enclos.getTypeHabitat());
            pstmt.setInt(4, enclos.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'enclos : " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteEnclos(int id) {
        String sql = "DELETE FROM enclos WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'enclos : " + e.getMessage());
            return false;
        }
    }
    
    // Fermeture de la connexion
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}