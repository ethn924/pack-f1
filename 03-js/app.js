/* =========================================================================
   MAILLON 3 — JAVASCRIPT : l'interface
   Les données arrivent du maillon Java, dans donnees.js :
     PILOTES = [{nom, ecurie, points, victoires}, ...]
     ECURIES = [{nom, points, victoires}, ...]
   Les trois fonctions sont terminées. Les tests se lancent au chargement de la page.
   ========================================================================= */

// 1. trierParPoints(liste) : renvoie une NOUVELLE liste triée par points
//    DÉCROISSANTS. La liste reçue ne doit pas être modifiée.
//    À points égaux, celui qui a le plus de victoires passe devant.
function trierParPoints(liste) {
  // sort() modifie la liste reçue : on trie donc une copie.
  return [...liste].sort((pilote1, pilote2) => {
    if (pilote1.points !== pilote2.points) {
      return pilote2.points - pilote1.points;
    }

    return pilote2.victoires - pilote1.victoires;
  });
}

// 2. remplirTableau(idCorps, liste) : remplit le <tbody> dont l'id est fourni.
//    Une ligne <tr> par entrée, avec dans l'ordre les cellules <td> :
//      rang (1, 2, 3...) | nom | écurie (chaîne vide si absente) | points | victoires
//    Chaque <tr> porte l'attribut data-nom. Un nouvel appel REMPLACE le contenu.
function remplirTableau(idCorps, liste) {
  const corps = document.getElementById(idCorps);

  // Un nouveau classement remplace entièrement l'ancien.
  corps.replaceChildren();

  for (let i = 0; i < liste.length; i++) {
    const ligne = document.createElement("tr");
    ligne.dataset.nom = liste[i].nom;

    // Le tableau attend toujours les mêmes cinq colonnes.
    const cellules = ["", "", "", "", ""].map(() =>
      document.createElement("td"),
    );

    cellules[0].textContent = i + 1;
    cellules[1].textContent = liste[i].nom;
    cellules[2].textContent = liste[i].ecurie || "";
    cellules[3].textContent = liste[i].points;
    cellules[4].textContent = liste[i].victoires;

    ligne.append(...cellules);
    corps.appendChild(ligne);
  }
}

// 3. marquerPodium(idCorps) : ajoute la classe CSS "podium" aux TROIS PREMIÈRES
//    lignes du tableau, et la retire de toutes les autres.
function marquerPodium(idCorps) {
  const corps = document.getElementById(idCorps);
  const lignes = corps.querySelectorAll("tr");

  // La classe doit suivre le nouvel ordre des lignes.
  for (let i = 0; i < lignes.length; i++) {
    if (i < 3) {
      lignes[i].classList.add("podium");
    } else {
      lignes[i].classList.remove("podium");
    }
  }
}

/* --- FOURNI — NE PAS MODIFIER : affichage de la saison ------------------- */
function afficherSaison() {
  if (typeof PILOTES === "undefined") {
    return;
  }
  remplirTableau("corps-pilotes", trierParPoints(PILOTES));
  marquerPodium("corps-pilotes");
  remplirTableau("corps-ecuries", trierParPoints(ECURIES));
  marquerPodium("corps-ecuries");
}
