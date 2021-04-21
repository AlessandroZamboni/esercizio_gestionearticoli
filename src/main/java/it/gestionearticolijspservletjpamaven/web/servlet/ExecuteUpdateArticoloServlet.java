package it.gestionearticolijspservletjpamaven.web.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.gestionearticolijspservletjpamaven.model.Articolo;
import it.gestionearticolijspservletjpamaven.service.MyServiceFactory;
import it.gestionearticolijspservletjpamaven.utility.UtilityArticoloForm;


@WebServlet("/ExecuteUpdateArticoloServlet")
public class ExecuteUpdateArticoloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ExecuteUpdateArticoloServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// estraggo input
		String idParam = request.getParameter("idArticolo");
		String codiceParam = request.getParameter("codice");
		String descrizioneParam = request.getParameter("descrizione");
		String prezzoStringParam = request.getParameter("prezzo");
		String dataArrivoStringParam = request.getParameter("dataArrivo");

		Date dataArrivoParsed = UtilityArticoloForm.parseDateArrivoFromString(dataArrivoStringParam);

		if (!UtilityArticoloForm.validateInput(codiceParam, descrizioneParam, prezzoStringParam,
				dataArrivoStringParam) || dataArrivoParsed == null) {
			
			Articolo temp = new Articolo();
			
			temp.setId(Long.parseLong(idParam));
			temp.setCodice(codiceParam);
			temp.setDescrizione(descrizioneParam);
			if(!prezzoStringParam.isEmpty())
				temp.setPrezzo(Integer.parseInt(prezzoStringParam));
			temp.setDataArrivo(dataArrivoParsed);			
			
			request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
			request.setAttribute("articoloDaModificare", temp);
			request.getRequestDispatcher("/articolo/edit.jsp").forward(request, response);			
			
			
			return;
		}

		Articolo articoloInstance = new Articolo(codiceParam, descrizioneParam,
				Integer.parseInt(prezzoStringParam), dataArrivoParsed);
		
		articoloInstance.setId(Long.parseLong(idParam));
		
		try {

			MyServiceFactory.getArticoloServiceInstance().aggiorna(articoloInstance);
			request.setAttribute("listaArticoliAttribute", MyServiceFactory.getArticoloServiceInstance().listAll());
			request.setAttribute("successMessage", "Operazione effettuata con successo");

		} catch (Exception e) {

			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/articolo/index.jsp").forward(request, response);
			return;

		}

		request.getRequestDispatcher("/articolo/results.jsp").forward(request, response);
	}

}
