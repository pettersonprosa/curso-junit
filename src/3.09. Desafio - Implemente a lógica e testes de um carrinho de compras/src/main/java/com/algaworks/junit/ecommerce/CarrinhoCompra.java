package com.algaworks.junit.ecommerce;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CarrinhoCompra {

	private final Cliente cliente;
	private final List<ItemCarrinhoCompra> itens;

	public CarrinhoCompra(Cliente cliente) {
		this(cliente, new ArrayList<>());
	}

	public CarrinhoCompra(Cliente cliente, List<ItemCarrinhoCompra> itens) {
		Objects.requireNonNull(cliente);
		Objects.requireNonNull(itens);
		this.cliente = cliente;
		this.itens = new ArrayList<>(itens); //Cria lista caso passem uma imutável
	}

	public List<ItemCarrinhoCompra> getItens() {
		return new ArrayList<>(itens);
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void adicionarProduto(Produto produto, int quantidade) {
		Objects.requireNonNull(produto);
		validarQuantidade(quantidade);

		encontrarItemPeloProduto(produto)
				.ifPresentOrElse(
						item -> item.adicionarQuantidade(quantidade),
						() -> itens.add(new ItemCarrinhoCompra(produto, quantidade)));
	}

	public void removerProduto(Produto produto) {
		Objects.requireNonNull(produto);
		boolean produtoRemovido = itens.removeIf(item -> item.getProduto().equals(produto));
		if (!produtoRemovido) throw new IllegalArgumentException("Produto não encontrado no carrinho de compras");
	}

	public void aumentarQuantidadeProduto(Produto produto) {
		Objects.requireNonNull(produto);
		ItemCarrinhoCompra item = encontrarItemPeloProduto(produto)
				.orElseThrow(()-> new IllegalArgumentException("Produto não encontrado no carrinho de compras"));
		item.adicionarQuantidade(1);
	}

    public void diminuirQuantidadeProduto(Produto produto) {
		Objects.requireNonNull(produto);
		ItemCarrinhoCompra item = encontrarItemPeloProduto(produto)
				.orElseThrow(()-> new IllegalArgumentException("Produto não encontrado no carrinho de compras"));
		if (item.getQuantidade() > 1) {
			item.subtrairQuantidade(1);
		} else {
			removerProduto(item.getProduto());
		}
	}

    public BigDecimal getValorTotal() {
		return itens.stream()
				.map(ItemCarrinhoCompra::getValorTotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
    }

	public int getQuantidadeTotalDeProdutos() {
		return itens.stream()
				.map(ItemCarrinhoCompra::getQuantidade)
				.reduce(0, Integer::sum);
	}

	public void esvaziar() {
		itens.clear();
	}

	private void validarQuantidade(int quantidade) {
		if (quantidade < 1) throw new IllegalArgumentException("Quantidade do produto dever ser maior que zero");
	}

	private Optional<ItemCarrinhoCompra> encontrarItemPeloProduto(Produto produto) {
		return itens.stream()
				.filter(item->item.getProduto().equals(produto))
				.findFirst();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CarrinhoCompra that = (CarrinhoCompra) o;
		return Objects.equals(itens, that.itens) && Objects.equals(cliente, that.cliente);
	}

	@Override
	public int hashCode() {
		return Objects.hash(itens, cliente);
	}
}