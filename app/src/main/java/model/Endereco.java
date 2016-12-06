package model;

/**
 * Created by vitalate on 14/10/16.
 */
public class Endereco
{
    private int id;
    private String rua;
    private int numero;
    private String complemento;
    private String bairro;

    public Endereco(){}

    public Endereco(String rua, int numero, String bairro, String complemento)
    {
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.complemento = complemento;
    }

    public void setId(int id) {this.id = id;}



    public int getId() {return id;}

    public String getRua() {return rua;}

    public void setRua(String rua) {this.rua = rua;}

    public int getNumero() {return numero;}

    public void setNumero(int numero) {this.numero = numero;}

    public String getComplemento() {return complemento;}

    public void setComplemento(String complemento) {this.complemento = complemento;}

    public String getBairro() {return bairro;}

    public void setBairro(String bairro) {this.bairro = bairro;}

    @Override
    public String toString()
    {
        return "Endereco{" +
                "bairro='" + bairro + '\'' +
                ", complemento='" + complemento + '\'' +
                ", numero=" + numero +
                ", rua='" + rua + '\'' +
                '}';
    }
}
