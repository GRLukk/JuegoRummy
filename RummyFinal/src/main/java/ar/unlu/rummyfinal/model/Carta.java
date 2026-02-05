package ar.unlu.rummyfinal.model;

import java.util.ArrayList;

public class Carta {
    private Palo palo;
    private int num; // 1 (As), 2-10, 11 (J), 12 (Q), 13 (K), comodin(0).
    private int valorTemporal = 0;

    public Carta(Palo palo, int num) {
        this.palo = palo;
        this.num = num;
    }

    public boolean esComodin() {
        return this.palo == Palo.COMODIN;
    }

    //GETTERS Y SETTERS

    public int getNum() {
        return num;
    }

    public Palo getPalo() {
        return palo;
    }

    public int getPuntos() {
        if (esComodin()) {
            return 25;
        }

        if (this.num == 1) {
            return 15;
        }

        if (this.num >= 11 && this.num <= 13) {
            return 10;
        }

        return this.num;
    }

    public void setValorTemporal(int v) {
        this.valorTemporal = v;
    }

    public int getValorTemporal() {
        return this.valorTemporal == 0 ? getNum() : this.valorTemporal;
    }

    public static void resetValoresTemporales(ArrayList<Carta> cartas) {
        for (Carta c : cartas) {
            c.setValorTemporal(0);
        }
    }

    @Override
    public String toString() {
        if (esComodin()) {
            return "[ COMODIN ]";
        }

        String valorComoTexto;
        switch (this.num) {
            case 1:
                valorComoTexto = "As";
                break;
            case 11:
                valorComoTexto = "J";
                break;
            case 12:
                valorComoTexto = "Q";
                break;
            case 13:
                valorComoTexto = "K";
                break;
            default:
                valorComoTexto = String.valueOf(this.num);
        }

        return valorComoTexto + " de " + this.palo.toString();
    }
}