package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class CategoriaCliente {

    private Long id;
    private String nome;
    private Long empresa;

    public CategoriaCliente() {
    }

    public CategoriaCliente(Long id, String nome, Long empresa) {
        super();
        this.id = id;
        this.nome = nome;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return nome;
    }
}
