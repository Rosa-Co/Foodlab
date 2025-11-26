package progetto.app.model;

import java.math.BigDecimal;

public class Ingrediente {
    private int id;
    private String nome;
    private String categoria;
    private String unitaMisura;
    private BigDecimal costoUnitario;

    public Ingrediente(String nome, String categoria, String unitaMisura, BigDecimal costoUnitario) {
        this.nome = nome;
        this.categoria = categoria;
        this.unitaMisura = unitaMisura;
        this.costoUnitario = costoUnitario;
    }

    public Ingrediente(int id, String nome, String categoria, String unitaMisura, BigDecimal costoUnitario) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.unitaMisura = unitaMisura;
        this.costoUnitario = costoUnitario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }
}
