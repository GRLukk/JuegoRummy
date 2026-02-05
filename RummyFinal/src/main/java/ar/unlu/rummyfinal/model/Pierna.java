package ar.unlu.rummyfinal.model;

import java.util.HashSet;

public class Pierna extends Jugada{
    @Override
    public boolean esValida() {

        if (this.cartas.size() < 3 || this.cartas.size() > 4) {
            return false;
        }

        int valorDeReferencia = -1;
        HashSet<Palo> palosUsados = new HashSet<>();

        for (Carta c : this.cartas) { //busca la primera carta que no sea comodín para fijar el valor
            if (!c.esComodin()) {
                valorDeReferencia = c.getNum();
                break;
            }
        }

        if (valorDeReferencia == -1) return true; //significa que son 3-4 comodines = valido

        for (Carta c : this.cartas) {
            if (!c.esComodin()) {
                if (c.getNum() != valorDeReferencia) { //validar que todas tengan el mismo número
                    return false;
                }

                if (palosUsados.contains(c.getPalo())) {
                    return false;
                }
                palosUsados.add(c.getPalo());
            }
        }
        return true;
    }
}
