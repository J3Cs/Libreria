/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.facade;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author juliocardona
 */
public interface GenericInterface<T> {
    public boolean save(T etity);
    public boolean edit(T entity);
    public boolean delete(T entity);
    public List<T> findAll();
}
