package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 29/10/15.
 */
public class Produto {

    private Long id;
    private String codigo;
    private String nome;
    private Double valorMinimoVenda;
    private Double valorDesejavelVenda;
    private Double estoque;
    private Double expositor;
    private Double peso;
    private Long unidade;
    private Long categoria;
    private Boolean ativo;
    private Long empresa;

    private byte[] imagem;

    private String codigo_ref;

    public Produto() {

    }

    public Produto(Long id, String codigo, String nome, Double valorMinimoVenda,
                   Double valorDesejavelVenda, Double estoque, Double expositor, Double peso, Long unidade,
                   Long categoria, Boolean ativo, Long empresa, byte[] imagem, String codigo_ref) {
        super();
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.valorMinimoVenda = valorMinimoVenda;
        this.valorDesejavelVenda = valorDesejavelVenda;
        this.estoque = estoque;
        this.expositor = expositor;
        this.peso = peso;
        this.unidade = unidade;
        this.categoria = categoria;
        this.ativo = ativo;
        this.empresa = empresa;
        this.imagem = imagem;
        this.codigo_ref = codigo_ref;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValorMinimoVenda() {
        return valorMinimoVenda;
    }

    public void setValorMinimoVenda(Double valorMinimoVenda) {
        this.valorMinimoVenda = valorMinimoVenda;
    }

    public Double getValorDesejavelVenda() {
        return valorDesejavelVenda;
    }

    public void setValorDesejavelVenda(Double valorDesejavelVenda) {
        this.valorDesejavelVenda = valorDesejavelVenda;
    }

    public Long getUnidade() {
        return unidade;
    }

    public void setUnidade(Long unidade) {
        this.unidade = unidade;
    }

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getEstoque() {
        return estoque;
    }

    public void setEstoque(Double estoque) {
        this.estoque = estoque;
    }

    public Double getExpositor() {
        return expositor;
    }

    public void setExpositor(Double expositor) {
        this.expositor = expositor;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getCodigo_ref() {
        return codigo_ref;
    }

    public void setCodigo_ref(String codigo_ref) {
        this.codigo_ref = codigo_ref;
    }
}
