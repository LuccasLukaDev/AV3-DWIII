package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.modelos.AdicionadorLinkMercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

	@Autowired
	private RepositorioMercadoria repositorio;

	@Autowired
	private AdicionadorLinkMercadoria adicionadorLink;

	@GetMapping("/mercadorias")
	public ResponseEntity<List<Mercadoria>> obterMercadorias() {
		List<Mercadoria> mercadorias = repositorio.findAll();
		if (mercadorias.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(mercadorias);
		return new ResponseEntity<>(mercadorias, HttpStatus.OK);
	}

	@GetMapping("/mercadoria/{id}")
	public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable long id) {
		Optional<Mercadoria> mercadoria = repositorio.findById(id);
		if (mercadoria.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(mercadoria.get());
		return new ResponseEntity<>(mercadoria.get(), HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
		if (mercadoria.getId() == null) {
			repositorio.save(mercadoria);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Mercadoria atualizacao) {
		Optional<Mercadoria> mercadoriaOptional = repositorio.findById(atualizacao.getId());
		if (mercadoriaOptional.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Mercadoria mercadoria = mercadoriaOptional.get();
		if (atualizacao.getNome() != null) {
			mercadoria.setNome(atualizacao.getNome());
		}
		if (atualizacao.getDescricao() != null) {
			mercadoria.setDescricao(atualizacao.getDescricao());
		}
		if (atualizacao.getValidade() != null) {
			mercadoria.setValidade(atualizacao.getValidade());
		}
		if (atualizacao.getFabricao() != null) {
			mercadoria.setFabricao(atualizacao.getFabricao());
		}
		if (atualizacao.getCadastro() != null) {
			mercadoria.setCadastro(atualizacao.getCadastro());
		}
		mercadoria.setQuantidade(atualizacao.getQuantidade());
		mercadoria.setValor(atualizacao.getValor());
		repositorio.save(mercadoria);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirMercadoria(@PathVariable long id) {
		Optional<Mercadoria> mercadoria = repositorio.findById(id);
		if (mercadoria.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(mercadoria.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
