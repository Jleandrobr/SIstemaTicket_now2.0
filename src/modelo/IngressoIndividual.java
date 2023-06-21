/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/
package modelo;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class IngressoIndividual extends Ingresso {
	
//	@OneToMany(mappedBy = "ingressoindividual", 
//			cascade={CascadeType.PERSIST, CascadeType.MERGE}, 
//			fetch = FetchType.LAZY)
//	@ManyToMany(mappedBy="ingresso",
//			cascade=CascadeType.ALL)
	private Jogo jogo;
	
	public IngressoIndividual() {}

	public IngressoIndividual(int codigo) {
		super(codigo);
	}

	public double calcularValor() {
		return 1.2 * jogo.getPreco();
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
		jogo.setEstoque(jogo.getEstoque() - 1 );
	}

	@Override
	public String toString() {
		return "codigo=" + codigo + ", jogo=" + jogo.getId();
	}
	
	
	
}
