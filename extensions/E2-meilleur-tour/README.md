# Extension E2 — le point du meilleur tour (Java)

Le point du meilleur tour a été introduit en 2019, à condition que son auteur termine dans les dix premiers. Il a ensuite été supprimé à partir de la saison 2025 ; l’extension travaille donc sur un cas historique.

## Ce qu'il faut ajouter à `Classement.java`

```java
// Renvoie le nom du pilote ayant signé le meilleur tour de cette course
// (le temps le plus PETIT, abandons compris), ou null si la course n'existe pas
// ou si aucun temps n'a été relevé. Un temps absent vaut -1.
public static String auteurMeilleurTour(List<Ligne> lignes, String course)

// Comme classementPilotes, mais en ajoutant le point du meilleur tour
// quand son auteur est classé entre la 1re et la 10e place.
public static List<Resultat> classementAvecMeilleurTour(List<Ligne> lignes)
```

## Vérification

```bash
cp ../extensions/E2-meilleur-tour/TestsE2.java src/      # depuis 02-java/
javac -encoding UTF-8 -d out src/*.java
java -Dstdout.encoding=UTF-8 -cp out TestsE2
```

Le jeu de test est conçu pour piéger : dans la première course, le meilleur tour est signé par un
pilote qui a abandonné, donc aucun point n'est attribué.

## Pour aller plus loin

Une fois E2 validée, faites produire `donnees.js` avec le nouveau classement : modifiez `Main.java`
pour appeler `classementAvecMeilleurTour`. Le classement change de peu — mais il change.
