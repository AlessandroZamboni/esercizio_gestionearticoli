package it.gestionearticolijspservletjpamaven.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.gestionearticolijspservletjpamaven.model.Articolo;
import it.gestionearticolijspservletjpamaven.service.MyServiceFactory;

/**
 * Servlet implementation class ExecuteDeleteArticoloServlet
 */
@WebServlet("/ExecuteDeleteArticoloServlet")
public class ExecuteDeleteArticoloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ExecuteDeleteArticoloServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idArticoloParam = request.getParameter("idArticoloDelete");
		
		Articolo articoloInstance = null;		
		
		try {
			articoloInstance = MyServiceFactory.getArticoloServiceInstance().caricaSingoloElemento(Long.parseLong(idArticoloParam));
			MyServiceFactory.getArticoloServiceInstance().rimuovi(articoloInstance);
			request.setAttribute("listaArticoliAttribute", MyServiceFactory.getArticoloServiceInstance().listAll());
			request.setAttribute("successMessage", "Operazione effettuata con successo");

		} catch (Exception e) {

			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/articolo/delete.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		request.getRequestDispatcher("/articolo/results.jsp").forward(request, response);
	}

}
