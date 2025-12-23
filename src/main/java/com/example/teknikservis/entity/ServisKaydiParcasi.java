package com.example.teknikservis.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "servis_kaydi_parcasi")
public class ServisKaydiParcasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int adet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parca_id")
    private Parca parca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servis_kaydi_id")
    private ServisKaydi servisKaydi;

    public ServisKaydiParcasi() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public Parca getParca() {
        return parca;
    }

    public void setParca(Parca parca) {
        this.parca = parca;
    }

    public ServisKaydi getServisKaydi() {
        return servisKaydi;
    }

    public void setServisKaydi(ServisKaydi servisKaydi) {
        this.servisKaydi = servisKaydi;
    }
}
