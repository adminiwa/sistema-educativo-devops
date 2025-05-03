package com.example.matriculas.client;

import com.example.matriculas.dto.AsignaturaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "asignaturas-servicio")
public interface AsignaturaClient {
    
    @GetMapping("/api/asignaturas/{id}")
    AsignaturaDto getAsignaturaById(@PathVariable("id") String id);
}