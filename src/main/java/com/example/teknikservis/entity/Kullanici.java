package com.example.teknikservis.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Kullanici {

    public enum Rol {
        ADMIN,
        MUSTERI,
        TEKNISYEN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ad;

    @Column(unique = true)
    private String email;

    private String sifre;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    public Kullanici() {
    }

    public Kullanici(String ad, String email, String sifre, Rol rol) {
        this.ad = ad;
        this.email = email;
        this.sifre = sifre;
        this.rol = rol;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
