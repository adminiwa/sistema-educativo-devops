package com.example.asignaturas.repository;

import com.example.asignaturas.model.Asignatura;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignaturaRepository extends MongoRepository<Asignatura, String> {
    Optional<Asignatura> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<Asignatura> findByProfesorId(String profesorId);
    List<Asignatura> findByNivel(Asignatura.Nivel nivel);
    List<Asignatura> findByActivaTrue();
}