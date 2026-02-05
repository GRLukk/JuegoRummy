package ar.unlu.rummyfinal.controller;

import ar.unlu.rummyfinal.model.*;
import ar.unlu.rummyfinal.observer.Observador;
import ar.unlu.rummyfinal.view.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controlador implements Observador {
    private IVista vista;
    private Rummy modelo;

    public Controlador(Rummy juego, IVista vista) {
        this.vista = vista;
        this.modelo = juego;
    }

    //----- LOGICA DE INICIO Y PARTIDA -----
    public void iniciarPartida(){
        int opcionModo = vista.pedirModoJuego();
        Modalidad modoSeleccionado;
        int limite = 0;

        switch (opcionModo) {
            case 1: modoSeleccionado = Modalidad.TRAMPA; break;
            case 3:
                modoSeleccionado = Modalidad.PUNTOS;
                limite = vista.pedirPuntajeLimite();
                break;
            default: modoSeleccionado = Modalidad.EXPRES;
        }
        modelo.configurarPartida(modoSeleccionado, limite);

        buclePrincipal();
    }

    private void buclePrincipal() {
        while (!modelo.isJuegoTerminado()) {
            Jugador actual = modelo.getJugadorActual();

            vista.mostrarEstadoJuego(modelo.getTopePozo(), modelo.getCantidadCartasMazo(), getJuegosBajadosString(), actual);
            robarCarta(vista.pedirOpcionRobar());
            vista.mostrarEstadoJuego(modelo.getTopePozo(), modelo.getCantidadCartasMazo(), getJuegosBajadosString(), actual);

            boolean turnoTerminado = false;
            while (!turnoTerminado) {
                int opcion = vista.pedirOpcionDescarte();
                switch (opcion) {
                    case 1:
                        manejarBajadaDeJuego(actual);
                        vista.mostrarEstadoJuego(modelo.getTopePozo(), modelo.getCantidadCartasMazo(), getJuegosBajadosString(), actual);
                        break;
                    case 2:
                        boolean descarteExitoso = manejarDescarte(actual);
                        if (descarteExitoso) {
                            turnoTerminado = true;
                        }
                        vista.limpiarConsola();
                        break;
                    case 3:
                        manejarEngancheJuego();
                        vista.mostrarEstadoJuego(modelo.getTopePozo(), modelo.getCantidadCartasMazo(), getJuegosBajadosString(), actual);
                        break;
                    default:
                        vista.mostrarMensaje("Opción no válida.");
                }
                if (actual.getCantidadCartas() == 0) {
                    modelo.setGanador(actual);
                    turnoTerminado = true;
                }
            }
        }
        vista.mostrarMensaje("=============================================");
        vista.mostrarMensaje("== ¡El juego termino! El ganador es: " + getGanador().getNombre() + " ==");
        vista.mostrarMensaje("=============================================");
        vista.mostrarMensaje("= GRACIAS POR JUGAR AL RUMMY, VUELVA PRONTO =");
    }

    //----- LOGICA DE ROBAR -----
    public void robarCarta(int opcion) {
        Carta cartaRobada;
        try {
            if (opcion == 1) {
                cartaRobada = modelo.robarDelMazo();
            } else {
                cartaRobada = modelo.robarDelPozo();
            }
            vista.mostrarMensaje(">>> Robaste un " + cartaRobada.toString());
        } catch (Exception e) {
            vista.mostrarMensaje(">>> " + e.getMessage());
        }
    }

    //----- LOGICA DE BAJAR/DESCARTAR/ENGANCHAR -----
    public boolean bajarJugada(String inputIndices) {
        ArrayList<Integer> indicesParseados = new ArrayList<>();
        try {
            String inputLimpio = inputIndices.replaceAll("\\s+", "");

            String[] indicesStr = inputLimpio.split(",");

            for (String s : indicesStr) {
                if (!s.isEmpty()) {
                    int indiceUsuario = Integer.parseInt(s);
                    int indiceReal = indiceUsuario - 1;
                    indicesParseados.add(indiceReal);
                }
            }
            Collections.sort(indicesParseados);
            return modelo.bajarJugada(indicesParseados);

        } catch (NumberFormatException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void manejarBajadaDeJuego(Jugador actual) {
        String inputIndices = vista.pedirIndicesCartasBajada(actual.getCantidadCartas());

        boolean exito = bajarJugada(inputIndices);

        if (exito) {
            vista.mostrarMensaje(">>> Jugada bajada correctamente");
        } else {
            vista.mostrarMensaje(">>> Jugada inválida (verifica índices o reglas).");
        }
    }

    private void manejarEngancheJuego() {
        Jugador actual = modelo.getJugadorActual();
        List<Jugada> jugadasEnMesa = modelo.getAllJugadasEnMesa();

        if (jugadasEnMesa.isEmpty()) {
            vista.mostrarMensaje(">>> No hay jugadas en la mesa para enganchar.");
            return;
        }

        vista.mostrarMensaje("Selecciona la carta de tu mano que quieres enganchar: ");
        int indiceCartaUsuario = vista.pedirIndiceCartaDescarte(actual.getCantidadCartas());
        int indiceCartaModelo = indiceCartaUsuario - 1;

        int indiceJugadaUsuario = vista.pedirIndiceJugadaObjetivo(jugadasEnMesa);
        int indiceJugadaModelo = indiceJugadaUsuario - 1;

        boolean exito = modelo.engancharCarta(indiceJugadaModelo, indiceCartaModelo);
        if (exito) {
            vista.mostrarMensaje(">> Carta enganchada exitosamente ");
            if (actual.getCantidadCartas() == 0) {
                actualizar(Evento.JUEGO_TERMINADO);
            }
        } else {
            vista.mostrarMensaje(">>> No se pudo enganchar. La carta no cumple la secuencia o el palo.");
        }
    }

    private boolean manejarDescarte(Jugador actual) {
        int indiceUsuario = vista.pedirIndiceCartaDescarte(actual.getCantidadCartas());
        int indiceModelo = indiceUsuario - 1;

        boolean exito = modelo.descartar(indiceModelo);

        if (exito) {
            vista.mostrarMensaje(">>> Has descartado correctamente.");
            if (actual.getCantidadCartas() == 0) {
                return true;
            }
            return true;
        } else {
            vista.mostrarMensaje(">>> Error: No se pudo descartar la carta. Verifica el índice.");
            return false;
        }
    }

    //GETTERS Y SETTERS
    public String getJuegosBajadosString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- JUGADAS BAJADAS (Mesa) ---\n");

        for (Jugador j : modelo.getJugadores()) {
            if (!j.getJugadasBajadas().isEmpty()) {
                sb.append("  Jugador: ").append(j.getNombre()).append("\n");
                for (Jugada jugada : j.getJugadasBajadas()) {
                    sb.append("    - ").append(jugada.toString()).append("\n");
                }
            }
        }
        return sb.toString();
    }

    private Jugador getGanador() {
        return modelo.getJugadores().stream()
                .filter(Jugador::sinCartas)
                .findFirst()
                .orElse(modelo.getJugadorActual());
    }

    @Override
    public void actualizar(Evento evento) {
        if (evento == Evento.RUMMY_DETECTADO) {
            vista.mostrarMensaje(">>> ¡¡RUMMY!! ¡¡EL JUGADOR BAJÓ TODO DE GOLPE!! (Puntos x2)");
        }
        if (evento == Evento.FIN_DE_RONDA) {
            vista.mostrarPuntajesParciales(modelo.getJugadores());
            modelo.iniciarRonda();
        }
    }
}