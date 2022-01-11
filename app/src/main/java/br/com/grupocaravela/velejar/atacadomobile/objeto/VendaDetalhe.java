package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class VendaDetalhe {


    private Long id;
    private Long vendaCabecalho;
    private Long produto;
    private String codigo;
    private Double quantidade;
    private Double valorParcial;
    private Double valorDesconto;
    private Double valorTotal;

    public VendaDetalhe() {
    }

    public VendaDetalhe(Long id, Long vendaCabecalho, Long produto, String codigo, Double quantidade,
                        Double valorParcial, Double valorDesconto, Double valorTotal) {
        super();
        this.id = id;
        this.vendaCabecalho = vendaCabecalho;
        this.produto = produto;
        this.codigo = codigo;
        this.quantidade = quantidade;
        this.valorParcial = valorParcial;
        this.valorDesconto = valorDesconto;
        this.valorTotal = valorTotal;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
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


}
