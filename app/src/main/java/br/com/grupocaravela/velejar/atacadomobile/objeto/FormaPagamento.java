package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class FormaPagamento {

    private Long id;
    private String nome;
    private Integer numeroParcelas;
    private Integer numeroDias;
    private String observacao;
    private Double juros;
    private Double valorMinimo;
    private Boolean geral;

    private Long empresa;

    public FormaPagamento() {
    }

    public FormaPagamento(Long id, String nome, Integer numeroParcelas, Integer numeroDias, String observacao,
                          Double juros, Double valorMinimo, Boolean geral, Long empresa) {
        super();
        this.id = id;
        this.nome = nome;
        this.numeroParcelas = numeroParcelas;
        this.numeroDias = numeroDias;
        this.observacao = observacao;
        this.juros = juros;
        this.valorMinimo = valorMinimo;
        this.geral = geral;
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

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public Integer getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(Integer numeroDias) {
        this.numeroDias = numeroDias;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Double getJuros() {
        return juros;
    }

    public void setJuros(Double juros) {
        this.juros = juros;
    }

    public Double getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(Double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public Boolean getGeral() {
        return geral;
    }

    public void setGeral(Boolean geral) {
        this.geral = geral;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }
}
