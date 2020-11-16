/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.relatorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Meneses
 */
public class Notas {
    
    
    List<String> notasEx;

    public Notas() {
        this.notasEx = Arrays.asList("Reprovado","(Um Valor)","(Dois Valores)","(Tres Valores)","(Quatro Valores)",
                "(Cinco Valores)","(Seis Valores)","(Sete Valores)","(Oito Valores)","(Nove Valores)","(Dez Valores)","(Onze Valores)",
                "(Doze Valores)","(Treze Valores)","(Catorze Valores)","(Quinze Valores)","(Dezasseis Valores)","(Dezassete Valores)",
                "(Dezoito Valores)","(Dezanove Valores)","(Vinte Valores)");
    }

    public List<String> getNotasEx() {
        return notasEx;
    }

    public void setNotasEx(List<String> notasEx) {
        this.notasEx = notasEx;
    }
    
    
    
    
    
}
