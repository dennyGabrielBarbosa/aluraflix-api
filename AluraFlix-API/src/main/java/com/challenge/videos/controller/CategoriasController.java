package com.challenge.videos.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.videos.controller.dto.CategoriasDto;
import com.challenge.videos.controller.form.CategoriasAtualizacaoForm;
import com.challenge.videos.controller.form.CategoriasForm;
import com.challenge.videos.modelo.Categorias;
import com.challenge.videos.repository.CategoriasRepository;

@RestController
@RequestMapping(value = "categorias")
public class CategoriasController {

	@Autowired
	private CategoriasRepository categoriasRepository;

	@GetMapping
	@ResponseBody
	public ResponseEntity<Stream<CategoriasDto>> listarCategorias() {
		List<Categorias> categorias = categoriasRepository.findAll();
		return ResponseEntity.ok().body(categorias.stream().map(CategoriasDto::new));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoriasDto> BuscaCategoriaPorId(@PathVariable(value = "id") Long id) {
		Optional<Categorias> optional = categoriasRepository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok().body(new CategoriasDto(optional.get()));
		}
		return ResponseEntity.badRequest().build();
	}

	@PostMapping
	@Transactional
	public ResponseEntity<CategoriasDto> CadastraCategoria(@Valid @RequestBody CategoriasForm form) {
		Categorias categoria = form.transformaEmCategorias(categoriasRepository);
		if (categoriasRepository.existsById(categoria.getId())) {
			return ResponseEntity.badRequest().build();
		}
		categoriasRepository.save(categoria);
		return ResponseEntity.ok().body(new CategoriasDto(categoria));
	}

	@PutMapping
	public ResponseEntity<CategoriasDto> AtualizaCategorias(@PathVariable(value = "id") Long id,
			@RequestBody @Valid CategoriasAtualizacaoForm form) {
		Optional<Categorias> optional = categoriasRepository.findById(id);
		if (optional.isPresent()) {
			Categorias categoria = form.atualizar(id, categoriasRepository);
			return ResponseEntity.ok(new CategoriasDto(categoria));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> DeletaCategoria(@PathVariable(value = "id") Long id) {
		if (id == 1) {
			return ResponseEntity.badRequest().body("Esta categoria não pode ser deletada");
		}
		if (categoriasRepository.existsById(id)) {
			categoriasRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}
}
