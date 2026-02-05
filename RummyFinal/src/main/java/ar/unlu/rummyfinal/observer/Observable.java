package ar.unlu.rummyfinal.observer;

import ar.unlu.rummyfinal.model.Evento;

public interface Observable {
    void agregarObservador(Observador observador);
    void quitarObservador(Observador observador);
    void notificarObservadores(Evento evento);
}
