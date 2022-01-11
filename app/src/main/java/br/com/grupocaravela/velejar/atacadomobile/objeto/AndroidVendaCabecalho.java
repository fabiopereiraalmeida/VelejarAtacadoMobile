package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class AndroidVendaCabecalho {

    private Long id;
    private Long codVenda;
    private String observacao;
    private Long cliente;
    private Long clienteMobile;
    private Double entrada;
    private Double juros;
    private Double valorParcial;
    private Double valorDesconto;
    private Double valorTotal;
    private String dataVenda;
    private String dataPrimeiroVencimento;
    private Long usuario;
    private Long formaPagamento;
    private Long empresa;
    private Boolean vendaAprovada;
    private Boolean enviado;

    public AndroidVendaCabecalho() {
    }

    public AndroidVendaCabecalho(Long id, Long codVenda, String observacao, Long cliente, Double entrada , Double juros, Double valorParcial,
                                 Double valorDesconto, Double valorTotal, String dataVenda, String dataPrimeiroVencimento, Long usuario, Long formaPagamento,
                                 Long empresa, Boolean vendaAprovada, Boolean enviado, Long clienteMobile) {
        super();
        this.id = id;
        this.codVenda = codVenda;
        this.observacao = observacao;
        this.cliente = cliente;
        this.entrada = entrada;
        this.juros = juros;
        this.valorParcial = valorParcial;
        this.valorDesconto = valorDesconto;
        this.valorTotal = valorTotal;
        this.dataVenda = dataVenda;
        this.dataPrimeiroVencimento = dataPrimeiroVencimento;
        this.usuario = usuario;
        this.formaPagamento = formaPagamento;
        this.empresa = empresa;
        this.vendaAprovada = vendaAprovada;
        this.enviado = enviado;
        this.clienteMobile = clienteMobile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodVenda() {
        return codVenda;
    }

    public void setCodVenda(Long codVenda) {
        this.codVenda = codVenda;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getCliente() {
        return cliente;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }

    public Double getEntrada() {
        return entrada;
    }

    public void setEntrada(Double entrada) {
        this.entrada = entrada;
    }

    public Double getJuros() {
        return juros;
    }

    public void setJuros(Double juros) {
        this.juros = juros;
    }

    public Double getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(Double valorParcial) {
        this.valorParcial = valorParcial;
    }

    public Double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(Double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getDataPrimeiroVencimento() {
        return dataPrimeiroVencimento;
    }

    public void setDataPrimeiroVencimento(String dataPrimeiroVencimento) {
        this.dataPrimeiroVencimento = dataPrimeiroVencimento;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Long getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(Long formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }

    public Boolean getVendaAprovada() {
        return vendaAprovada;
    }

    public void setVendaAprovada(Boolean vendaAprovada) {
        this.vendaAprovada = vendaAprovada;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Long getClienteMobile() {
        return clienteMobile;
    }

    public void setClienteMobile(Long clienteMobile) {
        this.clienteMobile = clienteMobile;
    }
}
