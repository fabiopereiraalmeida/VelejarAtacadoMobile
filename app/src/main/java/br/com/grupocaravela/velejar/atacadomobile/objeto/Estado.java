package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 28/10/15.
 */
public class Estado {

    private Long id;
    private String nome;
    private String uf_estado;
    private String codigo;

    public Estado() {
    }

    public Estado(Long id, String nome, String uf_estado, String codigo) {
        super();
        this.id = id;
        this.nome = nome;
        this.uf_estado = uf_estado;
        this.codigo = codigo;
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

    public String getUf_estado() {
        return uf_estado;
    }

    public void setUf_estado(String uf_estado) {
        this.uf_estado = uf_estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "Estado {" +
                "idEstado=" + id +
                ", nomeEstado='" + nome + '\'' +
                ", UfEstado='" + uf_estado + '\'' +
                '}' + "\n";
    }

}
