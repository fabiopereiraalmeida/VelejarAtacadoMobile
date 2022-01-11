package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class AndroidVendaDetalhe {

    private Long id;
    private Long vendaCabecalho;
    private Long produto;
    private Double quantidade;
    private Double valorUnitario;
    private Double valorParcial;
    private Double valorDesconto;
    private Double valorTotal;

    private Long empresa;

    public AndroidVendaDetalhe() {
    }

    public AndroidVendaDetalhe(Long id, Long vendaCabecalho, Long produto, Double quantidade, Double valorUnitario,
                               Double valorParcial, Double valorDesconto, Double valorTotal, Long empresa) {
        super();
        this.id = id;
        this.vendaCabecalho = vendaCabecalho;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorParcial = valorParcial;
        this.valorDesconto = valorDesconto;
        this.valorTotal = valorTotal;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVendaCabecalho() {
        return vendaCabecalho;
    }

    public void setVendaCabecalho(Long vendaCabecalho) {
        this.vendaCabecalho = vendaCabecalho;
    }

    public Long getProduto() {
        return produto;
    }

    public void setProduto(Long produto) {
        this.produto = produto;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
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

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }
}
