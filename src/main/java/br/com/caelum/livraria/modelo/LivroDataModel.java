package br.com.caelum.livraria.modelo;

import java.util.List;
import java.util.Map;

import javax.swing.SortOrder;

import org.primefaces.model.LazyDataModel;

import br.com.caelum.livraria.dao.DAO;

public class LivroDataModel extends LazyDataModel<Livro> {
	

	private static final long serialVersionUID = 1L;
	DAO dao = null;
	
	public LivroDataModel() {

		super.setRowCount(dao.quantidadeDeElementos());
	}
	
	public List<Livro> load(int inicio, int quantidade, String campoOrdenacao, SortOrder sentidoOrdenacao, Map<String, Object> filtros) {
	    String titulo = (String) filtros.get("titulo");

	    return dao.listaTodosPaginada(inicio, quantidade, "titulo", titulo);
	}
}
