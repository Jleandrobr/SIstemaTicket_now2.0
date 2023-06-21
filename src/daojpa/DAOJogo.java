package daojpa;

import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Jogo;

public class DAOJogo extends DAO<Jogo> {
	public Jogo read (Object chave){
		try{
			String id = (String) chave;
			TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.id = :i ",Jogo.class);
			q.setParameter("i", id);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}	}
	
//	public void create(Jogo obj){
//		int novoid = super.gerarId();  	//gerar novo id da classe
//		obj.setId(novoid);				//atualizar id do objeto antes de grava-lo no banco
//		manager.store( obj );
//	}
	
	//--------------------------------------------
	//  consultas
	//--------------------------------------------
	
	
//	public List<Jogo> consultarJogos(int id) {
//		Query q;
//		q = manager.query();
//		q.constrain(Jogo.class);
//		q.descend("id").constrain(id);
//		return q.execute();
//	}
	public List<Jogo> consultarJogos(int id){
		TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.id =:id ", Jogo.class);
		q.setParameter("id", id);
		return  q.getResultList();
	}

	//--------------------------------------------
	//  consulta 2
	//--------------------------------------------
//	public List<Jogo> jogosIngressos(int ingresso) {
//		Query q;
//		q = manager.query();
//		q.constrain(Jogo.class);
//		q.descend("ingressos").descend("codigo").constrain(ingresso);
//		return q.execute();
//	}
	public List<Jogo> jogosIngressos(int ingresso){
		TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j JOIN j.ingressos i where i.codigo = :ingresso", Jogo.class);
	    q.setParameter("ingresso", ingresso);
		return q.getResultList();
	}
	
	//--------------------------------------------
	//  consulta 3
	//--------------------------------------------
//	
//	public List<Jogo> consultarLocais(String local) {
//		Query q;
//		q = manager.query();
//		q.constrain(Jogo.class);
//		q.descend("local").constrain(local);
//		return q.execute();
//	}
	
	public List<Jogo> consultarLocais(String local){
		TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.local = :local", Jogo.class);
	    q.setParameter("local", local);
		return q.getResultList();
	}
	
	
	
	
	
	
//	
//	class Filtro implements Evaluation {
//		private int ingresso;
//		public Filtro(int ingresso) {
//			this.ingresso = ingresso;
//		}
//		public void evaluate(Candidate candidate) {
//			Jogo jogo = (Jogo) candidate.getObject();
//			if(jogo.getIngressos().size()== ingresso) 
//				candidate.include(true); 
//			else		
//				candidate.include(false);
//		}
//	}

	
	
	
}
