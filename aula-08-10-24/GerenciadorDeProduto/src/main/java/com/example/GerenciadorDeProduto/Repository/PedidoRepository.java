package com.example.GerenciadorDeProduto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.GerenciadorDeProduto.Model.Pedido;
import com.example.GerenciadorDeProduto.Model.Produto;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	Pedido findByProduto(Produto produto);

}
