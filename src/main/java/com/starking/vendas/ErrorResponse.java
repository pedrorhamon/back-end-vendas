package com.starking.vendas;

/**
 * @author pedroRhamon
 */
public record ErrorResponse<T>(T data, String message) {}

