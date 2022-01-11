package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private Boolean ativo;
    private Double credito;
    private Long rota;
    private Long empresa;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String senha, String telefone,
                   Boolean ativo, Double credito, Long rota, Long empresa) {
        super();
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.ativo = ativo;
        this.credito = credito;
        this.rota = rota;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getRota() {
        return rota;
    }

    public void setRota(Long rota) {
        this.rota = rota;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }

    public Double getCredito() {
        return credito;
    }

    public void setCredito(Double credito) {
        this.credito = credito;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Usuario {" +
                "idUsuario=" + id +
                ", nome='" + nome + '\'' +
                ", Ativo='" + ativo + '\'' +
                ", E-mail='" + email + '\'' +
                '}' + "\n";
    }
}
