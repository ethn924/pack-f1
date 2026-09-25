# Projet F1 — partie Python

Au début, je ne comprenais pas comment convertir le temps ni lire le CSV. J’ai fait plusieurs erreurs avant de terminer les trois fonctions Python et l’écriture du contrat.

## Mes exercices

### Exercice 1 — Fonction temps en chrono

- **Difficultés :** je ne comprenais pas le calcul, j’oubliais les parenthèses de `strip()` et je ne savais pas quand convertir les minutes et les secondes.
- **Ce que j’ai retenu :** il faut utiliser `split()`, `float()`, multiplier les minutes par 60 et arrondir avec `round()`.
- **Résultat :** `1:33.996` devient `93.996` et le test est réussi.

### Exercice 2 — Fonction `lire_resultats`

- **Difficultés :** je ne savais pas lire le CSV, sauter l’en-tête, séparer les colonnes et utiliser un dictionnaire.
- **Ce que j’ai retenu :** `open()`, `readlines()` et `split(";")` permettent de lire et de découper les données.
- **Résultat :** la fonction renvoie les résultats sans l’en-tête et le test est réussi.

### Exercice 3 — Fonction `ecrire_courses_propres`

- **Difficultés :** j’ai utilisé `readlines()` alors que je voulais écrire, puis j’ai oublié les `;`, les retours à la ligne et le traitement de `None`.
- **Ce que j’ai retenu :** `"w"` écrase le fichier, `write()` écrit et `:.3f` écrit un temps avec trois décimales.
- **Résultat :** le contrat est écrit dans le bon format et le test est réussi.

## Code Python complet de `01-python/ingestion.ipynb`

Le code ci-dessous reprend toutes les cellules Python du notebook.

```python
def temps_en_secondes(texte):
    """'1:33.996' -> 93.996 (arrondi à 3 décimales)."""
    if texte.strip() == "":  # strip = supprime les espaces
        return None
    try:
        minutes, secondes = texte.strip().split(":")  # split = sépare avec ":"
        temps_minute = float(minutes)
        temps_secondes = float(secondes)
        resultat = temps_minute * 60 + temps_secondes
        return round(resultat, 3)
    except ValueError: # exception si pb
        return None

def lire_resultats(chemin):
    """Lit le CSV brut et renvoie une liste de dictionnaires :
    {"course": str, "pilote": str, "ecurie": str, "position": int, "temps_tour": float|None}
    - position : l'entier du CSV, ou 0 si le statut est ABANDON
    - temps_tour : converti avec temps_en_secondes (None si absent)
    La ligne d'en-tête ne doit pas figurer dans le résultat."""
    with open(chemin, "r", encoding="utf-8") as fichier:  # J’ouvre le fichier
        lignes = fichier.readlines()  # Je lis les lignes
    resultats = []  # Je crée une liste vide
    for ligne in lignes[1:]:  # Je passe chaque ligne sans l’en-tête
        colonnes = ligne.strip().split(";")  # Je sépare les colonnes
        course = colonnes[0]
        pilote = colonnes[1]
        ecurie = colonnes[2]
        position = colonnes[3]
        meilleur_tour = colonnes[4]
        statut = colonnes[5]
        if (statut == "ABANDON"):  # Si le pilote a abandonné
            position = 0
        else:
            position = int(position)  # Je transforme la position en nombre
        temps_tour = temps_en_secondes(meilleur_tour)  # Je transforme le temps
        resultat = {  # Je crée un dictionnaire
            "course": course,
            "pilote": pilote,
            "ecurie": ecurie,
            "position": position,
            "temps_tour": temps_tour
        }
        resultats.append(resultat)  # J’ajoute le dictionnaire à la liste
    return resultats  # Je renvoie la liste

def ecrire_courses_propres(chemin, lignes):
    """Écrit le CONTRAT 1 : en-tête course;pilote;ecurie;position;temps_tour
    - temps_tour est écrit avec 3 décimales, ou vide si None
    - les lignes sont écrites dans l'ordre reçu"""
    with open(chemin, "w", encoding="utf-8") as fichier:  # J’ouvre le fichier
        fichier.write("course;pilote;ecurie;position;temps_tour\n")  # J’écris l’en-tête
        for ligne in lignes:  # Je prends chaque ligne
            course = ligne["course"]
            pilote = ligne["pilote"]
            ecurie = ligne["ecurie"]
            position = ligne["position"]
            temps_tour = ligne["temps_tour"]
            if temps_tour is None:  # Je laisse le temps vide
                temps_tour = ""
            else:
                temps_tour = f"{temps_tour:.3f}"  # J’arrondis le temps
            fichier.write(f"{course};{pilote};{ecurie};{position};{temps_tour}\n")  # J’écris la ligne

# ✅ Tests — exécutez cette cellule (ne pas modifier)
import os, tempfile

_resultats = {}

def _egal(obtenu, attendu):
    assert obtenu == attendu, f"attendu {attendu!r}, obtenu {obtenu!r}"

def verifier(nom, controle):
    try:
        controle()
        _resultats[nom] = True
        print(f"✅ {nom}")
    except Exception as err:
        _resultats[nom] = False
        print(f"❌ {nom} → {type(err).__name__} : {err}")

CSV_TEST = (
    "course;pilote;ecurie;position;meilleur_tour;statut\n"
    "Bahrein;VERSTAPPEN;Red Bull;1;1:33.996;ARRIVE\n"
    "Bahrein;LECLERC;Ferrari;2;1:34.211;ARRIVE\n"
    "Bahrein;HAMILTON;Mercedes;;;ABANDON\n"
)

def _fichier(contenu=""):
    chemin = os.path.join(tempfile.mkdtemp(), "f.csv")
    with open(chemin, "w", encoding="utf-8") as f:
        f.write(contenu)
    return chemin

def _test_temps():
    _egal(temps_en_secondes("1:33.996"), 93.996)
    _egal(temps_en_secondes("0:59.500"), 59.5)
    _egal(temps_en_secondes("2:00.000"), 120.0)
    _egal(temps_en_secondes(""), None)
    _egal(temps_en_secondes("   "), None)

def _test_lire():
    lignes = lire_resultats(_fichier(CSV_TEST))
    _egal(len(lignes), 3)
    _egal(lignes[0], {"course": "Bahrein", "pilote": "VERSTAPPEN", "ecurie": "Red Bull",
                      "position": 1, "temps_tour": 93.996})
    _egal(lignes[2]["position"], 0)
    _egal(lignes[2]["temps_tour"], None)
    _egal(type(lignes[1]["position"]).__name__, "int")

def _test_ecrire():
    chemin = _fichier("ancien contenu\n")
    ecrire_courses_propres(chemin, [
        {"course": "Bahrein", "pilote": "VERSTAPPEN", "ecurie": "Red Bull", "position": 1, "temps_tour": 93.996},
        {"course": "Bahrein", "pilote": "HAMILTON", "ecurie": "Mercedes", "position": 0, "temps_tour": None},
    ])
    with open(chemin, encoding="utf-8") as f:
        contenu = f.read()
    _egal(contenu,
          "course;pilote;ecurie;position;temps_tour\n"
          "Bahrein;VERSTAPPEN;Red Bull;1;93.996\n"
          "Bahrein;HAMILTON;Mercedes;0;\n")

def _test_chaine():
    entree = _fichier(CSV_TEST)
    sortie = _fichier()
    ecrire_courses_propres(sortie, lire_resultats(entree))
    with open(sortie, encoding="utf-8") as f:
        lignes = f.read().splitlines()
    _egal(len(lignes), 4)
    _egal(lignes[1], "Bahrein;VERSTAPPEN;Red Bull;1;93.996")
    _egal(lignes[3], "Bahrein;HAMILTON;Mercedes;0;")

verifier("1. temps_en_secondes", _test_temps)
verifier("2. lire_resultats", _test_lire)
verifier("3. ecrire_courses_propres", _test_ecrire)
verifier("4. chaîne complète", _test_chaine)

reussis = sum(1 for ok in _resultats.values() if ok)
print(f"\n{reussis} / {len(_resultats)} tests réussis" + (" — maillon Python validé 🎉" if reussis == 4 else ""))

# 🏁 Production du fichier réel (à exécuter une fois les 4 tests au vert)
ENTREE = "../donnees/resultats.csv"
SORTIE = "../02-java/courses_propres.csv"

lignes = lire_resultats(ENTREE)
ecrire_courses_propres(SORTIE, lignes)

print(f"{len(lignes)} lignes lues, fichier écrit : {SORTIE}\n")
for ligne in lignes[:5]:
    print(ligne)
abandons = [l for l in lignes if l["position"] == 0]
print(f"\n{len(abandons)} abandons :", [l["pilote"] for l in abandons])
```

## Tests

Les tests du notebook sont validés : **4/4**.
