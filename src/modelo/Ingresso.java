/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package modelo;

import java.util.ArrayList;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Ingresso  {
	@Id
	protected int codigo;
	
	@ManyToMany(mappedBy="jogo",
			cascade=CascadeType.ALL)
	private Jogo jogo;
	
	@OneToMany(mappedBy="jogo",
			cascade=CascadeType.ALL)
	private ArrayList<Jogo> jogos = new ArrayList<>();
	
	public Ingresso() {}
	
	public Ingresso(int codigo) {
		this.codigo = codigo;
	}

	public abstract double calcularValor();

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	

}
