package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class AndroidContaReceber {


    private Long id;
    private Long cliente;
    private Long usuario;
    private Long vendaCabecalho;
    private Double valorDevido;
    private String dataTransmissao;
    private String dataRecebimento;

    public AndroidContaReceber() {
    }

    public AndroidContaReceber(Long id, Long cliente, Long usuario, Long vendaCabecalho, Double valorDevido,
                               String dataTransmissao, String dataRecebimento) {
        super();
        this.id = id;
        this.cliente = cliente;
        this.usuario = usuario;
        this.vendaCabecalho = vendaCabecalho;
        this.valorDevido = valorDevido;
        this.dataTransmissao = dataTransmissao;
        this.dataRecebimento = dataRecebimento;
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

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
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

    public String getDataTransmissao() {
        return dataTransmissao;
    }

    public void setDataTransmissao(String dataTransmissao) {
        this.dataTransmissao = dataTransmissao;
    }

    public String getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(String dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }
}
