package ar.unlu.rummyfinal;

import ar.unlu.rummyfinal.controller.Controlador;
import ar.unlu.rummyfinal.model.*;
import ar.unlu.rummyfinal.view.*;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    public static void main(String[] args) {
        ArrayList<String> seleccion = new ArrayList<>();
        IVista vista = null;

        seleccion.add("Consola");
        seleccion.add("Ventana Grafica");
        String visualizacion = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el modo de vista que desea", "Modo de visualización",
                JOptionPane.QUESTION_MESSAGE,
                null,
                seleccion.toArray(),
                null
        );
        if (visualizacion.equals("Ventana Grafica")){
            vista = new VistaGrafica();
        } else{
            vista = new VistaConsola();
        }
        //VistaConsola vista = new VistaConsola();
        List<Jugador> nombres = vista.pedirDatosJugadores();
        Rummy juego = new Rummy(nombres);
        Controlador controlador = new Controlador(juego, vista);
        controlador.iniciarPartida();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
