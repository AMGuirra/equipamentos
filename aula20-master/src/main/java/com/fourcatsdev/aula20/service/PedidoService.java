package com.fourcatsdev.aula20.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourcatsdev.aula20.exception.EquipamentoNotFoundException;
import com.fourcatsdev.aula20.modelo.Equipamento;
import com.fourcatsdev.aula20.modelo.EstadoPedido;
import com.fourcatsdev.aula20.modelo.Pedido;
import com.fourcatsdev.aula20.modelo.Usuario;
import com.fourcatsdev.aula20.repository.PedidoRepository;
import com.fourcatsdev.aula20.repository.PedidoResponseData;
import com.fourcatsdev.aula20.repository.PedidoResponseEquipamento;

@Service
public class PedidoService {

	
	@Autowired //injeçao de dependencia
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private EstadoPedidoService estadoPedidoService;
	
	@Autowired
	private EquipamentoService equipamentoService;
	
	public void gravar(Usuario usuario, String dataEmprestimo, String dataDevolucao, String finalidade, int[] ids) throws ParseException, EquipamentoNotFoundException {
		
		EstadoPedido estadoPedido = estadoPedidoService.buscarPorNome("Em análise");		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");		
		Date dateEmprestimo = formatter.parse(dataEmprestimo);
		Date dateDevolucao = formatter.parse(dataDevolucao);
		
		for(int i = 0; i < ids.length; i++) {
			
			Pedido pedido = new Pedido();
			pedido.setDataEmprestimo(dateEmprestimo);
			pedido.setDataDevolucao(dateDevolucao);
			pedido.setFinalidade(finalidade);
			pedido.setDataPedido(new Date());
			pedido.setUsuario(usuario);
			pedido.setEstadoPedido(estadoPedido);
			Equipamento equipamento = equipamentoService.buscarEquipamentoPorId((long) ids[i]);
			pedido.setEquipamentoPedido(equipamento);			
			Pedido p = pedidoRepository.save(pedido);
			System.out.println( "Gravou " + p.getId());
		}
	}
	
	public List<Pedido> listar(){
		return pedidoRepository.findAll();
	}
	
	public Pedido buscarPedidoPorId(Long id) {
		Optional<Pedido> opt = pedidoRepository.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new IllegalArgumentException("Pedido com id : " + id + " não existe");
		}
	}
		
	public List<PedidoResponseData> buscarPedidoDeUsuario(Long id) {
		return pedidoRepository.buscarPedidoDeUsuario(id);
	}
	
	public void apagarPedidoPorId(Long id) {
		Pedido pedido = buscarPedidoPorId(id);
		pedidoRepository.delete(pedido);		
	}
	
	public void alterarPedido(Pedido pedido) {
		pedidoRepository.save(pedido);		
	}
	
	public List<PedidoResponseData> buscarPedido() {
		List<PedidoResponseData> pedidos = 	pedidoRepository.buscarPedidoDeUsuario();
		return pedidos;
	}
	
	public List<PedidoResponseEquipamento> buscarPedidoEquipamento(Long idUsuario, String dataPedido) {
		List<PedidoResponseEquipamento> pedidos = 	pedidoRepository.buscarPedidoEquipamento(idUsuario, dataPedido);
		return pedidos;
	}
	
	public void salvar(Pedido pedido) {
        pedidoRepository.save(pedido);
    }
	
	
}
