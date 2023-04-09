package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.Ingresso;

public class DAOIngresso extends DAO<Ingresso>{

	public Ingresso read(Object codigo) {
		int codigo1 = (int) codigo;	
		Query q = manager.query();
		q.constrain(Ingresso.class);
		q.descend("codigo1").constrain(codigo1);
		List<Ingresso> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
		return null;
	}
	
	public void create(Ingresso obj){
		int novoCodigo = super.gerarId();  	//gerar novo id da classe
		obj.setCodigo(novoCodigo);				//atualizar id do objeto antes de grava-lo no banco
		manager.store( obj );
	}


	
}
