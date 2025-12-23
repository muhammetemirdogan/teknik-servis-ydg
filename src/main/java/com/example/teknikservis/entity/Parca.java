package com.example.teknikservis.entity;

import jakarta.persistence.*;

@Entity
public class Parca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ad;

    @Column(name = "birim_fiyat", nullable = false)
    private double birimFiyat;

    public Parca() {
    }

    public Parca(String ad, double birimFiyat) {
        this.ad = ad;
        this.birimFiyat = birimFiyat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public double getBirimFiyat() {
        return birimFiyat;
    }

    public void setBirimFiyat(double birimFiyat) {
        this.birimFiyat = birimFiyat;
    }
}
