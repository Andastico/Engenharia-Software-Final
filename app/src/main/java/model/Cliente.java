package model;

/**
 * Created by vitalate on 14/10/16.
 */
public class Cliente
{
    private int id;
    private String nome;
    private String data;
    private int sexo;

    public Cliente() {}

    public Cliente(String nome, String data, int sexo)
    {
        this.nome = nome;
        this.data = data;
        this.sexo = sexo;
    }

    public void setId(int id) {this.id = id;}

    public int getId() {return id;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getData() {return data;}

    public void setData(String data) {this.data = data;}

    public int getSexo() {return sexo;}

    public void setSexo(int sexo) {this.sexo = sexo;}

    @Override
    public String toString()
    {
        return "Cliente{" +
                "sexo=" + sexo +
                ", data='" + data + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}


