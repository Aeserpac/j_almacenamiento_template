package com.uninorte;

import java.util.Arrays;

public class Registro {
    private Object[] datos;

    public Registro(Object... datos) {
        this.datos = datos;
    }

    public Object[] getDatos() {
        return datos;
    }

    public void setDatos(Object... nuevosDatos) {
        this.datos = nuevosDatos;
    }

    @Override
    public String toString() {
        return Arrays.toString(datos).substring(1, Arrays.toString(datos).length() - 1).replace(", ", " | ");
    }
}