/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package regras_negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import daojpa.DAO;
import daojpa.DAOIngresso;
import daojpa.DAOIngressoGrupo;
import daojpa.DAOIngressoIndividual;
import daojpa.DAOJogo;
import daojpa.DAOTime;
import daojpa.DAOUsuario;
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
		ArrayList<Ingresso> Listaringresso = new ArrayList<>();
		for (Ingresso i: daoingresso.readAll()){
				Listaringresso.add(i);
		}
		DAO.commit();
		return Listaringresso;
	}
	
	public static ArrayList<Jogo> listarJogos(String data) {
		DAO.begin();
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
		if (usu!=null) {
			DAO.rollback();
			throw new Exception("Usuario ja cadastrado:" + email);
		}
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
		if(time != null) {
			DAO.rollback();
			throw new Exception("Time"+ time + "já existe!"); 
		}
		time = new Time(nome,origem);
		
		daotime.create(time);//gravar time no banco
		DAO.commit();
		return time;
	}
	
	public static Jogo 	criarJogo(String data, String local, int estoque, double preco, String nometime1, String nometime2)  throws Exception {
		DAO.begin();
		//verificar regras de negocio
		
		//localizar time1 e time2
		Time time1 = daotime.read(nometime1);
		if (time1 == null) {
			DAO.rollback();
			throw new Exception("Time"+ ""+ time1 +""+""+ "não está cadastrado(a)");
		}
		Time time2 = daotime.read(nometime2);
		if (time2 == null) {
			DAO.rollback();
			throw new Exception("Time"+ ""+ time2 +""+ ""+ "não está cadastrado(a)");
		}
		
		//criar  jogo 
		Jogo jogo = new Jogo(data, local, estoque, preco, time1, time2);

		//relacionar o jogo com os times e vice-versa 
		jogo.setTime1(time1);
		jogo.setTime2(time2);
		time1.adicionar(jogo);
		time2.adicionar(jogo);
		
		//gravar jogo no banco
		daojogo.create(jogo);
		daotime.update(time1);
		daotime.update(time2);
		
		DAO.commit();
		return jogo;
	}

	public static IngressoIndividual criarIngressoIndividual(int id) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		Jogo jogo = daojogo.read(id);
		if(jogo == null) {
			DAO.rollback();
			throw new Exception("Id inexistente");
		}
		
		//verificar unicididade do codigo no sistema
		IngressoIndividual ingresso = daoingressoindividual.read(id);
		if(ingresso != null) {
			DAO.rollback();
			throw new Exception("Ingresso já existente");
		}
		//gerar codigo aleat�rio 
		int codigo = new Random().nextInt(1000000);
			
		//criar o ingresso individual 
		ingresso = new IngressoIndividual(codigo);
		
		

		//relacionar este ingresso com o jogo e vice-versa
		ingresso.setJogo(jogo);
		jogo.adicionar(ingresso);
		jogo.setEstoque(jogo.getEstoque()-1);

		//gravar ingresso no banco
		daoingressoindividual.create(ingresso);
		daojogo.update(jogo);
		DAO.commit();
		return ingresso;
	}

	public static IngressoGrupo	criarIngressoGrupo(int[] ids) throws Exception{
		DAO.begin();
		ArrayList<Integer> idsJogos = new ArrayList<>(); // arraylist para os ids de cada jogo
		ArrayList<Jogo> jogosIndicados = new ArrayList<Jogo>(); //jogos que o usuario queira adicionar no ingresso
		
		//criacao ids de jogos que estão no repositorio
		for(Jogo j: listarJogos()) {
			idsJogos.add(j.getId());
		}
		
		
		//verificar unicididade no sistema
		//gerar codigo aleat�rio 
		int codigo = new Random().nextInt(1000000);
		
		IngressoGrupo ingresso = daoingressogrupo.read(codigo);
		if(ingresso != null) {
			DAO.rollback();
			throw new Exception("Ingresso já cadastrado");
		}
		//criar o ingresso grupo 
		ingresso = new IngressoGrupo(codigo);
		
		
		for(Integer i: ids) {
			if(!idsJogos.contains(i)) {
				throw new Exception("Jogo no id"+ i + "não existe");
			}
			jogosIndicados.add(Fachada.localizarJogo(i));
		}
		
		// atualizar os jogos e o ingresso
		for (Jogo j : jogosIndicados) {
		    j.adicionar(ingresso);
		    j.setEstoque(j.getEstoque() - 1);
		    ingresso.adicionar(j);
		    daojogo.update(j);
		    daoingresso.update(ingresso);
		}
		
		
		//gravar ingresso no banco
		daoingressogrupo.create(ingresso);
		for (Jogo j : jogosIndicados) {
			daojogo.update(j);
		}
		
		DAO.commit();
		return ingresso;
	}

	public static void	apagarIngresso(int codigo) throws Exception {
		DAO.begin();

		//verificar regras de negocio
		Ingresso ingresso = daoingresso.read(codigo);
		if(ingresso == null) {
			DAO.rollback();
			throw new Exception("Ingresso inexistente");
		}
		//remover o relacionamento entre o ingresso e o(s) jogo(s) ligados a ele
		//o codigo refere-se a ingresso individual ou grupo
//		ingresso = daoingresso.read(codigo);
		if (ingresso instanceof IngressoGrupo grupo) {
			ArrayList<Jogo> jogos = grupo.getJogos();
			for (Jogo j : jogos) {
				j.remover(grupo);
				j.setEstoque(j.getEstoque()+1);
				daojogo.update(j);
			}
		}
		else 
			if (ingresso instanceof IngressoIndividual individuo) {
				Jogo jogo = individuo.getJogo();
				jogo.remover(individuo);
				jogo.setEstoque(jogo.getEstoque()+1);
				daojogo.update(jogo);
			}
		
		//apagar ingresso no banco		
		daoingresso.delete(ingresso);
		DAO.commit();
	}

	public static void	apagarTime(String nome) throws Exception {
		DAO.begin();
		//verificar regras de negocio
		Time time = daotime.read(nome);
		if(time == null) {
			DAO.rollback();
			throw new Exception("Time inexistente");
		}
		if(time.getJogos().size() > 0) {
			DAO.rollback();
			throw new Exception("time ainda possui jogos");
		}
		
		if (time instanceof Time) {
		    List<Jogo> jogos = time.getJogos();
		 // verifique se há ingressos antes de tentar removê-los
		    if (!jogos.isEmpty()) {
		    for(Jogo j : jogos) {
		    	time.remover(j);
		    	daojogo.update(j);
		    	daotime.update(time);
		    }
		}
		}
		
		//apagar time no banco
		daotime.delete(time);
		DAO.commit();
	}

	public static void 	apagarJogo(int id) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		Jogo jogo = daojogo.read(id);
		if(jogo == null) {
			DAO.rollback();
			throw new Exception("Jogo inexistente");
		}
		if(jogo.getIngressos().size()>0) {
			DAO.rollback();
			throw new Exception("jogo possui ingressos");
		}
				
		if (jogo instanceof Jogo ) {
		    List<Ingresso> ingressos = jogo.getIngressos();
		    // verifique se há ingressos antes de tentar removê-los
		    if (!ingressos.isEmpty()) {
		    	for(Ingresso i : ingressos) {
			    	jogo.remover(i);
			    	daojogo.update(jogo);
			    	daoingresso.update(i);
			    }
		    }
		}
		//apagar jogo no banco
		daojogo.delete(jogo);
		DAO.commit();
	}
	

	/**********************************
	 * 5 Consultas
	 **********************************/
	
	
	
	public static List<Time> jogosTimes(String nome) {
		DAO.begin();
		List<Time> resultados =  daotime.jogosTimes(nome);
		DAO.commit();
		return resultados;
	}
	public static List<Jogo> consultarJogos(int id) {
		DAO.begin();
		List<Jogo> resultados =  daojogo.consultarJogos(id);
		DAO.commit();
		return resultados;
	}
	public static List<Jogo> jogosIngressos(int ingresso) {
		DAO.begin();
		List<Jogo> resultados =  daojogo.jogosIngressos(ingresso);
		DAO.commit();
		return resultados;
	}
	public static List<Time> timeNTimes(int ingresso) {
		DAO.begin();
		List<Time> resultados =  daotime.timeNTimes(ingresso);
		DAO.commit();
		return resultados;
	}

	public static List<Jogo> consultarLocais(String local) {
		DAO.begin();
		List<Jogo> resultados =  daojogo.consultarLocais(local);
		DAO.commit();
		return resultados;
	}
}
