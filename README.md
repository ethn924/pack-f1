# Projet F1

Projet en Python, Java et JavaScript sur les résultats d’un championnat de F1.

Python nettoie les données, Java calcule les classements et JavaScript affiche le résultat.

J’ai terminé la conversion des temps et la lecture du fichier CSV. L’écriture du contrat est terminée.

## Mon parcours

- **Temps en secondes** : j’ai utilisé `split()`, `float()` et `round()`. J’ai eu du mal à comprendre le calcul. J’ai retenu qu’il faut convertir chaque partie avant de calculer.
- **Lecture du CSV** : j’ai ouvert le fichier, ignoré l’en-tête et utilisé `split(";")`. J’ai eu du mal avec les dictionnaires et l’indentation. J’ai retenu que `ligne["..."]` donne une valeur du dictionnaire.
- **Écriture du contrat** : j’ai écrit l’en-tête puis chaque ligne dans l’ordre. J’ai eu du mal à gérer `None` et à écrire le fichier. J’ai retenu que `"w"` écrase le fichier et qu’un temps absent doit être écrit `""`.