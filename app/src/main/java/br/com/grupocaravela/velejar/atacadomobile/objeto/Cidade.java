package br.com.grupocaravela.velejar.atacadomobile.objeto;

/**
 * Created by fabio on 28/10/15.
 */
public class Cidade {

    private Long id;
    private Long id_estado;
    private String nome;
    private String ibge;

    public Cidade() {
    }

    public Cidade(Long id, Long id_estado, String nome, String ibge) {
        super();
        this.id = id;
        this.id_estado = id_estado;
        this.nome = nome;
        this.ibge = ibge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_estado() {
        return id_estado;
    }

    public void setId_estado(Long id_estado) {
        this.id_estado = id_estado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    @Override
    public String toString() {
        return "Cidade {" +
                "idCidade=" + id +
                ", nomeCidade='" + nome + '\'' +
                ", idEstado='" + id_estado + '\'' +
                '}' + "\n";
    }

}
