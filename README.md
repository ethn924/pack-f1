# Projet F1

Ce dépôt montre une chaîne de traitement de données de F1 avec trois langages. Le CSV brut est nettoyé en Python, les classements sont calculés en Java, puis affichés dans une page web.

Le guide complet des notions est ici : [GUIDE_NOTIONS_F1.pdf](GUIDE_NOTIONS_F1.pdf).

## Avancement

| Étape | État | Détail |
|---|---|---|
| Python | Terminé | 4 tests sur 4 |
| Java | En cours | 2 tests sur 5 |
| JavaScript | Code écrit | 5 tests à lancer dans le navigateur |

En Java, deux fonctions ne sont pas finies : `classementEcuries` et `positionMoyenne`. Les tests 3 et 4 échouent à cause d'elles. Le test 5 les utilise aussi, donc il échoue également. Cela fait trois tests rouges pour deux fonctions manquantes : le score est bien 2/5, pas 3/5.

## La chaîne

```text
donnees/resultats.csv
        ↓ Python : lecture et nettoyage
02-java/courses_propres.csv
        ↓ Java : calcul et tri
03-js/donnees.js
        ↓ JavaScript : affichage
page de classements
```

`courses_propres.csv` et `donnees.js` sont des contrats : les colonnes et les noms de champs doivent rester exactement les mêmes d'une étape à l'autre.

## Python

Ce que j'ai fait :

- `temps_en_secondes` transforme `1:33.996` en `93.996`. Je sépare sur `:`, je convertis les deux parties en nombres, puis j'ajoute `minutes * 60 + secondes`.
- `lire_resultats` saute l'en-tête, découpe chaque ligne sur `;` et renvoie une liste de dictionnaires. Un abandon devient la position 0 et un temps vide.
- `ecrire_courses_propres` réécrit l'en-tête du contrat, garde l'ordre reçu et écrit le temps avec trois décimales.

Difficultés :

- oublier de sauter la première ligne du CSV ;
- utiliser la virgule au lieu du point-virgule ;
- écrire `None` dans le fichier au lieu de laisser la cellule vide ;
- se tromper sur l'indentation.

Ma logique : une fonction par responsabilité. Une convertit un temps, une lit, une écrit. Comme ça, si un test casse, je sais tout de suite quelle partie regarder.

Résultat : 4/4.

## Java

Ce que j'ai fait :

- `pointsPourPosition` utilise le tableau du barème et renvoie 0 pour un abandon ou une position au-delà de la 10e.
- `classementPilotes` regroupe les lignes par pilote, additionne les points, compte les victoires et les deuxièmes places, puis trie sur quatre critères : points, victoires, deuxièmes places, nom.

Ce qui reste :

- `classementEcuries` doit additionner les statistiques des pilotes d'une même écurie.
- `positionMoyenne` doit faire la moyenne des positions terminées, sans compter les abandons.

Difficultés :

- confondre la position 1 avec l'index 0 du tableau ;
- comparer des objets au lieu de comparer des nombres ;
- oublier un critère de départage quand deux pilotes ont les mêmes points ;
- compter un abandon dans une moyenne de positions.

Ma logique : j'ai commencé par le barème, puis par le classement des pilotes, parce que tout le reste s'appuie dessus. Pour le tri, je compare d'abord les points et je ne regarde les autres critères qu'en cas d'égalité.

Résultat : 2/5, deux fonctions encore à finir.

## JavaScript

Ce que j'ai fait :

- `trierParPoints` trie une copie de la liste pour ne pas modifier les données reçues. À points égaux, le pilote avec le plus de victoires passe devant.
- `remplirTableau` vide le tableau puis crée une ligne et cinq cellules par entrée.
- `marquerPodium` met la classe `podium` sur les trois premières lignes et la retire des autres.

Difficultés :

- oublier que `sort()` modifie la liste d'origine ;
- trier des nombres sans comparateur ;
- oublier de vider le tableau avant de le remplir à nouveau ;
- confondre une valeur JavaScript et un élément HTML.

Ma logique : trois petites fonctions, chacune avec un seul rôle. L'affichage complet les appelle dans le bon ordre.

La syntaxe est vérifiée, mais je ne marque pas 5/5 tant que les tests ne sont pas passés dans le navigateur.

## Lancer le projet

Depuis la racine du dépôt :

- ouvrir `01-python/ingestion.ipynb` dans Jupyter et exécuter les cellules ;
- pour Java :

```bash
cd 02-java
javac -encoding UTF-8 -d out src/*.java
java "-Dstdout.encoding=UTF-8" -cp out Tests
java "-Dstdout.encoding=UTF-8" -cp out Main
```

- ouvrir `03-js/index.html` dans un navigateur : les tests se lancent au chargement.

## Extensions

- E1 : nettoyer des données sales (casse, accents, doublons, virgules).
- E2 : ajouter le point du meilleur tour.
- E3 : filtrer par écurie et trier les colonnes au clic.
- E4 : lancer toute la chaîne en une commande.

## Ce que je retiens

- un format de fichier clair évite beaucoup d'erreurs entre deux langages ;
- une fonction courte est plus facile à tester ;
- un test qui échoue donne souvent l'indice le plus utile ;
- mieux vaut annoncer un projet incomplet que le présenter comme fini.
