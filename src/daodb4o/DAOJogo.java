package daodb4o;

import java.util.List;

import com.db4o.query.Candidate;
import com.db4o.query.Evaluation;
import com.db4o.query.Query;

import modelo.Jogo;

public class DAOJogo extends DAO<Jogo> {
	public Jogo read (Object chave){
		int id = (int) chave;	
		Query q = manager.query();
		q.constrain(Jogo.class);
		q.descend("id").constrain(id);
		List<Jogo> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}
	
	public void create(Jogo obj){
		int novoid = super.gerarId();  	//gerar novo id da classe
		obj.setId(novoid);				//atualizar id do objeto antes de grava-lo no banco
		manager.store( obj );
	}
	
	//--------------------------------------------
	//  consultas
	//--------------------------------------------
	
//	public List<Jogo> listarJogos(String data){
//		Query q;
//		q = manager.query();
//		q.constrain(Jogo.class);
//		q.descend(data).constrain(true);
//		return q.execute();
//		
//	}
	
	public List<Jogo> consultarJogos(int id) {
		Query q;
		q = manager.query();
		q.constrain(Jogo.class);
		q.descend("id").constrain(id);
		return q.execute();
	}

	public List<Jogo> jogosIngressos(int ingresso) {
		Query q;
		q = manager.query();
		q.constrain(Jogo.class);
		q.constrain(new Filtro(ingresso));
		return q.execute();
	}
	
	class Filtro implements Evaluation {
		private int ingresso;
		public Filtro(int ingresso) {
			this.ingresso = ingresso;
		}
		public void evaluate(Candidate candidate) {
			Jogo jogo = (Jogo) candidate.getObject();
			if(jogo.getIngressos().size()== ingresso) 
				candidate.include(true); 
			else		
				candidate.include(false);
		}
	}
	
}
