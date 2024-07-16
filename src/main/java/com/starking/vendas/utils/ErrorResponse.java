package com.starking.vendas.utils;

/**
 * @author pedroRhamon
 */
public record ErrorResponse<T>(T data, String message) {}

