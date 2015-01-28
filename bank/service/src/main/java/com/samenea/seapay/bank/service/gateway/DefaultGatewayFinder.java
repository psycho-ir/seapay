package com.samenea.seapay.bank.service.gateway;

import com.samenea.seapay.bank.gateway.model.GatewayFinder;
import com.samenea.seapay.bank.gateway.model.GatewayNotFoundException;
import com.samenea.seapay.bank.gateway.model.GatewayPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class DefaultGatewayFinder implements GatewayFinder, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(DefaultGatewayFinder.class);
    private ApplicationContext applicationContext;
    private static final String pluginSuffix = "GatewayPlugin";

    @Override
    public GatewayPlugin findByName(String gatewayName) throws GatewayNotFoundException {

        try {
            return this.applicationContext.getBean(gatewayName + pluginSuffix, GatewayPlugin.class);
        } catch (NoSuchBeanDefinitionException ex) {
            logger.error("plugin with name: {} requested but it does not exist!", gatewayName);
            throw new GatewayNotFoundException("Gateway not found: " + gatewayName);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
