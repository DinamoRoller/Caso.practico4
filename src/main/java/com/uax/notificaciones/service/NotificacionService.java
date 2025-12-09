package com.uax.notificaciones.service;

import com.uax.notificaciones.model.Notificacion;
import com.uax.notificaciones.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

   
    private final Sinks.Many<Notificacion> sink = Sinks.many().multicast().onBackpressureBuffer();

    public Mono<Notificacion> addNotificacion(Notificacion n) {
        return repository.save(n)
                .doOnSuccess(saved -> {
                    // Empujamos la notificación al "canal de radio" (Sink)
                    sink.tryEmitNext(saved);
                });
    }

   
    public Flux<Notificacion> getNotificacionesEnTiempoReal(String usuario) {
   
        Flux<Notificacion> historico = repository.findByUsuario(usuario);

    
        Flux<Notificacion> enVivo = sink.asFlux()
                .filter(n -> n.getUsuario().equals(usuario));

       
        return Flux.concat(historico, enVivo);
    }

    public Mono<Notificacion> marcarLeido(String id) {
        return repository.findById(id)
                .flatMap(n -> {
                    n.setLeido(true);
                    return repository.save(n);
                });
    }

    public Mono<Void> eliminarNotificacion(String id) {
        return repository.deleteById(id);
    }

    public Flux<Notificacion> filtrarPorTipo(String usuario, String tipo) {
        return repository.findByUsuarioAndTipo(usuario, tipo);
    }
}
