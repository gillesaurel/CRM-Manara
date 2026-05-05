package com.manara.portail.service;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.ActiviteSeance;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.ActiviteRepository;
import com.manara.portail.repository.InscriptionEnfantRepository;
import com.manara.portail.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ActiviteService {

    @Autowired
    private ActiviteRepository activiteRepository;
    @Autowired
    private InscriptionEnfantRepository inscriptionRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<ActiviteAdmin> getAllActivites() {
        return activiteRepository.findAll();
    }

    public ActiviteAdmin save(ActiviteAdmin activite) {
        // Lier chaque séance à l'activité parent
        if (activite.getSeances() != null) {
            activite.getSeances().forEach(s -> s.setActivite(activite));
        }
        return activiteRepository.save(activite);
    }

    public ActiviteAdmin getById(Long id) {
        return activiteRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        ActiviteAdmin activite = getById(id);
        if (activite == null) {
            throw new RuntimeException("Activité introuvable");
        }
        activiteRepository.delete(activite);
    }

    public int getPourcentageInscription() {
        List<ActiviteAdmin> activites = activiteRepository.findAll();
        if (activites.isEmpty()) return 0;
        long totalInscrits = activites.stream().mapToLong(ActiviteAdmin::getNbInscrits).sum();
        long totalCapacite = activites.stream().mapToInt(ActiviteAdmin::getCapacite).sum();
        if (totalCapacite == 0) return 0;
        return (int) ((totalInscrits * 100) / totalCapacite);
    }

    public boolean isComplet(ActiviteAdmin activite) {
        return activite.isComplet();
    }

    public List<ActiviteAdmin> getActivites() {
        return activiteRepository.findAll();
    }

    /**
     * Construit un ActiviteAdmin depuis un payload JSON flexible
     * pour gérer animateurId, séances, prix sans boucle circulaire
     */
    public ActiviteAdmin fromPayload(Map<String, Object> payload) {
        ActiviteAdmin activite = new ActiviteAdmin();

        // ID (pour update)
        if (payload.get("id") != null) {
            activite.setId(Long.valueOf(payload.get("id").toString()));
        }

        // Champs de base
        activite.setNom((String) payload.get("nom"));
        activite.setDescription((String) payload.get("description"));
        activite.setCapacite(Integer.parseInt(payload.get("capacite").toString()));

        if (payload.get("dateDebut") != null) {
            activite.setDateDebut(LocalDateTime.parse(payload.get("dateDebut").toString()));
        }

        if (payload.get("prix") != null) {
            activite.setPrix(Double.valueOf(payload.get("prix").toString()));
        }

        // Animateur par ID
        if (payload.get("animateurId") != null) {
            Long animateurId = Long.valueOf(payload.get("animateurId").toString());
            Utilisateur animateur = utilisateurRepository.findById(animateurId).orElse(null);
            activite.setAnimateur(animateur);
        }

        // Séances
        if (payload.get("seances") != null) {
            List<Map<String, Object>> seancesPayload = (List<Map<String, Object>>) payload.get("seances");
            List<ActiviteSeance> seances = new ArrayList<>();
            for (Map<String, Object> sp : seancesPayload) {
                ActiviteSeance seance = new ActiviteSeance();
                seance.setDateHeure(LocalDateTime.parse(sp.get("dateHeure").toString()));
                seance.setActivite(activite);
                seances.add(seance);
            }
            activite.setSeances(seances);
        }

        return activite;
    }
}
