package servicos;

import static utils.DataUtils.adicionarDias;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException{
		
		
		if(filmes == null || filmes.isEmpty()){
			throw new LocadoraException("Filme Vazio");
		}
		
		if(usuario == null){
			throw new LocadoraException("Usuário Vazio");
		}
		
		Locacao locacao = new Locacao();
		locacao.setListaFilmes(new ArrayList<Filme>());
		locacao.getListaFilmes().addAll(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(getValorTotal(filmes));
		
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		
		return locacao;
		
	}
	
	public double getValorTotal(List<Filme> listaFilmes) throws FilmeSemEstoqueException{
		Double valorTotalLocacao = 0.0;
		for(int i = 0; i < listaFilmes.size(); i++){
			Filme filme = listaFilmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			if(listaFilmes.get(i).getEstoque() <= 0){
				throw new FilmeSemEstoqueException();
			}
			switch (i) {
			case 2: valorFilme = valorFilme * 0.75;	break;
			case 3: valorFilme = valorFilme * 0.5;	break;
			case 4: valorFilme = valorFilme * 0.25;	break;
			case 5: valorFilme = 0d; break;
			}
			valorTotalLocacao += valorFilme;
		}
		return valorTotalLocacao;
	}
	
	public static void main(String[] args) {
		
	}

}
