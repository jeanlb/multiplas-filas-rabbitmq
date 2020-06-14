package com.example.listener;

import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.model.dto.MessageDTO;
import com.example.model.enumerator.RoutingKeyEnum;
import com.example.service.FilaAmareloService;
import com.example.service.FilaVerdeService;
import com.example.service.FilaVermelhoService;

@Component
public class MultiplasFilasRabbitmqListener implements MessageListener {
	
	@Autowired
	private FilaVerdeService filaVerdeService;
	
	@Autowired
	private FilaAmareloService filaAmareloService;
	
	@Autowired
	private FilaVermelhoService filaVermelhoService;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${rabbitmq.fila.reenviar.delay}")
	private String delayParaReenviarParaFila; 
	
	public void onMessage(Message message) {
		
		String routingKeyRecebida = message.getMessageProperties().getReceivedRoutingKey();
		String jsonMensagemRecebida = new String(message.getBody(), StandardCharsets.UTF_8);
		
		System.out.println("Routing Key - " + routingKeyRecebida
        		+ ", Consuming Message - " + jsonMensagemRecebida);
		
		RoutingKeyEnum routingKey = RoutingKeyEnum.getEnum(routingKeyRecebida);
		
		try {
			
			switch (routingKey) {
			
				case VERDE:
					filaVerdeService.responderMensagemRecebida();
					break;
					
				case AMARELO:
					filaAmareloService.responderMensagemRecebida();
					break;
					
				case VERMELHO:
					filaVermelhoService.responderMensagemRecebida(jsonMensagemRecebida);
					break;
					
				default:
					break;
			}
			
		} catch(Exception e) {
			reenviarMensagemParaFila(message.getMessageProperties().getReceivedExchange(), routingKeyRecebida, jsonMensagemRecebida);
		}
	}
	
	private void reenviarMensagemParaFila(String exchange, String routingKeyRecebida, String jsonMensagemRecebida) {
		
		Long delay = Long.valueOf(delayParaReenviarParaFila);
		System.out.println(String.format("Uma excecao ocorreu, reenviando mensagem para fila em %s milisegundos", delay));
		
		// Enviar mensagem novamente para fila
		TimerTask task = new TimerTask() {

			public void run() {
				MessageDTO dto = new MessageDTO(exchange, routingKeyRecebida, jsonMensagemRecebida);
				amqpTemplate.convertAndSend(exchange, routingKeyRecebida, dto.getMessage());
			}
		};
		
		Timer timer = new Timer("TimerMessageResend");
		timer.schedule(task, delay);
	}
	
}
