package com.algaworks.junit.ecommerce;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Carrinho de compras")
@Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
class CarrinhoCompraTest {

    private Cliente cliente;
    private Produto produto1;
    private Produto produto2;
    private ItemCarrinhoCompra item1;
    private ItemCarrinhoCompra item2;
    private CarrinhoCompra carrinho;

    @BeforeEach
    void befoceEach() {
        cliente = new Cliente(1L, "Joao");
        produto1 = new Produto(1L, "Bola de futebol", "bola de futebol...", BigDecimal.valueOf(93.99));
        produto2 = new Produto(1L, "Bola de volei", "bola de vôlei...", BigDecimal.valueOf(88.10));
        item1 = new ItemCarrinhoCompra(produto1, 1);
        item2 = new ItemCarrinhoCompra(produto2, 3);
        carrinho = new CarrinhoCompra(cliente);
    }

    @Test
    void clienteNuloDeveLancarExcecao() {
        //Arrange
        List<ItemCarrinhoCompra> itens = new ArrayList<>();
        itens.add(item1);
        itens.add(item2);
        //Act e Assert
        assertThrows(NullPointerException.class, () -> new CarrinhoCompra(null));
        assertThrows(NullPointerException.class, () -> new CarrinhoCompra(null, itens));
    }

    @Test
    void listaItensNuloDeveLancarExcecao() {
        assertThrows(NullPointerException.class, () -> new CarrinhoCompra(cliente, null));
    }

    @Test
    void verifiquaSeItensDoCarrinhoRetornaUmaNovaLista() {
        // Arrange
        ItemCarrinhoCompra item1 = new ItemCarrinhoCompra(produto1, 1);
        ItemCarrinhoCompra item2 = new ItemCarrinhoCompra(produto2, 2);
        List<ItemCarrinhoCompra> itens = new ArrayList<>();
        itens.add(item1);
        itens.add(item2);
        carrinho = new CarrinhoCompra(cliente, itens);
        // Act
        List<ItemCarrinhoCompra> itensRetornados = carrinho.getItens();
        // Asserts
        assertNotSame(itens, itensRetornados, "A lista retornada deve ser uma nova instância");
        assertEquals(itens.size(), itensRetornados.size(), "A lista retornada deve ter o mesmo tamanho");
        assertTrue(itensRetornados.containsAll(itens), "A lista retornada deve conter todos os itens originais");
        itensRetornados.clear();
        assertEquals(2, itens.size(),
                "A lista original não deve ser alterada ao modificar a lista retornada");
    }

    @Test
    public void adicionarProdutoNuloDeveLancarExcecao() {
        //Act, Assert
        assertThrows(NullPointerException.class, () -> carrinho.adicionarProduto(null, 2));
    }

    @Test
    public void adicionarProdutoComAQuantidadeZeroOuNegativaDeveLancarExcecao() {
        // Act, Assert
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> carrinho.adicionarProduto(produto1, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> carrinho.adicionarProduto(produto2, -1))
        );
    }

    @Test
    public void adicionaNovoProdutoNaListaDeCarrinho() {
        // Arrange
        carrinho.adicionarProduto(produto2, 5);
        // Act
        carrinho.adicionarProduto(produto1, 2);
        // Assert
        assertAll(
                () -> assertEquals(carrinho.getItens().get(carrinho.getItens().size() - 1).getProduto(), produto1),
                () -> assertEquals(carrinho.getItens().get(carrinho.getItens().size() - 1).getQuantidade(), 2)
        );
    }

    @Test
    public void deveIncrementarQuantidadeCasoProdutoJaExista() {
        // Arrange
        carrinho.adicionarProduto(produto1, 2);
        // Act
        carrinho.adicionarProduto(produto1, 3);
        // Assert
        assertEquals(carrinho.getItens().stream()
                .filter(item -> item.getProduto().equals(produto1))
                .map(item -> item.getQuantidade())
                .reduce(0, Integer::sum), 5);
    }

    @Test
    public void removerProdutoNuloDeveRetornarExcecao() {
        // Arrange
        carrinho.adicionarProduto(produto1, 5);
        // Act, Assert
        assertThrows(NullPointerException.class, () -> carrinho.removerProduto(null));
    }

    @Test
    public void removerProduto() {
        // Arrange
        carrinho.adicionarProduto(produto1, 5);
        carrinho.adicionarProduto(produto2, 7);
        // Act
        carrinho.removerProduto(produto2);
        // Assert
        assertEquals(false, carrinho.getItens().stream()
                .anyMatch(item -> item.getProduto().equals(produto2)));
    }

    @Test
    public void aumentarQuantidadeDoProdutoNuloDeveRetornarExcecao() {
        // Arrange
        carrinho.adicionarProduto(produto1, 2);
        carrinho.adicionarProduto(produto2, 1);
        // Act, Assert
        assertThrows(NullPointerException.class, () -> carrinho.aumentarQuantidadeProduto(null));
    }

    @Test
    public void aumentarQuantidadeDoProdutoQueNaoExisteNoCarrinhoDeveRetornarExcecao() {
        // Arrange
        carrinho.adicionarProduto(produto1, 2);
        // Act, Assert
        assertThrows(IllegalArgumentException.class, () -> carrinho.aumentarQuantidadeProduto(produto2));
    }

    @Test
    public void aumentarQuantidadeDoProdutoExistenteNaLista() {
        // Arrange
        carrinho.adicionarProduto(produto1, 4);
        carrinho.adicionarProduto(produto2, 7);
        // Act
        carrinho.aumentarQuantidadeProduto(produto1);
        // Assert
        Optional<Integer> quantidade = carrinho.getItens().stream()
                .filter(item -> item.getProduto().equals(produto1))
                .findFirst()
                .map(ItemCarrinhoCompra::getQuantidade);
        assertEquals(5, quantidade.get());
    }

    @Test
    public void diminuirQuantidadeDoProdutoNuloDeveLancarExcecao() {
        // Arrange
        carrinho.adicionarProduto(produto1, 2);
        // Act, Assert
        assertThrows(NullPointerException.class, () -> carrinho.diminuirQuantidadeProduto(null));
    }

    @Test
    public void diminuirQuantidadeDoProdutoQueNaoExisteNoCarrinhoDeveRetornarExcecao() {
        // Arrange
        carrinho.adicionarProduto(produto1, 2);
        // Act, Assert
        assertThrows(IllegalArgumentException.class, () -> carrinho.diminuirQuantidadeProduto(produto2));
    }

    @Test
    public void diminuirQuantidadeMaiorQueUmDeProdutoExistenteNaLista() {
        // Arrange
        carrinho.adicionarProduto(produto1, 4);
        // Act
        carrinho.diminuirQuantidadeProduto(produto1);
        // Assert
        Optional<Integer> quantidade = carrinho.getItens().stream()
                .filter(item -> item.getProduto().equals(produto1))
                .findFirst()
                .map(ItemCarrinhoCompra::getQuantidade);

        assertEquals(3, quantidade.get());
    }

    @Test
    public void diminuirProdutoComQuantidadeIgualAUmNaListaExcluiProdutoDoCarrinho() {
        // Arrange
        carrinho.adicionarProduto(produto1, 1);
        carrinho.adicionarProduto(produto2, 6);

        // Act
        carrinho.diminuirQuantidadeProduto(produto1);

        // Assert
        assertFalse(carrinho.getItens().stream().anyMatch(
                item -> item.getProduto().equals(produto1)));
    }

    @Test
    public void retornarValorTotalDaListaDeItensDoCarrinho() {
        // Arrange
        carrinho.adicionarProduto(produto1, 2);
        carrinho.adicionarProduto(produto2, 1);
        // Act
        BigDecimal valorTotalProduto1 = BigDecimal.valueOf(93.99).multiply(new BigDecimal("2"));
        BigDecimal valorTotalProduto2 = BigDecimal.valueOf(88.10).multiply(new BigDecimal("1"));
        BigDecimal valorTotalPrevisto = valorTotalProduto1.add(valorTotalProduto2);
        // Assert
        assertEquals(valorTotalPrevisto, carrinho.getValorTotal());
    }

    @Test
    public void retornarQuantidadeTotalDeProdutosDoCarrinho() {
        // Arrange
        carrinho.adicionarProduto(produto1, 9);
        carrinho.adicionarProduto(produto2, 11);
        // Act, Assert
        assertEquals(20, carrinho.getQuantidadeTotalDeProdutos());
    }

    @Test
    public void esvaziarCarrinho() {
        // Arrange
        carrinho.adicionarProduto(produto1, 2);
        carrinho.adicionarProduto(produto2, 1);
        // Act
        carrinho.esvaziar();
        // Assert
        assertEquals(0, carrinho.getItens().size());
    }
}