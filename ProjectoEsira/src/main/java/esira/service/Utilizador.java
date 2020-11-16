/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Ussimane
 */
public class Utilizador extends User{
    
    private String esquema;

    public Utilizador(String username, String password, Collection<? extends GrantedAuthority> authorities, String esquema) {
        super(username, password, authorities);
        this.esquema = esquema;
    }

    public String getEsquema() {
        return esquema;
    }

    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }
    
    
}
