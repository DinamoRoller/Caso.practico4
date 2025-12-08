package com.uax.notificaciones.repository;

import com.uax.notificaciones.model.Notificacion;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NotificacionRepository extends ReactiveMongoRepository<Notificacion, String> {

    // Método mágico de Spring Data para buscar por usuario
    // Devuelve un Flux (flujo de 0 a N elementos)
    Flux<Notificacion> findByUsuario(String usuario);

    // Método para filtrar por usuario y tipo
    Flux<Notificacion> findByUsuarioAndTipo(String usuario, String tipo);
}