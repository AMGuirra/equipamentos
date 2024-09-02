package com.fourcatsdev.aula20.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fourcatsdev.aula20.modelo.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	@Query(value = "select distinct usuario_id as id, CAST(data_pedido AS date) as data, nome \n"
			     + "  from pedido \n"
			     + "  join usuario \n"
		         + "    on usuario.id = pedido.usuario_id where usuario.id = :idUsuario", nativeQuery = true)
	List<PedidoResponseData> buscarPedidoDeUsuario(@Param("idUsuario") Long idUsuario);
	
	@Query(value = "select pedido.id, pedido.data_devolucao as devolucao, pedido.data_emprestimo as emprestimo,  pedido.estado_pedido_id as estado, "
			+ "       equipamento.nome, equipamento.numero_de_serie as numero, equipamento.fabricante  "
			+ "  from pedido "
			+ "  join usuario  "
			+ "    on usuario.id = pedido.usuario_id "
			+ "  join equipamento "
			+ "	on equipamento.id = pedido.equipamento_pedido_id "
			+ " where pedido.usuario_id = :idUsuario"
			+ "   and CAST(pedido.data_pedido AS date) = cast(:dataPedido as date) ", nativeQuery = true)
    List<PedidoResponseEquipamento> buscarPedidoEquipamento(@Param("idUsuario") Long idUsuario, @Param("dataPedido") String data);

	@Query(value = "select distinct usuario_id as id, CAST(data_pedido AS date) as data, nome \n"
		     + "  from pedido \n"
		     + "  join usuario \n"
	         + "    on usuario.id = pedido.usuario_id", nativeQuery = true)
List<PedidoResponseData> buscarPedidoDeUsuario();
	
}
