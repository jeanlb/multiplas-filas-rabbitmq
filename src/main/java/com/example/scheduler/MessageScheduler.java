package com.example.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.model.dto.MessageDTO;
import com.example.model.enumerator.RoutingKeyEnum;

@Component
public class MessageScheduler {
	
	@Value("${rabbitmq.topic.exchange.name}")
	private String topicExchangeName;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Scheduled(fixedRate = 60000)
	public void mandarMensagensParaFila() {
		
		// mandar mensagem para fila verde
		Map<String, String> mapMensagem = new HashMap<String, String>();
		mapMensagem.put("codigo", "7");
		mapMensagem.put("descricao", "mensagem para fila verde");
		
		MessageDTO dto = new MessageDTO(topicExchangeName, RoutingKeyEnum.VERDE.getValor(), mapMensagem);
		
		amqpTemplate.convertAndSend(dto.getExchange(), dto.getRoutingKey(), dto.getMessage());
		
		// mandar mensagem para fila amarelo
		mapMensagem.clear();
		mapMensagem.put("codigo", "10");
		mapMensagem.put("descricao", "mensagem para fila amarelo");
		
		dto.setRoutingKey(RoutingKeyEnum.AMARELO.getValor());
		dto.setMessage(mapMensagem);
		
		amqpTemplate.convertAndSend(dto.getExchange(), dto.getRoutingKey(), dto.getMessage());
		
		// mandar mensagem para fila vermelho
		mapMensagem.clear();
		mapMensagem.put("codigo", "2");
		mapMensagem.put("descricao", "mensagem para fila vermelho");
		
		dto.setRoutingKey(RoutingKeyEnum.VERMELHO.getValor());
		dto.setMessage(mapMensagem);
		
		amqpTemplate.convertAndSend(dto.getExchange(), dto.getRoutingKey(), dto.getMessage());
		
		System.out.println(String.format("Mensagens enviadas pelo scheduler as %s", dateFormat.format(new Date())));
	}
	
}