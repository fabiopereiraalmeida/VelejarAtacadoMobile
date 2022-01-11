package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class RotaVendedor {

    private Long id;
    private Long rota;
    private Long usuario;
    private Long empresa;

    public RotaVendedor() {
    }

    public RotaVendedor(Long id, Long rota, Long usuario, Long empresa) {
        super();
        this.id = id;
        this.rota = rota;
        this.usuario = usuario;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRota() {
        return rota;
    }

    public void setRota(Long rota) {
        this.rota = rota;
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
