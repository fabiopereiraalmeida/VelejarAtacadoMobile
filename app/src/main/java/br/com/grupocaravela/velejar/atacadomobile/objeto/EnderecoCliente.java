package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class EnderecoCliente {

    private Long id;
    private String endereco;
    private String numero;
    private String complemento;
    private Long cidade;
    private String bairro;
    private String uf;
    private String cep;
    private Long cliente;

    public EnderecoCliente() {
    }

    public EnderecoCliente(Long id, String endereco, String numero, String complemento, Long cidade, String bairro,
                           String uf, String cep, Long cliente) {
        super();
        this.id = id;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.bairro = bairro;
        this.uf = uf;
        this.cep = cep;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public Long getCidade() {
        return cidade;
    }

    public void setCidade(Long cidade) {
        this.cidade = cidade;
    }

    public Long getCliente() {
        return cliente;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }


}
