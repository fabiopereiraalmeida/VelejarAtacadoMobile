package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class Cargo {

    private Long id;
    private String nome;
    private String observacao;
    private boolean acessoConfiguracao;
    private boolean acessoVendas;
    private boolean acessoProdutos;
    private boolean acessoClientes;
    private boolean acessoCompras;
    private boolean acessoRelatorios;
    private boolean acessoContasReceber;
    private boolean acessoContasPagar;
    private boolean acessoAndroid;
    private boolean acessoRotas;
    private boolean acessoCidades;
    private boolean acessoCategorias;
    private boolean acessoUnidade;
    private boolean acessoFormaPagamento;
    private boolean acessoFornecedores;
    private boolean acessoUsuarios;

    public Cargo() {
    }

    public Cargo(Long id, String nome, String observacao, boolean acessoConfiguracao, boolean acessoVendas,
                 boolean acessoProdutos, boolean acessoClientes, boolean acessoCompras, boolean acessoRelatorios,
                 boolean acessoContasReceber, boolean acessoContasPagar, boolean acessoAndroid, boolean acessoRotas,
                 boolean acessoCidades, boolean acessoCategorias, boolean acessoUnidade, boolean acessoFormaPagamento,
                 boolean acessoFornecedores, boolean acessoUsuarios) {
        super();
        this.id = id;
        this.nome = nome;
        this.observacao = observacao;
        this.acessoConfiguracao = acessoConfiguracao;
        this.acessoVendas = acessoVendas;
        this.acessoProdutos = acessoProdutos;
        this.acessoClientes = acessoClientes;
        this.acessoCompras = acessoCompras;
        this.acessoRelatorios = acessoRelatorios;
        this.acessoContasReceber = acessoContasReceber;
        this.acessoContasPagar = acessoContasPagar;
        this.acessoAndroid = acessoAndroid;
        this.acessoRotas = acessoRotas;
        this.acessoCidades = acessoCidades;
        this.acessoCategorias = acessoCategorias;
        this.acessoUnidade = acessoUnidade;
        this.acessoFormaPagamento = acessoFormaPagamento;
        this.acessoFornecedores = acessoFornecedores;
        this.acessoUsuarios = acessoUsuarios;
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

    public boolean isAcessoConfiguracao() {
        return acessoConfiguracao;
    }

    public void setAcessoConfiguracao(boolean acessoConfiguracao) {
        this.acessoConfiguracao = acessoConfiguracao;
    }

    public boolean isAcessoVendas() {
        return acessoVendas;
    }

    public void setAcessoVendas(boolean acessoVendas) {
        this.acessoVendas = acessoVendas;
    }

    public boolean isAcessoProdutos() {
        return acessoProdutos;
    }

    public void setAcessoProdutos(boolean acessoProdutos) {
        this.acessoProdutos = acessoProdutos;
    }

    public boolean isAcessoClientes() {
        return acessoClientes;
    }

    public void setAcessoClientes(boolean acessoClientes) {
        this.acessoClientes = acessoClientes;
    }

    public boolean isAcessoCompras() {
        return acessoCompras;
    }

    public void setAcessoCompras(boolean acessoCompras) {
        this.acessoCompras = acessoCompras;
    }

    public boolean isAcessoRelatorios() {
        return acessoRelatorios;
    }

    public void setAcessoRelatorios(boolean acessoRelatorios) {
        this.acessoRelatorios = acessoRelatorios;
    }

    public boolean isAcessoContasReceber() {
        return acessoContasReceber;
    }

    public void setAcessoContasReceber(boolean acessoContasReceber) {
        this.acessoContasReceber = acessoContasReceber;
    }

    public boolean isAcessoContasPagar() {
        return acessoContasPagar;
    }

    public void setAcessoContasPagar(boolean acessoContasPagar) {
        this.acessoContasPagar = acessoContasPagar;
    }

    public boolean isAcessoAndroid() {
        return acessoAndroid;
    }

    public void setAcessoAndroid(boolean acessoAndroid) {
        this.acessoAndroid = acessoAndroid;
    }

    public boolean isAcessoRotas() {
        return acessoRotas;
    }

    public void setAcessoRotas(boolean acessoRotas) {
        this.acessoRotas = acessoRotas;
    }

    public boolean isAcessoCidades() {
        return acessoCidades;
    }

    public void setAcessoCidades(boolean acessoCidades) {
        this.acessoCidades = acessoCidades;
    }

    public boolean isAcessoCategorias() {
        return acessoCategorias;
    }

    public void setAcessoCategorias(boolean acessoCategorias) {
        this.acessoCategorias = acessoCategorias;
    }

    public boolean isAcessoUnidade() {
        return acessoUnidade;
    }

    public void setAcessoUnidade(boolean acessoUnidade) {
        this.acessoUnidade = acessoUnidade;
    }

    public boolean isAcessoFormaPagamento() {
        return acessoFormaPagamento;
    }

    public void setAcessoFormaPagamento(boolean acessoFormaPagamento) {
        this.acessoFormaPagamento = acessoFormaPagamento;
    }

    public boolean isAcessoFornecedores() {
        return acessoFornecedores;
    }

    public void setAcessoFornecedores(boolean acessoFornecedores) {
        this.acessoFornecedores = acessoFornecedores;
    }

    public boolean isAcessoUsuarios() {
        return acessoUsuarios;
    }

    public void setAcessoUsuarios(boolean acessoUsuarios) {
        this.acessoUsuarios = acessoUsuarios;
    }


}
