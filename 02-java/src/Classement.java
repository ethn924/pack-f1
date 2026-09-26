/* =========================================================================
   MAILLON 2 — JAVA : le moteur de calcul
   Complétez les quatre méthodes. Les classes Ligne, Resultat et Chargeur
   sont fournies : ne les modifiez pas.
       javac -encoding UTF-8 -d out src/*.java
       java -Dstdout.encoding=UTF-8 -cp out Tests     (les tests)
       java -Dstdout.encoding=UTF-8 -cp out Main      (la production)
   ========================================================================= */

import java.util.List;
import java.util.ArrayList;

public class Classement {

	/** Barème officiel des dix premiers. FOURNI — NE PAS MODIFIER. */
	public static final int[] BAREME = { 25, 18, 15, 12, 10, 8, 6, 4, 2, 1 };

	// 1. pointsPourPosition(position) : points marqués pour cette position.
	// 1 -> 25, 2 -> 18, ..., 10 -> 1. Au-delà de la 10e place : 0.
	// Un abandon vaut la position 0, donc 0 point.
	public static int pointsPourPosition(int position) {
		if (position < 1 || position > BAREME.length) {
			return 0;
		}

		return BAREME[position - 1];

	}

	// 2. classementPilotes(lignes) : un Resultat par pilote, avec ses points,
	// ses victoires (position 1) et ses 2e places, trié par :
	// points décroissants, puis victoires, puis 2e places, puis nom (A→Z).
	public static List<Resultat> classementPilotes(List<Ligne> lignes) {
		List<Resultat> resultats = new ArrayList<>();

		for (Ligne ligne : lignes) {
			boolean existe = false;

			for (Resultat resultat : resultats) {
				if (ligne.pilote().equals(resultat.nom)) {
					existe = true;

					resultat.points += pointsPourPosition(ligne.position());

					if (ligne.position() == 1) {
						resultat.victoires++;
					}

					if (ligne.position() == 2) {
						resultat.deuxiemes++;
					}

					break;
				}
			}

			if (!existe) {
				Resultat resultatPilote = new Resultat(ligne.pilote(), ligne.ecurie());

				resultatPilote.points += pointsPourPosition(ligne.position());

				if (ligne.position() == 1) {
					resultatPilote.victoires++;
				}

				if (ligne.position() == 2) {
					resultatPilote.deuxiemes++;
				}

				resultats.add(resultatPilote);
			}
		}

		resultats.sort((r1, r2) -> {
			if (r1.points != r2.points) {
				return Integer.compare(r2.points, r1.points);
			}

			if (r1.victoires != r2.victoires) {
				return Integer.compare(r2.victoires, r1.victoires);
			}

			if (r1.deuxiemes != r2.deuxiemes) {
				return Integer.compare(r2.deuxiemes, r1.deuxiemes);
			}

			return r1.nom.compareTo(r2.nom);
		});

		return resultats;
	}

	// 3. classementEcuries(pilotes) : additionne les points, victoires et
	// 2e places des pilotes de chaque écurie. Même ordre de tri.
	// Étapes à suivre :
	// 1. Créer une HashMap pour regrouper les totaux par nom d'écurie
	// 2. Parcourir la liste des pilotes, ajouter les points/victoires/deuxièmes à l'écurie correspondante
	// 3. Convertir la HashMap en ArrayList de Resultat (un par écurie)
	// 4. Trier par points décroissants, puis victoires, puis deuxièmes places, puis nom
	public static List<Resultat> classementEcuries(List<Resultat> pilotes) {
		// À COMPLÉTER
		return null;
	}

	// 4. positionMoyenne(lignes, pilote) : moyenne des positions de ce pilote,
	// ABANDONS EXCLUS, arrondie à 2 décimales. 0 s'il n'a jamais terminé.
	// Ex. positions 1, 2 et un abandon -> 1.5
	// Étapes à suivre :
	// 1. Parcourir toutes les lignes du championnat
	// 2. Ne considérer que les lignes du pilote demandé (ignorer les autres)
	// 3. Ignorer les abandons (position = 0)
	// 4. Sommation des positions valides + comptage des courses terminées
	// 5. Si au moins une course terminée : retourner (somme / compteur) arrondi à 2 décimales
	// 6. Sinon retourner 0
	public static double positionMoyenne(List<Ligne> lignes, String pilote) {
		// À COMPLÉTER
		return 0;
	}
}
