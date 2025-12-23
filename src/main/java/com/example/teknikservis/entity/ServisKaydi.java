package com.example.teknikservis.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "servis_kaydi")
public class ServisKaydi {

    public enum Durum {
        ACIK,
        IPTAL,
        PARCA_BEKLENIYOR,
        TAMAMLANDI
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aciklama;

    @Column(name = "acilis_tarihi")
    private LocalDateTime acilisTarihi;

    @Column(name = "kapanis_tarihi")
    private LocalDateTime kapanisTarihi;

    @Enumerated(EnumType.STRING)
    private Durum durum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cihaz_id")
    private Cihaz cihaz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teknisyen_id")
    private Kullanici teknisyen;

    public ServisKaydi() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public LocalDateTime getAcilisTarihi() {
        return acilisTarihi;
    }

    public void setAcilisTarihi(LocalDateTime acilisTarihi) {
        this.acilisTarihi = acilisTarihi;
    }

    public LocalDateTime getKapanisTarihi() {
        return kapanisTarihi;
    }

    public void setKapanisTarihi(LocalDateTime kapanisTarihi) {
        this.kapanisTarihi = kapanisTarihi;
    }

    public Durum getDurum() {
        return durum;
    }

    public void setDurum(Durum durum) {
        this.durum = durum;
    }

    public Cihaz getCihaz() {
        return cihaz;
    }

    public void setCihaz(Cihaz cihaz) {
        this.cihaz = cihaz;
    }

    public Kullanici getTeknisyen() {
        return teknisyen;
    }

    public void setTeknisyen(Kullanici teknisyen) {
        this.teknisyen = teknisyen;
    }
}
