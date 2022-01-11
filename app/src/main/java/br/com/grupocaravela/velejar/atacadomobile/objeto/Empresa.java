package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class Empresa {

    private Long id;
    private String razaoSocial;
    private String fantasia;
    private String cnpj;
    private String inscricaoEstadual;
    private String telefone1;
    private String telefone2;
    private String endereco;
    private String enderecoNumero;
    private String bairro;
    private Long cidade;
    private Long estado;
    private String email;
    private String complemento;


    public Empresa() {
    }

    public Empresa(Long id, String razaoSocial, String fantasia, String cnpj, String inscricaoEstadual,
                   String telefone1, String telefone2, String endereco, String enderecoNumero, String bairro,
                   Long cidade, Long estado, String email, String complemento) {
        super();
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.fantasia = fantasia;
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.telefone1 = telefone1;
        this.telefone2 = telefone2;
        this.endereco = endereco;
        this.enderecoNumero = enderecoNumero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.email = email;
        this.complemento = complemento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        fantasia = fantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEnderecoNumero() {
        return enderecoNumero;
    }

    public void setEnderecoNumero(String enderecoNumero) {
        this.enderecoNumero = enderecoNumero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Long getCidade() {
        return cidade;
    }

    public void setCidade(Long cidade) {
        this.cidade = cidade;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }
}
