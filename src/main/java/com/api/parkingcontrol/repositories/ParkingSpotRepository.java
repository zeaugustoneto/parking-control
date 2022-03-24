package com.api.parkingcontrol.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.parkingcontrol.models.ParkingSpotModel;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID> { // model e identificador; 
	//extendemos o JpaRepository pois ele possui varios metodos prontos para se utilizar em transações com 
	//banco de dados exmp: buscar listagem de recurso, buscar recurso unico, deletar, atualizar. 
	//se nao fosse isso, teriamos que criar tudo na mão
	
	// nao é necessario utilizar a anotação nessa interface pois extendemos o JpaRepository, mas se quiser pode
	

	boolean existsByLicensePlateCar(String licensePlateCar);
	boolean existsByParkingSpotNumber(String parkingSpotNumber);
	boolean existsByApartmentAndBlock(String apartment, String block);
}
