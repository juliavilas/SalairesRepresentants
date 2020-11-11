package representation;

import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestRepresentant {

    // Quelques constantes
    private static final float FIXE_BASTIDE = 1000f;
    private static final float INDEMNITE_OCCITANIE = 200f;

    private Representant r; // L'objet à tester
    private ZoneGeographique occitanie;

    @BeforeEach
    public void setUp() {
        // Initialiser les objets utilisés dans les tests
        occitanie = new ZoneGeographique(1, "Occitanie");
        occitanie.setIndemniteRepas(INDEMNITE_OCCITANIE);

        r = new Representant(36, "Bastide", "Rémi", occitanie);
        r.setSalaireFixe(FIXE_BASTIDE);
    }

    @Test
    public void testGetNumero(){
        assertEquals(36,r.getNumero(),"getNumero() renvoie le mauvais numéro");
    }
    
    @Test
    public void testGetNom(){
        assertEquals("Bastide",r.getNom(),"getNom() renvoie le mauvais nom");
    }
    
    @Test
    public void testGetPrenom(){
        assertEquals("Rémi",r.getPrenom(),"getPrenom() renvoie le mauvais prénom");
    }
    
    @Test
    public void testSetAdresse() {
        r.setAdresse("2 rue des champs");
        assertEquals("2 rue des champs", r.getAdresse(), "setAdresse() modifie mal l'adresse");
    }

    @Test
    public void testGetAdresse() {
        r.setAdresse("2 rue des champs");
        r.getAdresse();
        assertEquals("2 rue des champs", r.getAdresse(), "getAdresse() renvoie la mauvaise adresse");
    }

    @Test
    public void testSetSalaireFixe() {
        r.setSalaireFixe(FIXE_BASTIDE);
        assertEquals(FIXE_BASTIDE, r.getSalaireFixe(), "setSalaireFixe() modifie mal le salaire fixe");
    }

    @Test
    public void testGetSalaireFixe() {
        r.setSalaireFixe(FIXE_BASTIDE);
        r.getSalaireFixe();
        assertEquals(FIXE_BASTIDE, r.getSalaireFixe(), "getSalaireFixe() renvoie le mauvais salaire");
    }

    @Test
    public void testSetCAMensuel() {
        float tab[] = {50000, 49000, 46000, 53000, 50100, 48000, 51050, 48500, 56100, 50300, 48000, 47500, 49500};
        r.setCAMensuel(tab);
        assertEquals(tab, r.getCAMensuel(), "setCAMensuel() modifie mal le CA mensuel");
    }

    @Test
    public void testGetCAMensuel() {
        float tab[] = {50000, 49000, 46000, 53000, 50100, 48000, 51050, 48500, 56100, 50300, 48000, 47500, 49500};
        r.setCAMensuel(tab);
        r.getCAMensuel();
        assertEquals(tab, r.getCAMensuel(), "getCAMensuel() revoie le mauvais CA mensuel");
    }

    @Test
    public void testSetSecteur() {
        r.setSecteur(occitanie);
        assertEquals(occitanie, r.getSecteur(), "setSecteur() modifie mal le secteur");
    }

    @Test
    public void testGetSecteur() {
        r.setSecteur(occitanie);
        r.getSecteur();
        assertEquals(occitanie, r.getSecteur(), "getSecteur() renvoie le mauvais secteur");
    }

    @Test
    public void testSalaireMensuel() {
        float CA = 50000f;
        float POURCENTAGE = 0.1f; // 10% de pourcentage sur CA
        // On enregistre un CA pour le mois 0 (janvier)
        r.enregistrerCA(0, CA);

        // On calcule son salaire pour le mois 0 avec 10% de part sur CA
        float salaire = r.salaireMensuel(0, POURCENTAGE);

        // A quel résultat on s'attend ?
		
        assertEquals(// Comparaison de "float"
                // valeur attendue
                FIXE_BASTIDE + INDEMNITE_OCCITANIE + CA * POURCENTAGE,
                // Valeur calculée
                salaire,
                // Marge d'erreur tolérée
                0.001,
                // Message si erreur
                "Le salaire mensuel est incorrect"
        );
    }

    @Test
    public void testSalaireMensuelMois() {
        try {
            r.salaireMensuel(36, (float) 0.1);
            fail("Un mois non compris entre 0 et 11 doit lever une exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testSalaireMensuelPourcentage() {
        try {
            r.salaireMensuel(2, 2);
            fail("Un pourcentage non compris entre 0 et 1 doit lever une exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testCAParDefaut() {
        float POURCENTAGE = 0.1f; // 10% de pourcentage sur CA

        // On n'enregistre aucun CA
        r.enregistrerCA(0, 0);

        // On calcule son salaire pour le mois 0 avec 10% de part sur CA
        float salaire = r.salaireMensuel(0, POURCENTAGE);

        // A quel résultat on s'attend ?
        // Le CA du mois doit avoir été initialisé à 0
		
        assertEquals(
                FIXE_BASTIDE + INDEMNITE_OCCITANIE,
                salaire,
                0.001,
                "Le CA n'est pas correctement initialisé"
        );
    }

    @Test
    public void testCANegatifImpossible() {

        try {
            // On enregistre un CA négatif, que doit-il se passer ?
            // On s'attend à recevoir une exception
            r.enregistrerCA(0, -10000f);
            // Si on arrive ici, c'est une erreur, le test doit échouer
            fail("Un CA négatif doit générer une exception"); // Forcer l'échec du test			
        } catch (IllegalArgumentException e) {
            // Si on arrive ici, c'est normal, c'est le comportement attendu
        }

    }

    @Test
    public void testEnregistrerCAMois() {
        try {
            r.enregistrerCA(16, 48000f);
            fail("Un mois non compris entre 0 et 11 doit lever une exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testEnregistrerCA() {
        r.enregistrerCA(3, 50000f);
        assertEquals(50000f, r.getCAMensuel()[3], "Le CA enregistré n'est pas le bon");
    }

    @Test
    public void testToString() {
        assertEquals("Representant{numero=36, nom=Bastide, prenom=Rémi, secteur=ZoneGeographique{numero=1, nom=Occitanie, indemniteRepas=200.0}}", r.toString(), "toString() n'affiche pas le bon message");
    }
}
