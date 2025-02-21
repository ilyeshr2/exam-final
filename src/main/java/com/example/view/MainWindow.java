package com.example.view;

import com.example.controller.ZooService;
import com.example.model.Animal;
import com.example.model.Enclos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindow extends JFrame {
    // Services
    private final ZooService service;
    
    // Composants principaux
    private JPanel mainPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // Tables et modèles
    private JTable animauxTable;
    private DefaultTableModel animauxTableModel;
    private JTable enclosTable;
    private DefaultTableModel enclosTableModel;
    
    // Champs de formulaire pour animal
    private JTextField nomField;
    private JTextField especeField;
    private JSpinner ageSpinner;
    private JTextField regimeField;
    
    // Champ de recherche
    private JTextField searchField;
    
    public MainWindow() {
        service = new ZooService();
        
        setTitle("Gestion de Zoo");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer sur l'écran
        
        initComponents();
        chargerAnimaux();
    }
    
    private void initComponents() {
        // Configuration du panel principal avec CardLayout
        mainPanel = new JPanel(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Initialiser les différents panneaux
        initPanneauAnimaux();
        initPanneauAjoutAnimal();
        initPanneauEnclos();
        
        // Ajouter le card panel au panel principal
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Barre de menu
        initMenuBar();
        
        // Ajouter le panel principal à la fenêtre
        setContentPane(mainPanel);
    }
    
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> System.exit(0));
        menuFichier.add(itemQuitter);
        
        JMenu menuAnimaux = new JMenu("Animaux");
        JMenuItem itemAfficherAnimaux = new JMenuItem("Liste des animaux");
        JMenuItem itemAjouterAnimal = new JMenuItem("Ajouter un animal");
        
        itemAfficherAnimaux.addActionListener(e -> {
            chargerAnimaux();
            cardLayout.show(cardPanel, "animaux");
        });
        
        itemAjouterAnimal.addActionListener(e -> {
            clearAnimalForm();
            cardLayout.show(cardPanel, "ajouterAnimal");
        });
        
        menuAnimaux.add(itemAfficherAnimaux);
        menuAnimaux.add(itemAjouterAnimal);
        
        JMenu menuEnclos = new JMenu("Enclos");
        JMenuItem itemVoirEnclos = new JMenuItem("Liste des enclos");
        
        itemVoirEnclos.addActionListener(e -> {
            chargerEnclos();
            cardLayout.show(cardPanel, "enclos");
        });
        
        menuEnclos.add(itemVoirEnclos);
        
        menuBar.add(menuFichier);
        menuBar.add(menuAnimaux);
        menuBar.add(menuEnclos);
        
        setJMenuBar(menuBar);
    }
    
    private void initPanneauAnimaux() {
        JPanel animauxPanel = new JPanel(new BorderLayout(10, 10));
        animauxPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Titre
        JLabel titleLabel = new JLabel("Gestion des Animaux");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        animauxPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Panneau central avec la table et la recherche
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        
        // Panneau de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JLabel searchLabel = new JLabel("Rechercher:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Rechercher");
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Table des animaux
        animauxTableModel = new DefaultTableModel(
                new Object[]{"ID", "Nom", "Espèce", "Âge", "Régime Alimentaire"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        animauxTable = new JTable(animauxTableModel);
        animauxTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        animauxTable.getTableHeader().setReorderingAllowed(false);
        
        // Améliorer l'apparence de la table
        animauxTable.setRowHeight(25);
        animauxTable.setGridColor(Color.LIGHT_GRAY);
        animauxTable.setShowVerticalLines(true);
        animauxTable.setShowHorizontalLines(true);
        
        JScrollPane animauxScrollPane = new JScrollPane(animauxTable);
        centerPanel.add(animauxScrollPane, BorderLayout.CENTER);
        
        animauxPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panneau des boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton afficherBtn = new JButton("Afficher");
        afficherBtn.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        
        JButton ajouterBtn = new JButton("Ajouter");
        ajouterBtn.setIcon(UIManager.getIcon("FileView.fileIcon"));
        
        JButton modifierBtn = new JButton("Modifier");
        modifierBtn.setIcon(UIManager.getIcon("FileView.hardDriveIcon"));
        
        JButton supprimerBtn = new JButton("Supprimer");
        supprimerBtn.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
        
        JButton enclosBtn = new JButton("Voir les Enclos");
        enclosBtn.setIcon(UIManager.getIcon("Tree.openIcon"));
        
        buttonPanel.add(afficherBtn);
        buttonPanel.add(ajouterBtn);
        buttonPanel.add(modifierBtn);
        buttonPanel.add(supprimerBtn);
        buttonPanel.add(enclosBtn);
        
        animauxPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Événements
        afficherBtn.addActionListener(e -> {
            chargerAnimaux();
            JOptionPane.showMessageDialog(this,
                    "Liste des animaux actualisée",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        
        ajouterBtn.addActionListener(e -> {
            clearAnimalForm();
            cardLayout.show(cardPanel, "ajouterAnimal");
        });
        
        modifierBtn.addActionListener(e -> {
            int selectedRow = animauxTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner un animal",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int id = (int) animauxTableModel.getValueAt(selectedRow, 0);
            Animal animal = service.getAnimalParId(id);
            
            if (animal != null) {
                // Afficher le formulaire de modification
                showModifierAnimalDialog(animal);
            }
        });
        
        supprimerBtn.addActionListener(e -> {
            int selectedRow = animauxTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner un animal",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int id = (int) animauxTableModel.getValueAt(selectedRow, 0);
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir supprimer cet animal ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            
            if (confirmation == JOptionPane.YES_OPTION) {
                boolean succes = service.supprimerAnimal(id);
                
                if (succes) {
                    JOptionPane.showMessageDialog(this,
                            "Animal supprimé avec succès",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    chargerAnimaux();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erreur lors de la suppression de l'animal",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        enclosBtn.addActionListener(e -> {
            chargerEnclos();
            cardLayout.show(cardPanel, "enclos");
        });
        
        searchButton.addActionListener(e -> {
            rechercherAnimaux(searchField.getText().trim());
        });
        
        searchField.addActionListener(e -> {
            rechercherAnimaux(searchField.getText().trim());
        });
        
        cardPanel.add(animauxPanel, "animaux");
    }
    
    private void initPanneauAjoutAnimal() {
        JPanel ajouterAnimalPanel = new JPanel(new BorderLayout(10, 10));
        ajouterAnimalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Titre
        JLabel titleLabel = new JLabel("Ajouter un Animal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        ajouterAnimalPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Informations de l'animal"),
                new EmptyBorder(10, 10, 10, 10)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nom
        JLabel nomLabel = new JLabel("Nom:");
        nomField = new JTextField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nomLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nomField, gbc);
        
        // Espèce
        JLabel especeLabel = new JLabel("Espèce:");
        especeField = new JTextField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(especeLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(especeField, gbc);
        
        // Âge
        JLabel ageLabel = new JLabel("Âge:");
        ageSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(ageLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(ageSpinner, gbc);
        
        // Régime alimentaire
        JLabel regimeLabel = new JLabel("Régime Alimentaire:");
        regimeField = new JTextField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(regimeLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(regimeField, gbc);
        
        ajouterAnimalPanel.add(formPanel, BorderLayout.CENTER);
        
        // Panneau des boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton enregistrerBtn = new JButton("Enregistrer");
        enregistrerBtn.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
        
        JButton annulerBtn = new JButton("Annuler");
        annulerBtn.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        
        buttonPanel.add(enregistrerBtn);
        buttonPanel.add(annulerBtn);
        
        ajouterAnimalPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Événements
        enregistrerBtn.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String espece = especeField.getText().trim();
            int age = (int) ageSpinner.getValue();
            String regime = regimeField.getText().trim();
            
            if (nom.isEmpty() || espece.isEmpty() || regime.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "Tous les champs sont obligatoires", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean succes = service.ajouterAnimal(nom, espece, age, regime);
            
            if (succes) {
                JOptionPane.showMessageDialog(this, 
                        "Animal ajouté avec succès", 
                        "Succès", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                clearAnimalForm();
                chargerAnimaux();
                cardLayout.show(cardPanel, "animaux");
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Erreur lors de l'ajout de l'animal", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        annulerBtn.addActionListener(e -> {
            clearAnimalForm();
            cardLayout.show(cardPanel, "animaux");
        });
        
        cardPanel.add(ajouterAnimalPanel, "ajouterAnimal");
    }
    
    private void initPanneauEnclos() {
        JPanel enclosPanel = new JPanel(new BorderLayout(10, 10));
        enclosPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Titre
        JLabel titleLabel = new JLabel("Liste des Enclos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        enclosPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table des enclos
        enclosTableModel = new DefaultTableModel(
                new Object[]{"ID", "Nom", "Capacité", "Type d'Habitat"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enclosTable = new JTable(enclosTableModel);
        enclosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enclosTable.getTableHeader().setReorderingAllowed(false);
        
        // Améliorer l'apparence de la table
        enclosTable.setRowHeight(25);
        enclosTable.setGridColor(Color.LIGHT_GRAY);
        enclosTable.setShowVerticalLines(true);
        enclosTable.setShowHorizontalLines(true);
        
        JScrollPane enclosScrollPane = new JScrollPane(enclosTable);
        enclosPanel.add(enclosScrollPane, BorderLayout.CENTER);
        
        // Panneau des boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton retourBtn = new JButton("Retour aux Animaux");
        retourBtn.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
        
        buttonPanel.add(retourBtn);
        
        enclosPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Événements
        retourBtn.addActionListener(e -> {
            chargerAnimaux();
            cardLayout.show(cardPanel, "animaux");
        });
        
        cardPanel.add(enclosPanel, "enclos");
    }
    
    private void clearAnimalForm() {
        nomField.setText("");
        especeField.setText("");
        ageSpinner.setValue(1);
        regimeField.setText("");
    }
    
    private void showModifierAnimalDialog(Animal animal) {
        JTextField nomFieldModif = new JTextField(animal.getNom(), 20);
        JTextField especeFieldModif = new JTextField(animal.getEspece(), 20);
        JSpinner ageSpinnerModif = new JSpinner(new SpinnerNumberModel(animal.getAge(), 0, 100, 1));
        JTextField regimeFieldModif = new JTextField(animal.getRegimeAlimentaire(), 20);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(nomFieldModif, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Espèce:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(especeFieldModif, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Âge:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(ageSpinnerModif, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Régime Alimentaire:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(regimeFieldModif, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
                "Modifier l'animal", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            animal.setNom(nomFieldModif.getText().trim());
            animal.setEspece(especeFieldModif.getText().trim());
            animal.setAge((int) ageSpinnerModif.getValue());
            animal.setRegimeAlimentaire(regimeFieldModif.getText().trim());
            
            boolean succes = service.mettreAJourAnimal(animal);
            
            if (succes) {
                JOptionPane.showMessageDialog(this,
                        "Animal modifié avec succès",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                chargerAnimaux();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la modification de l'animal",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void chargerAnimaux() {
        // Vider la table
        animauxTableModel.setRowCount(0);
        
        // Charger les animaux depuis le service
        List<Animal> animaux = service.getTousLesAnimaux();
        
        // Ajouter les animaux au modèle de table
        for (Animal animal : animaux) {
            animauxTableModel.addRow(new Object[]{
                    animal.getId(),
                    animal.getNom(),
                    animal.getEspece(),
                    animal.getAge(),
                    animal.getRegimeAlimentaire()
            });
        }
    }
    
    private void chargerEnclos() {
        // Vider la table
        enclosTableModel.setRowCount(0);
        
        // Charger les enclos depuis le service
        List<Enclos> enclosList = service.getTousLesEnclos();
        
        // Ajouter les enclos au modèle de table
        for (Enclos enclos : enclosList) {
            enclosTableModel.addRow(new Object[]{
                    enclos.getId(),
                    enclos.getNom(),
                    enclos.getCapacite(),
                    enclos.getTypeHabitat()
            });
        }
    }
    
    private void rechercherAnimaux(String searchTerm) {
        // Vider la table
        animauxTableModel.setRowCount(0);
        
        // Rechercher les animaux
        List<Animal> animauxFiltered = service.rechercherAnimaux(searchTerm);
        
        // Ajouter les animaux filtrés au modèle de table
        for (Animal animal : animauxFiltered) {
            animauxTableModel.addRow(new Object[]{
                    animal.getId(),
                    animal.getNom(),
                    animal.getEspece(),
                    animal.getAge(),
                    animal.getRegimeAlimentaire()
            });
        }
        
        // Afficher un message si aucun résultat
        if (animauxFiltered.isEmpty() && !searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Aucun animal ne correspond à votre recherche",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            // Définir le look and feel pour une meilleure apparence
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}