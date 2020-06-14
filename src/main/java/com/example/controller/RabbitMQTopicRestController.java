package com.example.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.dto.MessageDTO;

@RestController
@RequestMapping(value = "/rest")
public class RabbitMQTopicRestController {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		String response = "OK";
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/producer/{exchangeName}/{routingKey}/{message}")
	public String producer(@PathVariable("exchangeName") String exchange, @PathVariable("routingKey") String routingKey,
			@PathVariable("message") String message) {

		amqpTemplate.convertAndSend(exchange, routingKey, message);

		return "Mensagem enviada com sucesso via metodo HTTP GET";
	}
	
	@PostMapping(value = "/producer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String producer(@RequestBody MessageDTO dto) {

		amqpTemplate.convertAndSend(dto.getExchange(), dto.getRoutingKey(), dto.getMessage());
		
		return "Mensagem enviada com sucesso via metodo HTTP POST";
	}
	
}
