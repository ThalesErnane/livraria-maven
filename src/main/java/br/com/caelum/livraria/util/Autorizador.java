package br.com.caelum.livraria.util;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.caelum.livraria.modelo.Usuario;

public class Autorizador implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent evento) {
		FacesContext facesContext = evento.getFacesContext();
		String nomeDaPagina = facesContext.getViewRoot().getViewId(); // devolve qual a pag

		if ("/login.xhtml".equals(nomeDaPagina)) {
			return;
		}

		Usuario usuarioLogado = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuarioLogado");
	
		if(usuarioLogado != null) {
			return;
		}
		
		NavigationHandler handler = facesContext.getApplication().getNavigationHandler();
		handler.handleNavigation(facesContext, null, "/login?faces-redirect=true");
		facesContext.renderResponse();
	}

	@Override
	public void beforePhase(PhaseEvent event) {

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
