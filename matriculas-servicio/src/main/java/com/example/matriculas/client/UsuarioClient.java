package com.example.matriculas.client;

import com.example.matriculas.dto.UsuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios-servicio")
public interface UsuarioClient {
    
    @GetMapping("/api/usuarios/{id}")
    UsuarioDto getUsuarioById(@PathVariable("id") String id);
}