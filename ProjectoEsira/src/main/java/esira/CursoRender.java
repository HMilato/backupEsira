/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira;

import esira.domain.Curso;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Label;

/**
 *
 * @author Administrator
 */
public class CursoRender implements ComboitemRenderer<Curso> {

    public void render(Comboitem cmbtm, Curso c, int i) throws Exception {
      cmbtm.setValue(c);
      cmbtm.appendChild(new Label(c.getDescricao()));
    }
    
}
