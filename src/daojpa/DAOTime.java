package daojpa;

import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Time;

public class DAOTime extends DAO<Time> {
	
	public Time read (Object chave){
		try{
			String nome = (String) chave;
			TypedQuery<Time> q = manager.createQuery("select t from Time t where t.nome = :n ",Time.class);
			q.setParameter("n", nome);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}


	//--------------------------------------------
	//  consultas
	//--------------------------------------------
	
	
	
	//--------------------------------------------
	//  consulta 1
	//--------------------------------------------
//	public List<Time> jogosTimes(String nome) {
//		Query q;
//		q = manager.query();
//		q.constrain(Time.class);
//		q.descend("nome").constrain(nome);
//		return q.execute();
//	}
//	
	public List<Time> jogosTimes(String nome){
		TypedQuery<Time> q = manager.createQuery("select t from Time t where t.nome = :nome", Time.class);
		q.setParameter("nome", nome);
		return q.getResultList();
	}
	
	//--------------------------------------------
	//  consulta 2
	//--------------------------------------------
//	public List<Time> timeNTimes(int ingresso) {
//		Query q;
//		q = manager.query();
//		q.constrain(Time.class);
//		q.constrain(new Filtro(ingresso));
//		return q.execute();
//	}
//	
	public List<Time> timeNTimes(int ingresso){
		TypedQuery<Time> q = manager.createQuery("select t from Time t where size(t.ingresso) = :cod", Time.class);
		q.setParameter("cod", ingresso);
		return q.getResultList();
	}
	
	
	
//	class Filtro implements Evaluation {
//		private int ingresso;
//		public Filtro(int ingresso) {
//			this.ingresso = ingresso;
//		}
//		public void evaluate(Candidate candidate) {
//			Time time = (Time) candidate.getObject();
//			if(time.getJogos().size()== ingresso) 
//				candidate.include(true); 
//			else		
//				candidate.include(false);
//		}
//	}

}

