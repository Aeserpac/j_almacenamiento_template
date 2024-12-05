package com.uninorte;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BaseDeDatos {
    private static List<Tabla> tablas = new ArrayList<>();
    private static int contadorTablas = 1;

    //cargar la tabla apenas se cargue la clase BaseDeDatos por primera vez. (Evito tener que crear la instancia en el main)
    static {
        cargarTablasDesdeArchivos();
    }

    public static String AgregarRegistro(Object... datos) {
        String estructura = generarEstructura(datos);

        
        Tabla tabla = encontrarTablaPorEstructura(estructura);

        if (tabla == null) {
            
            tabla = new Tabla(estructura, "tabla_" + String.format("%03d", contadorTablas++) + ".txt");
            tablas.add(tabla);
        }

        
        int registroId = tabla.cantidadRegistros() + 1;
        tabla.agregarRegistro(new Registro(datos));
        return String.format("%03d%03d", tablas.indexOf(tabla) + 1, registroId);
    }

    public static void BorrarTodo() {
        tablas.clear();
        contadorTablas = 1;
        eliminarArchivosDeTablas();
    }

    public static String ImprimirTodo() {
        StringBuilder resultado = new StringBuilder();
        try {
            if (tablas.isEmpty()) {
                return "No hay tablas disponibles.";
            }
    
            for (int i = 0; i < tablas.size(); i++) {
                resultado.append(tablas.get(i).imprimirTodos(i)).append(" ----- ");
            }
    
            if (resultado.length() > 0) {
                resultado.setLength(resultado.length() - " ----- ".length());
            }
        } catch (Exception e) {
            return "Error al intentar imprimir las tablas: " + e.getMessage();
        }
        return resultado.toString();
    }

    public static boolean EditarReg(String rId, Object... nuevosValores) {
        try {
            // Extraer IDs de la tabla y el registro
            int tablaId = Integer.parseInt(rId.substring(0, 3)) - 1;
            int registroId = Integer.parseInt(rId.substring(3, 6)) - 1;
    
            // Verificar que los índices sean válidos
            if (tablaId < 0 || tablaId >= tablas.size()) {
                System.out.println("Tabla no encontrada para el ID: " + rId);
                return false;
            }
    
            Tabla tabla = tablas.get(tablaId);
            if (registroId < 0 || registroId >= tabla.cantidadRegistros()) {
                System.out.println("Registro no encontrado en la tabla: " + rId);
                return false;
            }
    
            // Editar el registro en la tabla
            Registro nuevoRegistro = new Registro(nuevosValores);
            tabla.editarRegistro(registroId, nuevoRegistro);
            return true;
    
        } catch (Exception e) {
            System.out.println("Error al editar el registro: " + e.getMessage());
            return false;
        }
    }
    

    public static boolean BorrarReg(String rId) {
        int tablaId = Integer.parseInt(rId.substring(0, 3)) - 1;
        int registroId = Integer.parseInt(rId.substring(3, 6)) - 1;

        Tabla tabla = tablas.get(tablaId);
        if (tabla != null && registroId < tabla.cantidadRegistros()) {
            tabla.borrarRegistro(registroId);
            return true;
        }
        return false;
    }

    public static int ObtenerNumRegistrosEnTabla(int i) {
        
        if (i <= 0 || i > tablas.size()) {
            return -1; 
        }

        Tabla tabla = tablas.get(i - 1);  
        
        if (tabla == null) 
            return -1;
        

        return tabla.cantidadRegistros();  
        }

    public static Integer ObtenerNumeroTablas() {
        return tablas.size();
    }

    private static Tabla encontrarTablaPorEstructura(String estructura) {
        for (Tabla tabla : tablas) {
            if (tabla.getEstructura().equals(estructura)) {
                return tabla;
            }
        }
        return null;
    }

    private static String generarEstructura(Object... datos) {
        StringBuilder sb = new StringBuilder();
        for (Object dato : datos) {
            sb.append(dato.getClass().getSimpleName()).append(",");
        }
        return sb.toString();
    }

    private static void cargarTablasDesdeArchivos() {
        File dir = new File(".");  // Directorio actual
        File[] archivos = dir.listFiles();  // Listar todos los archivos en el directorio
    
        if (archivos != null) {
            for (File archivo : archivos) {
                // Comprobar si el archivo comienza con "tabla_" y termina con ".txt"
                if (archivo.getName().startsWith("tabla_") && archivo.getName().endsWith(".txt")) {
                    String nombre = archivo.getName();
                    int tablaId = Integer.parseInt(nombre.substring(6, 9));  // Extraer el ID de la tabla
                    Tabla tabla = new Tabla("", nombre);  // Crear la tabla con el nombre del archivo
                    tablas.add(tabla);  // Añadir la tabla a la lista
                }
            }
        }
    }

    private static void eliminarArchivosDeTablas() {
        File dir = new File(".");  
        File[] archivos = dir.listFiles();  
    
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.getName().startsWith("tabla_") && archivo.getName().endsWith(".txt")) {
                    archivo.delete();
                }
            }
        }
    }
}