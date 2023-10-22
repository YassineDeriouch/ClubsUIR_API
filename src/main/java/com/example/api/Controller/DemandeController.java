package com.example.api.Controller;

import com.example.api.Models.DemandeModel;
import com.example.api.Service.DemandeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/demande")
@Tag(name = "Demande", description = "Gestion des Demandes d'intégration des clubs et Validation des demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @PostMapping("/envoi/demande/integration/club")
    public ResponseEntity<DemandeModel> envoiDemandeCreationClub(@RequestParam int etudiantId, @RequestParam int clubId) {
        try {
            DemandeModel demande = demandeService.EnvoiDemandeIntegrationClub(etudiantId, clubId);
            return ResponseEntity.ok(demande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/valider/demande/creation/club")
    public ResponseEntity<DemandeModel> validerDemandeCreationClub(@RequestParam int demandeId) {
        try {
            DemandeModel demande = demandeService.ValiderDemandeCreationClub(demandeId);
            return ResponseEntity.ok(demande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/list/demande/integration/club/president")
    public ResponseEntity<?> listDemandeIntegrationClubPresident(@RequestParam int etudiantId) {
        try {
            return ResponseEntity.ok(demandeService.listDemandeIntegrationClubPresident(etudiantId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/list/demande/integration/club/president/approuvee")
    public ResponseEntity<List<DemandeModel>> DemandesPresidentClubApprouvee(@RequestParam int etudiantId) {
        try {
            return ResponseEntity.ok(demandeService.DemandesPresidentClubApprouvee(etudiantId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/list/demande/integration/club/president/EnAttente")
    public ResponseEntity<List<DemandeModel>> DemandesPresidentClubEnAttente(@RequestParam int etudiantId) {
        try {
            return ResponseEntity.ok(demandeService.DemandesPresidentClubEnAttente(etudiantId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
