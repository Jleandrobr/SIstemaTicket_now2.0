package daojpa;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Ingresso;

public class DAOIngresso extends DAO<Ingresso>{

	public Ingresso read(Object chave) {
		try{
			String codigo = (String) chave;
			TypedQuery<Ingresso> q = manager.createQuery("select i from Ingresso i where i.codigo = :c ",Ingresso.class);
			q.setParameter("c", codigo);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
//	public void create(Ingresso obj){
//		int novoCodigo = super.gerarId();  	//gerar novo id da classe
//		obj.setCodigo(novoCodigo);				//atualizar id do objeto antes de grava-lo no banco
//		manager.store( obj );
//	}


	
}
