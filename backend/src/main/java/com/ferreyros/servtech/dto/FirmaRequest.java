package com.ferreyros.servtech.dto;

public class FirmaRequest {
    private String firmanteNombre;
    private String firmanteDni;
    private String firmanteCargo;
    private String firmaBase64;

    public FirmaRequest() {}

    public String getFirmanteNombre() { return firmanteNombre; }
    public void setFirmanteNombre(String firmanteNombre) { this.firmanteNombre = firmanteNombre; }

    public String getFirmanteDni() { return firmanteDni; }
    public void setFirmanteDni(String firmanteDni) { this.firmanteDni = firmanteDni; }

    public String getFirmanteCargo() { return firmanteCargo; }
    public void setFirmanteCargo(String firmanteCargo) { this.firmanteCargo = firmanteCargo; }

    public String getFirmaBase64() { return firmaBase64; }
    public void setFirmaBase64(String firmaBase64) { this.firmaBase64 = firmaBase64; }
}
