package com.example.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.listener.MultiplasFilasRabbitmqListener;
import com.example.model.enumerator.RoutingKeyEnum;

@Configuration
public class RabbitMQTopicConfig {
	
	@Value("${rabbitmq.topic.exchange.name}")
	private String topicExchangeName; 
	
	@Value("${rabbitmq.fila.verde}")
	private String filaVerdeNome;
	
	@Value("${rabbitmq.fila.amarelo}")
	private String filaAmareloNome;
	
	@Value("${rabbitmq.fila.vermelho}")
	private String filaVermelhoNome;
	
	// Exchange: gerencia para que fila a mensagem deve ir
	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange(topicExchangeName);
	}
	
	// Filas
	@Bean
	Queue filaVerde() {
		return new Queue(filaVerdeNome, Boolean.FALSE);
	}
	
	@Bean
	Queue filaAmarelo() {
		return new Queue(filaAmareloNome, Boolean.FALSE);
	}
	
	@Bean
	Queue filaVermelho() {
		return new Queue(filaVermelhoNome, Boolean.FALSE);
	}
	
	// Bindings
	@Bean
	Binding filaVerdeBinding(Queue filaVerde, TopicExchange topicExchange) {
		// bind = queue/fila, to = exchange, with = routing key
		return BindingBuilder.bind(filaVerde).to(topicExchange).with(RoutingKeyEnum.VERDE.getValor());
	}
	
	@Bean
	Binding filaAmareloBinding(Queue filaAmarelo, TopicExchange topicExchange) {
		return BindingBuilder.bind(filaAmarelo).to(topicExchange).with(RoutingKeyEnum.AMARELO.getValor());
	}
	
	@Bean
	Binding filaVermelhoBinding(Queue filaVermelho, TopicExchange topicExchange) {
		return BindingBuilder.bind(filaVermelho).to(topicExchange).with(RoutingKeyEnum.VERMELHO.getValor());
	}
	
	// Listener configuration
	@Bean
	MessageListenerAdapter listenerAdapter(MultiplasFilasRabbitmqListener receiver) {
		return new MessageListenerAdapter(receiver);
	}
	
	@Bean
	MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, 
			MessageListenerAdapter listenerAdapter) {
		
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		
		// filas a serem observadas
		simpleMessageListenerContainer.setQueues(
				filaVerde(), 
				filaAmarelo(),
				filaVermelho()
			);
		
		simpleMessageListenerContainer.setMessageListener(listenerAdapter);
		
		return simpleMessageListenerContainer;
	}
	
	// Converter corretamente mensagens que estejam no formato JSON
	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	    rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
	    
	    return rabbitTemplate;
	}
	
}