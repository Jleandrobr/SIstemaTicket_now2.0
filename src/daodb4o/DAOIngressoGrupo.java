package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.IngressoGrupo;

public class DAOIngressoGrupo extends DAO<IngressoGrupo> {
	
	public IngressoGrupo read (Object chave){
		int codigo = (int) chave;	
		Query q = manager.query();
		q.constrain(IngressoGrupo.class);
		q.descend("codigo").constrain(codigo);
		List<IngressoGrupo> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}
}
