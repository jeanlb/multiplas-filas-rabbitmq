package com.example.model.dto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageDTO {
	
	private String exchange;
	private String routingKey;
	private Map<String, String> message;
	
	public MessageDTO() {};
	
	public MessageDTO(String exchange, String routingKey, Map<String, String> message) {
		super();
		this.exchange = exchange;
		this.routingKey = routingKey;
		this.message = message;
	}

	public MessageDTO(String exchange, String routingKey, String jsonMessage) {
		super();
		this.exchange = exchange;
		this.routingKey = routingKey;
		this.message = messageConvertedFromJsonString(jsonMessage);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> messageConvertedFromJsonString(String jsonMessage) {
		
		try {
			// convert JSON string to Map
			Map<String, String> mapMensagemRecebida = new ObjectMapper().readValue(jsonMessage, Map.class);
            
           return mapMensagemRecebida;
            
        } catch (IOException e) {
        	e.printStackTrace();
		}
		
		return new HashMap<String, String>();
	}
	
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	public Map<String, String> getMessage() {
		return message;
	}
	public void setMessage(Map<String, String> message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		
		return super.toString();
	}
	
//	public static void main(String[] args) {
//		MessageDTO dto = new MessageDTO();
//		dto.setExchange("multiplas-filas-rabbitmq-topic-exchange");
//		dto.setRoutingKey("verde");
//		
//		Map<String, String> mapMessage = new HashMap<String, String>();
//		mapMessage.put("codigo", "7");
//		mapMessage.put("descricao", "teste de mensagem como json");
//		
//		dto.setMessage(mapMessage);
//		
//		System.out.println(dto.toString());
//	}

}
