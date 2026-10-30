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

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.modelos.AdicionadorLinkServico;
import com.autobots.automanager.repositorios.RepositorioServico;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

	@Autowired
	private RepositorioServico repositorio;

	@Autowired
	private AdicionadorLinkServico adicionadorLink;

	@GetMapping("/servicos")
	public ResponseEntity<List<Servico>> obterServicos() {
		List<Servico> servicos = repositorio.findAll();
		if (servicos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(servicos);
		return new ResponseEntity<>(servicos, HttpStatus.OK);
	}

	@GetMapping("/servico/{id}")
	public ResponseEntity<Servico> obterServico(@PathVariable long id) {
		Optional<Servico> servico = repositorio.findById(id);
		if (servico.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(servico.get());
		return new ResponseEntity<>(servico.get(), HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarServico(@RequestBody Servico servico) {
		if (servico.getId() == null) {
			repositorio.save(servico);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarServico(@RequestBody Servico atualizacao) {
		Optional<Servico> servicoOptional = repositorio.findById(atualizacao.getId());
		if (servicoOptional.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Servico servico = servicoOptional.get();
		if (atualizacao.getNome() != null) {
			servico.setNome(atualizacao.getNome());
		}
		if (atualizacao.getDescricao() != null) {
			servico.setDescricao(atualizacao.getDescricao());
		}
		servico.setValor(atualizacao.getValor());
		repositorio.save(servico);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirServico(@PathVariable long id) {
		Optional<Servico> servico = repositorio.findById(id);
		if (servico.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(servico.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
