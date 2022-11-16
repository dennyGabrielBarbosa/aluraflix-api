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

import com.challenge.videos.controller.dto.VideosDto;
import com.challenge.videos.controller.form.VideosAtualizacaoForm;
import com.challenge.videos.controller.form.VideosForm;
import com.challenge.videos.modelo.Videos;
import com.challenge.videos.repository.CategoriasRepository;
import com.challenge.videos.repository.VideosRepository;

@RestController
@RequestMapping(value = "videos")
public class VideosController {

	@Autowired
	private VideosRepository videosRepository;
	@Autowired
	private CategoriasRepository categoriaRepostiry;

	@GetMapping
	@ResponseBody
	public List<Videos> listarDespesas() {
		return videosRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> BuscaVideoPorId(@PathVariable(value = "id") Long id) {
		if (videosRepository.existsById(id)) {
			return ResponseEntity.ok().body(videosRepository.findById(id));
		}
		return ResponseEntity.badRequest().body("Video não encontrado");
	}

	@PostMapping
	public ResponseEntity<?> CadastraVideo(@Valid @RequestBody VideosForm form) {
		Videos video = form.transformaEmVideo(categoriaRepostiry);
		if (videosRepository.existsById(video.getId())) {
			return ResponseEntity.badRequest().body("Video Já cadastrado");
		}
//		videosRepository.save(video);
		return ResponseEntity.ok(new VideosDto(video));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> AtualizaVideo(@PathVariable(value = "id") Long id,
			@RequestBody @Valid VideosAtualizacaoForm form) {
		Optional<Videos> optional = videosRepository.findById(id);
		if (optional.isPresent()) {
			Videos video = form.atualizar(id, videosRepository);
			return ResponseEntity.ok(new VideosDto(video));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> DeletaVideo(@PathVariable(value = "id") Long id) {
		if (videosRepository.existsById(id)) {
			videosRepository.deleteById(id);
			return ResponseEntity.ok().body("Video deletado com sucesso");
		}
		return ResponseEntity.badRequest().body("Id não encontrado");
	}
}