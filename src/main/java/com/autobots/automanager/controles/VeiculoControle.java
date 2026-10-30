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

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.modelos.AdicionadorLinkVeiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

	@Autowired
	private RepositorioVeiculo repositorio;

	@Autowired
	private AdicionadorLinkVeiculo adicionadorLink;

	@GetMapping("/veiculos")
	public ResponseEntity<List<Veiculo>> obterVeiculos() {
		List<Veiculo> veiculos = repositorio.findAll();
		if (veiculos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(veiculos);
		return new ResponseEntity<>(veiculos, HttpStatus.OK);
	}

	@GetMapping("/veiculo/{id}")
	public ResponseEntity<Veiculo> obterVeiculo(@PathVariable long id) {
		Optional<Veiculo> veiculo = repositorio.findById(id);
		if (veiculo.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(veiculo.get());
		return new ResponseEntity<>(veiculo.get(), HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
		if (veiculo.getId() == null) {
			repositorio.save(veiculo);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarVeiculo(@RequestBody Veiculo atualizacao) {
		Optional<Veiculo> veiculoOptional = repositorio.findById(atualizacao.getId());
		if (veiculoOptional.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Veiculo veiculo = veiculoOptional.get();
		if (atualizacao.getTipo() != null) {
			veiculo.setTipo(atualizacao.getTipo());
		}
		if (atualizacao.getModelo() != null) {
			veiculo.setModelo(atualizacao.getModelo());
		}
		if (atualizacao.getPlaca() != null) {
			veiculo.setPlaca(atualizacao.getPlaca());
		}
		if (atualizacao.getProprietario() != null) {
			veiculo.setProprietario(atualizacao.getProprietario());
		}
		repositorio.save(veiculo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirVeiculo(@PathVariable long id) {
		Optional<Veiculo> veiculo = repositorio.findById(id);
		if (veiculo.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(veiculo.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
