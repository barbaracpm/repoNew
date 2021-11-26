package com.formacionjava.springboot.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Añadimos tabla clientes
@Entity
@Table(name = "clientes")
//prepara los atributos y métodos para que no tenga problemas en convertir esto en propiedades de base de datos
public class Cliente implements Serializable {
	
	//Indicamos que la variable inferior será un ID
	@Id
	//generamos valor AI, PK, etc
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
	//Hacemos que las columnas sean obligatorias con nullable=fase 
	@Column(nullable = false)
	private String nombre;
	
	@Column(nullable = false)
	private String apellido;
	
	//Hacemos que las columnas sean obligatorias con nullable=false y el mail y teléfono sea único
	@Column(nullable = false, unique=true)
	private String email;
	
	@Column(nullable = false, unique=true)
	private int telefono;
	
	//Nombrar columna de mi tabla y escoger tipo de dato
	@Column(name="created_at")
	@Temporal(TemporalType.DATE)
	private Date createdAt;
	
	//PARA SUBIDA DE ARCHIVOS
	private String imagen;
	
	
	//indicamos que no puede estar vacío con not null y mandamos  un mensajes (igual que nullable=false)
	@NotNull(message="No puede estar vacío")
	//Se trata de una subconsulta a la tabla region
	//La palabra de la izda (many) siempre hace referencia a la entidad en la que te encuentras
	//pasamos tipo de entity (Region), no un valor de dato STRIGN
	//FetchType.LAZY (esta subconsulta me la haces solo a través de un método
	@ManyToOne(fetch = FetchType.LAZY)
	//para ayudar a la carga perezosa añadiimos la linea de abajo (cuando usamos el fetch type lazy tenemos q añadirlo.
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	//creamos nombre de la columna
	@JoinColumn(name = "region_id")
	private Region region;
	
	@PrePersist
	public void prePersist() {
		createdAt = new Date();
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getTelefono() {
		return telefono;
	}
	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	

	public Region getRegion() {
		return region;
	}


	public void setRegion(Region region) {
		this.region = region;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}