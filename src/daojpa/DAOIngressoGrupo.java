package daojpa;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.IngressoGrupo;

public class DAOIngressoGrupo extends DAO<IngressoGrupo> {
	
	public IngressoGrupo read (Object chave){
		try{
			String codigo = (String) chave;
			TypedQuery<IngressoGrupo> q = manager.createQuery("select ig from IngressoGrupo ig where ig.codigo = :c ",IngressoGrupo.class);
			q.setParameter("c", codigo);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
}
