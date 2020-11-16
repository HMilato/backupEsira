/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicorestful;

import esira.domain.Estudante;
import esira.service.CRUDService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Ussimane
 */
//@RestController
//@RequestMapping( value = "/estudante" )
@Controller
public class EstudanteService {
    @Autowired
    private CRUDService CRUDService;
    
    @RequestMapping(value = "/todos1", method = RequestMethod.GET)
	public @ResponseBody List<Estudante> getEstudantes1() {
		List<Estudante> estudantes = CRUDService.getAll(Estudante.class);
		return estudantes;
	}
        
        @RequestMapping(value = "/todos2", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Estudante> getEStudatnes2() {
		List<Estudante> estudantes = CRUDService.getAll(Estudante.class);
		return estudantes;
	}
}
