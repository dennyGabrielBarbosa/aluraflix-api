package com.challenge.videos.controller;

import java.util.List;
import java.util.Optional;

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
	public List<Categorias> ListaCategorias() {
		return categoriasRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> BuscaCategoriaPorId(@PathVariable(value = "id") Long id) {
		if (categoriasRepository.existsById(id)) {
			return ResponseEntity.ok().body(categoriasRepository.findById(id));
		}
		return ResponseEntity.badRequest().body("Não encontrado");
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> CadastraCategoria(@Valid @RequestBody CategoriasForm form) {
		Categorias categoria = form.transformaEmCategorias(categoriasRepository);

		if (categoriasRepository.existsById(categoria.getId())) {
			return ResponseEntity.badRequest().body("Categoria já cadastrada");
		}
//		categoriasRepository.save(categoria);
		return ResponseEntity.ok().body(categoria);
	}

	@PutMapping
	public ResponseEntity<?> AtualizaCategorias(@PathVariable(value = "id") Long id,
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
//			categoriasRepository.deleteById(id);
			return ResponseEntity.ok().body("Categoria Deletada com sucesso");
		}

		return ResponseEntity.badRequest().body("Id não encontrado");
	}
}
