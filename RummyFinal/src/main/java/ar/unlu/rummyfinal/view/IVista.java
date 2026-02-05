package ar.unlu.rummyfinal.view;

import ar.unlu.rummyfinal.model.Carta;
import ar.unlu.rummyfinal.model.Jugada;
import ar.unlu.rummyfinal.model.Jugador;

import java.util.List;

public interface IVista {
    List<Jugador> pedirDatosJugadores();

    int pedirPuntajeLimite();

    int pedirModoJuego();

    void mostrarEstadoJuego(Carta topePozo, int cantidadCartasMazo, String juegosBajadosString, Jugador actual);

    int pedirOpcionRobar();

    int pedirOpcionDescarte();

    void limpiarConsola();

    void mostrarMensaje(String s);

    String pedirIndicesCartasBajada(int cantidadCartas);

    int pedirIndiceCartaDescarte(int cantidadCartas);

    int pedirIndiceJugadaObjetivo(List<Jugada> jugadasEnMesa);

    void mostrarPuntajesParciales(List<Jugador> jugadores);
}
