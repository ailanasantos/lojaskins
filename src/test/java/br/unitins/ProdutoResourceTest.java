package br.unitins;

import br.unitins.dto.AuthUsuarioDTO;
import br.unitins.dto.produto.ProdutoDTO;
import br.unitins.dto.produto.ProdutoResponseDTO;
import br.unitins.service.produto.ProdutoService;
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
public class ProdutoResourceTest {

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
    ProdutoService produtoService;

    @Test
    public void getAllTest() {
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/produtos")
                .then()
                .statusCode(200);
    }

    @Test
    public void testInsert() {
        ProdutoDTO produto = new ProdutoDTO(
                "Skin",
                "Dragao",
                10,
                40.00);

        ProdutoResponseDTO produtoCriado = produtoService.create(produto);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(produtoCriado)
                .when().post("/produtos")
                .then()
                .statusCode(201)
                .body("id", notNullValue(),
                        "nome", is("Skin"),
                        "descricao", is("Dragao"),
                        "estoque", is(10),
                        "preco", is(40.00F));
    }

    @Test
    public void testUpdate() {
        ProdutoDTO produto = new ProdutoDTO(
                "Skin",
                "Dragao",
                10,
                40.00);

        Long id = produtoService.create(produto).id();

        // Criando outra pessoa para atuailzacao
        ProdutoDTO corpoRequisicao = new ProdutoDTO(
                "Skin",
                "Esmeralda",
                5,
                80.00);

        ProdutoResponseDTO produtoAtualizado = produtoService.update(id, corpoRequisicao);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(produtoAtualizado)
                .when().put("/produtos/" + id)
                .then()
                .statusCode(200);

        // Verificando se os dados foram atualizados no banco de dados
        ProdutoResponseDTO produtoResponse = produtoService.findById(id);
        assertThat(produtoResponse.nome(), is("Skin"));
        assertThat(produtoResponse.descricao(), is("Esmeralda"));
        assertThat(produtoResponse.estoque(), is(5));
        assertThat(produtoResponse.preco(), is(80.00));
    }

    @Test
    public void testDelete() {
        ProdutoDTO produto = new ProdutoDTO(
                "Skin",
                "Dragao",
                10,
                40.00);
        Long id = produtoService.create(produto).id();

        given()
                .header("Authorization", "Bearer " + token)
                .when().delete("/produtos/" + id)
                .then()
                .statusCode(204);

        // verificando se a pessoa fisica foi excluida
        ProdutoResponseDTO produtoResponse = null;
        try {
            produtoResponse = produtoService.findById(id);
        } catch (Exception e) {
            assert true;
        } finally {
            assertNull(produtoResponse);
        }
    }
}
