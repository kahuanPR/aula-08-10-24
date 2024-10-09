package com.example.GerenciadorDeProduto.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.GerenciadorDeProduto.Model.Pedido;
import com.example.GerenciadorDeProduto.Model.Produto;
import com.example.GerenciadorDeProduto.Enum.StatusEnum;
import com.example.GerenciadorDeProduto.Repository.PedidoRepository;
import com.example.GerenciadorDeProduto.Repository.ProdutoRepository;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("pedidos", pedidoRepository.findAll()); // Adicione esta linha para listar pedidos
        return "produtos";
    }

    @PostMapping
    public String criarProduto(Produto produto) {
        produtoRepository.save(produto);
        criarPedido(produto);
        return "redirect:/produtos";
    }

    private void criarPedido(Produto produto) {
        Pedido pedido = new Pedido();
        pedido.setProduto(produto);
        pedido.setQuantidade(1); // Defina uma quantidade padrão
        pedido.setData(LocalDate.now()); // Defina a data atual
        pedido.setValor(produto.getPreco()); // O valor do pedido pode ser igual ao preço do produto
        pedido.setStatus(StatusEnum.ABERTO); 
        pedidoRepository.save(pedido);
    }

    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable Long id, Model model) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        model.addAttribute("produto", produto);
        return "editarProduto"; // Nome do template para edição
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarProduto(@PathVariable Long id, Produto produtoAtualizado) {
        produtoAtualizado.setId(id); // Manter o ID do produto existente
        produtoRepository.save(produtoAtualizado);
        return "redirect:/produtos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarProduto(@PathVariable Long id) {
        // Primeiro, buscamos o produto
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        
        // Depois, buscamos o último pedido associado a este produto
        Pedido ultimoPedido = pedidoRepository.findByProduto(produto);
        if (ultimoPedido != null) {
            pedidoRepository.delete(ultimoPedido); // Deleta o último pedido
        }
        
        // Finalmente, deletamos o produto
        produtoRepository.delete(produto);
        return "redirect:/produtos";
    }
}
