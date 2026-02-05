package ar.unlu.rummyfinal.view;

import ar.unlu.rummyfinal.model.Carta;
import ar.unlu.rummyfinal.model.Jugada;
import ar.unlu.rummyfinal.model.Jugador;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VistaConsola implements IVista {
    private Scanner sc;

    public VistaConsola() {
        this.sc = new Scanner(System.in);
    }

    //----- PEDIRES con validación try-catch -----

    private int pedirCantidadJugadores() {
        mostrarMensaje("Ingrese cantidad de jugadores a participar [2-4]: ");

        int n;
        while (true) {
            try {
                n = Integer.parseInt(sc.nextLine());
                if (n >= 2 && n <= 5) return n;
                mostrarMensaje("Debe ser entre 2 y 4 jugadores. Intente de nuevo: ");
            } catch (NumberFormatException e) {
                mostrarMensaje("Entrada inválida. Intente de nuevo: ");
            }
        }
    }

    public List<Jugador> pedirDatosJugadores(){
        mostrarMensaje("=============================================");
        mostrarMensaje("== BIENVENIDO AL JUEGO RUMMY (Consola) ==");
        mostrarMensaje("=============================================");

        int n = pedirCantidadJugadores();

        List<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            String nombre = pedirNombreJugador(i);
            jugadores.add(new Jugador(nombre));
        }
        return jugadores;
    }

    private String pedirNombreJugador(int numeroJugador) {
        System.out.print("Nombre del jugador " + numeroJugador + ": ");
        String nombre = sc.nextLine().trim();
        String patron = "^[a-zA-Z]{1,15}$";

        while (!nombre.matches(patron)) {
            mostrarMensaje("Nombre inválido. Debe contener solo letras, entre 1 y 15 caracteres. Intente nuevamente: ");
            nombre = sc.nextLine().trim();
        }

        return nombre;
    }

    public int pedirModoJuego(){
        mostrarMensaje("Ingrese el modo en el que quiere jugar ");
        return pedirNumero("[1: Trampa | 2: Expres | 3: Puntos]: ", 1, 3);
    }

    public int pedirPuntajeLimite() {
        return pedirNumero("Ingrese el puntaje límite para terminar el juego (ej: 100) [1-1000]: ", 1, 1000);
    }

    private int pedirNumero(String mensaje, int min, int max) {
        while (true) {
            mostrarMensaje(mensaje);
            try {
                String input = sc.nextLine();
                int opcion = Integer.parseInt(input);

                if (opcion >= min && opcion <= max) {
                    return opcion; // El número es válido
                } else {
                    System.out.println(">>> Error: Opción fuera de rango. Debe ser entre " + min + " y " + max + ".");
                }
            } catch (NumberFormatException e) {
                mostrarMensaje(">>> Error: Ingrese un número válido.");
            }
        }
    }

    public int pedirOpcionRobar() {
        mostrarMensaje("\nElige una acción para robar:");
        mostrarMensaje("1. Robar del Mazo");
        mostrarMensaje("2. Robar del Pozo (Descarte)");
        return pedirNumero("Opción: ", 1, 2);
    }

    public int pedirIndiceCartaDescarte(int cantidadCartas) {
        Scanner sc = new Scanner(System.in);
        int indice;

        while (true) {
            System.out.print("Ingresa el índice de la carta a descartar (1 a "
                    + cantidadCartas + "): ");

            if (sc.hasNextInt()) {
                indice = sc.nextInt();

                if (indice >= 1 && indice <= cantidadCartas) {
                    return indice;  // ✔ válido
                } else {
                    mostrarMensaje(">>> Error: índice fuera de rango.");
                }
            } else {
                mostrarMensaje(">>> Error: debes ingresar un número.");
                sc.next(); //limpiar entrada inválida
            }
        }
    }

    public String pedirIndicesCartasBajada(int cantCartas) {
        mostrarMensaje("Ingresa los índices de las cartas separados por coma (ej: 1,2,3):");
        String inputIndices = sc.nextLine();

        try {
            //limpia espacios
            String inputLimpio = inputIndices.replaceAll("\\s+", "");

            //separa por comas
            String[] indicesStr = inputLimpio.split(",");

            //que cada parte sea un número y esté en rango
            for (String s : indicesStr) {
                if (s.isEmpty())
                    throw new NumberFormatException();

                int valor = Integer.parseInt(s);
                if (valor < 1 || valor > cantCartas)
                    throw new IndexOutOfBoundsException();

            }
            return inputLimpio;

        } catch (NumberFormatException e) {
            mostrarMensaje("ERROR: Debes ingresar solo números separados por comas.");
        } catch (IndexOutOfBoundsException e) {
            mostrarMensaje("ERROR: Algún índice está fuera del rango permitido (0 a " + cantCartas + ").");
        } catch (Exception e) {
            mostrarMensaje("ERROR: Entrada inválida.");
        }
        return null;
    }

    public int pedirOpcionDescarte() {
        mostrarMensaje("\nElige una acción:");
        mostrarMensaje("1. Bajar juego");
        mostrarMensaje("2. Descartar y terminar turno");
        mostrarMensaje("3. Enganchar juego a jugada existente");
        return pedirNumero("Opción: ", 1, 3);
    }

    public int pedirIndiceJugadaObjetivo(List<Jugada> jugadasEnMesa) {
        mostrarMensaje("\n--- SELECCIONA DÓNDE ENGANCHAR ---");
        for (int i = 0; i < jugadasEnMesa.size(); i++) {
            System.out.println( (i + 1) + ". " + jugadasEnMesa.get(i).toString());
        }

        return pedirNumero("Ingresa el número de la jugada destino: ", 1, jugadasEnMesa.size());
    }

    //----- MOSTRARES -----

    public void mostrarPuntajesParciales(List<Jugador> jugadores) {
        mostrarMensaje("\n=== FIN DE LA RONDA - TABLA DE PUNTOS ===");
        for (Jugador j : jugadores) {
            System.out.println(j.getNombre() + ": " + j.getPuntajeAcumulado() + " puntos.");
        }
        mostrarMensaje("Presione Enter para comenzar la siguiente ronda...");
        sc.nextLine();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarMano(Jugador jugador) {
        List<Carta> mano = jugador.getMano();
        for (int i = 0; i < mano.size(); i++) {
            System.out.println((i + 1) + ") " + mano.get(i));
        }
    }

    public void mostrarEstadoJuego(Carta topePozo, int cantidadCartasMazo, String jugadasEnMesa, Jugador jugadorActual) {
        mostrarMensaje("\n-----------------------------------------");
        mostrarMensaje("           ESTADO DE LA MESA:");

        if (topePozo != null) {
            mostrarMensaje("Pozo de descarte (visible): " + topePozo);
        } else {
            mostrarMensaje("Pozo de descarte (visible): [Vacío]");
        }

        mostrarMensaje("Cartas restantes en Mazo: " + cantidadCartasMazo);

        // Asumimos que jugadasEnMesa ya viene formateado como String,
        // o podrías pasar una List<Juego> e iterarla aquí.
        mostrarMensaje(jugadasEnMesa);

        mostrarMensaje("-----------------------------------------");

        mostrarMensaje(jugadorActual.haBajadoEnTurnosAnteriores() ? "(Ya abrió)" : "(Busca Rummy)");
        mostrarMensaje("--- TURNO DE: " + jugadorActual.getNombre() + " ---");

        // La vista se encarga de mostrar la mano del jugador que recibe
        mostrarMano(jugadorActual);
    }

    public void limpiarConsola() {
        mostrarMensaje("\n--- FIN DEL TURNO ---");
        mostrarMensaje("Presiona Enter para pasar al siguiente jugador...");

        if (sc.hasNextLine()) {
            sc.nextLine();
        }

        for (int i = 0; i < 50; i++) {
            mostrarMensaje("");
        }

        mostrarMensaje("¡Turno del siguiente jugador!");
    }
    //
}