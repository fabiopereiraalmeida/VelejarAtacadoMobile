package br.com.grupocaravela.velejar.atacadomobile.bancoDados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	/********* CRIANDO TABELAS PARA O BANCO DE DADOS ***************/

	private static final String TABLE_CARGO = "CREATE TABLE IF NOT EXISTS [cargo](_id INTEGER PRIMARY KEY, acessoAndroid BOOLEAN, " +
			"acessoCategorias BOOLEAN, acessoCidades BOOLEAN, acessoClientes BOOLEAN, acessoCompras BOOLEAN, acessoConfiguracao BOOLEAN, " +
			"acessoContasPagar BOOLEAN, acessoContasReceber BOOLEAN, acessoFormaPagamento BOOLEAN, acessoFornecedores BOOLEAN, " +
			"acessoProdutos BOOLEAN, acessoRelatorios BOOLEAN, acessoRotas BOOLEAN, acessoUnidade BOOLEAN, acessoUsuarios BOOLEAN, acessoVendas BOOLEAN, " +
			"nome VARCHAR(20), observacao TEXT)";

	private static final String TABLE_CATEGORIA = "CREATE TABLE IF NOT EXISTS [categoria](_id INTEGER PRIMARY KEY, nome VARCHAR(50), empresa_id INTEGER)";

	private static final String TABLE_CATEGORIA_CLIENTE = "CREATE TABLE IF NOT EXISTS [categoria_cliente](_id INTEGER PRIMARY KEY, nome VARCHAR(50), empresa_id INTEGER)";

	private static final String TABLE_CIDADE = "CREATE TABLE IF NOT EXISTS [cidade](_id INTEGER PRIMARY KEY, estado_id INTEGER, nome VARCHAR(64), ibge VARCHAR(8))";

	private static final String TABLE_ESTADO = "CREATE TABLE IF NOT EXISTS [estado](_id INTEGER PRIMARY KEY, nome VARCHAR(64), uf VARCHAR(2), codigo VARCHAR(2))";

	private static final String TABLE_CLIENTE = "CREATE TABLE IF NOT EXISTS [cliente](_id INTEGER PRIMARY KEY, razaoSocial VARCHAR(80), fantasia VARCHAR(30), " +
			"inscricaoEstadual VARCHAR(18), cpf VARCHAR(20), cnpj VARCHAR(20), data_nascimento VARCHAR(20), data_cadastro VARCHAR(20), email VARCHAR(70), " +
			"limite_credito DOUBLE(11,2), ativo BOOLEAN, observacao TEXT, telefone1 VARCHAR(18), telefone2 VARCHAR(18), endereco VARCHAR(100), " +
			"endereco_numero VARCHAR(5), complemento VARCHAR(30), bairro VARCHAR(30), cidade_id INTEGER, estado_id INTEGER, cep VARCHAR(12), " +
			"rota_id INTEGER, empresa_id INTEGER, novo BOOLEAN, alterado BOOLEAN, forma_pagamento_id INTEGER, cidade VARCHAR(30), uf VARCHAR(2), categoria_cliente_id INTEGER," +
			"imagem BLOB, localizacao VARCHAR(20))";

	private static final String TABLE_CONTA_RECEBER = "CREATE TABLE IF NOT EXISTS [conta_receber](_id INTEGER PRIMARY KEY, cliente_id INTEGER, " +
			"venda_cabecalho_id INTEGER, valor_devido DOUBLE(11,2), vencimento VARCHAR(20), data_pagamento VARCHAR(20), quitada BOOLEAN, atrasada BOOLEAN, " +
			"empresa_id INTEGER, valor_desconto DOUBLE(11,2), observacao VARCHAR(200))";

	private static final String TABLE_CREDITO_USUARIO_DETALHES = "CREATE TABLE IF NOT EXISTS [credito_usuario_detalhes](_id INTEGER PRIMARY KEY, data VARCHAR(20), " +
			"empresa_id INTEGER, valor DOUBLE(11,2), usuario_id INTEGER, venda_detalhe_id INTEGER)";

	private static final String TABLE_EMPRESA = "CREATE TABLE IF NOT EXISTS [empresa](_id INTEGER PRIMARY KEY, razaoSocial VARCHAR(60), fantasia VARCHAR(30), " +
			"cnpj VARCHAR(20), inscricao_estadual VARCHAR(25), telefone_1 VARCHAR(15), telefone_2 VARCHAR(15), endereco VARCHAR(80), endereco_numero VARCHAR(6), " +
			"bairro VARCHAR(50), cidade_id INTEGER, estado_id INTEGER, email VARCHAR(150), complemento VARCHAR(50))";

	private static final String TABLE_FORMA_PAGAMENTO = "CREATE TABLE IF NOT EXISTS [forma_pagamento](_id INTEGER PRIMARY KEY, juros DOUBLE(11,3), " +
			"nome VARCHAR(45), numero_dias INTEGER, numero_parcelas INTEGER, observacao TEXT, valor_minimo DOUBLE(11,2), geral BOOLEAN, empresa_id INTEGER)";

	private static final String TABLE_PRODUTO = "CREATE TABLE IF NOT EXISTS [produto](_id INTEGER PRIMARY KEY, codigo VARCHAR(20)," +
			"nome VARCHAR(45), estoque DOUBLE(11,3), expositor DOUBLE(11,3),valor_desejavel_venda DOUBLE(11,2), " +
			"valor_minimo_venda DOUBLE(11,2), categoria_id INTEGER, unidade_id INTEGER, ativo BOOLEAN, peso DOUBLE(11,3), empresa_id INTEGER, " +
			"imagem BLOB, codigo_ref VARCHAR(14))";

	private static final String TABLE_ROTA = "CREATE TABLE IF NOT EXISTS [rota](_id INTEGER PRIMARY KEY, nome VARCHAR(60), observacao TEXT, empresa_id INTEGER)";

	private static final String TABLE_ROTA_VENDEDOR = "CREATE TABLE IF NOT EXISTS [rota_vendedor](_id INTEGER PRIMARY KEY, rota_id INTEGER, usuario_id INTEGER, empresa_id INTEGER)";

	private static final String TABLE_UNIDADE = "CREATE TABLE IF NOT EXISTS [unidade](_id INTEGER PRIMARY KEY, nome VARCHAR(20), empresa_id INTEGER)";

	private static final String TABLE_MARCA = "CREATE TABLE IF NOT EXISTS [marca](_id INTEGER PRIMARY KEY, nome VARCHAR(40), empresa_id INTEGER)";

	private static final String TABLE_USUARIO = "CREATE TABLE IF NOT EXISTS [usuario](_id INTEGER PRIMARY KEY, ativo BOOLEAN, " +
			"nome VARCHAR(45), senha VARCHAR(16), email VARCHAR(70), credito DOUBLE(11,2), telefone VARCHAR(16), rota_id INTEGER, empresa_id INTEGER)";

	private static final String TABLE_VENDA_CABECALHO = "CREATE TABLE IF NOT EXISTS [venda_cabecalho](_id INTEGER PRIMARY KEY, data_primeiro_vencimento VARCHAR(20), " +
			"data_venda VARCHAR(20), entrada DOUBLE(11,2), juros DOUBLE(11,2), observacao TEXT, valor_desconto DOUBLE(11,2), valor_parcial DOUBLE(11,2), " +
			"valor_total DOUBLE(11,2), cliente_id INTEGER, empresa_id INTEGER, forma_pagamento_id INTEGER, usuario_id INTEGER, status VARCHAR(20))";

	private static final String TABLE_VENDA_DETALHE = "CREATE TABLE IF NOT EXISTS [venda_detalhe](_id INTEGER PRIMARY KEY, quantidade DOUBLE(11,3), " +
			"valor_desconto DOUBLE(11,2), valor_unitario DOUBLE(11,2), valor_parcial DOUBLE(11,2), valor_total DOUBLE(11,2), produto_id INTEGER, venda_cabecalho_id INTEGER)";

	private static final String TABLE_ANDROID_CAIXA = "CREATE TABLE IF NOT EXISTS [android_caixa](_id INTEGER PRIMARY KEY AUTOINCREMENT, data_recebimento VARCHAR(20), " +
			"data_transmissao VARCHAR(20), valor DOUBLE(11,2), android_venda_cabecalho_id INTEGER, venda_cabecalho_id INTEGER, usuario_id INTEGER, cliente_id INTEGER, " +
			"conta_receber_id INTEGER, empresa_id INTEGER)";

	private static final String TABLE_ANDROID_CONTA_RECEBER = "CREATE TABLE IF NOT EXISTS [android_conta_receber](_id INTEGER PRIMARY KEY AUTOINCREMENT, dataTransmissao VARCHAR(20), " +
			"dataRecebimento VARCHAR(20), valor_devido DOUBLE(11,2), cliente_id INTEGER, usuario_id INTEGER, venda_cabecalho_id INTEGER)";

	private static final String TABLE_ANDROID_VENDA_CABECALHO = "CREATE TABLE IF NOT EXISTS [android_venda_cabecalho](_id INTEGER PRIMARY KEY AUTOINCREMENT, codVenda INTEGER, " +
			"observacao TEXT, cliente_id INTEGER, entrada DOUBLE(11,2), juros DOUBLE(11,2), valor_parcial DOUBLE(11,2), valor_desconto DOUBLE(11,2), valor_total DOUBLE(11,2), " +
			"dataVenda VARCHAR(20), dataPrimeiroVencimento VARCHAR(20), usuario_id INTEGER, forma_pagamento_id INTEGER, rota_id INTEGER, " +
			"empresa_id INTEGER, venda_aprovada BOOLEAN, enviado BOOLEAN, cliente_mobile_id INTEGER)";

	private static final String TABLE_ANDROID_VENDA_DETALHE = "CREATE TABLE IF NOT EXISTS [android_venda_detalhe](_id INTEGER PRIMARY KEY AUTOINCREMENT, android_venda_cabecalho_id INTEGER, " +
			"produto_id INTEGER, quantidade DOUBLE(11,3), valor_unitario DOUBLE(11,2), valor_parcial DOUBLE(11,2), valor_desconto DOUBLE(11,2), valor_total DOUBLE(11,2), empresa_id INTEGER)";
/*
	private static final String TABLE_USUARIO_LOGADO = "CREATE TABLE IF NOT EXISTS [usuario_logado](_id INTEGER PRIMARY KEY, id_usuario INTEGER, nome VARCHAR(45), email VARCHAR(70), " +
			"senha VARCHAR(16), credito DOUBLE(11,2), empresa_id INTEGER, rota_id INTEGER)";

	private static final String TABLE_CONFIGURACAO = "CREATE TABLE IF NOT EXISTS [configuracao](_id INTEGER PRIMARY KEY, email VARCHAR(70), senha VARCHAR(16))";

 */
	// *****************************************************************************************

	/*************** APAGAR O BANCO DE DADOS *********************************/

	private static final String DELETE_TABELA_CARGO = "DROP TABLE IF EXISTS cargo";
	private static final String DELETE_TABELA_CATEGORIA = "DROP TABLE IF EXISTS categoria";
	private static final String DELETE_TABELA_CIDADE = "DROP TABLE IF EXISTS cidade";
    private static final String DELETE_TABELA_ESTADO = "DROP TABLE IF EXISTS estado";
	private static final String DELETE_TABELA_CLIENTE = "DROP TABLE IF EXISTS cliente";
	//private static final String DELETE_TABELA_CLIENTE = "DELETE FROM cliente WHERE novo LIKE '0'";
	private static final String DELETE_TABELA_CONTA_RECEBER = "DROP TABLE IF EXISTS conta_receber";
	private static final String DELETE_TABELA_CREDITO_USUARIO = "DROP TABLE IF EXISTS credito_usuario";
	private static final String DELETE_TABELA_CREDITO_USUARIO_DETALHES = "DROP TABLE IF EXISTS credito_usuario_detalhes";
	private static final String DELETE_TABELA_EMPRESA = "DROP TABLE IF EXISTS empresa";
	private static final String DELETE_TABELA_ENDERECO_CLIENTE = "DROP TABLE IF EXISTS endereco_cliente";
	private static final String DELETE_TABELA_FORMA_PAGAMENTO = "DROP TABLE IF EXISTS forma_pagamento";
	private static final String DELETE_TABELA_PRODUTO = "DROP TABLE IF EXISTS produto";
	private static final String DELETE_TABELA_ROTA = "DROP TABLE IF EXISTS rota";
	private static final String DELETE_TABELA_ROTA_VENDEDOR = "DROP TABLE IF EXISTS rota_vendedor";
	private static final String DELETE_TABELA_TELEFONE_CLIENTE = "DROP TABLE IF EXISTS telefone_cliente";
	private static final String DELETE_TABELA_TELEFONE_USUARIO = "DROP TABLE IF EXISTS telefone_usuario";
	private static final String DELETE_TABELA_UNIDADE = "DROP TABLE IF EXISTS unidade";
	private static final String DELETE_TABELA_MARCA = "DROP TABLE IF EXISTS marca";
	private static final String DELETE_TABELA_USUARIO = "DROP TABLE IF EXISTS usuario";
	private static final String DELETE_TABELA_VENDA_CABECALHO = "DROP TABLE IF EXISTS venda_cabecalho";
	private static final String DELETE_TABELA_VENDA_DETALHE = "DROP TABLE IF EXISTS venda_detalhe";

	private static final String DELETE_TABELA_ANDROID_CAIXA = "DROP TABLE IF EXISTS android_caixa";
	private static final String DELETE_TABELA_ANDROID_CONTA_RECEBER = "DROP TABLE IF EXISTS android_conta_receber";
	private static final String DELETE_TABELA_ANDROID_VENDA_CABECALHO = "DROP TABLE IF EXISTS android_venda_cabecalho";
	private static final String DELETE_TABELA_ANDROID_VENDA_DETALHE = "DROP TABLE IF EXISTS android_venda_detalhe";
/*
	private static final String TRUNCATE_TABELA_ANDROID_CAIXA = "TRUNCATE TABLE IF EXISTS android_caixa";
	private static final String TRUNCATE_TABELA_ANDROID_CONTA_RECEBER = "TRUNCATE TABLE IF EXISTS android_conta_receber";
	private static final String TRUNCATE_TABELA_ANDROID_VENDA_CABECALHO = "TRUNCATE TABLE IF EXISTS android_venda_cabecalho";
	private static final String TRUNCATE_TABELA_ANDROID_VENDA_DETALHE = "TRUNCATE TABLE IF EXISTS android_venda_detalhe";

	private static final String DELETE_TABELA_USUARIO_LOGADO = "DROP TABLE IF EXISTS usuario_logado";
*/
	// private static final String DELETE_BANCO = "DROP TABLE IF EXISTS caixa";
	public DBHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CARGO);
		db.execSQL(TABLE_CATEGORIA);
		db.execSQL(TABLE_CATEGORIA_CLIENTE);
		db.execSQL(TABLE_CLIENTE);
		db.execSQL(TABLE_CIDADE);
        db.execSQL(TABLE_ESTADO);
		db.execSQL(TABLE_CONTA_RECEBER);
		db.execSQL(TABLE_CREDITO_USUARIO_DETALHES);
		db.execSQL(TABLE_EMPRESA);
		db.execSQL(TABLE_FORMA_PAGAMENTO);
		db.execSQL(TABLE_PRODUTO);
		db.execSQL(TABLE_ROTA);
		db.execSQL(TABLE_ROTA_VENDEDOR);
		db.execSQL(TABLE_UNIDADE);
		db.execSQL(TABLE_MARCA);

		db.execSQL(TABLE_USUARIO);
		db.execSQL(TABLE_VENDA_CABECALHO);
		db.execSQL(TABLE_VENDA_DETALHE);

		db.execSQL(TABLE_ANDROID_CAIXA);
		db.execSQL(TABLE_ANDROID_CONTA_RECEBER);
		db.execSQL(TABLE_ANDROID_VENDA_CABECALHO);
		db.execSQL(TABLE_ANDROID_VENDA_DETALHE);

		//db.execSQL(TABLE_USUARIO_LOGADO;

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DELETE_TABELA_CARGO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CATEGORIA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CIDADE); // Apaga o banco de dados
        db.execSQL(DELETE_TABELA_ESTADO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CLIENTE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CONTA_RECEBER); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CREDITO_USUARIO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CREDITO_USUARIO_DETALHES); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_EMPRESA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ENDERECO_CLIENTE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_FORMA_PAGAMENTO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_PRODUTO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ROTA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ROTA_VENDEDOR); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_TELEFONE_CLIENTE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_TELEFONE_USUARIO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_UNIDADE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_MARCA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_USUARIO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_VENDA_CABECALHO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_VENDA_DETALHE); // Apaga o banco de dados

		db.execSQL(DELETE_TABELA_ANDROID_CAIXA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ANDROID_CONTA_RECEBER); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ANDROID_VENDA_CABECALHO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ANDROID_VENDA_DETALHE); // Apaga o banco de dados

		//db.execSQL(DELETE_TABELA_USUARIO_LOGADO); // Apaga o banco de dados

		db.execSQL(TABLE_CARGO);
		db.execSQL(TABLE_CATEGORIA);
		db.execSQL(TABLE_CATEGORIA_CLIENTE);
		db.execSQL(TABLE_CLIENTE);
		db.execSQL(TABLE_CIDADE);
        db.execSQL(TABLE_ESTADO);
		db.execSQL(TABLE_CONTA_RECEBER);
		db.execSQL(TABLE_CREDITO_USUARIO_DETALHES);
		db.execSQL(TABLE_EMPRESA);
		db.execSQL(TABLE_FORMA_PAGAMENTO);
		db.execSQL(TABLE_PRODUTO);
		db.execSQL(TABLE_ROTA);
		db.execSQL(TABLE_ROTA_VENDEDOR);
		db.execSQL(TABLE_UNIDADE);
		db.execSQL(TABLE_MARCA);
		db.execSQL(TABLE_USUARIO);
		db.execSQL(TABLE_VENDA_CABECALHO);
		db.execSQL(TABLE_VENDA_DETALHE);

		db.execSQL(TABLE_ANDROID_CAIXA);
		db.execSQL(TABLE_ANDROID_CONTA_RECEBER);
		db.execSQL(TABLE_ANDROID_VENDA_CABECALHO);
		db.execSQL(TABLE_ANDROID_VENDA_DETALHE);

		//db.execSQL(TABLE_USUARIO_LOGADO);
	}

	public void deleteBanco(SQLiteDatabase db) {
		db.execSQL(DELETE_TABELA_CARGO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CATEGORIA); // Apaga o banco de dados
		//db.execSQL(DELETE_TABELA_CIDADE); // Apaga o banco de dados
        //db.execSQL(DELETE_TABELA_ESTADO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CLIENTE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CONTA_RECEBER); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CREDITO_USUARIO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CREDITO_USUARIO_DETALHES); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_EMPRESA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ENDERECO_CLIENTE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_FORMA_PAGAMENTO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_PRODUTO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ROTA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ROTA_VENDEDOR); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_UNIDADE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_MARCA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_USUARIO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_VENDA_CABECALHO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_VENDA_DETALHE); // Apaga o banco de dados

		//db.close();
	}

	public void deleteBancoCompleto(SQLiteDatabase db) {
		db.execSQL(DELETE_TABELA_CARGO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CATEGORIA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CIDADE); // Apaga o banco de dados
        db.execSQL(DELETE_TABELA_ESTADO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CLIENTE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CONTA_RECEBER); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CREDITO_USUARIO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_CREDITO_USUARIO_DETALHES); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_EMPRESA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ENDERECO_CLIENTE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_FORMA_PAGAMENTO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_PRODUTO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ROTA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ROTA_VENDEDOR); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_UNIDADE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_MARCA); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_USUARIO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_VENDA_CABECALHO); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_VENDA_DETALHE); // Apaga o banco de dados
		db.execSQL(DELETE_TABELA_ANDROID_VENDA_CABECALHO);
		db.execSQL(DELETE_TABELA_ANDROID_VENDA_DETALHE);
	}
/*
	public void deleteAndroidTabelas(SQLiteDatabase db){
		//db.execSQL(DELETE_TABELA_ANDROID_CAIXA); // Apaga o banco de dados
		//db.execSQL(DELETE_TABELA_ANDROID_CONTA_RECEBER); // Apaga o banco de dados
		//db.execSQL(DELETE_TABELA_ANDROID_VENDA_CABECALHO); // Apaga o banco de dados
		//db.execSQL(DELETE_TABELA_ANDROID_VENDA_DETALHE); // Apaga o banco de dados
	}

	public void limparAndroidTabelas(SQLiteDatabase db){
		//db.execSQL(DELETE_TABELA_ANDROID_CAIXA); // Apaga o banco de dados
		//db.execSQL(DELETE_TABELA_ANDROID_CONTA_RECEBER); // Apaga o banco de dados
		//db.execSQL(TRUNCATE_TABELA_ANDROID_VENDA_CABECALHO); // Apaga o banco de dados
		//db.execSQL(DELETE_TABELA_ANDROID_VENDA_DETALHE); // Apaga o banco de dados
	}
*/

}