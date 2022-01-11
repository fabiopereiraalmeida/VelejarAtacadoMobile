package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class AndroidCaixa {

    private Long id;
    private Double valor;
    private Long androidVendaCabecalho;
    private Long vendaCabecalho;
    private String dataRecebimento;
    private String dataTransmissao;
    private Long usuario;
    private Long cliente;
    private Long contaReceber;
    private Long empresa;
    private String observacao;

    public AndroidCaixa() {
    }

    public AndroidCaixa(Long id, Double valor, Long androidVendaCabecalho, Long vendaCabecalho, String dataRecebimento,
                        String dataTransmissao, Long usuario, Long cliente, Long contaReceber, Long empresa, String observacao) {
        super();
        this.id = id;
        this.valor = valor;
        this.androidVendaCabecalho = androidVendaCabecalho;
        this.vendaCabecalho = vendaCabecalho;
        this.dataRecebimento = dataRecebimento;
        this.dataTransmissao = dataTransmissao;
        this.usuario = usuario;
        this.cliente = cliente;
        this.contaReceber = contaReceber;
        this.empresa = empresa;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Long getAndroidVendaCabecalho() {
        return androidVendaCabecalho;
    }

    public void setAndroidVendaCabecalho(Long androidVendaCabecalho) {
        this.androidVendaCabecalho = androidVendaCabecalho;
    }

    public Long getVendaCabecalho() {
        return vendaCabecalho;
    }

    public void setVendaCabecalho(Long vendaCabecalho) {
        this.vendaCabecalho = vendaCabecalho;
    }

    public String getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(String dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public String getDataTransmissao() {
        return dataTransmissao;
    }

    public void setDataTransmissao(String dataTransmissao) {
        this.dataTransmissao = dataTransmissao;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Long getCliente() {
        return cliente;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }

    public Long getContaReceber() {
        return contaReceber;
    }

    public void setContaReceber(Long contaReceber) {
        this.contaReceber = contaReceber;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
