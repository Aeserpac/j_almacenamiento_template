package com.uninorte;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tabla {
    private List<Registro> registros = new ArrayList<>();
    private String estructura;
    private String nombreArchivo;

    public Tabla(String estructura, String nombreArchivo) {
        this.estructura = estructura;
        this.nombreArchivo = nombreArchivo;
        cargarDesdeArchivo();
    }

    public void agregarRegistro(Registro registro) {
        registros.add(registro);
        guardarEnArchivo();
    }

    public Registro obtenerRegistro(int indice) {
        return registros.get(indice);
    }

    public void editarRegistro(int indice, Registro nuevoRegistro) {
        registros.set(indice, nuevoRegistro);
        guardarEnArchivo();
    }

    public void borrarRegistro(int indice) {
        registros.remove(indice);
        guardarEnArchivo();
    }

    public void borrarTodosLosRegistros() {
        registros.clear();
        guardarEnArchivo();
    }

    public int cantidadRegistros() {
        return registros.size();
    }

    public String getEstructura() {
        return estructura;
    }

    public String imprimirTodos(int indiceDeTabla) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < registros.size(); i++) {
            sb.append("ID: ").append(String.format("%03d%03d", indiceDeTabla + 1, i + 1)).append(" ").append(registros.get(i).toString());
            if (i < registros.size() - 1) {
                sb.append("--- ");
            }
        }
        return sb.toString();
    }

    private void cargarDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                registros.add(new Registro((Object[]) parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarEnArchivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (Registro registro : registros) {
                Object[] datos = registro.getDatos();
                for (int i = 0; i < datos.length; i++) {
                    writer.print(datos[i]);
                    if (i < datos.length - 1) {
                        writer.print("|");
                    }
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}