package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class FilaAmareloService {
	
	public void responderMensagemRecebida() {
		System.out.println("Sinal Amarelo - Atencao, daqui a pouco o sinal vai fechar");
	}

}
