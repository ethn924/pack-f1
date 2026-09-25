# Projet F1

Un projet en trois étapes. En Python, je nettoie les données de F1 ; en Java, je calcule les classements ; en JavaScript, je les affiche dans une page web.

## Où j'en suis

| Étape | État | Détail |
|---|---|---|
| Python | Terminé | 4 tests sur 4 |
| Java | En cours | 2 tests sur 5 |
| JavaScript | Écrit | 5 tests à lancer dans le navigateur |

En Java, il me reste deux fonctions à écrire : `classementEcuries` (les points par écurie) et `positionMoyenne` (la moyenne des positions, sans les abandons). Trois tests sur cinq en ont besoin, d'où le 2/5. Je préfère l'annoncer plutôt que de laisser croire que tout fonctionne.

## Comment ça marche

```text
donnees/resultats.csv    (le fichier brut)
        ↓ Python : je lis et je nettoie
02-java/courses_propres.csv
        ↓ Java : je calcule les classements
03-js/donnees.js
        ↓ JavaScript : j'affiche le tableau
la page des classements
```

Chaque étape écrit un fichier que l'étape suivante doit savoir lire. Si je change un nom de colonne en route, tout ce qui suit ne fonctionne plus, et c'est ce qui m'a coûté le plus de temps.

## Python

Dans `01-python/ingestion.ipynb`, trois fonctions :

- `temps_en_secondes` : transforme `1:33.996` en `93.996`. Je coupe au `:`, puis minutes × 60 + secondes.
- `lire_resultats` : lit le CSV et range chaque ligne dans un dictionnaire. Un abandon, c'est la position 0 et pas de temps.
- `ecrire_courses_propres` : écrit le fichier propre, même en-tête, temps à trois décimales.

Ce qui m'a donné du mal :

- `float("1:33.614")` ne fonctionne pas : un temps n'est pas un nombre, il faut découper avant.
- sur une ligne d'abandon la position est vide, donc `int("")` provoque une erreur. J'ai dû regarder le statut avant de convertir la position.
- le fichier produit est comparé à celui attendu, au caractère près : un espace en trop et le test échoue.

Résultat : 4/4.

## Java

`pointsPourPosition` et `classementPilotes` sont faites. Il reste `classementEcuries` et `positionMoyenne`.

- `pointsPourPosition` : je prends le barème (25, 18, 15...) et je donne 0 point à un abandon ou à un pilote classé après la 10e place.
- `classementPilotes` : j'additionne les points de chaque pilote, je compte ses victoires et ses deuxièmes places, puis je trie par points, puis victoires, puis deuxièmes places, puis nom.

Ce qui m'a donné du mal :

- j'avais écrit `BAREME[position]` au lieu de `BAREME[position - 1]`. Aucune erreur ne s'affichait, mais les points étaient faux : je ne l'ai vu que lorsqu'un test a regardé la 11e place.
- mon premier tri ne comparait que les points : deux pilotes à égalité pouvaient donc changer d'ordre d'une exécution à l'autre.
- `classementEcuries` reçoit les résultats des pilotes. Si je les modifie en regroupant les écuries, le classement des pilotes devient faux lui aussi, donc je crée de nouvelles lignes pour les écuries.
- la moyenne doit ignorer les abandons, et il ne faut pas diviser par zéro si un pilote n'a terminé aucune course.

Résultat : 2/5, il me reste deux fonctions.

## JavaScript

Dans `03-js/app.js`, trois fonctions :

- `trierParPoints` : je trie une copie de la liste, du plus grand nombre de points au plus petit, sans toucher à la liste reçue.
- `remplirTableau` : je vide le tableau de la page, puis j'écris une ligne par pilote.
- `marquerPodium` : j'ajoute la classe `podium` aux trois premières lignes.

Ce qui m'a donné du mal :

- `sort()` trie dans l'ordre du texte, du plus petit au plus grand, et il modifie la liste reçue. Je lui indique donc la comparaison voulue et je travaille sur une copie.
- le texte lu dans une case reste du texte : `"9"` n'est pas le nombre 9.
- quand je recrée les lignes, les clics que j'avais ajoutés dessus ne répondent plus.

La syntaxe est vérifiée, mais je n'ai pas encore lancé les tests dans le navigateur : je ne marque donc pas 5/5.

## Lancer le projet

- Python : ouvrir `01-python/ingestion.ipynb` dans Jupyter et exécuter les cellules.
- Java :

```bash
cd 02-java
javac -encoding UTF-8 -d out src/*.java
java -Dstdout.encoding=UTF-8 -cp out Tests
java -Dstdout.encoding=UTF-8 -cp out Main
```

- JavaScript : ouvrir `03-js/index.html` dans un navigateur, les tests se lancent au chargement de la page.

## Extensions

À faire une fois les trois étapes au vert.

- E1 (Python) : nettoyer des données sales (casse, accents, doublons, virgules décimales).
- E2 (Java) : attribuer le point du meilleur tour.
- E3 (JavaScript) : filtrer par écurie et trier les colonnes au clic.
- E4 : lancer toute la chaîne en une commande.

## Ce que je retiens

- finir une étape avant de commencer la suivante, plutôt que d'écrire dans trois langages en même temps ;
- ce que j'écris doit rester lisible par l'étape d'après : c'est le point qui m'a coûté le plus de temps ;
- un test rouge signale une consigne que j'ai mal lue, pas une fatalité, et le corriger est plus rapide que de relire mon code au hasard ;
- la suite pour moi : `classementEcuries` et `positionMoyenne`.
