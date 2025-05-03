package com.example.asignaturas.integration;

import com.example.asignaturas.dto.AsignaturaRequest;
import com.example.asignaturas.dto.AsignaturaResponse;
import com.example.asignaturas.model.Asignatura;
import com.example.asignaturas.repository.AsignaturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AsignaturaIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        asignaturaRepository.deleteAll();

        // Crear asignatura para pruebas
        Asignatura asignatura = Asignatura.builder()
                .codigo("FIS101")
                .nombre("Física Básica")
                .descripcion("Introducción a la física")
                .creditos(4)
                .profesorId("prof456")
                .nivel(Asignatura.Nivel.BASICO)
                .activa(true)
                .build();

        asignaturaRepository.save(asignatura);
    }

    @Test
    void deberiaCrearYObtenerAsignaturas() {
        // Crear nueva asignatura
        AsignaturaRequest asignaturaRequest = new AsignaturaRequest();
        asignaturaRequest.setCodigo("MAT101");
        asignaturaRequest.setNombre("Matemáticas Básicas");
        asignaturaRequest.setDescripcion("Curso introductorio de matemáticas");
        asignaturaRequest.setCreditos(4);
        asignaturaRequest.setProfesorId("prof123");
        asignaturaRequest.setNivel("BASICO");
        asignaturaRequest.setActiva(true);

        // Realizar solicitud POST
        webTestClient.post()
                .uri("/api/asignaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(asignaturaRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AsignaturaResponse.class);

        // Verificar que existen 2 asignaturas (la creada en setUp y la nueva)
        webTestClient.get()
                .uri("/api/asignaturas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AsignaturaResponse.class)
                .hasSize(2);
    }

    @Test
    void deberiaObtenerAsignaturaPorCodigo() {
        webTestClient.get()
                .uri("/api/asignaturas/codigo/FIS101")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.codigo").isEqualTo("FIS101")
                .jsonPath("$.nombre").isEqualTo("Física Básica");
    }

    @Test
    void deberiaActualizarAsignatura() {
        // Primero obtenemos la asignatura existente
        AsignaturaResponse existente = webTestClient.get()
                .uri("/api/asignaturas/codigo/FIS101")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AsignaturaResponse.class)
                .returnResult()
                .getResponseBody();

        // Creamos el request con los datos actualizados
        AsignaturaRequest actualizacionRequest = new AsignaturaRequest();
        actualizacionRequest.setCodigo("FIS101");
        actualizacionRequest.setNombre("Física Actualizada");
        actualizacionRequest.setDescripcion("Descripción actualizada");
        actualizacionRequest.setCreditos(5);
        actualizacionRequest.setProfesorId("prof456");
        actualizacionRequest.setNivel("INTERMEDIO");
        actualizacionRequest.setActiva(true);

        // Actualizamos la asignatura
        webTestClient.put()
                .uri("/api/asignaturas/" + existente.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(actualizacionRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Física Actualizada")
                .jsonPath("$.creditos").isEqualTo(5)
                .jsonPath("$.nivel").isEqualTo("INTERMEDIO");
    }

    @Test
    void deberiaEliminarAsignatura() {
        // Primero obtenemos la asignatura existente
        AsignaturaResponse existente = webTestClient.get()
                .uri("/api/asignaturas/codigo/FIS101")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AsignaturaResponse.class)
                .returnResult()
                .getResponseBody();

        // Eliminamos la asignatura
        webTestClient.delete()
                .uri("/api/asignaturas/" + existente.getId())
                .exchange()
                .expectStatus().isNoContent();

        // Verificamos que ya no existe
        webTestClient.get()
                .uri("/api/asignaturas/codigo/FIS101")
                .exchange()
                .expectStatus().isNotFound();
    }
}