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

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.modelos.AdicionadorLinkEmpresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

	@Autowired
	private RepositorioEmpresa repositorio;

	@Autowired
	private AdicionadorLinkEmpresa adicionadorLink;

	@GetMapping("/empresas")
	public ResponseEntity<List<Empresa>> obterEmpresas() {
		List<Empresa> empresas = repositorio.findAll();
		if (empresas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(empresas);
		return new ResponseEntity<>(empresas, HttpStatus.OK);
	}

	@GetMapping("/empresa/{id}")
	public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
		Optional<Empresa> empresa = repositorio.findById(id);
		if (empresa.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(empresa.get());
		return new ResponseEntity<>(empresa.get(), HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa) {
		if (empresa.getId() == null) {
			repositorio.save(empresa);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEmpresa(@RequestBody Empresa atualizacao) {
		Optional<Empresa> empresaOptional = repositorio.findById(atualizacao.getId());
		if (empresaOptional.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Empresa empresa = empresaOptional.get();
		// atualiza campos simples
		if (atualizacao.getRazaoSocial() != null) {
			empresa.setRazaoSocial(atualizacao.getRazaoSocial());
		}
		if (atualizacao.getNomeFantasia() != null) {
			empresa.setNomeFantasia(atualizacao.getNomeFantasia());
		}
		if (atualizacao.getCadastro() != null) {
			empresa.setCadastro(atualizacao.getCadastro());
		}
		if (atualizacao.getEndereco() != null) {
			empresa.setEndereco(atualizacao.getEndereco());
		}
		if (atualizacao.getTelefones() != null && !atualizacao.getTelefones().isEmpty()) {
			empresa.getTelefones().clear();
			empresa.getTelefones().addAll(atualizacao.getTelefones());
		}
		repositorio.save(empresa);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirEmpresa(@PathVariable long id) {
		Optional<Empresa> empresa = repositorio.findById(id);
		if (empresa.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(empresa.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
