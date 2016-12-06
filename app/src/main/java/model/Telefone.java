package model;

/**
 * Created by vitalate on 14/10/16.
 */
public class Telefone
{
    private int id;
    private String numero;
    private String tipo;

    public Telefone() {}

    public Telefone(String numero, String tipo)
    {
        this.numero = numero;
        this.tipo = tipo;
    }

    public int getId() {return id;}

    public String getNumero() {return numero;}

    public void setId(int id) {this.id = id;}

    public void setNumero(String numero) {this.numero = numero;}

    public String getTipo() {return tipo;}

    public void setTipo(String tipo) {this.tipo = tipo;}

    @Override
    public String toString() {
        return "Telefone{" +
                "numero='" + numero + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
