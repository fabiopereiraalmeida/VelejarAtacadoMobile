package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class VendaCabecalho {

    private Long id;
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
    private String status;

    private Long empresa;

    public VendaCabecalho() {
    }

    public VendaCabecalho(Long id, String observacao, Long cliente, Long clienteMobile, Double entrada, Double juros, Double valorParcial,
                          Double valorDesconto, Double valorTotal, String dataVenda, String dataPrimeiroVencimento, Long usuario, Long formaPagamento,
                          Long empresa, String status) {
        super();
        this.id = id;
        this.observacao = observacao;
        this.cliente = cliente;
        this.clienteMobile = clienteMobile;
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
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getClienteMobile() {
        return clienteMobile;
    }

    public void setClienteMobile(Long clienteMobile) {
        this.clienteMobile = clienteMobile;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }
}
