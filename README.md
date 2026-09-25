# Projet F1

Projet en Python, Java et JavaScript sur les résultats d’un championnat de F1.

Python nettoie les données, Java calcule les classements et JavaScript affiche le résultat.

J’ai terminé la conversion des temps, la lecture du fichier CSV et l’écriture du fichier nettoyé.

## Prérequis

- Python 3 avec Jupyter
- Java avec `javac` et `java`
- Un navigateur

## Lancer le projet

### 1. Python

Ouvre `01-python/ingestion.ipynb` dans VS Code, puis exécute les cellules dans l’ordre.

Les tests doivent afficher **4/4**. La cellule de production crée le fichier `02-java/courses_propres.csv`, utilisé par la partie Java.

### 2. Java

Depuis le dossier `02-java` :

```bash
javac -encoding UTF-8 -d out src/*.java
java -Dstdout.encoding=UTF-8 -cp out Tests
java -Dstdout.encoding=UTF-8 -cp out Main
```

La dernière commande crée `03-js/donnees.js`.

### 3. JavaScript

Ouvre `03-js/index.html` dans ton navigateur pour voir les tests et le classement.

## Structure

- `donnees/` : données brutes du championnat
- `01-python/` : lecture et nettoyage des données
- `02-java/` : calcul des classements
- `03-js/` : affichage des résultats
- `extensions/` : extensions du projet

## État

- Python : conversion, lecture et écriture terminées
- Java : à compléter
- JavaScript : à compléter