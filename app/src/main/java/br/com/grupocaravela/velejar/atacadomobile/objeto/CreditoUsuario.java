package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class CreditoUsuario {


    private Long id;
    private Double valor;
    private Long usuario;

    public CreditoUsuario() {
    }

    public CreditoUsuario(Long id, Double valor, Long usuario) {
        super();
        this.id = id;
        this.valor = valor;
        this.usuario = usuario;

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

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }
}
