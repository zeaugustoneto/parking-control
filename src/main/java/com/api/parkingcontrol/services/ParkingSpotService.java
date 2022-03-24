package com.api.parkingcontrol.services;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;

@Service
public class ParkingSpotService {
//camada intermediaria entre o controler e repository, 
	//precisa acionar o repository em determinados casos. exmp: salvar novo registro de vaga, quando for deletar vaga, etc.
	//para isso é preciso criar um ponto de injeção do repository dentro do service, que é o @Autowired
	
	
	//@Autowired //avisa pro spring que aqui é preciso injetar as dependencias mas há outros métodos
	// tal como o construtor usando final e passa a dependencia que vai usar em determinado momento
	final ParkingSpotRepository parkingSpotRepository;
	public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
		this.parkingSpotRepository = parkingSpotRepository;
	}

	@Transactional //caso algo der errado, garante o rollback (voltar ao normal sem dados quebrados)
	public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
		return parkingSpotRepository.save(parkingSpotModel);
	}

	public boolean existsByLicensePlateCar(String licensePlateCar) {
		// TODO Auto-generated method stub
		return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
	}

	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		// TODO Auto-generated method stub
		return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
	}

	public boolean existsByApartmentAndBlock(String apartment, String block) {
		// TODO Auto-generated method stub
		return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
	}

	public List<ParkingSpotModel> findAll() {
		// TODO Auto-generated method stub
		return parkingSpotRepository.findAll();
	}

	public Optional<ParkingSpotModel> findById(UUID id) {
		// TODO Auto-generated method stub
		return parkingSpotRepository.findById(id);
	}

	@Transactional //usado para deleções em cascata(garantir que se algo der errado, ainda há o rollback)
	public void delete(ParkingSpotModel parkingSpotModel) {
		// TODO Auto-generated method stub
		parkingSpotRepository.delete(parkingSpotModel);
		
	}
	
	
	
}
