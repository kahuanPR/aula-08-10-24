package com.example.GerenciadorDeProduto.Enum;

public enum StatusEnum {
    ABERTO("Aberto"),
    ENCERRADO("Encerrado"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusEnum(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
