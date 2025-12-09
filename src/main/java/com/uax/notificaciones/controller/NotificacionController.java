package com.uax.notificaciones.controller;

import com.uax.notificaciones.model.Notificacion;
import com.uax.notificaciones.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

   
    @GetMapping("/{usuario}")
    public String verNotificaciones(@PathVariable String usuario,
                                    @RequestParam(required = false) String tipo,
                                    Model model) {

    
        Flux<Notificacion> flujo;
        if (tipo != null && !tipo.isEmpty()) {
            flujo = service.filtrarPorTipo(usuario, tipo);
        } else {
            flujo = service.getNotificacionesEnTiempoReal(usuario);
        }

       
        model.addAttribute("listaNotificaciones", new ReactiveDataDriverContextVariable(flujo, 1));

        model.addAttribute("usuario", usuario);
        model.addAttribute("nuevaNotificacion", new Notificacion()); // Para el formulario vacío

        return "lista"; // Esto buscará el archivo lista.html en templates
    }


    @PostMapping("/crear")
    public Mono<String> crearNotificacion(@ModelAttribute Notificacion notificacion) {
        return service.addNotificacion(notificacion)
                // Al terminar, redirigimos a la página del usuario para ver el cambio
                .thenReturn("redirect:/notificaciones/" + notificacion.getUsuario());
    }

    @PostMapping("/leer/{id}")
    public Mono<String> marcarLeida(@PathVariable String id, @RequestParam String usuario) {
        return service.marcarLeido(id)
                .thenReturn("redirect:/notificaciones/" + usuario);
    }

    @PostMapping("/eliminar/{id}")
    public Mono<String> eliminar(@PathVariable String id, @RequestParam String usuario) {
        return service.eliminarNotificacion(id)
                .thenReturn("redirect:/notificaciones/" + usuario);
    }
}
