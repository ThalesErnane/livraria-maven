package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDAO;
import br.com.caelum.livraria.dao.LivroDAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.tx.Transacional;

//@ManagedBean
@Named
@ViewScoped // Bean fica durante todo scopo da requisição
public class LivroBean implements Serializable {
	
	/*
    @PostConstruct
    void init() {
        System.out.println("AutorBean está nascendo ....");
    }

    @PreDestroy
    void morte() {
        System.out.println("AutorBean está morrendo ....");
    }
	*/
	
	private static final long serialVersionUID = 1L;

	private Livro livro = new Livro();
	
	private List<String> generos = Arrays.asList("Romance", "Drama", "Ação");
	
	private Integer autorId;

	private Integer livroId;

	private List<Livro> livros;
	
	@Inject
	private LivroDAO livroDao;
	
	@Inject
	private AutorDAO autorDao;
	
	@Inject 
	private FacesContext context;
	
	public List<String> getGeneros() {
	    return generos;
	}

	public Integer getLivroId() {
		return livroId;
	}

	public void setLivroId(Integer livroId) {
		this.livroId = livroId;
	}

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}

	public Livro getLivro() {
		return livro;
	}

	public List<Livro> getLivros() {

		if (this.livros == null) {
			this.livros = this.livroDao.listaTodos();
		}

		return livros;
	}

	public List<Autor> getAutoresDoLivro() {
		return this.livro.getAutores();
	}

	public List<Autor> getAutores() {
		return this.autorDao.listaTodos();
	}

	public void carregarlivroPeloId() {
		this.livro = this.livroDao.buscaPorId(this.livroId);
	}

	@Transacional
	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) { // pega o id autor da view, e add uma msg se o mesmo estiver empty na hora
											// do cadastro
			context.addMessage("autor",
					new FacesMessage("Livro deve ter pelo menos um Autor"));
			return;
		}


		if (this.livro.getId() == null) {
			this.livroDao.adiciona(this.livro);
			// Novo livro adicionado, listamos todos os livros novamente
			this.livros = this.livroDao.listaTodos();
		} else {
			this.livroDao.atualiza(this.livro);
		}

		this.livro = new Livro();
	}

	public String formAutor() {
		System.out.println("Chamando o formulário do Autor");
		return "autor?faces-redirect=true"; // redirect
	}

	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException {

		String valor = value.toString();
		if (!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("Deveria começar com 1"));
		}
	}

	public void gravarAutor() {
		Autor autor = this.autorDao.buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
	}
	
	@Transacional
	public void remover(Livro livro) {
		System.out.println("Removendo livro");
		 this.livroDao.remove(livro);
	}

	public void removerAutorDoLivro(Autor autor) {
		this.livro.removeAutor(autor);
	}

	public void carregar(Livro livro) {
		this.livro = this.livroDao.buscaPorId(livro.getId());
	}

	public boolean precoEhMenor(Object valorColuna, Object filtroDigitado, Locale locale) { // java.util.Locale

		// tirando espaços do filtro
		String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();

		System.out.println("Filtrando pelo " + textoDigitado + ", Valor do elemento: " + valorColuna);

		// o filtro é nulo ou vazio?
		if (textoDigitado == null || textoDigitado.equals("")) {
			return true;
		}

		// elemento da tabela é nulo?
		if (valorColuna == null) {
			return false;
		}

		try {
			// fazendo o parsing do filtro para converter para Double
			Double precoDigitado = Double.valueOf(textoDigitado);
			Double precoColuna = (Double) valorColuna;

			// comparando os valores, compareTo devolve um valor negativo se o value é menor
			// do que o filtro
			return precoColuna.compareTo(precoDigitado) < 0;

		} catch (NumberFormatException e) {

			// usuario nao digitou um numero
			return false;
		}
	}
}
