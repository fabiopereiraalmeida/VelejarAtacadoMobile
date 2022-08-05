package br.com.grupocaravela.velejar.atacadomobile.objeto;

import java.util.Date;

/**
 * Created by fabio on 29/10/15.
 */
public class CreditoUsuario {


    private Long id;
    private Double valor;
    private String data;
    private Long vendaDetalhe;
    private Long usuario;
    private Long empresa;

    public CreditoUsuario() {
    }

    public CreditoUsuario(Long id, Double valor, String data, Long vendaDetalhe, Long usuario, Long empresa) {
        super();
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.vendaDetalhe = vendaDetalhe;
        this.usuario = usuario;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getVendaDetalhe() {
        return vendaDetalhe;
    }

    public void setVendaDetalhe(Long vendaDetalhe) {
        this.vendaDetalhe = vendaDetalhe;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }
}
