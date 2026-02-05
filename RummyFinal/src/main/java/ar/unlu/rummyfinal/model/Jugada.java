package ar.unlu.rummyfinal.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public abstract class Jugada {
    protected ArrayList<Carta> cartas;

    public Jugada() {
        this.cartas = new ArrayList<>();
    }

    protected void ordenar() {
        this.cartas.sort(Comparator.comparingInt(Carta::getNum));
    }

    public abstract boolean esValida();

    public boolean intentarEnganchar(Carta carta) {
        this.cartas.add(carta);

        if (this.esValida()) {
            return true;
        }
        this.cartas.remove(carta);
        return false;
    }

    //GETTERS Y SETTERS
    public void agregarCarta(Carta carta) {
        this.cartas.add(carta);
        ordenar();
    }

    public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas;
        ordenar();
    }

    public List<Carta> getCartas() {
        return this.cartas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Jugada [");
        for (Carta c : cartas) {
            sb.append(c.toString()).append(", ");
        }
        if (!cartas.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");
        return sb.toString();
    }
}