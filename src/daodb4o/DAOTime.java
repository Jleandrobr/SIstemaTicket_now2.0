package daodb4o;

import java.util.List;

import com.db4o.query.Candidate;
import com.db4o.query.Evaluation;
import com.db4o.query.Query;

import modelo.Time;

public class DAOTime extends DAO<Time> {
	
	public Time read (Object chave){
		String nome = (String) chave;	
		Query q = manager.query();
		q.constrain(Time.class);
		q.descend("nome").constrain(nome);
		List<Time> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}


	//--------------------------------------------
	//  consultas
	//--------------------------------------------
	
	
	public List<Time> jogosTimes(String nome) {
		Query q;
		q = manager.query();
		q.constrain(Time.class);
		q.descend("nome").constrain(nome);
		return q.execute();
	}
	
	
	public List<Time> timeNTimes(int ingresso) {
		Query q;
		q = manager.query();
		q.constrain(Time.class);
		q.constrain(new Filtro(ingresso));
		return q.execute();
	}
	
	class Filtro implements Evaluation {
		private int ingresso;
		public Filtro(int ingresso) {
			this.ingresso = ingresso;
		}
		public void evaluate(Candidate candidate) {
			Time time = (Time) candidate.getObject();
			if(time.getJogos().size()== ingresso) 
				candidate.include(true); 
			else		
				candidate.include(false);
		}
	}
}

