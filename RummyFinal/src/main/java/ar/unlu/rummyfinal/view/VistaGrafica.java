package ar.unlu.rummyfinal.view;

import ar.unlu.rummyfinal.model.Carta;
import ar.unlu.rummyfinal.model.Jugada;
import ar.unlu.rummyfinal.model.Jugador;

import java.util.List;
import java.util.Scanner;

public class VistaGrafica implements IVista {
    private Scanner sc;

    public VistaGrafica() {
        this.sc = new Scanner(System.in);
    }

    @Override
    public List<Jugador> pedirDatosJugadores() {
        return List.of();
    }

    @Override
    public int pedirPuntajeLimite() {
        return 0;
    }

    @Override
    public int pedirModoJuego() {
        return 0;
    }

    @Override
    public void mostrarEstadoJuego(Carta topePozo, int cantidadCartasMazo, String juegosBajadosString, Jugador actual) {

    }

    @Override
    public int pedirOpcionRobar() {
        return 0;
    }

    @Override
    public int pedirOpcionDescarte() {
        return 0;
    }

    @Override
    public void limpiarConsola() {

    }

    @Override
    public void mostrarMensaje(String s) {

    }

    @Override
    public String pedirIndicesCartasBajada(int cantidadCartas) {
        return "";
    }

    @Override
    public int pedirIndiceCartaDescarte(int cantidadCartas) {
        return 0;
    }

    @Override
    public int pedirIndiceJugadaObjetivo(List<Jugada> jugadasEnMesa) {
        return 0;
    }

    @Override
    public void mostrarPuntajesParciales(List<Jugador> jugadores) {

    }
}
