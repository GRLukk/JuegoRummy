package ar.unlu.rummyfinal.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Escalera extends Jugada {
    @Override
    public boolean esValida() {
        if (this.cartas.size() < 3) { return false; }

        ArrayList<Carta> cartasNormales = new ArrayList<>();
        int cantComodines = 0;
        Palo paloDeReferencia = null;

        for (Carta c : this.cartas) { //separar comodines y cartas normales, validar el palo
            if (c.esComodin()) {
                cantComodines++;
            } else {
                if (paloDeReferencia == null) {
                    paloDeReferencia = c.getPalo();
                }
                if (c.getPalo() != paloDeReferencia) {
                    return false;
                }
                cartasNormales.add(c);
            }
        }

        if (cartasNormales.isEmpty()) {
            return true;
        }

        cartasNormales.sort(Comparator.comparingInt(Carta::getNum)); //As=1, K=13

        boolean asEsAlto = false; //manejar el caso especial del As (A-K-Q)
        if (cartasNormales.getFirst().getNum() == 1 && cartasNormales.getLast().getNum() == 13) {
            asEsAlto = true;
            Carta as = cartasNormales.removeFirst(); //muevo el As al final
            as.setValorTemporal(14); //seteo su valor temporal en 14
            cartasNormales.add(as); //lo pongo al final
        }

        for (int i = 0; i < cartasNormales.size() - 1; i++) {
            int val1 = asEsAlto ? cartasNormales.get(i).getValorTemporal() : cartasNormales.get(i).getNum();
            int val2 = asEsAlto ? cartasNormales.get(i + 1).getValorTemporal() : cartasNormales.get(i + 1).getNum();

            int diferencia = val2 - val1;

            if (diferencia == 1) {
                continue; //son consecutivas
            }

            if (diferencia > 1) { //hay huecos, busca rellenarlos con comodines
                int comodinesNecesarios = diferencia - 1;
                if (cantComodines >= comodinesNecesarios) {
                    cantComodines -= comodinesNecesarios;
                } else {
                    if (asEsAlto) Carta.resetValoresTemporales(cartasNormales);
                    return false;
                }
            } else if (diferencia == 0) { //cartas duplicadas
                if (asEsAlto) Carta.resetValoresTemporales(cartasNormales);
                return false;
            }
        }

        if (asEsAlto) Carta.resetValoresTemporales(cartasNormales);
        return true;
    }


}
