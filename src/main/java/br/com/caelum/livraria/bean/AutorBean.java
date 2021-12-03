package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDAO;
import br.com.caelum.livraria.dao.LivroDAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.tx.Transacional;

//@ManagedBean anotação sem ultilizar CDI
@Named
@ViewScoped
public class AutorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Autor autor = new Autor();

	private Livro livro = new Livro();

	private Integer autorId;

	@Inject
	private AutorDAO autorDao;
		
	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Autor getAutor() {
		return autor;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public List<Autor> getAutores() {
		return this.autorDao.listaTodos();
	}

	public List<Autor> getAutoresDoLivro() {
		return this.livro.getAutores();
	}

	public void carregaPelaId() {
		Integer id = this.autor.getId();
		this.autor = this.autorDao.buscaPorId(id);
		if (this.autor == null) {
			this.autor = new Autor();
		}
	}

	@Transacional
	public String gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());

		if (this.autor.getId() == null) {
			this.autorDao.adiciona(this.autor);
		} else {
			this.autorDao.atualiza(this.autor);
		}
		this.autor = new Autor();
		return "livro?faces-redirect=true";
	}

	@Transacional
	public void remover(Autor autor) {
		
		System.out.println(this.livro.getAutores().contains(autor));
		boolean contemAutor = this.livro.getAutores().contains(autor);
		// boolean existe = this.autorDao.existe(this.autor);
		//this.livro.getAutores().contains(autor) == true
		
		System.out.println( contemAutor == true);
		if (contemAutor == true) {
			FacesContext.getCurrentInstance().addMessage("autor",
					new FacesMessage("Nao pode remover autor que contem um livro!"));
		} else {

			this.autorDao.remove(autor);
		}

	}

	public void carregar(Autor autor) {
		this.autor = autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

}
