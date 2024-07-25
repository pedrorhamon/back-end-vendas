package com.starking.vendas.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pedroRhamon
 */

@Getter
@Setter
@AllArgsConstructor
public class RecaptchaResponse {
	
	private boolean success;
    @JsonProperty("error-codes")
    private List<String> errorCodes;

}
