package ar.unlu.rummyfinal.model;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;
    private ArrayList<Jugada> jugadasBajadas;
    private int puntajeAcumulado; //para las cartas no ligadas
    private boolean bajoEnTurnosAnteriores;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.jugadasBajadas = new ArrayList<>();
    }

    //METODOS DE CARTAS SOBRE EL JUGADOR

    public void sumarPuntos(int puntos) {
        this.puntajeAcumulado += puntos;
    }

    public int calcularPuntosEnMano() {
        int suma = 0;
        for (Carta c : this.mano) {
            suma += c.getPuntos();
        }
        return suma;
    }

    public void resetearMano() {
        this.mano.clear();
        this.jugadasBajadas.clear();
        this.bajoEnTurnosAnteriores = false;
    }

    public Carta descartarCarta(int indice) {
        if (indice >= 0 && indice < this.mano.size()) {
            return this.mano.remove(indice);
        } else {
            return null;
        }
    }

    public void addCarta(Carta newCarta){
        this.mano.add(newCarta);
    }

    public void addJugadaBajada(Jugada jugada) {
        this.jugadasBajadas.add(jugada);
    }

    public void removerCartasDeMano(List<Carta> cartas) {
        this.mano.removeAll(cartas);
    }

    //METODOS POR SOBRE EL JUGADOR
    public boolean haBajadoEnTurnosAnteriores() {
        return bajoEnTurnosAnteriores;
    }

    public void marcarQueYaBajoCartas() {
        if (!this.jugadasBajadas.isEmpty()) {
            this.bajoEnTurnosAnteriores = true;
        }
    }

    public boolean sinCartas() {
        return mano.isEmpty();
    }

    //GETTERS Y SETTERS
    public int getCantidadCartas() {
        return this.mano.size();
    }

    public int getPuntajeAcumulado() {
        return puntajeAcumulado;
    }

    public ArrayList<Carta> getMano() {
        return this.mano;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Jugada> getJugadasBajadas() {
        return this.jugadasBajadas;
    }

    public Carta getCartaEnMano(int indice) {
        // Validamos que el índice esté dentro del rango real de la lista (0 a size-1)
        if (indice >= 0 && indice < this.mano.size()) {
            return this.mano.get(indice);
        }
        // Si el índice no es válido, retornamos null para que quien llame sepa que hubo error
        return null;
    }
}
