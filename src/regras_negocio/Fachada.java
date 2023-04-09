/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package regras_negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import daodb4o.DAO;
import daodb4o.DAOIngresso;
import daodb4o.DAOIngressoGrupo;
import daodb4o.DAOIngressoIndividual;
import daodb4o.DAOJogo;
import daodb4o.DAOTime;
import daodb4o.DAOUsuario;
import modelo.Ingresso;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;
import modelo.Jogo;
import modelo.Time;
import modelo.Usuario;

public class Fachada {
	private Fachada() {}	

	private static DAOUsuario daousuario = new DAOUsuario(); 
	private static DAOTime daotime = new DAOTime(); 
	private static DAOJogo daojogo = new DAOJogo(); 
	private static DAOIngresso daoingresso = new DAOIngresso(); 
	private static DAOIngressoIndividual daoingressoindividual = new DAOIngressoIndividual(); 
	private static DAOIngressoGrupo daoingressogrupo = new DAOIngressoGrupo(); 


	public static Usuario logado;
	public static void inicializar(){
		DAO.open();
	}
	public static void finalizar(){
		DAO.close();
	}


	public static ArrayList<Time> listarTimes() {
		DAO.begin();
		ArrayList<Time> Listartime = new ArrayList<>();
		for (Time t: daotime.readAll()){
				Listartime.add(t);
		}
		DAO.commit();
		return Listartime;
	}
	
	public static ArrayList<Jogo> listarJogos() {
		DAO.begin();
//		List<Jogo> resultados = daojogo.readAll();	//retorna todos os jogos
//		return resultados;
		ArrayList<Jogo> Listarjogos = new ArrayList<>();
		for (Jogo j: daojogo.readAll()){
			Listarjogos.add(j);
		}
		DAO.commit();
		return Listarjogos;
	}
	
	public static List<Usuario> listarUsuarios() {
		DAO.begin();
		List<Usuario> resultados = daousuario.readAll();   //retorna todos os usuarios
		DAO.commit();
		return resultados;
	}
	
	public static ArrayList<Ingresso> listarIngressos() {
		DAO.begin();
//		List<Ingresso> resultados = daoingresso.readAll();   //retorna todos os ingressos
//		return resultados;
		ArrayList<Ingresso> Listaringresso = new ArrayList<>();
		for (Ingresso i: daoingresso.readAll()){
				Listaringresso.add(i);
		}
		DAO.commit();
		return Listaringresso;
	}
	
	public static ArrayList<Jogo> listarJogos(String data) {
		DAO.begin();
//		ArrayList<Jogo> resultados =  (ArrayList<Jogo>) daojogo.listarJogos(data);
//		DAO.commit();
//		return resultados; //retorna todos os jogos
		ArrayList<Jogo> jogosPorData = new ArrayList<>();
		for (Jogo j: daojogo.readAll()){
			if(j.getData().equals(data)){
				jogosPorData.add(j);
			}
		}
		DAO.commit();
		return jogosPorData;
	}
	
	public static Ingresso localizarIngresso(int codigo) {
		return daoingresso.read(codigo);   //retorna o ingresso com o c�digo fornecido
	}

	public static Jogo	localizarJogo(int id) {
		return daojogo.read(id);     //retorna o jogo com o id fornecido
	}

	public static Usuario criarUsuario(String email, String senha) throws Exception{
		DAO.begin(); 
		Usuario usu = daousuario.read(email);
		if (usu!=null)
			throw new Exception("Usuario ja cadastrado:" + email);
		usu = new Usuario(email, senha);

		daousuario.create(usu);
		DAO.commit();
		return usu;
		
	}
	public static Usuario validarUsuario(String email, String senha) {
		DAO.begin();
		Usuario usu = daousuario.read(email);
		if (usu==null)
			return null;

		if (! usu.getSenha().equals(senha))
			return null;

		DAO.commit();
		return usu;
	}

	public static Time criarTime(String nome, String origem) throws Exception {
		DAO.begin();
		//verificar regras de negocio
		//criar o time
		Time time = daotime.read(nome);
		if(time != null)
			throw new Exception("Time"+ time + "já existe!"); 
			
		time = new Time(nome,origem);
		
		daotime.create(time);//gravar time no banco
		DAO.commit();
		return time;
	}
	
	public static Jogo 	criarJogo(String data, String local, int estoque, double preco, String nometime1, String nometime2)  throws Exception {
		DAO.begin();
		//verificar regras de negocio
//		Jogo jogo = daojogo.read(nometime1);
//		if(nometime1 == nometime2)
//			throw new Exception("Não é permitido dois times iguais em um mesmo jogo");
		
		//localizar time1 e time2
		Time time1 = daotime.read(nometime1);
		if (time1 == null)
			throw new Exception("Time"+ ""+ time1 +""+""+ "não está cadastrado(a)");
		
		Time time2 = daotime.read(nometime2);
		if (time2 == null)
			throw new Exception("Time"+ ""+ time2 +""+ ""+ "não está cadastrado(a)");
		
		//gerar id sequencial do jogo 
//		int id = daojogo.gerarId();
		
		//criar  jogo 
		Jogo jogo = new Jogo(data, local, estoque, preco, time1, time2);

		//relacionar o jogo com os times e vice-versa 
		jogo.setTime1(time1);
		jogo.setTime2(time2);
		time1.adicionar(jogo);
		time2.adicionar(jogo);
		
		//gravar jogo no banco
		daojogo.create(jogo);
		
		DAO.commit();
		return jogo;
	}

	public static IngressoIndividual criarIngressoIndividual(int id) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		Jogo jogo = daojogo.read(id);
		if(jogo == null)
			throw new Exception("Id inexistente");
		
		//verificar unicididade do codigo no sistema
//		jogo = daojogo.read(id);
//		if(jogo != null)
//			throw new Exception("Ingresso já existente");
		
		//gerar codigo aleat�rio 
		int codigo = new Random().nextInt(1000000);
			
		//criar o ingresso individual 
		IngressoIndividual ingresso = new IngressoIndividual(codigo);
		
		ingresso = daoingressoindividual.read(ingresso);
		if(ingresso != null)
			throw new Exception("Ingresso já existente");

		//relacionar este ingresso com o jogo e vice-versa
		ingresso.setJogo(jogo);
		jogo.adicionar(ingresso);
		jogo.setEstoque(jogo.getEstoque()-1);

		//gravar ingresso no banco
		daoingressoindividual.create(ingresso);
		DAO.commit();
		return ingresso;
	}

	public static IngressoGrupo	criarIngressoGrupo(int[] ids) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		Jogo jogo = daojogo.read(ids);
		if(jogo == null)
			throw new Exception("Id inexistente");
		
		//verificar unicididade no sistema
		jogo = daojogo.read(ids);
		if(jogo != null)
			throw new Exception("Ingresso já cadastrado");
		
		//gerar codigo aleat�rio 
		int codigo = new Random().nextInt(1000000);
		
		
		//criar o ingresso grupo 
		IngressoGrupo ingresso = new IngressoGrupo(codigo);
		
//		//relacionar este ingresso com os jogos indicados e vice-versa
//		for (Jogo j: ids) {
//			j.adicionar(ingresso);
//			j.setEstoque(j.getEstoque()-1);
//			ingresso.adicionar(j);
//		}
		ArrayList<Jogo> jogos = new ArrayList<Jogo>();
		for (int id : ids) {
		    jogo = daojogo.read(id); // método para consultar jogo por ID
		    jogos.add(jogo);
		}

		// atualizar os jogos e o ingresso
		for (Jogo j : jogos) {
		    j.adicionar(ingresso);
		    j.setEstoque(j.getEstoque() - 1);
		    ingresso.adicionar(j);
		    daojogo.update(j);
		}
		
		//gravar ingresso no banco
		daoingressogrupo.create(ingresso);
		DAO.commit();
		return ingresso;
	}

	public static void	apagarIngresso(int codigo) throws Exception {
		DAO.begin();

		//verificar regras de negocio
		Ingresso ingresso = daoingresso.read(codigo);
		if(ingresso == null)
			throw new Exception("Ingresso inexistente");
		
		Jogo jogo = daojogo.read(ingresso);
		if(jogo == null)
			throw new Exception("Jogo inexistente");
		
		//remover o relacionamento entre o ingresso e o(s) jogo(s) ligados a ele
		//o codigo refere-se a ingresso individual ou grupo
		ingresso = daoingresso.read(codigo);
		if (ingresso instanceof IngressoGrupo grupo) {
			ArrayList<Jogo> jogos = grupo.getJogos();
			for (Jogo j : jogos) {
				j.remover(grupo);
				j.setEstoque(j.getEstoque()+1);
				daojogo.update(j);
				daoingresso.delete(grupo); 
			}
		}
		else 
			if (ingresso instanceof IngressoIndividual individuo) {
				jogo = individuo.getJogo();
				jogo.remover(individuo);
				jogo.setEstoque(jogo.getEstoque()+1);
				daojogo.update(jogo);
				daoingresso.delete(individuo);
			}
		
		//apagar ingresso no banco
		daoingresso.delete(ingresso);
		DAO.commit();
	}

	public static void	apagarTime(int id) throws Exception {
		DAO.begin();
		//verificar regras de negocio
		Time time = daotime.read(id);
		if(time == null)
			throw new Exception("Time inexistente");
		
		if(! time.isFinalizado()) 
			throw new Exception ("Time ainda contem jogos nao pode ser excluido " + id);
		
		//apagar time no banco
		daotime.delete(time);
		DAO.commit();
	}

	public static void 	apagarJogo(int id) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		Jogo jogo = daojogo.read(id);
		if(jogo == null)
			throw new Exception("Jogo inexistente");
		
		//apagar jogo no banco
		daojogo.delete(jogo);
		DAO.commit();
	}

	/**********************************
	 * 5 Consultas
	 **********************************/
}
