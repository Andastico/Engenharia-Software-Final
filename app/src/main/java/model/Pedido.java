package model;

/**
 * Created by Anderson on 20/10/2016.
 */

public class Pedido
{
    private int id;
    private int pago;
    private int codCliente;
    private double preco;
    private String observacao;
    private String dataInicial;
    private String dataLimite;
    private String dataFinal;

    public Pedido()
    {

    }

    public Pedido(int id, double preco, String observacao, String dataInicial, String dataFinal, int pago) {
        this.id = id;
        this.preco = preco;
        this.observacao = observacao;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.pago = pago;
    }

    public Pedido(int pago, int codCliente, double preco, String observacao, String dataInicial, String dataFinal, String dataLimite) {
        this.pago = pago;
        this.codCliente = codCliente;
        this.preco = preco;
        this.observacao = observacao;
        this.dataInicial = dataInicial;
        this.dataLimite = dataLimite;
        this.dataFinal = dataFinal;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public int isPago() {
        return pago;
    }

    public void setPago(int pago)
    {
        this.pago = pago;
    }

    public int getCodCliente()
    {
        return codCliente;
    }

    public void setCodCliente(int codCliente)
    {
        this.codCliente = codCliente;
    }

    public String getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(String dataLimite) {
        this.dataLimite = dataLimite;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", pago=" + pago +
                ", codCliente=" + codCliente +
                ", preco=" + preco +
                ", observacao='" + observacao + '\'' +
                ", dataInicial='" + dataInicial + '\'' +
                ", dataLimite='" + dataLimite + '\'' +
                ", dataFinal='" + dataFinal + '\'' +
                '}';
    }
}
