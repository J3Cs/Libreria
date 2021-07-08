/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j3cs.test.library.beans.converter;

import com.j3cs.test.datalib.Autor;
import java.math.BigDecimal;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "AutorConverter")
public class AutorConverter implements Converter {

    private final static String DELIMITER = "~";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        String[] parts = value.split(DELIMITER);
        Autor result = new Autor();
        char[] sexo = parts[3].toCharArray();
        result.setIdAutor(BigDecimal.valueOf(Integer.valueOf(parts[0])));
        result.setNacionalidad(parts[1]);
        result.setNombre(parts[2]);
        result.setSexo(sexo[0]);
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (!(value instanceof Autor)) {
            throw new RuntimeException(String.format("Inconvertable type: %s of value '%s'", value.getClass(), value));
        }

        Autor autor = (Autor) value;
        StringBuilder sb = new StringBuilder();
        sb
                .append(autor.getIdAutor())
                .append(DELIMITER)
                .append(autor.getNacionalidad())
                .append(DELIMITER)
                .append(autor.getNombre())
                .append(DELIMITER)
                .append(autor.getSexo());

        String result = new String(sb);

        return result;
    }
}
