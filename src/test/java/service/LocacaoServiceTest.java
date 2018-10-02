package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import servicos.LocacaoService;
import utils.DataUtils;

public class LocacaoServiceTest {
	
	private static int contador = 0;

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private LocacaoService service;
	
	@Before
	public void setup(){
		service = new LocacaoService();
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {

		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> listaFilmes = new ArrayList<Filme>();
		Filme filme = new Filme("Filme 1", 2, 4.0);
		Filme filme1 = new Filme("Filme 2", 2, 44.0);

		listaFilmes.add(filme);
		listaFilmes.add(filme1);
		// acao
		Locacao locacao = service.alugarFilme(usuario, listaFilmes);

		// verificacao
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(48.0));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				CoreMatchers.is(true));
	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> listaFilmes = new ArrayList<Filme>();
		Filme filme = new Filme("Filme 1", 0, 4.0);
		Filme filme1 = new Filme("Filme 2", 2, 44.0);
		
		listaFilmes.add(filme);
		listaFilmes.add(filme1);

		// acao
		service.alugarFilme(usuario, listaFilmes);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException{
		
		//cenario
		List<Filme> listaFilmes = new ArrayList<Filme>();
		Filme filme = new Filme("Filme 1", 2, 4.0);
		listaFilmes.add(filme);
		
			try {
				service.alugarFilme(null, listaFilmes);
				Assert.fail();
			} catch (LocadoraException e) {
				Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuário Vazio"));
			}
	}
	
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio");

		service.alugarFilme(usuario, null);
	}
	
	@Test
	public void devePagar75pctNoFilme3() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> listaFilmes = new ArrayList<Filme>();
		Filme filme = new Filme("Filme 1", 2, 4.0);
		Filme filme1 = new Filme("Filme 2", 1, 4.0);
		Filme filme2 = new Filme("Filme 3", 1, 4.0);
		listaFilmes.add(filme);
		listaFilmes.add(filme1);
		listaFilmes.add(filme2);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, listaFilmes);
		
		//verificacao
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(11.0));
	}
	
	@Test
	public void devePagar50pctNoFilme4() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> listaFilmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 1, 4.0),
				new Filme("Filme 4", 1, 4.0));
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, listaFilmes);
		
		//verificacao
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(13.0));
	}
	
	@Test
	public void devePagar25pctNoFilme5() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> listaFilmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 1, 4.0),
				new Filme("Filme 4", 1, 4.0),
				new Filme("Filme 5", 1, 4.0));
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, listaFilmes);
		
		//verificacao
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(14.0));
	}
	
	@Test
	public void devePagar0pctNoFilme6() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> listaFilmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 1, 4.0),
				new Filme("Filme 4", 1, 4.0),
				new Filme("Filme 5", 1, 4.0),
				new Filme("Filme 6", 1, 4.0));
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, listaFilmes);
		
		//verificacao
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(14.0));
	}

	/*
	 * @Test public void filmeSemEstoqueTest2() { //cenario LocacaoService
	 * service = new LocacaoService(); Usuario usuario = new
	 * Usuario("Usuario 1"); Filme filme = new Filme("Filme 1", 0, 4.0);
	 * 
	 * //acao try { service.alugarFilme(usuario, filme);
	 * Assert.fail("Deveria ter lançado uma exception"); } catch (Exception e) {
	 * Assert.assertThat(e.getMessage(),
	 * CoreMatchers.is("Filme fora de estoque")); } }
	 * 
	 * @Test public void filmeSemEstoqueTest3() throws Exception{ //cenario
	 * LocacaoService service = new LocacaoService(); Usuario usuario = new
	 * Usuario("Usuario 1"); Filme filme = new Filme("Filme 1", 0, 4.0);
	 * exception.expect(Exception.class);
	 * exception.expectMessage("Filme fora de estoque");
	 * 
	 * //acao service.alugarFilme(usuario, filme);
	 * 
	 * }
	 */
}
