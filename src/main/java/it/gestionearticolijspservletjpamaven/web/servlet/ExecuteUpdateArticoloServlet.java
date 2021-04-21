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

/**
 * Servlet implementation class ExecuteUpdateArticoloServlet
 */
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

		// questa variabile mi serve in quanto sfrutto in un colpo la validazione
		// della data ed il suo parsing che non posso fare senza un try catch
		// a questo punto lo incapsulo in un metodo apposito
		Date dataArrivoParsed = UtilityArticoloForm.parseDateArrivoFromString(dataArrivoStringParam);

		// valido input tramite apposito metodo e se la validazione fallisce torno in
		// pagina

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

		// se sono qui i valori sono ok quindi posso creare l'oggetto da inserire
		Articolo articoloInstance = new Articolo(codiceParam, descrizioneParam,
				Integer.parseInt(prezzoStringParam), dataArrivoParsed);
		
		articoloInstance.setId(Long.parseLong(idParam));
		// occupiamoci delle operazioni di business
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

		// andiamo ai risultati
		request.getRequestDispatcher("/articolo/results.jsp").forward(request, response);
	}

}
