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

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelos.AdicionadorLinkUsuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

	@Autowired
	private RepositorioUsuario repositorio;

	@Autowired
	private AdicionadorLinkUsuario adicionadorLink;

	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		if (usuarios.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(usuarios);
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}

	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
		Optional<Usuario> usuario = repositorio.findById(id);
		if (usuario.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLink(usuario.get());
		return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
		if (usuario.getId() == null) {
			repositorio.save(usuario);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao) {
		Optional<Usuario> usuarioOptional = repositorio.findById(atualizacao.getId());
		if (usuarioOptional.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Usuario usuario = usuarioOptional.get();
		if (atualizacao.getNome() != null) {
			usuario.setNome(atualizacao.getNome());
		}
		if (atualizacao.getNomeSocial() != null) {
			usuario.setNomeSocial(atualizacao.getNomeSocial());
		}
		if (atualizacao.getPerfis() != null && !atualizacao.getPerfis().isEmpty()) {
			usuario.getPerfis().clear();
			usuario.getPerfis().addAll(atualizacao.getPerfis());
		}
		if (atualizacao.getTelefones() != null && !atualizacao.getTelefones().isEmpty()) {
			usuario.getTelefones().clear();
			usuario.getTelefones().addAll(atualizacao.getTelefones());
		}
		if (atualizacao.getEndereco() != null) {
			usuario.setEndereco(atualizacao.getEndereco());
		}
		if (atualizacao.getDocumentos() != null && !atualizacao.getDocumentos().isEmpty()) {
			usuario.getDocumentos().clear();
			usuario.getDocumentos().addAll(atualizacao.getDocumentos());
		}
		if (atualizacao.getEmails() != null && !atualizacao.getEmails().isEmpty()) {
			usuario.getEmails().clear();
			usuario.getEmails().addAll(atualizacao.getEmails());
		}
		if (atualizacao.getCredenciais() != null && !atualizacao.getCredenciais().isEmpty()) {
			usuario.getCredenciais().clear();
			usuario.getCredenciais().addAll(atualizacao.getCredenciais());
		}
		repositorio.save(usuario);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable long id) {
		Optional<Usuario> usuario = repositorio.findById(id);
		if (usuario.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(usuario.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
