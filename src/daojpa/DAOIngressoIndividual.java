package daojpa;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.IngressoIndividual;

public class DAOIngressoIndividual extends DAO<IngressoIndividual> {
	
	public IngressoIndividual read (Object chave){
		try{
			String codigo = (String) chave;
			TypedQuery<IngressoIndividual> q = manager.createQuery("select ii from IngressoGrupo ii where ii.codigo = :c ",IngressoIndividual.class);
			q.setParameter("c", codigo);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
}
