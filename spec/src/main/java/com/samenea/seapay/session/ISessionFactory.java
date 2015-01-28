package com.samenea.seapay.session;

public interface ISessionFactory {
	public ISession createSession(String merchantId, String serviceId, String password) throws RequestNotValidException ;
}
