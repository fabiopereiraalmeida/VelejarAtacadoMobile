package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class ContaReceber {

    private Long id;
    private Long cliente;
    private Long vendaCabecalho;
    private Double valorDevido;
    private String vencimento;
    private String dataPagamento;
    private Boolean quitada;
    private Boolean atrasada;
    private Double valorDesconto;
    private String observacao;

    private Long empresa;

    public ContaReceber() {
    }

    public ContaReceber(Long id, Long cliente, Long vendaCabecalho, Double valorDevido, String vencimento,
                        String dataPagamento, Boolean quitada, Boolean atrasada, Long empresa, Double valorDesconto,
                        String observacao) {
        super();
        this.id = id;
        this.cliente = cliente;
        this.vendaCabecalho = vendaCabecalho;
        this.valorDevido = valorDevido;
        this.vencimento = vencimento;
        this.dataPagamento = dataPagamento;
        this.quitada = quitada;
        this.atrasada = atrasada;
        this.empresa = empresa;
        this.valorDesconto = valorDesconto;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCliente() {
        return cliente;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }

    public Long getVendaCabecalho() {
        return vendaCabecalho;
    }

    public void setVendaCabecalho(Long vendaCabecalho) {
        this.vendaCabecalho = vendaCabecalho;
    }

    public Double getValorDevido() {
        return valorDevido;
    }

    public void setValorDevido(Double valorDevido) {
        this.valorDevido = valorDevido;
    }

    public String getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(String dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public Boolean getQuitada() {
        return quitada;
    }

    public void setQuitada(Boolean quitada) {
        this.quitada = quitada;
    }

    public Boolean getAtrasada() {
        return atrasada;
    }

    public void setAtrasada(Boolean atrasada) {
        this.atrasada = atrasada;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }

    public Double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(Double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
