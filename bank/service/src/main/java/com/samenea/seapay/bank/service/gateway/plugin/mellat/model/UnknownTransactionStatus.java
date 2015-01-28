package com.samenea.seapay.bank.service.gateway.plugin.mellat.model;

public enum UnknownTransactionStatus {
    /**
     * When we have problem in settle
     *      and we are not sure about it the unknow transacton has this status
     */
    SETTLE_PROBLEM,
    /**
     * when we could solve the problem of UnknowTransaction the UnknowTransaction marked as SOLVED
     */
    SOLVED,
    /**
     *when we can't settle because it's not verified before it.
     **/
    UNRESOLVED;
}
