package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class Rota {

    private Long id;
    private String nome;
    private String observacao;
    private Long empresa;

    public Rota() {
    }

    public Rota(Long id, String nome, String observacao, Long empresa) {
        super();
        this.id = id;
        this.nome = nome;
        this.observacao = observacao;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }
}
