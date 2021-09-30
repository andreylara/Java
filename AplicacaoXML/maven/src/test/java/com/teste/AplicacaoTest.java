package com.teste;

import static org.junit.Assert.*;

import org.junit.Test;

public class AplicacaoTest {

	@Test
	public void testConverteStringParaInt() {
		Aplicacao app = new Aplicacao();
		int valor = app.converteStringParaInt("10");
		assertEquals(10, valor);		
	}

	@Test
	public void testConverteIntParaString() {
		Aplicacao app = new Aplicacao();
		String valor = app.converteIntParaString(10);
		assertEquals("10", valor);
	}

	@Test
	public void testCalculaValorTotal() {
		Aplicacao app = new Aplicacao();
		int valor = app.calculaValorTotal(2, 5);
		assertEquals(10, valor);
	}

}
