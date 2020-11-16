/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import esira.domain.Curso;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

/**
 *
 * @author Administrator
 */
public class PlanoMRender implements ListitemRenderer<Curso> {

    public void render(Listitem lstm, Curso t, int i) throws Exception {
        lstm.setValue(t);
        lstm.appendChild(new Listcell(t.getCodigoCurso()));
         lstm.appendChild(new Listcell(t.getDescricao()));
        lstm.appendChild(new Listcell(t.getFaculdade().getDesricao()));
        
    }
    
}
