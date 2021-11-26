package com.formacionjava.springboot.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.formacionjava.springboot.apirest.models.entity.Cliente;
import com.formacionjava.springboot.apirest.models.entity.Region;


//CRUD - create, read, add, delete
//heredamos de la clase de librería CrudRepository, que incorpora muchos métodos
//Vamos a crear el método de región dentro del DAO Cliente ya que solo nos interesa consultar la región y no hacer el crud completo. Si fuera así haríamos su propio archivo dao
public interface ClienteDao extends CrudRepository<Cliente, Long> {

	//notación para pasar la query que necesite. Referenciamos desde donde vamos a llamar al método.
	@Query("from Region")
	public List<Region> findAllRegions();
	
	
}


