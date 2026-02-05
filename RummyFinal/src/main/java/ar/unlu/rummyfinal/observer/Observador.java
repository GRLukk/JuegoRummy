package ar.unlu.rummyfinal.observer;

import ar.unlu.rummyfinal.model.Evento;

public interface Observador {
    void actualizar(Evento evento);
}
