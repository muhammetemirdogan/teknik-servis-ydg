package com.example.teknikservis.controller;

import com.example.teknikservis.entity.ServisKaydi;
import com.example.teknikservis.repository.ServisKaydiRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servis-kayitlari")
public class ServisKaydiController {

    private final ServisKaydiRepository servisKaydiRepository;

    public ServisKaydiController(ServisKaydiRepository servisKaydiRepository) {
        this.servisKaydiRepository = servisKaydiRepository;
    }

    @GetMapping
    public List<ServisKaydi> tumServisKayitlari() {
        return servisKaydiRepository.findAll();
    }

    @GetMapping("/musteri/{musteriId}")
    public List<ServisKaydi> musteriyeGoreServisKayitlari(@PathVariable Long musteriId) {
        return servisKaydiRepository.findByCihaz_Musteri_Id(musteriId);
    }

    @PostMapping
    public ServisKaydi servisKaydiEkle(@RequestBody ServisKaydi servisKaydi) {
        return servisKaydiRepository.save(servisKaydi);
    }
}
