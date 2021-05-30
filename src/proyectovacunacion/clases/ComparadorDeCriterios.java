/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectovacunacion.clases;

import java.util.Comparator;

/**
 *
 * @author danie
 */
public class ComparadorDeCriterios implements Comparator<Criterio> {

    @Override
    public int compare(Criterio criterio1, Criterio criterio2) {
        return criterio1.getPrioridad() - criterio2.getPrioridad();
    }
}
