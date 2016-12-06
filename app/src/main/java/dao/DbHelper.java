package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import model.Cliente;
import model.Endereco;
import model.Pedido;
import model.Telefone;

/**
 * Created by vitalate on 14/10/16.
 */
public class DbHelper extends SQLiteOpenHelper
{
    /**
     * Variavel estatica que define o nome do banco de dados que sera armazenado no android e para onde
     * todas as informações serão salvas!
     */
    private static final String NOME_DB = "Clids";

    /**
     * Variavel estatica que indica a versão do banco de dados!
     * Alterar isto quando você inserir uma alteração no medo onCreate
     * quando esta variavel for alterada, o metodo onUpgrade sera chamado automaticamente.
     */
    private static final int VERSAO_DB = 15;


    public DbHelper(Context context)
    {
        super(context, NOME_DB, null, VERSAO_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        try {
            String sqlCreate;
            SQLiteDatabase db = getWritableDatabase();

            sqlCreate = "CREATE TABLE IF NOT EXISTS clientes (" +
                    "cliId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "cliNome TEXT NOT NULL, " +
                    "cliDataNasc TEXT, " +
                    "cliSexo TEXT NOT NULL )";

           db.execSQL(sqlCreate);

                    sqlCreate ="CREATE TABLE IF NOT EXISTS telefones (" +
                    " telId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " telNum TEXT NOT NULL," +
                    " telTipo TEXT," +
                    " telCliente INTEGER NOT NULL," +
                    " FOREIGN KEY(telCliente) REFERENCES clientes(cliId) )";

            db.execSQL(sqlCreate);

                    sqlCreate ="CREATE TABLE IF NOT EXISTS enderecos " +
                    "(endId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " endRua TEXT NOT NULL," +
                    " endNum INTEGER NOT NULL," +
                    " endComplemento TEXT," +
                    " endBairro TEXT," +
                    " endCliente INTEGER NOT NULL," +
                    " FOREIGN KEY(endCliente) REFERENCES clientes(cliId) )";

            db.execSQL(sqlCreate);

            sqlCreate = "CREATE TABLE IF NOT EXISTS pedidos " +
                    "(pedId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "pedPreco REAL NOT NULL," +
                    "pedObservacao TEXT," +
                    "pedDataInicial TEXT NOT NULL," +
                    "pedDataLimite TEXT," +
                    "pedDataFinal TEXT," +
                    "pedPago INTEGER NOT NULL," +
                    "pedCliente INTEGER NOT NULL," +
                    "FOREIGN KEY(pedCliente) REFERENCES clientes(cliId) )";

            db.execSQL(sqlCreate);

            sqlCreate = "CREATE TABLE IF NOT EXISTS blacklist (" +
                    "blcId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "blcCliente INTEGER NOT NULL, " +
                    "FOREIGN KEY (blcCliente) REFERENCES clientes(cliID))";

            db.execSQL(sqlCreate);

            sqlCreate = "CREATE TABLE IF NOT EXISTS atrasos (" +
                    "atrId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "atrPedido INTEGER NOT NULL, " +
                    "FOREIGN KEY (atrPedido) REFERENCES pedidos(pedId))";

            db.execSQL(sqlCreate);

            db.close();
        }
        catch (Exception ex)
        {
            Log.i("EngenhariaSoftware", ex.getMessage() +"\n" + "erro fatal!");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    public void chamarCreate()
    {
        SQLiteDatabase db = getReadableDatabase();
        onCreate(db);
    }

    public int lastCliId()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Max(cliId) from clientes",null);
        if(c.moveToFirst())
            return c.getInt(0);
        return 0;
    }

    public String cadastrarTabelas()
    {
        try
        {
            String sqlCreate;
            SQLiteDatabase db = getWritableDatabase();

            sqlCreate = "CREATE TABLE clientes (" +
                    "cliId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "cliNome TEXT NOT NULL, " +
                    "cliDataNasc TEXT, " +
                    "cliSexo TEXT NOT NULL )";

            db.execSQL(sqlCreate);

            sqlCreate ="CREATE TABLE telefones (" +
                    " telId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " telNum TEXT NOT NULL," +
                    " telTipo TEXT," +
                    " telCliente INTEGER NOT NULL," +
                    " FOREIGN KEY(telCliente) REFERENCES clientes(cliId) )";

            db.execSQL(sqlCreate);

            sqlCreate ="CREATE TABLE enderecos " +
                    "(endId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " endRua TEXT NOT NULL," +
                    " endNum INTEGER NOT NULL," +
                    " endComplemento TEXT," +
                    " endBairro TEXT," +
                    " endCliente INTEGER NOT NULL," +
                    " FOREIGN KEY(endCliente) REFERENCES clientes(cliId) )";

            db.execSQL(sqlCreate);

            sqlCreate = "CREATE TABLE pedidos " +
                    "(pedId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "pedPreco REAL NOT NULL," +
                    "pedObservacao TEXT," +
                    "pedDataInicial TEXT NOT NULL," +
                    "pedDataLimite TEXT," +
                    "pedDataFinal TEXT," +
                    "pedPago INTEGER NOT NULL," +
                    "pedCliente INTEGER NOT NULL," +
                    "FOREIGN KEY(pedCliente) REFERENCES clientes(cliId) )";

            db.execSQL(sqlCreate);

            sqlCreate = "CREATE TABLE blacklist (" +
                    "blcId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "blcCliente INTEGER NOT NULL, " +
                    "FOREIGN KEY (blcCliente) REFERENCES clientes(cliID))";

            sqlCreate = "CREATE TABLE atrasos (" +
                    "atrId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "atrPedido INTEGER NOT NULL, " +
                    "FOREIGN KEY (atrPedido) REFERENCES pedidos(pedId))";

            db.execSQL(sqlCreate);

            db.execSQL(sqlCreate);

            return "tabelas criadas com sucesso!";
        }
        catch(Exception e)
        {
            return e.getMessage();
        }
    }

    /**
     * Faz o insert do cliente, de seu endereço, e de seu telefone!
     * @param cliente
     * Objeto cliente a ser inserido!
     * @param enderecos
     * Arraylist de enderecos
     * endereços do cliente que esta sendo inserido. Estes endereços podem vir com o valor Null,
     * caso isso ocorra, não sera inserido nenhum endereço a este cliente, pelo fato do endereço ser opcional!
     * @param telefones
     * Mesmo ocorre com telefones. Opcional!
     * @return
     */
    public boolean insertCliente(Cliente cliente, ArrayList<Endereco> enderecos, ArrayList<Telefone> telefones)
    {
        try
        {
            /*Instancia um objeto SqliteDatabase do tipo Writable para interação com banco de dados*/
            SQLiteDatabase db = getWritableDatabase();

            /*ContentValues é uma classe usada para fazer um "insert seguro" no banco de dados, pode
            ser feito assim, ou até mesmo usando uma simples query */
            ContentValues cv = new ContentValues();

            /*Coloca as informações no ContentValues seguindo os nomes das tabelas do banco de dados*/
            cv.put("cliNome", cliente.getNome());
            cv.put("cliDataNasc", cliente.getData());
            cv.put("cliSexo", cliente.getSexo());

            /*Insere na tabela clientes "Primeiro parametro, o segundo parametro é null e é só usando
            * para casos mais especificos, e o terceiro parametro é onde inserimos o contentValues onde
            * esta toda informação a ser inserida e assim o insert é concretizado no banco de dados*/
            db.insert("clientes", null, cv);

            /*Inicialização da variavel idCLiente para ser usada posteriormente na função*/
            int idCliente = 0;

            /*Instancia agora um objeto de Sqlitedatabase, porem no modo Readable, para buscar o ultimo
            * id de cliente foi acabado de ser inserido */
            SQLiteDatabase rdb = getReadableDatabase();

            /*Seleciona o max, ou seja, o ultimo id que foi inserido Utilizando a classe cursos, que é
            * usada para fazer consultas no banco de dados e percorrer os resultados, onde no primeiro parametro
            * colocamos a query de consulta e no segundo podemos passar os parametros de forma mais dinamica,
            * porem como tudo esta esclarecido na propria query, o parametro é passado como null*/
            Cursor c = rdb.rawQuery("SELECT MAX(cliId) FROM Clientes",null);

            /*Função booleana do sqlite que move para a primeira posição dos registros encontrados
            * "que no caso é só um" e retorna true caso ele conseguiu fazer esta operação, se for falso
            * é porque provavelmente a consulta não encontrou  nenhum resultado, porem como neste caso
            * esta tratado para não acontecer, não colocamos um else*/
            if(c.moveToFirst())
                idCliente = c.getInt(0); //Pega o idCLiente na Coluna atual na linha 0

            /*Verifica se o parametro de telefones desta função não foi passado como null
            * caso não, ele percorre todas os telefones do arrayList e usa o db Writable instanciado
            * anteriormente para adicionar cada telefone do loop no banco de dados */
            if(telefones!=null)
            for(Telefone t : telefones)//For avançado que percorre o arraylist de forma ascendente e cada posição vira o objeto 't'
            {
                ContentValues infoTelefone = new ContentValues();
                infoTelefone.put("telNum",t.getNumero());
                infoTelefone.put("telTipo",t.getTipo());
                infoTelefone.put("telCliente", idCliente);
                db.insert("telefones",null,infoTelefone);
            }

            /*Verifica se o parametro de enderecos desta função não foi passado como null
            * caso não, ele percorre todos os endereços do arraylist e usa o db Writable instanciado
            * anteriormente para adicionar cada endereço do loop no banco de dados*/
            if(enderecos != null)
            for(Endereco e : enderecos)//For avançado que percorre o arraylist de forma ascendente e cada posição vira o objeto 'e'
            {
                ContentValues infoEndereco = new ContentValues();
                infoEndereco.put("endRua", e.getRua());
                infoEndereco.put("endNum", e.getNumero());
                infoEndereco.put("endBairro", e.getBairro());
                infoEndereco.put("endComplemento",e.getComplemento());
                infoEndereco.put("endCliente",idCliente);
                db.insert("enderecos",null,infoEndereco);
            }
            /*Fecha os objetos de banco de dados antes abertos para evitar erro de conflito do bd*/
            db.close();
            rdb.close();

            /*retorna true se chegou até aqui, significa que tudo ocorreu conforme o esperado!*/
            return true;
        }
        catch (Exception e)
        {
            /*Caso ele entre nesta exceção é porque algo errado aconteceu, portanto sera impresso
            * uma mensagem no LOG onde o usuário não pode ver, e retorna falso indicando que a aplicação
            * não conseguiu cadastrar*/
            System.err.println("Erro ao cadastrar cliente : " + e.getMessage());
            return false;
        }
    }

	/**
	 * Faz a consulta dos clientes atraves do nome
     * @param nome
     * nome a ser consultado
     * @return
     * retorna uma lista de clientes que contem o nome
     */

    public List<Cliente> consultaClientes(String nome)
    {
        try
        {
            /*Cria uma arraylist de clientes onde sera inserido os objetos que forem encontrados
            * na consulta*/
            List<Cliente> listClientes = new ArrayList<>();

            /*Instancia um objeto SQliteDatabase no modo readable para executar uma consulta*/
            SQLiteDatabase db = getReadableDatabase();

            /*Query para enviar ao banco de dados contendo uma consulta sqlite, com a função LIKE
            * para trazer qualquer nome que contenha uma incidencia do parametro em seu registro
            * COLLATE NOCASE serve para ele ordenar independente se maiuscula e minuscula */
            String sqlSelect = "SELECT cliId, cliNome FROM CLIENTES WHERE cliNome LIKE '%"+nome+"%' ORDER BY cliNome COLLATE NOCASE";

            /*Instancia um objeto Cursor para fazer a consulta e trazer os resultados
            * o primeiro parametro é a query criada acima, e o segundo é null, pois ja estamos definindo
            * o select completo na propria query*/
            Cursor c = db.rawQuery(sqlSelect, null);

            /*Função booleana do sqlite que move para a primeira posição dos registros encontrados
            * "que no caso é só um" e retorna true caso ele conseguiu fazer esta operação, se for falso
            * é porque provavelmente a consulta não encontrou  nenhum resultado*/
            if (c.moveToFirst())
                /*Utilizado o metodo do while para percorrer cada registro encontrado pela consulta
                * caso seja somente utilizado o WHILE, ele ira descartar o primeiro registro, por isso
                * é importante utilizar o Do!*/
                do
                {
                    /*Instancia um objeto cliente que recebera as informações do respectivo registro
                    * encontrado no banco de dados*/
                    Cliente cliente = new Cliente();

                    /*estas funções "c.getInt(n)" pega o valor na linha n do registro que vem ordenado
                     * de acordo com a consulta, nesse caso, id e nome */
                    cliente.setId(c.getInt(0));
                    cliente.setNome(c.getString(1));
                    listClientes.add(cliente);
                } while (c.moveToNext());//função que tenta mover para o proximo registro e retorna se conseguiu ou não


            /*Fecha o objeto db para evitar conflitos do bd*/
            db.close();

            /*Retorna uma lista com todos os clientes encontrados na consulta com estes parametros*/
            return listClientes;
        }
        catch(Exception ex)
        {
            System.err.println("Erro ao consultar " + ex.getMessage() );
        }

        /*Caso algo de errado é devolvido um objeto NULL, que deve ser tratado na aplicação para
        * evitar erros de nullPointerException*/
        return null;
    }

    /**
     * Faz o select de todos os clientes
     * @return
     * Retorna um conjunto do tipo List de clientes
     */

    public List<Cliente> selectClientes()
    {
        try
        {
            List<Cliente> listClientes = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            String sqlSelect = "SELECT cliId, cliNome FROM CLIENTES ORDER BY cliNome COLLATE NOCASE";
            Cursor c = db.rawQuery(sqlSelect, null);
            if (c.moveToFirst())
                do
                {
                    Cliente cliente = new Cliente();
                    cliente.setId(c.getInt(0));
                    cliente.setNome(c.getString(1));
                    listClientes.add(cliente);
                } while (c.moveToNext());

            db.close();

            return listClientes;
        }
        catch(Exception ex)
        {
            Log.i("Erro Banco", "Erro: "+ex.getMessage());
        }
        return null;
    }

    /**
     * Faz o select de um cliente em especifico
     * @param id
     * id do cliente que se deseja obter as informações
     * @return
     * Retorna um objeto Cliente contendo as informações obtidas no select
     */

    public Cliente detalhesCliente(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cliente cliente = new Cliente();
        String sqlSelect = "SELECT * FROM CLIENTES WHERE cliId = " + id;
        Cursor c = db.rawQuery(sqlSelect,null);
        if (c.moveToFirst())
        {
            cliente.setId(c.getInt(0));
            cliente.setNome(c.getString(1));
            cliente.setData(c.getString(2));
            cliente.setSexo(c.getInt(3));
        }
        db.close();
        return cliente;
    }

    /**
     * Faz o select de todos os endereços de um cliente especifico
     * @param codCliente
     * codigo do cliente que deseja se obter os endereços
     * @return
     * retorna um conjunto do tipo List de objetos da classe Endereco
     */

    public List<Endereco> selectEnderecoCliente(int codCliente)
    {
        List<Endereco> listClientes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelect = "SELECT e.* FROM Enderecos e, Clientes c WHERE e.endCliente = c.cliId AND c.cliId = " + codCliente;
        Cursor c = db.rawQuery(sqlSelect,null);
        if (c.moveToFirst())
            do
            {
                Endereco e = new Endereco();
                e.setId(c.getInt(0));
                e.setRua(c.getString(1));
                e.setNumero(c.getInt(2));;
                e.setBairro(c.getString(3));
                e.setComplemento(c.getString(4));
                listClientes.add(e);
            }while(c.moveToNext());

        db.close();
        return listClientes;
    }

    /**
     * Faz o select de todos os telefones de um cliente especifico
     * @param codCliente
     * O codigo do cliente que deseja se obter os telefones
     * @return
     * retorna um conjunto do tipo List de objetos da classe telefone
     */

    public List<Telefone> selectTelefoneCliente(int codCliente)
    {
        List<Telefone> listClientes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelect = "SELECT t.* FROM Telefones t, Clientes c where TelCliente = c.cliId AND c.cliId = "+codCliente;
        Cursor c = db.rawQuery(sqlSelect,null);
        if (c.moveToFirst())
            do
            {
                Telefone t = new Telefone();
                t.setId(c.getInt(0));
                t.setNumero(c.getString(1));
                t.setTipo(c.getString(2));
                listClientes.add(t);
            }while(c.moveToNext());

        db.close();
        return listClientes;
    }


	/**
	 * Faz o update de algum cliente na tabela clientes
     * @param cliente
     * Parametro do objeto cliente que sera salvo
     * @return
     * retorna true se o update foi bem sucedido
     * ou false caso um erro tenha acontecido
     */
    public boolean editarCliente(Cliente cliente)
    {
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            String query = "UPDATE clientes SET cliNome = '"+cliente.getNome()
                         + "', cliDataNasc = '" + cliente.getData()
                         + "', cliSexo = " + cliente.getSexo()
                         + " WHERE cliId = " + cliente.getId();
            db.execSQL(query);
            db.close();
            return true;
        }
        catch(Exception e)
        {
            System.err.println("Erro ao editar cliente :" + e.getMessage());
        }
        return false;
    }

	/**
     * Ao invés de dar update em um endereco, ele deleta todos e re-cadastra os endereços
     * @param enderecos
     * Arraylist contendo todos os endereços do cliente seja eles alterados ou não
     * @param idCliente
     * Id do cliente a quem pertence os endereços que estão sendo re-cadastrados
     * @return
     * true caso tudo ocorra certo
     * false caso aconteça algum erro
     */
    public boolean editarEndereco(ArrayList<Endereco> enderecos, int idCliente)
    {
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            String query = "Delete from enderecos where endCliente = " + idCliente;
            db.execSQL(query);

            for(Endereco e : enderecos)
            {
                ContentValues cv = new ContentValues();
                cv.put("endRua", e.getRua());
                cv.put("endNum", e.getNumero());
                cv.put("endBairro", e.getBairro());
                cv.put("endComplemento", e.getComplemento());
                cv.put("endCliente" , idCliente);
                db.insert("enderecos",null,cv);
            }
            db.close();
            return true;
        }
        catch(Exception e)
        {
            Log.i("erro", e.getMessage());
        }
        return false;
    }

    /**
     * Supostamente edita o telefone, mas na verdade ele apaga todos e reecadastra novamente, xddddd
     * @param telefones
     * ArrayList de telefones que serao "editados"
     * @param idCliente
     * id do cliente a qual pertence a edição
     * @return
     */

    public boolean editarTelefone(ArrayList<Telefone> telefones, int idCliente)
    {
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            String query = "Delete from telefones where telCliente = " + idCliente;
            db.execSQL(query);

            for(Telefone t : telefones)
            {
                ContentValues cv = new ContentValues();
                cv.put("telNum", t.getNumero());
                cv.put("telTipo", t.getTipo());
                cv.put("telCliente", idCliente);
                db.insert("telefones",null,cv);
            }
            db.close();
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void insertPedido(Pedido pedido)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("pedPreco", pedido.getPreco());
        cv.put("pedObservacao", pedido.getObservacao());
        cv.put("pedDataInicial", pedido.getDataInicial());
        cv.put("pedDataLimite", pedido.getDataLimite());
        cv.put("pedDataFinal", pedido.getDataFinal());
        cv.put("pedPago", pedido.isPago());
        cv.put("pedCliente", pedido.getCodCliente());

        db.insert("pedidos", null, cv);

        db.close();
    }

    /**
     * Faz um select de todos os pedidos para listagem
     * @return
     * retorna um List contendo todos os pedidos
     */
    public List<Pedido> selectPedidos()
    {
        List<Pedido> listPedidos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectPedidos = "SELECT * FROM pedidos";
        Cursor c = db.rawQuery(selectPedidos, null);
        if (c.moveToFirst())
        {
            do
            {
                Pedido pedido = new Pedido();
                pedido.setId(c.getInt(0));
                pedido.setPreco(c.getDouble(1));
                pedido.setObservacao(c.getString(2));
                pedido.setDataInicial(c.getString(3));
                pedido.setDataLimite(c.getString(4));
                pedido.setDataFinal(c.getString(5));
                pedido.setPago(c.getInt(6));
                pedido.setCodCliente(c.getInt(7));

                listPedidos.add(pedido);
            }while(c.moveToNext());
        }

        db.close();
        return listPedidos;
    }

    /**
     * Seleciona um pedido especifico
     * @param pedId
     * Id do pedido a ser procurado
     * @return
     * Retorna um Objeto pedido contendo o pedido especificado pelo id
     */
    public Pedido selectPedidosId(int pedId)
    {
        Pedido pedido = new Pedido();
        SQLiteDatabase db = getReadableDatabase();
        String selectPedidos = "SELECT * FROM pedidos where pedId = " + pedId;
        Cursor c = db.rawQuery(selectPedidos, null);
        if (c.moveToFirst())
        {
            pedido.setId(c.getInt(0));
            pedido.setPreco(c.getDouble(1));
            pedido.setObservacao(c.getString(2));
            pedido.setDataInicial(c.getString(3));
            pedido.setDataLimite(c.getString(4));
            pedido.setDataFinal(c.getString(5));
            pedido.setPago(c.getInt(6));
            pedido.setCodCliente(c.getInt(7));
        }

        db.close();
        return pedido;
    }

    public List<Pedido> selectPedidosCodCliente(int pedCliente)
    {
        List<Pedido> listPedidos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectPedidos = "SELECT * FROM pedidos where pedCliente = " + pedCliente;
        Cursor c = db.rawQuery(selectPedidos, null);
        if (c.moveToFirst())
        {
            do
            {
                Pedido pedido = new Pedido();
                pedido.setId(c.getInt(0));
                pedido.setPreco(c.getDouble(1));
                pedido.setObservacao(c.getString(2));
                pedido.setDataInicial(c.getString(3));
                pedido.setDataLimite(c.getString(4));
                pedido.setDataFinal(c.getString(5));
                pedido.setPago(c.getInt(6));
                pedido.setCodCliente(c.getInt(7));
                listPedidos.add(pedido);
            }while(c.moveToNext());
        }

        db.close();
        return listPedidos;
    }

    public boolean updateFinalizaPedido(Pedido pedido, String data)
    {
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("pedPreco", pedido.getPreco());
            cv.put("pedObservacao", pedido.getObservacao());
            cv.put("pedDataInicial", pedido.getDataInicial());
            cv.put("pedDataLimite", pedido.getDataLimite());
            cv.put("pedDataFinal", data);
            cv.put("pedPago", 1);
            cv.put("pedCliente", pedido.getCodCliente());

            db.update("pedidos", cv, "pedId = ?", new String[] {String.valueOf(pedido.getId())});
            db.close();

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public boolean updateEditaPedido(Pedido pedido)
    {
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("pedPreco", pedido.getPreco());
            cv.put("pedObservacao", pedido.getObservacao());
            cv.put("pedDataInicial", pedido.getDataInicial());
            cv.put("pedDataLimite", pedido.getDataLimite());
            cv.put("pedDataFinal", pedido.getDataFinal());
            cv.put("pedPago", pedido.isPago());
            cv.put("pedCliente", pedido.getCodCliente());

            db.update("pedidos", cv, "pedId = ?", new String[]{String.valueOf(pedido.getId())});
            db.close();

            return true;
        }
        catch (Exception e)
        {
            return false;

        }
    }

    public void insertAtrasos(int pedId)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("atrPedido", pedId);

        db.insert("atrasos", null, cv);

        db.close();
    }

    public boolean selectExisteAtraso(int pedId)
    {
        try
        {
            String select = "SELECT * from atrasos where atrPedido = " + pedId;
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery(select, null);

            if(c.moveToFirst())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean deleteAtraso(int pedId)
    {
        try
        {
            String delete = "DELETE FROM atrasos WHERE atrPedido = " + pedId;
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(delete);

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean adicionarBlacklist(int idCliente){

        try{

            SQLiteDatabase db = getWritableDatabase();
            /*String query = "CREATE TABLE blacklist (" +
                    "blcId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "blcCliente INTEGER NOT NULL, " +
                    "FOREIGN KEY (blcCliente) REFERENCES clientes(cliID))";
            db.execSQL(query);*/

            ContentValues cv = new ContentValues();
            cv.put("blcCliente", idCliente);

            db.insert("blacklist", null, cv);
            return true;

        } catch (Exception e){
            return false;
        }

    }

    public boolean removerBlacklist (int idCliente){

        try {

            String query = "DELETE FROM blacklist WHERE blcCliente = " + idCliente;
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(query);
            return true;

        } catch (Exception e){
            return false;
        }
    }

    public boolean isOnBlackList(int idCliente) {
        try {

            String select = "SELECT * from blacklist where blcCliente = " + idCliente;
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery(select, null);

            if(c.moveToFirst())
                return true;
            else
                return false;

        } catch (Exception e){
            return false;
        }
    }

    public List<Cliente> consultaBlacklist(String nome)
    {
        try
        {
            List<Cliente> listClientes = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            String sqlSelect = "SELECT B.blcCliente, C.cliNome FROM CLIENTES C, BLACKLIST B WHERE C.cliNome LIKE '%"+nome+"%' AND B.blcCliente = C.cliId ORDER BY cliNome COLLATE NOCASE";
            Cursor c = db.rawQuery(sqlSelect, null);
            if (c.moveToFirst())
                do
                {
                    Cliente cliente = new Cliente();
                    cliente.setId(c.getInt(0));
                    cliente.setNome(c.getString(1));
                    listClientes.add(cliente);
                } while (c.moveToNext());

            db.close();

            return listClientes;
        }
        catch(Exception ex)
        {
            Log.i("Erro Banco", "Erro: "+ex.getMessage());
        }
        return null;
    }


    public List<Cliente> selectBlacklist()
    {
        try
        {
            List<Cliente> listClientes = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            String sqlSelect = "SELECT B.blcCliente, C.cliNome FROM CLIENTES C, BLACKLIST B WHERE C.cliId = B.blcCliente ORDER BY cliNome COLLATE NOCASE";
            Cursor c = db.rawQuery(sqlSelect, null);
            if (c.moveToFirst())
                do
                {
                    Cliente cliente = new Cliente();
                    cliente.setId(c.getInt(0));
                    cliente.setNome(c.getString(1));
                    listClientes.add(cliente);
                } while (c.moveToNext());

            db.close();

            return listClientes;
        }
        catch(Exception ex)
        {
            Log.i("Erro Banco", "Erro: "+ex.getMessage());
        }
        return null;
    }


    /**
     * Função que obtem as datas uteis ao usuario em ordem cronologica ascendente
     * @return
     * retorna uma arraylist com objetos do tipo Cliente e Pedido em ordem cronologica ascendente
     */
    public ArrayList getTimeLine(int meses) throws ParseException {
        int dataLimite = 30; // limite em dias para pesquisar apartir da data atual
        ArrayList zezeiaqui = new ArrayList();
        SQLiteDatabase database = this.getReadableDatabase();
        String zezando = "SELECT * FROM Clientes";
        Cursor c = database.rawQuery(zezando, null);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        long date = System.currentTimeMillis();


        String dataAtual = dateFormat.format(date);
        String auxXd[] = dataAtual.split("-");

        int diaFinal = Integer.parseInt(dataAtual.substring(0, 2));
        int mesInicial = Integer.parseInt(dataAtual.substring(3, 5));
        int mesFinal = Integer.parseInt(dataAtual.substring(3, 5));
        int anoInicial = Integer.parseInt(dataAtual.substring(6, 10));
        int anoFinal = Integer.parseInt(dataAtual.substring(6, 10));

        int mesMeta = mesInicial + meses;
        int anoMeta = anoInicial;
        if(mesMeta > 12) {
            mesMeta -= 12;
            anoMeta++;
        }



        for(int i = mesFinal ; mesFinal != mesMeta; i++)
        {
            if(i == 13) {
                i = 1;
                anoFinal ++;
            }
            mesFinal = i;
        }

        String mesAux;
        if(mesFinal < 10)
            mesAux = "0" + String.valueOf(mesFinal);
        else
            mesAux = String.valueOf(mesFinal);

        if(diaFinal >= 29)
            diaFinal = 28;

        String datacomMeses = diaFinal + "-" + mesFinal + "-" + anoFinal;



        //String datacomMeses = dateFormat.format(date + ((8640000 * 30) * meses));//8640000 é um dia em milisegundos
        Date dataConsulta = (Date) dateFormat.parse(datacomMeses);
        Date dataHoje = (Date) dateFormat.parse(dataAtual);


        String helper[] = dataAtual.split("-");
        int diaAtual = Integer.parseInt(helper[0]);
        int mesAtual = Integer.parseInt(helper[1]);
        int anoAtual = Integer.parseInt(helper[2]);


        if (c.moveToFirst())
        {
            do {

                Cliente ze = new Cliente();

                ze.setId(c.getInt(0));
                ze.setNome(c.getString(1));
                ze.setData(c.getString(2));
                ze.setSexo(c.getInt(3));

                zezeiaqui.add(ze);

            }while (c.moveToNext());
        }

        zezando = "SELECT * FROM Pedidos";
        c = database.rawQuery(zezando, null);

        if (c.moveToFirst())
        {
            do {

                Pedido ze = new Pedido();

                ze.setId(c.getInt(0));
                ze.setPreco(c.getDouble(1));
                ze.setObservacao(c.getString(2));
                ze.setDataInicial(c.getString(3));
                ze.setDataLimite(c.getString(4));
                ze.setDataFinal(c.getString(5));
                ze.setPago(c.getInt(6));
                ze.setCodCliente(c.getInt(7));

                zezeiaqui.add(ze);

            }while (c.moveToNext());
        }

        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o, Object t1) {

                String data1, data2;
                if(o instanceof Cliente)
                    data1 = ((Cliente) o).getData();
                else if(((Pedido) o).isPago() == 0 )
                    data1 = ((Pedido)o).getDataLimite();
                else
                    data1 = ((Pedido)o).getDataInicial();

                if(t1 instanceof Cliente)
                    data2 = ((Cliente) t1).getData();
                else if(((Pedido)t1).isPago() == 0)
                    data2 = ((Pedido)t1).getDataLimite();
                else
                    data2 = ((Pedido)t1).getDataInicial();

                String auxd1[];
                    auxd1 = data1.split("-");


                String auxd2[];
                    auxd2 = data1.split("-");


                int ano1 = Integer.parseInt(auxd1[2]);
                int mes1 = Integer.parseInt(auxd1[1]);
                int dia1 = Integer.parseInt(auxd1[0]);

                int ano2 = Integer.parseInt(auxd2[2]);
                int mes2 = Integer.parseInt(auxd2[1]);
                int dia2 = Integer.parseInt(auxd2[0]);

                if (ano1 > ano2)
                    return 1;
                else if (ano1 < ano2)
                    return -1;
                else if (mes1 > mes2)
                    return 1;
                else if (mes1 < mes2)
                    return -1;
                else if (dia1 > dia2)
                    return 1;
                else if (dia1 < dia2)
                    return -1;
                else
                    return 0;
            }
        };

        Collections.sort(zezeiaqui, comparator);

        ArrayList ordenado = new ArrayList();

        // SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        //long date = System.currentTimeMillis();
        //String dataAtual = dateFormat.format(date);

        for (Object obj : zezeiaqui)
        {
            if(obj instanceof  Cliente)
            {
                String ax[] = ((Cliente) obj).getData().split("-");
                String dt1 = ax[0] + "-" + ax[1] + "-" + Integer.parseInt(String.valueOf(dataHoje.getYear() + 1900));
                String dt2 = ax[0] + "-" + ax[1] + "-" + Integer.parseInt(String.valueOf(dataConsulta.getYear() + 1900));

                Date dateCliente;

                Date auxData1 = (Date) dateFormat.parse(dt1);
                Date auxData2 = (Date) dateFormat.parse(dt2);

                if(auxData1.before(dataHoje)) {
                    ((Cliente) obj).setData(dt2);
                    dateCliente = auxData2;
                }
                else {
                    ((Cliente) obj).setData(dt1);
                    dateCliente = auxData1;
                }



//                String dataCliente = dateFormat.format(((Cliente) obj).getData());
               // Date dateCliente = (Date) dateFormat.parse(dataCliente);
                if(dateCliente.before(dataConsulta) && dateCliente.after(dataHoje))
                    ordenado.add(obj);

            }
            else if(((Pedido) obj).isPago() == 0)
            {
                Date dataPedido = (Date) dateFormat.parse(((Pedido) obj).getDataLimite());
                if(dataPedido.before(dataConsulta) && dataPedido.after(dataHoje))
                    ordenado.add(obj);
            }
        }

        Comparator sortAgain =  new Comparator() {
            @Override
            public int compare(Object o, Object t1) {
                Date d1 = null,d2 = null;

                if(o instanceof Cliente)
                    try {
                        d1 = dateFormat.parse(((Cliente) o).getData());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                else
                    try {
                        d1 = dateFormat.parse(((Pedido) o).getDataLimite());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                if(t1 instanceof Cliente)
                    try {
                        d2 = dateFormat.parse(((Cliente) t1).getData());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                else
                    try {
                        d2 = dateFormat.parse(((Pedido) t1).getDataLimite());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                if(d1.after(d2))
                    return 1;
                else return -1;
            }
        };

        Collections.sort(ordenado,sortAgain);

            System.out.println("devolvendo " + ordenado.size() + " valores");
                return ordenado;

    }


	/**
	 * Estatisticas dos clientes
     * @param id
     * id do cliente que é desejado se obter as estatisticas
     * @return
     * retorna uma arraylist de String contendo em cada posição do array, uma estatistica sobre o cliente selecionado
     */
    public ArrayList<String> estatisticasCliente(int id)
    {
        ArrayList<String> estatisticas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        boolean contemDados = false;
        String queryPedido = "SELECT Count(p.pedId) FROM Clientes c, Pedidos p where p.pedCliente = c.cliId AND c.cliId = " + id;
        Cursor c = db.rawQuery(queryPedido,null);
        if(c.moveToFirst()) {
            if(c.getInt(0) > 0)
            contemDados = true;
        }

        if(contemDados)
        {
            c = db.rawQuery("select blcCliente from blacklist where blcCliente = " + id,null);
            if(c.moveToFirst())
                estatisticas.add("Este cliente atualmente esta na lista negra!");

            String querytotalComprado =  "SELECT SUM(p.pedPreco) FROM Clientes c, Pedidos p where p.pedCliente = c.CliId AND c.CliId = " + id;
            c = db.rawQuery(querytotalComprado, null);
            if(c.moveToFirst())
                estatisticas.add("Este cliente comprou no total :" +String.format("%.2f", c.getDouble(0)));

            c = db.rawQuery("Select pedId, pedPreco, pedDataInicial from pedidos where pedCliente = " + id + " ORDER BY pedId",null);
            ArrayList<Integer> meses = new ArrayList<>();
            double somaPreco = 0;
            while(c.moveToNext())
            {
                somaPreco += c.getDouble(1);
                String aux[] = c.getString(2).split("-");
                int mes = Integer.parseInt(aux[1]);
                boolean achou = false;
                for(int i : meses)
                {
                    if(i == mes)
                        achou = true;
                }
                if(!achou)
                    meses.add(mes);
            }
            estatisticas.add("Media mensal de compra é de R$ " + String.format("%.2f", Double.valueOf(somaPreco / meses.size())));

            c = db.rawQuery("SELECT Count(pedID) from pedidos where pedCliente = " + id,null);
            while (c.moveToNext())
                estatisticas.add("Este cliente realizou " +c.getInt(0) + " pedido(s)");

            c = db.rawQuery("SELECT Count(pedID) from pedidos where pedPago = 0 AND pedCliente = " + id,null);
            while (c.moveToNext())
            {
                int contagem = c.getInt(0);
                if(contagem > 0)
                    estatisticas.add("Este cliente possui " + contagem + " pedido(s) em aberto");
            }

            c = db.rawQuery("SELECT p.pedDataInicial from pedidos p, (Select max(PedId)maior from pedidos) sc where pedId = sc.maior AND pedCliente = " + id,null);
            while (c.moveToNext()) {
                String data = c.getString(0);
                Date dataUltimoPedido;
                try {
                    dataUltimoPedido = new SimpleDateFormat("dd-MM-yyyy").parse(data);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                estatisticas.add("A ultima compra deste cliente foi em " + data.replace("-", "/"));
            }


        }
        else
            estatisticas.add("Este Cliente ainda não tem nenhum pedido, que tal ligar para ele(a)?");
        return estatisticas;
    }

    public void executaQuery(String query)
    {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(query);
        }
        catch(Exception e)
        {
            System.err.println("Erro " + e.getMessage());
        }
    }

    public ArrayList<String> estatisticasUsuario()
    {
        ArrayList<String> est = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        c = db.rawQuery("SELECT count(cliId) from clientes",null);
        if(c.moveToFirst())
            est.add("\n\nVocê possui " + c.getInt(0) + " clientes");

        c = db.rawQuery("SELECT sum(pedPreco) from pedidos where pedPago = 1",null);
        if(c.moveToFirst())
            est.add("Você ja recebeu R$" + String.format("%.2f", c.getDouble(0)) + " no total");

        c = db.rawQuery("SELECT sum(pedPreco) from pedidos where pedPago = 0",null);
        if(c.moveToFirst())
            est.add("Você tem R$" + String.format("%.2f", c.getDouble(0)) + " em aberto");

        c = db.rawQuery("Select pedId, pedPreco, pedDataInicial from pedidos ORDER BY pedId",null);
        ArrayList<Integer> meses = new ArrayList<>();
        double somaPreco = 0;
        while(c.moveToNext())
        {
            somaPreco += c.getDouble(1);
            String aux[] = c.getString(2).split("-");
            int mes = Integer.parseInt(aux[1]);
            boolean achou = false;
            for(int i : meses)
            {
                if(i == mes)
                    achou = true;
            }
            if(!achou)
                meses.add(mes);
        }
        est.add("Média mensal de venda é de R$ " + String.format("%.2f",Double.valueOf(somaPreco / meses.size())));

        c = db.rawQuery("SELECT max(sc.soma), sc.pedCliente, c.cliNome from clientes c, (select sum(pedPreco) soma, pedCliente from pedidos group by pedCliente )sc  where c.cliId = sc.pedCliente",null);
        if(c.moveToFirst()) {
            est.add("O seu cliente que mais comprou é : " + c.getString(2) + ", que comprou no total: " + String.format("%.2f",c.getDouble(0)));
        }


        return est;
    }
}
