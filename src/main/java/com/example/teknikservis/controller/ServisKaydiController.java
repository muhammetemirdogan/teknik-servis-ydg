package com.example.teknikservis.controller;

import com.example.teknikservis.dto.ServisKaydiCreateRequest;
import com.example.teknikservis.entity.ServisKaydi;
import com.example.teknikservis.service.ServisKaydiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servis-kayitlari")
public class ServisKaydiController {

    private final ServisKaydiService servisKaydiService;

    public ServisKaydiController(ServisKaydiService servisKaydiService) {
        this.servisKaydiService = servisKaydiService;
    }

    @GetMapping
    public java.util.List<ServisKaydi> getAll() {
        return servisKaydiService.getAllServisKayitlari();
    }

    @GetMapping("/musteri/{musteriId}")
    public java.util.List<ServisKaydi> getForMusteri(@PathVariable Long musteriId) {
        return servisKaydiService.getServisKayitlariForMusteri(musteriId);
    }

    @PostMapping
    public ResponseEntity<ServisKaydi> create(@RequestBody ServisKaydiCreateRequest request) {
        ServisKaydi kayit = servisKaydiService.createServisKaydi(
                request.getMusteriId(),
                request.getCihazId(),
                request.getTeknisyenId(),
                request.getAciklama(),
                request.getAcilisTarihi()
        );
        return new ResponseEntity<>(kayit, HttpStatus.CREATED);
    }

    @PostMapping("/{servisKaydiId}/iptal")
    public ResponseEntity<Void> cancel(@PathVariable Long servisKaydiId,
                                       @RequestParam Long musteriId) {
        servisKaydiService.cancelServisKaydi(servisKaydiId, musteriId);
        return ResponseEntity.noContent().build();
    }
}
