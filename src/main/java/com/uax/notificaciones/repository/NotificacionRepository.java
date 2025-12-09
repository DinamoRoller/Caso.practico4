package com.uax.notificaciones.repository;

import com.uax.notificaciones.model.Notificacion;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NotificacionRepository extends ReactiveMongoRepository<Notificacion, String> {

    Flux<Notificacion> findByUsuario(String usuario);

    Flux<Notificacion> findByUsuarioAndTipo(String usuario, String tipo);
}
