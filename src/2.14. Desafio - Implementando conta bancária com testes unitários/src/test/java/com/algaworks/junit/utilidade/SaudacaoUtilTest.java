package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaudacaoUtilTest {

    @Test
    public void saudarComBomDia() {
        int horario = 9;
        String saudacao = SaudacaoUtil.saudar(horario);
        assertEquals("Bom dia", saudacao, "Saudação incorreta");
    }

    @Test
    public void saudarComBoaTarde() {
        int horario = 13;
        String saudacao = SaudacaoUtil.saudar(horario);
        assertEquals("Boa tarde", saudacao, "Saudação incorreta");
    }

    @Test
    public void saudarComBoaNoite() {
        int horario = 22;
        String saudacao = SaudacaoUtil.saudar(horario);
        assertEquals("Boa noite", saudacao, "Saudação incorreta");
    }

    @Test
    protected void deveLancarException() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> SaudacaoUtil.saudar(-10));
        assertEquals("Hora inválida", illegalArgumentException.getMessage());
    }

    @Test
    public void naoDeveLancarException() {
        assertDoesNotThrow(() -> SaudacaoUtil.saudar(0));
    }

}