package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class TelefoneCliente {

    private Long id;
    private String telefone;
    private String tipoTelefone;
    private Long cliente;

    public TelefoneCliente() {
    }

    public TelefoneCliente(Long id, String telefone, String tipoTelefone, Long cliente) {
        super();
        this.id = id;
        this.telefone = telefone;
        this.tipoTelefone = tipoTelefone;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public Long getCliente() {
        return cliente;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }
}
