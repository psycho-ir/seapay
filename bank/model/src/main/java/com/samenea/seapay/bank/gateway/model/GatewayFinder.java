package com.samenea.seapay.bank.gateway.model;

public interface GatewayFinder {
	GatewayPlugin findByName(String gatewayName) throws GatewayNotFoundException;

}
