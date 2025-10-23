package com.logsoluprobl.appbank.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT); 

    /**
     * Convierte un objeto Java en su representación JSON como String.
     * @param object Objeto a convertir.
     * @return Cadena JSON resultante.
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir objeto a JSON", e);
        }
    }

    /**
     * Guarda un objeto en un archivo JSON.
     * @param object Objeto a guardar.
     * @param file   Archivo de destino.
     */
    public static void saveToFile(Object object, File file) {
        try {
            objectMapper.writeValue(file, object);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo JSON", e);
        }
    }

    /**
     * Lee un objeto desde un archivo JSON.
     * @param file Archivo de origen.
     * @param clazz Clase del objeto esperado.
     * @param <T> Tipo genérico.
     * @return Objeto deserializado.
     */
    public static <T> T readFromFile(File file, Class<T> clazz) {
        try {
            if (!file.exists()) {
                return null;
            }
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON", e);
        }
    }

    /**
     * Lee una lista de objetos desde un archivo JSON.
     * @param file Archivo de origen.
     * @param clazz Clase de los objetos que contiene la lista.
     * @param <T> Tipo genérico.
     * @return Lista de objetos deserializados.
     */
    public static <T> List<T> readListFromFile(File file, Class<T> clazz) {
        try {
            if (!file.exists()) {
                return List.of();
            }
            return objectMapper.readValue(
                    file,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la lista desde archivo JSON", e);
        }
    }
}
