/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicorestful;

import esira.domain.Estudante;
import esira.service.CRUDService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 *
 * @author Ussimane
 */

@Path("entities.customer")
public class CustomerFacadeREST {
   @Autowired
    private CRUDService CRUDService;
   
   
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Estudante> getEstudantes1() {
//		List<Estudante> estudantes = CRUDService.getAll(Estudante.class);
        List<Estudante> estudantes = new ArrayList<Estudante>();
        Estudante e = new Estudante();
        e.setNomeCompleto("dsfsdf");
        estudantes.add(e);
		return estudantes;
	}
    
}
