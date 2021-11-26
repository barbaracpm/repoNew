package com.formacionjava.springboot.apirest.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formacionjava.springboot.apirest.models.entity.Cliente;
import com.formacionjava.springboot.apirest.models.entity.Region;
import com.formacionjava.springboot.apirest.models.service.ClienteService;

import io.swagger.annotations.Api;

//CREAMOS EL CONTROLADOR

//De qué manera lo mostraremos (especificar url)
//puede haber muchos controladores por separar pero no siempre hace falta

@RestController
//nombrar api en swagger
@Api(tags = "Documentation Api SpringBoot")
//url enrutador o cabecera 
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private ClienteService clienteService;

	// Petición GET
	@GetMapping("/clientes")
	//@ApiOperation(value = "Devuelve el listado completo de los clientes", notes="descripción larga")
	public List<Cliente> index() {
		return clienteService.findAll();
	}

	// petición GET2 (cliente específico por id)
	/*
	 * @GetMapping("/clientes/{id}") public Cliente show(@PathVariable Long id) {
	 * return clienteService.findById(id); }
	 */

	// Petición GET2 (cliente específico por ID)
	// Con control de errores

	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();

		// Manejar errores del servidor
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error.al realizar consulta en la BBDD");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Manejar error de que el id consultado no existe
		if (cliente == null) {
			response.put("mensaje", "El cliente ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}

	// POST
	/*
	 * @PostMapping("/clientes") //recibir respuesta
	 * 
	 * @ResponseStatus(HttpStatus.CREATED) public Cliente create(@RequestBody
	 * Cliente cliente) { return clienteService.save(cliente); }
	 */

	// MÉTODO POST PARA SUBIR NUEVOS DATOS DE CLIENTES
	@PostMapping("/clientes")
	// recibir respuesta
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {
		Cliente clienteNew = null;
		Map<String, Object> response = new HashMap<>();

		try {
			clienteNew = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar consulta en la BBDD");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente ha sido creado con éxito!");
		response.put("cliente", clienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/*
	 * @PutMapping("/clientes/{id}")
	 * 
	 * @ResponseStatus(HttpStatus.CREATED) public Cliente create(@RequestBody
	 * Cliente cliente, @PathVariable Long id) { Cliente clienteUpdate =
	 * clienteService.findById(id);
	 * 
	 * clienteUpdate.setApellido(cliente.getApellido());
	 * clienteUpdate.setNombre(cliente.getNombre());
	 * clienteUpdate.setEmail(cliente.getEmail());
	 * 
	 * return clienteService.save(clienteUpdate);
	 * 
	 * }
	 */

	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente clienteActual = clienteService.findById(id);

		Cliente clienteUpdated = null;
		Map<String, Object> response = new HashMap<>();

		if (clienteActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el cliente ID "
					.concat(id.toString().concat("no existe el id en la bbdd.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setTelefono(cliente.getTelefono());
			clienteActual.setCreatedAt(cliente.getCreatedAt());
			clienteActual.setRegion(cliente.getRegion());
			/*
			 * if(cliente.getCreatedAt() == null) {
			 * clienteActual.setCreatedAt(cliente.getCreatedAt()) } else {
			 * clienteActual.setCreatedAt(clienteActual.getCreatedAt())}
			 */
			clienteUpdated = clienteService.save(clienteActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar consulta en la BBDD");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente ha sido actualizado con éxito!");
		response.put("cliente", clienteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	/*
	 * @DeleteMapping("clientes/{id}") public void delete(@PathVariable Long id) {
	 * clienteService.delete(id); }
	 */

	@DeleteMapping("clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			clienteService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el cliente de la BBDD");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	// MÉTODO POST PARA SUBIR ARCHIVOS
	@PostMapping("clientes/uploads")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();

		Cliente cliente = clienteService.findById(id);

		if (!archivo.isEmpty()) {
			String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", " ");
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();

			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);

			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			String nombreFotoAnterior = cliente.getImagen();

			if (nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();

				if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}

			}

			cliente.setImagen(nombreArchivo);

			clienteService.save(cliente);

			response.put("cliente", cliente);
			response.put("mensaje", "Has subido correctamente la imagen " + nombreArchivo);

		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}
	
	//Método GET para recuperar foto
	//mirar en el navegador: http://localhost:8080/api/uploads/img/99b37205-10e1-41b0-b18a-c35b165ffaaf_PERRETE-300x300.png
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		
		Resource recurso= null;
		
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		if(!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("Error no se puede cargar la imagen "+ nombreFoto);
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+recurso.getFilename()+"\"");
		
		return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
	}
	
	@GetMapping("clientes/regiones")
	public List<Region> listarRegiones(){
		
		return clienteService.findAllRegions();
	}

}


