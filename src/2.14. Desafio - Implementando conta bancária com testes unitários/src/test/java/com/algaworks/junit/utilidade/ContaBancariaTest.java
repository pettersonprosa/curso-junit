package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ContaBancariaTest {

    @Test
    void criarContaComSaldoNullFalha() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContaBancaria(null));
    }

    @Test
    void saqueComValorNuloFalha() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContaBancaria(null));
    }

    @Test
    void saqueComValorZeroOuNegativoFalha() {
        BigDecimal saldoInicial = new BigDecimal("300");
        ContaBancaria conta = new ContaBancaria(saldoInicial);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> conta.saque(new BigDecimal("0"))),
                () -> assertThrows(IllegalArgumentException.class, () -> conta.saque(new BigDecimal("-100")))
        );
    }

    @Test
    void saque() {
        BigDecimal saldoInicial = new BigDecimal("300.12");
        ContaBancaria conta = new ContaBancaria(saldoInicial);
        conta.saque(new BigDecimal("100.02"));

        assertEquals(new BigDecimal("200.10").stripTrailingZeros(), conta.saldo().stripTrailingZeros());
    }

    @Test
    void saqueComSaldoInsuficienteFalha() {
        BigDecimal saldoInicial = new BigDecimal("300");
        ContaBancaria conta = new ContaBancaria(saldoInicial);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> conta.saque(new BigDecimal("500")));
        assertEquals(exception.getClass().getName(), "java.lang.RuntimeException");
    }

    @Test
    void depositoComvalorNuloFalha() {
        BigDecimal saldoInicial = new BigDecimal("300.12");
        ContaBancaria conta = new ContaBancaria(saldoInicial);

        assertThrows(IllegalArgumentException.class,
                () -> conta.deposito(null));
    }

    @Test
    void deposito(){
        BigDecimal saldoInicial = new BigDecimal("300.12");
        ContaBancaria conta = new ContaBancaria(saldoInicial);
        conta.deposito(new BigDecimal("100.02"));

        assertEquals(new BigDecimal("400.14").stripTrailingZeros(), conta.saldo().stripTrailingZeros());
    }
}