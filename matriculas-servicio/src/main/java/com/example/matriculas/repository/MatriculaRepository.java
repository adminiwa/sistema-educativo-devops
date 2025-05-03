package com.example.matriculas.repository;

import com.example.matriculas.model.Matricula;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends MongoRepository<Matricula, String> {
    List<Matricula> findByEstudianteId(String estudianteId);
    List<Matricula> findByAsignaturaId(String asignaturaId);
    List<Matricula> findByPeriodoAcademico(String periodoAcademico);
    List<Matricula> findByEstudianteIdAndPeriodoAcademico(String estudianteId, String periodoAcademico);
    boolean existsByEstudianteIdAndAsignaturaIdAndPeriodoAcademico(String estudianteId, String asignaturaId, String periodoAcademico);
}