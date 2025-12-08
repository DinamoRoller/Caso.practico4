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

    // 📡 ESTA ES LA CLAVE DEL TIEMPO REAL:
    // Un 'Sink' actúa como un canal de radio. Nosotros emitimos datos por aquí
    // y todos los navegadores conectados ("suscriptores") reciben el dato al instante.
    // .multicast() permite que haya múltiples pestañas escuchando a la vez.
    private final Sinks.Many<Notificacion> sink = Sinks.many().multicast().onBackpressureBuffer();

    /**
     * Guarda la notificación en Mongo Y TAMBIÉN la emite al canal en vivo.
     */
    public Mono<Notificacion> addNotificacion(Notificacion n) {
        return repository.save(n)
                .doOnSuccess(saved -> {
                    // Empujamos la notificación al "canal de radio" (Sink)
                    sink.tryEmitNext(saved);
                });
    }

    /**
     * Devuelve un flujo infinito que mezcla:
     * 1. El historial guardado en BD (Cold Stream).
     * 2. Las nuevas notificaciones que lleguen al Sink en vivo (Hot Stream).
     */
    public Flux<Notificacion> getNotificacionesEnTiempoReal(String usuario) {
        // 1. Recuperar historial de la BD
        Flux<Notificacion> historico = repository.findByUsuario(usuario);

        // 2. Escuchar el canal en vivo y filtrar solo las de este usuario
        Flux<Notificacion> enVivo = sink.asFlux()
                .filter(n -> n.getUsuario().equals(usuario));

        // 3. Fusionar ambos mundos
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