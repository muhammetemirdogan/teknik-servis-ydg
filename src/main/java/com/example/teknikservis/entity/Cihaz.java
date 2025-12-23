package com.example.teknikservis.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cihaz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marka;

    private String model;

    @Column(name = "seri_no")
    private String seriNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musteri_id")
    private Kullanici musteri;

    public Cihaz() {
    }

    public Cihaz(String marka, String model, String seriNo, Kullanici musteri) {
        this.marka = marka;
        this.model = model;
        this.seriNo = seriNo;
        this.musteri = musteri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSeriNo() {
        return seriNo;
    }

    public void setSeriNo(String seriNo) {
        this.seriNo = seriNo;
    }

    public Kullanici getMusteri() {
        return musteri;
    }

    public void setMusteri(Kullanici musteri) {
        this.musteri = musteri;
    }
}
