package com.starking.vendas.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pedroRhamon
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequest {

	private String name;

	private String createdAt;

	private String updatedAt;
}
