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

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelos.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/venda")
public class VendaControle {

	@Autowired
	private RepositorioVenda repositorio;

	@Autowired
	private AdicionadorLinkVenda adicionadorLink;

	@GetMapping("/vendas")
	public ResponseEntity<List<Venda>> obterVendas() {
		List<Venda> vendas = repositorio.findAll();
		if (vendas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(vendas);
		return new ResponseEntity<>(vendas, HttpStatus.OK);
	}

	@GetMapping("/venda/{id}")
	public ResponseEntity<Venda> obterVenda(@PathVariable long id) {
		Optional<Venda> venda = repositorio.findById(id);
		if (venda.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(venda.get());
		return new ResponseEntity<>(venda.get(), HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda) {
		if (venda.getId() == null) {
			repositorio.save(venda);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarVenda(@RequestBody Venda atualizacao) {
		Optional<Venda> vendaOptional = repositorio.findById(atualizacao.getId());
		if (vendaOptional.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Venda venda = vendaOptional.get();
		if (atualizacao.getCadastro() != null) {
			venda.setCadastro(atualizacao.getCadastro());
		}
		if (atualizacao.getIdentificacao() != null) {
			venda.setIdentificacao(atualizacao.getIdentificacao());
		}
		if (atualizacao.getCliente() != null) {
			venda.setCliente(atualizacao.getCliente());
		}
		if (atualizacao.getFuncionario() != null) {
			venda.setFuncionario(atualizacao.getFuncionario());
		}
		if (atualizacao.getVeiculo() != null) {
			venda.setVeiculo(atualizacao.getVeiculo());
		}
		if (atualizacao.getMercadorias() != null && !atualizacao.getMercadorias().isEmpty()) {
			venda.getMercadorias().clear();
			venda.getMercadorias().addAll(atualizacao.getMercadorias());
		}
		if (atualizacao.getServicos() != null && !atualizacao.getServicos().isEmpty()) {
			venda.getServicos().clear();
			venda.getServicos().addAll(atualizacao.getServicos());
		}
		repositorio.save(venda);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirVenda(@PathVariable long id) {
		Optional<Venda> venda = repositorio.findById(id);
		if (venda.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(venda.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
