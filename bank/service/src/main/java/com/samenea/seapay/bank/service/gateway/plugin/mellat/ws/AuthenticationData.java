/**
 * User: Soroosh Sarabadani
 * Date: 12/29/12
 * Time: 3:04 PM
 */
package com.samenea.seapay.bank.service.gateway.plugin.mellat.ws;

public class AuthenticationData {
    private final long terminalId;
    private final String username;
    private final String password;

    public AuthenticationData(long terminalId,String username , String password){
        this.terminalId = terminalId;
        this.username = username;
        this.password = password;
    }

    public long getTerminalId() {
        return terminalId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "AuthenticationData{" +
                "terminalId=" + terminalId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
