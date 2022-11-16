package com.challenge.videos.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

@Entity
public class Categorias {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotEmpty(message = "O campo é obrigatório")
	private String titulo;

	@NotEmpty(message = "O campo é obrigatório")
	private String cor;
	
	@OneToMany(mappedBy = "categoria")
	private List<Videos> videos;

	public Categorias() {
	}

	public List<Videos> getVideos() {
		return videos;
	}

	public void setVideos(List<Videos> video) {
		this.videos = video;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}
}