package com.uax.notificaciones.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "notificaciones")
public class Notificacion {

    @Id
    private String id;

    private String usuario;
    private String mensaje;
    private String tipo; // "INFO", "ALERTA", "URGENTE"
    private Date fecha = new Date(); // Fecha actual por defecto
    private Boolean leido = false;   // No leído por defecto

    // Constructor conveniente para crear notificaciones rápido
    public Notificacion(String usuario, String mensaje, String tipo) {
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }
}