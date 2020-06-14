package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.dto.MessageDTO;

@Service
public class FilaVermelhoService {
	
	public void responderMensagemRecebida(String jsonMensagemRecebida) throws Exception {
		
		System.out.println("Sinal Vermelho - Voce nao pode passar agora. Por favor espere abrir o sinal");
		
		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			JsonNode jsonNode = objectMapper.readTree(jsonMensagemRecebida);
//			Long codigoMensagemRecebida = jsonNode.get("codigo").asLong();
			
			Long codigoMensagemRecebida = Long.valueOf(MessageDTO
					.messageConvertedFromJsonString(jsonMensagemRecebida).get("codigo"));
			
			if (codigoMensagemRecebida > 7L) {
				
				// lancar excecao para simular reenvio de mensagem para fila
				throw new RuntimeException();
			}
			
		} catch (Exception e) {
			throw e;
		}
	}
	
}
