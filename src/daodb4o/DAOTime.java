package daodb4o;

import java.util.List;

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


//	public List<Time> consultarJogos(String id) {
//		Query q;
//		q = manager.query();
//		q.constrain(Time.class);
//		q.descend("nome").descend("nome").constrain(nome);
//		return q.execute();
//	}
}

