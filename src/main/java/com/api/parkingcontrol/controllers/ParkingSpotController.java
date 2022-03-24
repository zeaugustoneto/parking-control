package com.api.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;



// camada recebe as soliticaçõs (get, delete, push, post) e aciona o service que vai acionar o repository e fazer todas essas transações
//com a base de dados

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // permitir que seja acessado de qualquer fonte
@RequestMapping("/parking-spot")
public class ParkingSpotController {
	
	final ParkingSpotService parkingSpotService;
// ponto de injeção
	public ParkingSpotController(ParkingSpotService parkingSpotService) {
		this.parkingSpotService = parkingSpotService;
	}
	
	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){ //dto vai vir como json pelo requestbody
		if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
		}
		
		if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
		}
		
		if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
		}
		
		//sem o @Valid, nenhuma validação vai ser validada
		var parkingSpotModel = new ParkingSpotModel(); //dentro de um escopo fechado, posso usar o var no novo JDK
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel); //converter dto para model (há varias maneiras). BeanUtils.copyProperties
		// é bem util e simples pois passo o que vai ser convertido e para qual tipo vai ser convertido (dto -> model)
		parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC"))); // seta a data de registro (não há no dto)
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel)); 
		
	}
	
	//método getall (usa lista pois retorna todas vagas, mesmo que vazias)
	@GetMapping
	public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpots(){
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll()); //envia listagem das vagas de estacionamento
	}
	
	//metodo getone (usa object pois retorna apenas um, assim como no POST - pois caso não exista o registro, é preciso retornar ao cliente)
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
		Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
		if(!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
		
	}
	
	//método DELETE
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value="id") UUID id){
		Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id); //usa o mesmo método anterior findById
		if(!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		parkingSpotService.delete(parkingSpotModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
													@RequestBody @Valid ParkingSpotDto parkingSpotDto){ //validação do dto para fazer as mudanças
		Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
		if(!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
	
		
		//há dois modos de usar o PUT para atualizações
		//1 modo 
		//var parkingSpotModel = parkingSpotModelOptional.get(); //aproveita o registro optional mas nao inicia, é apenas um aproveitamento
		// e atualiza um por um como abaixo, pois não há como saber qual campo vai ser atualizado
		//parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
		// -----------------------------------------------------
		//2 modo
		var parkingSpotModel = new ParkingSpotModel(); //usado apenas no escopo definido // inves de obter o optional, é possivel criar uma nova instancia
		//de parking model, fazer a conversao com beanutils como no método POST, seta apenas o id e a registration date para permanecer
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
		parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
	}
	
	

}
