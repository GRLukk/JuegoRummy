package ar.unlu.rummyfinal.model;

import ar.unlu.rummyfinal.observer.Observable;
import ar.unlu.rummyfinal.observer.Observador;

import java.util.ArrayList;
import java.util.List;

public class Rummy implements Observable {
    private final List<Jugador> jugadores;
    private final List<Observador> observadores;
    private ArrayList<Carta> pilaDescarte;
    private Mazo mazo;
    private int indiceJugadorActual;
    private Jugador ganador;
    private boolean juegoTerminado = false;
    private Modalidad modalidad;
    private int puntajeLimite;

    public Rummy(List<Jugador> nombres) {
        this.jugadores = nombres;
        this.observadores = new ArrayList<>();
        this.mazo = new Mazo();
        this.pilaDescarte = new ArrayList<>();
        this.indiceJugadorActual = 0;
        this.ganador = null;
    }

    //----- LOGICA DE INICIO/FIN PARTIDA -----

    public void configurarPartida(Modalidad modo, int limitePuntos) {
        this.modalidad = modo;
        this.puntajeLimite = limitePuntos;
        this.juegoTerminado = false;
        iniciarRonda();
    }

    public void iniciarRonda() {
        this.mazo = new Mazo(); // Mazo nuevo y fresco
        this.pilaDescarte.clear();
        this.indiceJugadorActual = 0;

        for(Jugador j : jugadores) {
            j.resetearMano();
        }

        if (this.modalidad != Modalidad.TRAMPA) {
            this.mazo.barajar();
        }

        int cartasARepartir = (this.jugadores.size() == 2) ? 10 : 7;
        for (Jugador j : this.jugadores) {
            for (int i = 0; i < cartasARepartir; i++) {
                j.addCarta(this.mazo.sacarCarta());
            }
        }
        this.pilaDescarte.add(this.mazo.sacarCarta());

        notificarObservadores(Evento.INICIO_PARTIDA);
    }

    private void procesarCorte(Jugador cortador) {
        boolean esRummy = !cortador.haBajadoEnTurnosAnteriores(); //si no bajó en otros turnos y entra en este método, es porque hizo rummy
        int multiplicador = esRummy ? 2 : 1;

        if (esRummy) {
            notificarObservadores(Evento.RUMMY_DETECTADO);
        }

        for (Jugador j : jugadores) { //calcula puntos con el multiplicador
            if (j != cortador) {
                int puntosMano = j.calcularPuntosEnMano();
                j.sumarPuntos(puntosMano * multiplicador);
            }
        }

        if (modalidad == Modalidad.PUNTOS) { //verifica fin de juego o ronda
            verificarLimiteDePuntos();
        } else {
            this.ganador = cortador;
            this.juegoTerminado = true;
            notificarObservadores(Evento.JUEGO_TERMINADO);
        }
    }

    private void verificarLimiteDePuntos() {
        boolean limiteAlcanzado = false;
        for (Jugador j : jugadores) {
            if (j.getPuntajeAcumulado() >= puntajeLimite) {
                limiteAlcanzado = true;
                break;
            }
        }

        if (limiteAlcanzado) { //gana el jugador con menos puntos
            this.ganador = getJugadorConMenosPuntos();
            this.juegoTerminado = true;
            notificarObservadores(Evento.JUEGO_TERMINADO);
        } else {
            notificarObservadores(Evento.FIN_DE_RONDA);
        }
    }

    //----- LOGICA DE TURNO -----
    public void siguienteTurno() {
        avanzarTurno();
        notificarObservadores(Evento.TURNO_CAMBIADO);
    }

    private void avanzarTurno() {
        if (juegoTerminado){
            return;
        }
        if (true) { //voy a suponer que siempre se va a querer ir en sentido horario
            indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
        } else {
            indiceJugadorActual = (indiceJugadorActual - 1 + jugadores.size()) % jugadores.size();
        }
    }

    private void reciclarPilaDescarte() {
        if (this.pilaDescarte.size() <= 1) { return; }
        Carta topePozo = this.pilaDescarte.removeLast();

        this.mazo.recargar(new ArrayList<>(this.pilaDescarte));
        this.pilaDescarte.clear();
        this.pilaDescarte.add(topePozo);
        this.mazo.barajar();
        notificarObservadores(Evento.MAZO_RECICLADO);
    }

    //----- LOGICA DE ROBAR DEL POZO/MAZO -----
    public Carta robarDelMazo() {
        if (this.mazo.estaVacio()) {
            reciclarPilaDescarte(); //pregunta si le quedan cartas
        }

        if (this.mazo.estaVacio()) {
            return null;
        }

        Carta carta = this.mazo.sacarCarta();
        Jugador actual = this.jugadores.get(this.indiceJugadorActual);
        actual.addCarta(carta);

        notificarObservadores(Evento.CARTA_ROBADA);

        return carta;
    }

    public Carta robarDelPozo() {
        if (this.pilaDescarte.isEmpty()) return null;

        Carta carta = this.pilaDescarte.removeLast();
        this.jugadores.get(this.indiceJugadorActual).addCarta(carta);

        notificarObservadores(Evento.CARTA_ROBADA);
        return carta;
    }

    //----- LOGICA DE BAJAR, DESCARTAR Y ENGANCHAR -----
    public boolean bajarJugada(ArrayList<Integer> indices) {
        Jugador jugadorActual = getJugadorEnTurno();
        ArrayList<Carta> mano = jugadorActual.getMano();
        ArrayList<Carta> cartasParaJugada = new ArrayList<>();
        ArrayList<Integer> indicesUsados = new ArrayList<>(); //para evitar duplicados

        //acá tendria que considerar poner la lógica de no repetir indices

        for (int indice : indices) {
            if (indice < 0 || indice >= mano.size() || indicesUsados.contains(indice)) {
                return false;
            }
            cartasParaJugada.add(mano.get(indice));
            indicesUsados.add(indice);
        }

        // 1. Intentamos ver si es una Escalera
        Jugada posibleJugada = null;
        Pierna intentoEscalera = new Pierna();
        intentoEscalera.setCartas(new ArrayList<>(cartasParaJugada)); // Copia para no afectar referencias
        if (intentoEscalera.esValida()) {
            posibleJugada = intentoEscalera;
        }
        else {
            // 2. Si no es escalera, intentamos ver si es Pierna
            Escalera intentoPierna = new Escalera();
            intentoPierna.setCartas(new ArrayList<>(cartasParaJugada));
            if (intentoPierna.esValida()) {
                posibleJugada = intentoPierna;
            }
        }

        // 3. Si encontramos alguna válida, procedemos
        if (posibleJugada != null) {
            // Lógica de remover cartas de la mano (Tu lógica original estaba bien aquí)
            for (int i = indices.size() - 1; i >= 0; i--) {
                jugadorActual.descartarCarta(indices.get(i));
                // Ojo: verifica que tu método descartarCarta use el índice correctamente
            }

            // Agregamos la jugada YA creada (que es Escalera o Pierna)
            jugadorActual.addJugadaBajada(posibleJugada);

            // Es importante agregarla también a la lista global de jugadas en mesa si la tienes
            this.getAllJugadasEnMesa().add(posibleJugada);

            notificarObservadores(Evento.JUGADA_BAJADA);
            return true;
        }

        return false; // No formaba ni escalera ni pierna
    }

    public boolean descartar(int indice) {
        Jugador actual = this.jugadores.get(this.indiceJugadorActual);
        Carta cartaDescartada = actual.descartarCarta(indice);
        if (cartaDescartada == null) return false;

        notificarObservadores(Evento.CARTA_DESCARTADA);
        this.pilaDescarte.add(cartaDescartada);

        if (actual.getCantidadCartas() == 0) {
            procesarCorte(actual);
        } else {
            if (!actual.getJugadasBajadas().isEmpty()) {
                actual.marcarQueYaBajoCartas();
            }
            siguienteTurno();
        }

        return true;
    }

    public boolean engancharCarta(int indiceJugadaGlobal, int indiceCartaMano) {
        Jugador actual = getJugadorActual();
        List<Jugada> jugadasEnMesa = getAllJugadasEnMesa();

        if (indiceJugadaGlobal < 0 || indiceJugadaGlobal >= jugadasEnMesa.size()) return false; //valida indices

        Carta cartaAEnganchar = actual.getCartaEnMano(indiceCartaMano);
        if (cartaAEnganchar == null) return false;

        Jugada jugadaObjetivo = jugadasEnMesa.get(indiceJugadaGlobal);

        if (jugadaObjetivo.intentarEnganchar(cartaAEnganchar)) {
            actual.getMano().remove(cartaAEnganchar);

            if (actual.getCantidadCartas() == 0) {
                procesarCorte(actual);
            } else {
                notificarObservadores(Evento.JUGADA_BAJADA);
            }
            return true;
        }

        return false;
    }

    //SETTERS Y GETTERS
    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }

    public Jugador getJugadorEnTurno() {
        if (jugadores.isEmpty()) {
            return null;
        }
        return this.jugadores.get(this.indiceJugadorActual);
    }

    public List<Jugada> getAllJugadasEnMesa() {
        List<Jugada> todas = new ArrayList<>();
        for (Jugador j : jugadores) {
            todas.addAll(j.getJugadasBajadas());
        }
        return todas;
    }

    private Jugador getJugadorConMenosPuntos() {
        Jugador mejor = jugadores.getFirst();
        for(Jugador j : jugadores) {
            if(j.getPuntajeAcumulado() < mejor.getPuntajeAcumulado()) {
                mejor = j;
            }
        }
        return mejor;
    }

    public Carta getTopePozo() {
        if (this.pilaDescarte.isEmpty()) {
            return null;
        }
        return this.pilaDescarte.getLast();
    }

    public Jugador getJugadorActual() {
        return jugadores.get(indiceJugadorActual);
    }

    public List<Jugador> getJugadores() {
        return this.jugadores;
    }

    public int getCantidadCartasMazo() {
        return mazo.getCantidadCartas();
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    @Override
    public void agregarObservador(Observador o){
        observadores.add(o);
    }

    public void quitarObservador(Observador o){
        observadores.remove(o);
    }

    public void notificarObservadores(Evento evento){
        for (Observador observador : observadores) {
            observador.actualizar(evento);
        }
    }
}
