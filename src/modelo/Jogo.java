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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;


@Entity
@Access(AccessType.PROPERTY)
public class Jogo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; //autogerado
	private String data;
	private String local;
	private int estoque;
	private double preco;
	private Time time1;
	private Time time2;
	
	@ManyToMany(mappedBy="ingresso",
			cascade=CascadeType.ALL)
	private ArrayList<Ingresso> ingressos = new ArrayList<>();
	
	@ManyToMany(mappedBy="time",
			cascade=CascadeType.ALL)
	private Time time;
	
//	@ManyToMany(mappedBy="ingresso",
//			cascade=CascadeType.ALL)
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private Ingresso ingresso;
	
	
	public Jogo() {}

	public Jogo(String data, String local, int estoque, double preco, Time time1, Time time2) {
		//id ser� gerado pelo banco;
		
		this.data = data;
		this.local = local;
		this.estoque = estoque;
		this.preco = preco;
		this.time1 = time1;
		this.time2 = time2;
		
	}
	
	public void adicionar(Ingresso i){
		ingressos.add(i);
	}
	public void remover(Ingresso i){
		ingressos.remove(i);
	}

	public Ingresso localizar(int codigo){
		for(Ingresso i : ingressos){
			if(i.getCodigo() == codigo)
				return i;
		}
		return null;
	}

	public double obterValorArrecadado() {
		double soma=0;
		for(Ingresso i : ingressos)
			soma = soma + i.calcularValor();

		return soma;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public int getEstoque() {
		return estoque;
	}

	public void setEstoque(int estoque) {
		this.estoque = estoque;
	}

	public Time getTime1() {
		return time1;
	}

	public Time getTime2() {
		return time2;
	}
	
	public void setTime1(Time time1) {
		this.time1 = time1;
	}

	public void setTime2(Time time2) {
		this.time2 = time2;
	}

	public ArrayList<Ingresso> getIngressos() {
		return ingressos;
	}
	@Override
	public String toString() {

		String texto = "id=" + id + ", data=" + data + ", local=" + local + ", estoque=" + estoque + ", preco=" + preco
				+ ", time1=" + time1.getNome() + " x time2=" + time2.getNome();

		texto += "\ningressos:";
		for(Ingresso i : ingressos)
			texto += i.getCodigo() + ",";
		return texto;
	}


}
