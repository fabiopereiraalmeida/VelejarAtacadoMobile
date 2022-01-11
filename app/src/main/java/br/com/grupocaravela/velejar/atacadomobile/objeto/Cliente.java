package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class Cliente {

    private Long id;
    private String razaoSocial;
    private String fantasia;
    private String inscricaoEstadual;
    private String cpf;
    private String cnpj;
    private String dataNascimento;
    private String dataCadastro;
    private String email;
    private Double limiteCredito;
    private Boolean ativo;
    private String observacao;

    private String telefone1;
    private String telefone2;

    private String endereco;
    private String enderecoNumero;
    private String complemento;
    private String bairro;
    private Long cidade_id;
    private Long estado_id;
    private String cep;
    private Long rota;
    private Long formaPagamento;

    private Boolean novo;
    private Boolean alterado;

    private Long empresa;

    private byte[] imagem;

    private String localizacao;

    private String cidade;
    private String uf;

    private Long categoriaCliente;

    public Cliente() {
    }

    public Cliente(Long id, String razaoSocial, String fantasia, String inscricaoEstadual, String cpf, String cnpj, String dataNascimento, String dataCadastro,
                   String email, Double limiteCredito, Boolean ativo, String observacao, String telefone1, String telefone2, String endereco,
                   String enderecoNumero, String complemento, String bairro, Long cidade_id, Long estado_id, String cep, Long rota, Long formaPagamento, Boolean novo,
                   Boolean alterado, Long empresa, String cidade, String uf, Long categoriaCliente, byte[] imagem, String localizacao){
        super();
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.fantasia = fantasia;
        this.inscricaoEstadual = inscricaoEstadual;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
        this.email = email;
        this.limiteCredito = limiteCredito;
        this.ativo = ativo;
        this.observacao = observacao;
        this.telefone1 = telefone1;
        this.telefone2 = telefone2;
        this.endereco = endereco;
        this.enderecoNumero = enderecoNumero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade_id = cidade_id;
        this.estado_id = estado_id;
        this.cep = cep;
        this.rota = rota;
        this.formaPagamento = formaPagamento;
        this.novo = novo;
        this.alterado = alterado;
        this.empresa = empresa;
        this.cidade = cidade;
        this.uf = uf;
        this.categoriaCliente = categoriaCliente;
        this.imagem = imagem;
        this.localizacao = localizacao;
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
        this.fantasia = fantasia;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(Double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
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

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Long getCidade_id() {
        return cidade_id;
    }

    public void setCidade_id(Long cidade_id) {
        this.cidade_id = cidade_id;
    }

    public Long getEstado_id() {
        return estado_id;
    }

    public void setEstado_id(Long estado_id) {
        this.estado_id = estado_id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Long getRota() {
        return rota;
    }

    public void setRota(Long rota) {
        this.rota = rota;
    }

    public Long getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(Long formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Boolean getNovo() {
        return novo;
    }

    public void setNovo(Boolean novo) {
        this.novo = novo;
    }

    public Boolean getAlterado() {
        return alterado;
    }

    public void setAlterado(Boolean alterado) {
        this.alterado = alterado;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }

    public Long getCategoriaCliente() {
        return categoriaCliente;
    }

    public void setCategoriaCliente(Long categoriaCliente) {
        this.categoriaCliente = categoriaCliente;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public String toString() {
        return "Cliente {" +
                "id=" + id +
                ", Razão Social='" + razaoSocial + '\'' +
                ", Data Cadastro='" + dataCadastro + '\'' +
                ", Ativo='" + ativo + '\'' +
                ", Cargo='" + limiteCredito + '\'' +
                ", Cidade='" + fantasia + '\'' +
                '}' + "\n";
    }
}
