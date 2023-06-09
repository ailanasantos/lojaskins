package br.unitins;

import br.unitins.dto.AuthUsuarioDTO;
import br.unitins.dto.skins.SkinsDTO;
import br.unitins.dto.skins.SkinsResponseDTO;
import br.unitins.service.skins.SkinsService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;

@QuarkusTest
public class SkinsResourceTest {

        private String token;

        @BeforeEach
        public void setUp() {
                var auth = new AuthUsuarioDTO("Ailana", "123");
                Response response = (Response) given()
                                .contentType("application/json")
                                .body(auth).when()
                                .post("/auth")
                                .then().statusCode(200).extract().response();
                token = response.header("Authorization");
        }

        @Inject
        SkinsService skinsService;

        @Test
        public void getAllTest() {
                given()
                                .header("Authorization", "Bearer " + token)
                                .when().get("/skins")
                                .then()
                                .statusCode(200);
        }

        @Test
        public void testInsert() {
                SkinsDTO skins = new SkinsDTO(
                                "Dragao",
                                "Rara");

                SkinsResponseDTO skinsCriado = skinsService.create(skins);

                given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(skinsCriado)
                                .when().post("/skins")
                                .then()
                                .statusCode(201)
                                .body("id", notNullValue(),
                                                "nome", is("Dragao"),
                                                "tipo", is("Rara"));

        }

        @Test
        public void testUpdate() {
                SkinsDTO skins = new SkinsDTO(
                                "Dragao",
                                "Rara");

                Long id = skinsService.create(skins).id();

                // Criando outra pessoa para atuailzacao
                SkinsDTO skinsRequisicao = new SkinsDTO(
                                "Esmeralda",
                                "Colecionador");

                SkinsResponseDTO produtoAtualizado = skinsService.update(id, skinsRequisicao);

                given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(produtoAtualizado)
                                .when().put("/skins/" + id)
                                .then()
                                .statusCode(200);

                // Verificando se os dados foram atualizados no banco de dados
                SkinsResponseDTO skinsResponse = skinsService.findById(id);
                assertThat(skinsResponse.nome(), is("Esmeralda"));
                assertThat(skinsResponse.tipo(), is("Colecionador"));

        }

        @Test
        public void testDelete() {
                SkinsDTO skins = new SkinsDTO(
                                "Esmeralda",
                                "Colecionador");
                Long id = skinsService.create(skins).id();

                given()
                                .header("Authorization", "Bearer " + token)
                                .when().delete("/skins/" + id)
                                .then()
                                .statusCode(204);

                // verificando se skins foi excluida
                SkinsResponseDTO skinsResponse = null;
                try {
                        skinsResponse = skinsService.findById(id);
                } catch (Exception e) {
                        assert true;
                } finally {
                        assertNull(skinsResponse);
                }
        }
}
