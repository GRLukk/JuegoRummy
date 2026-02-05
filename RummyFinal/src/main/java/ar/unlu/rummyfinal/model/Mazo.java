package ar.unlu.rummyfinal.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private ArrayList<Carta> cartas;

    public Mazo() {
        this.cartas = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (Palo palo : Palo.values()) {
                if (palo == Palo.COMODIN) {
                    continue;
                }
                for (int valor = 1; valor <= 13; valor++) {
                    this.cartas.add(new Carta(palo, valor));
                }
            }
        }
        this.cartas.add(new Carta(Palo.COMODIN, 0));
        this.cartas.add(new Carta(Palo.COMODIN, 0));
    }

    public void barajar() {
        Collections.shuffle(this.cartas);
    }

    public Carta sacarCarta() {
        if (!this.cartas.isEmpty()) {
            return this.cartas.removeFirst();
        }
        return null;
    }

    public void recargar(List<Carta> cartasViejas) {
        this.cartas.addAll(cartasViejas);
    }

    //GETTERS Y SETTERS
    public int getCantidadCartas() {
        return this.cartas.size();
    }

    public boolean estaVacio() {
        return this.cartas.isEmpty();
    }
}
