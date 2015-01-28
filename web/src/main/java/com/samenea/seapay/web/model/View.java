package com.samenea.seapay.web.model;

/**
 * this class contains tiles view address that you must use in Controller.
 *
 * @author soroosh
 */
public class View {
    private View() {
    }

    public static class Error {
        public final static String Excption = "error/excption";
        public final static String PAGE_NOT_FOUND = "error/pageNotFound";
    }

    public static class Bank {
        public final static String PERMITTED_BANKS = "bank/banks";
        public final static String REDIRECT = "bank/redirect";
    }

    public static class Merchant {
        public final static String REDIRECT = "merchant/redirect";
    }

    public static class Transaction {
        public final static String TRANSACTION_EXIST = "transaction/exist";
        public final static String UPLOAD_EXCEL = "admin/upload";

        public static final String UPLOAD_RESULT = "admin/uploadResult";
    }

}
