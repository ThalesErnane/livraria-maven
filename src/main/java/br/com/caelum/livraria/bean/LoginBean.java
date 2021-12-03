package br.com.caelum.livraria.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.UsuarioDao;
import br.com.caelum.livraria.modelo.Usuario;

//@ManagedBean
@Named
@ViewScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	Usuario usuario = new Usuario();
	
	@Inject 
	private	UsuarioDao usuarioDao;
	
	@Inject 
	private FacesContext context;
	
	public Usuario getUsuario() {
		return usuario;
	}

	public String efetuarLogin() {
		System.out.println("Efetuando Login " + this.getUsuario().getEmail());
		// FacesContext context = FacesContext.getCurrentInstance();
		
		boolean existe = usuarioDao.existe(this.usuario);
		if(existe) {
		
			context.getExternalContext().getSessionMap().put("usuarioLogado", this.usuario);
			return "livro?faces-redirect=true";
		}
			context.getExternalContext().getFlash().setKeepMessages(true); // mantem a mensagem por uma de uma req
			context.addMessage(null, new FacesMessage("Usuário não encontrado"));
		
			return "login?faces-redirect=true";
	}
	
	public String deslogar() {
		// FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("usuarioLogado", this.usuario);
		return "login?faces-redirect=true";
	}
}
