/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.facade;

import com.j3cs.test.datalib.Usuario;
import javax.ejb.Local;

/**
 *
 * @author juliocardona
 */
@Local
public interface UsuarioLocal extends GenericInterface<Usuario> {
     
    
}
