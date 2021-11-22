package com.formacionjava.springboot.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.formacionjava.springboot.apirest.models.entity.Cliente;


//CRUD - create, read, add, delete
//heredamos de la clase de librería CrudRepository, que incorpora muchos métodos
public interface ClienteDao extends CrudRepository<Cliente, Long> {

}
