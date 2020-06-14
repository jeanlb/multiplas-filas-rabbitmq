package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class FilaVerdeService {
	
	public void responderMensagemRecebida() {
		System.out.println("Sinal Verde - Pode passar agora");
	}

}
