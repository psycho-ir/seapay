package com.samenea.seapay.bank.service.gateway.plugin.mellat.ws;

public class MellatWSResponse {

    private String resultCode = null;
    private String referenceId = null;

    public String getReferenceId() {
        if (isOK()) {
            return referenceId;
        }
        return "";

    }

    public MellatWSResponse(String serverCode) {
        refineServerCode(serverCode);

    }

    public String getResultCode() {
        return resultCode;
    }

    public boolean isOK() {
        return "0".equals(resultCode);
    }

    public boolean isSettleBefore(){
        return "45".equals(resultCode);
    }

    private void refineServerCode(String serverCode) {
        String[] messages = serverCode.split(",");
        resultCode = messages[0].trim();
        if (messages.length > 1) {
            referenceId = messages[1].trim();
        }
    }

    @Override
    public String toString() {
        return "MellatWSResponse{" +
                "resultCode='" + resultCode + '\'' +
                ", referenceId='" + referenceId + '\'' +
                '}';
    }
}
