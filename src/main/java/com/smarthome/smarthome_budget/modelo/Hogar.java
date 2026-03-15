package com.smarthome.smarthome_budget.modelo;

public class Hogar {
    private String NombreHogar;
        private int IDHogar;

    public Hogar() {
    }

    public Hogar(String NombreHogar, int IDHogar) {
        this.NombreHogar = NombreHogar;
        this.IDHogar = IDHogar;
    }
   
    public String getNombreHogar() {
        return NombreHogar;
    }
    
        public int getIDHogar() {
        return IDHogar;
    }

    public void setNombreHogar(String NombreHogar) {
        this.NombreHogar = NombreHogar;
    }
    
    public void setIDHogar(int IDHogar) {
        this.IDHogar = IDHogar;
    }

}
