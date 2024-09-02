package com.fourcatsdev.aula20.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourcatsdev.aula20.exception.EquipamentoNotFoundException;
import com.fourcatsdev.aula20.modelo.Equipamento;
import com.fourcatsdev.aula20.repository.EquipamentoRepository;


@Service
public class EquipamentoService {

	@Autowired
	private EquipamentoRepository equipamentoRepository;
	
	public Equipamento criarEquipamento(Equipamento equipamento) {
		return equipamentoRepository.save(equipamento);
	}
	
	public List<Equipamento> buscarTodosEquipamentos() {
		return equipamentoRepository.findAll();
	}
	
	public Equipamento buscarEquipamentoPorId(Long id)throws EquipamentoNotFoundException {
		Optional<Equipamento> opt = equipamentoRepository.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new EquipamentoNotFoundException("Equipamento com id : " + id + " não existe");
		}		
	}	
	
	public void apagarEquipamento(Long id) throws EquipamentoNotFoundException {
		Equipamento equipamento = buscarEquipamentoPorId(id);
		equipamentoRepository.delete(equipamento);
	}
	
	public Equipamento alterarEquipamento(Equipamento equipamento) {
		return equipamentoRepository.save(equipamento);
	}
	
	public List<Equipamento> buscarTodosEquipamentosPorNome(String nome){
		return equipamentoRepository.findByNomeOrFabricanteContainingIgnoreCase(nome, nome);
	}
	
	
	
	
	
	
	
	
}
