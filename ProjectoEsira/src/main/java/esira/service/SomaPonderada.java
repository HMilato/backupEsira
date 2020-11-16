/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ussimane
 */
public class SomaPonderada {

    private List<Integer> qtd;
    private List<Float> soma;
    private List<Integer> tipo;

    public SomaPonderada() {
        qtd = new ArrayList<Integer>();
        soma = new ArrayList<Float>();
        tipo = new ArrayList<Integer>();
    }

    public void add(float s, int t) {
        for (int i = 0; i < tipo.size(); i++) {
            if (t == tipo.get(i)) {
                soma.set(i, s + soma.get(i));
                qtd.set(i, qtd.get(i) + 1);
            }
        }
        qtd.add(1);
        soma.add(s);
        tipo.add(t);
    }

    public Float getMedia() {
        Float media = null;
        media = (soma.get(0) / qtd.get(0))*(tipo.get(0)/100);
        for (int i = 1; i < tipo.size(); i++) {
            media = media + ((soma.get(i) / qtd.get(i))*(tipo.get(i)/100));
        }
        return media;
    }

    public List<Integer> getQtd() {
        return qtd;
    }

    public void setQtd(List<Integer> qtd) {
        this.qtd = qtd;
    }

    public List<Float> getSoma() {
        return soma;
    }

    public void setSoma(List<Float> soma) {
        this.soma = soma;
    }

    public List<Integer> getTipo() {
        return tipo;
    }

    public void setTipo(List<Integer> tipo) {
        this.tipo = tipo;
    }

}
