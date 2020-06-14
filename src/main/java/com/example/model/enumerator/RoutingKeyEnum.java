package com.example.model.enumerator;

public enum RoutingKeyEnum {
	
	VERDE("verde"),
	AMARELO("amarelo"),
	VERMELHO("vermelho");

	private String valor;

	RoutingKeyEnum (String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
    
    public static RoutingKeyEnum getEnum(String valor) {
        for (RoutingKeyEnum routingKey : RoutingKeyEnum.values()) {
            if (routingKey.getValor().equals(valor)) {
                return routingKey;
            }
        }
        return null;
    }
    
}
