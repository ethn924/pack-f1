# Projet F1

Ce dépôt montre une chaîne de traitement de données de F1 avec trois langages. Le CSV brut est nettoyé en Python, les classements sont calculés en Java, puis affichés dans une page web.

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

Pièges techniques :

- `float("1:33.614")` lève une `ValueError` : un chrono n'est pas un nombre, je découpe puis je recompose `minutes * 60 + secondes`.
- `int("")` explose aussi : sur une ligne abandon la position est vide, donc je teste le statut **avant** de convertir, sinon l'ordre des instructions change la robustesse.
- `None`, `0` et `""` sont trois sentinelles qui se ressemblent mais ne vivent pas dans la même couche ; mal gérée, la valeur `None` part telle quelle dans le fichier.
- le contrat est comparé texte à texte : un `\r\n` de `csv.writer` sous Windows ou un temps resérialisé différemment suffit à le faire échouer, donc j'écris moi-même l'encodage et le format.
- `round(x, 3)` fixe la valeur et `f"{x:.3f}"` fixe la chaîne : les flottants binaires m'ont obligé à vérifier que le `double` relu côté Java retombe bien sur la même valeur.

Ma logique : j'ai écrit le chemin critique `lire → convertir → écrire` en laissant le formatage dans la fonction d'écriture. Une responsabilité par fonction, donc un test rouge désigne directement la couche fautive.

Résultat : 4/4.

## Java

Ce que j'ai fait :

- `pointsPourPosition` utilise le tableau du barème et renvoie 0 pour un abandon ou une position au-delà de la 10e.
- `classementPilotes` regroupe les lignes par pilote, additionne les points, compte les victoires et les deuxièmes places, puis trie sur quatre critères : points, victoires, deuxièmes places, nom.

Ce qui reste :

- `classementEcuries` doit additionner les statistiques des pilotes d'une même écurie.
- `positionMoyenne` doit faire la moyenne des positions terminées, sans compter les abandons.

Pièges techniques :

- `BAREME[position]` contre `BAREME[position - 1]` : le barème est indexé à partir de 1, et l'erreur reste silencieuse tant qu'aucun test ne regarde la 11e place ou l'abandon.
- ma boucle de recherche dans `resultats` est en O(n²) : correct sur 10 pilotes, mais une `HashMap` (clé = nom) est le bon réflexe, et il faut un ordre total explicite pour le tri.
- un comparateur doit être transitif : soustraire deux `int` peut déborder, et `TimSort` lève alors `Comparison method violates its general contract!`.
- `classementEcuries` reçoit des `Resultat` de pilotes, pas des `Ligne` : j'agrège par nom d'écurie en créant de nouveaux objets, sinon je modifie les pilotes et les deux classements divergent.
- `"a;b;;".split(";")` ne renvoie que 2 éléments : sur un contrat avec champs vides, il faut `split(";", -1)`, sinon la ligne abandon se décale et `IndexOutOfBoundsException` tombe.
- moyenne de positions : exclure la position 0 et traiter le cas « aucun résultat » (0.0) sans division par zéro ; l'arrondi à 2 décimales se fait sur la valeur, pas sur la chaîne.

Ma logique : j'ai commencé par le barème, puis par le classement des pilotes, parce que tout le reste s'appuie dessus. Le tri compare les points d'abord et n'examine les critères suivants qu'à égalité, ce qui garantit un ordre déterministe.

Résultat : 2/5, deux fonctions encore à finir.

## JavaScript

Ce que j'ai fait :

- `trierParPoints` trie une copie de la liste pour ne pas modifier les données reçues. À points égaux, le pilote avec le plus de victoires passe devant.
- `remplirTableau` vide le tableau puis crée une ligne et cinq cellules par entrée.
- `marquerPodium` met la classe `podium` sur les trois premières lignes et la retire des autres.

Pièges techniques :

- `sort()` trie en lexicographique **et** sur place : `[9, 25, 100]` devient `[100, 25, 9]`, et la liste reçue doit rester intacte ; la copie superficielle `[...liste]` suffit tant qu'aucun objet n'est modifié.
- `textContent` renvoie toujours une chaîne : le `9` lu dans une cellule n'est plus un nombre, ce qui change la comparaison.
- `innerHTML` interprète le HTML : un nom de pilote peut injecter du balisage et les écouteurs posés sur les lignes disparaissent. Je crée les `<tr>`/`<td>` et j'écris avec `textContent`.
- les lignes sont recréées à chaque réaffichage : un écouteur posé sur un `<tr>` est perdu, il doit vivre sur le `<th>` (ou être délégué depuis le `<tbody>`).
- mieux vaut assembler la ligne hors du DOM et l'insérer d'un coup : `appendChild` appelé cellule par cellule multiplie les recalculs de mise en page.

Ma logique : trois fonctions sans état caché, une pour chaque responsabilité du rendu ; l'affichage complet les compose dans l'ordre (trier → remplir → marquer).

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

- le contrat de données est la vraie interface du projet : le figer avant d'écrire évite de déboguer trois langages en même temps ;
- une sentinelle (`0`, `None`, `""`, `-1`) est efficace mais coûteuse : elle doit être documentée et confinée à une seule couche ;
- un test rouge est une spécification que j'avais mal lue, pas un obstacle ;
- les choix qui tiennent à l'échelle (une `HashMap` au lieu d'une boucle imbriquée, un ordre total explicite au lieu d'un tri implicite) sont ceux qu'on relit sans crainte six mois plus tard ;
- annoncer 2/5 est plus utile que d'afficher 5/5 : ça situe exactement où le travail doit reprendre.
