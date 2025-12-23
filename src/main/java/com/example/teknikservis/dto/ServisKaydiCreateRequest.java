package com.example.teknikservis.dto;

import java.time.LocalDateTime;

public class ServisKaydiCreateRequest {

    private Long musteriId;
    private Long cihazId;
    private Long teknisyenId;
    private String aciklama;
    private LocalDateTime acilisTarihi;

    public ServisKaydiCreateRequest() {
    }

    public Long getMusteriId() {
        return musteriId;
    }

    public void setMusteriId(Long musteriId) {
        this.musteriId = musteriId;
    }

    public Long getCihazId() {
        return cihazId;
    }

    public void setCihazId(Long cihazId) {
        this.cihazId = cihazId;
    }

    public Long getTeknisyenId() {
        return teknisyenId;
    }

    public void setTeknisyenId(Long teknisyenId) {
        this.teknisyenId = teknisyenId;
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
}
