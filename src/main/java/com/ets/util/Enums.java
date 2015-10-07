package com.ets.util;

/**
 *
 * @author Yusuf
 */
public class Enums {

    public enum UserType {

        /**
         * SU - Super User Only Software Vendor,AD - Admin, SM - Sales Manager,
         * GS - General Sales
         */
        GS(0), SM(1), AD(2), SU(3);
        private int id;

        UserType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return GS.toString();
                case 1:
                    return SM.toString();
                case 2:
                    return AD.toString();
                case 3:
                    return SU.toString();
                default:
                    return null;
            }
        }
    }

    public enum ClientType {

        AGENT(0), CUSTOMER(1);
        private int id;

        ClientType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return AGENT.toString();
                case 1:
                    return CUSTOMER.toString();
                default:
                    return null;
            }
        }
    }

    public enum TaskPriority {

        HIGH(0), MEDIUM(1), LOW(2);
        private int id;

        TaskPriority(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return HIGH.toString();
                case 1:
                    return MEDIUM.toString();
                case 2:
                    return LOW.toString();
                default:
                    return null;
            }
        }
    }

    public enum AIRType {

        BT(0), TTP(1), INV(2), TRFP(3);
        private int id;

        AIRType(int id) {
            this.id = id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return BT.toString();
                case 1:
                    return TTP.toString();
                case 2:
                    return INV.toString();
                case 3:
                    return TRFP.toString();
                default:
                    return null;
            }
        }
    }

    public enum TicketStatus {

        BOOK(0), ISSUE(1), REISSUE(2), REFUND(3), VOID(4);
        private int id;

        TicketStatus(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return BOOK.toString();
                case 1:
                    return ISSUE.toString();
                case 2:
                    return REISSUE.toString();
                case 3:
                    return REFUND.toString();
                case 4:
                    return VOID.toString();
                default:
                    return null;
            }
        }
    }

    public enum AcDocType {

        INVOICE(0), PAYMENT(1), CREDITMEMO(2), DEBITMEMO(3), REFUND(4);
        private int id;

        AcDocType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return INVOICE.toString();
                case 1:
                    return PAYMENT.toString();
                case 2:
                    return CREDITMEMO.toString();
                case 3:
                    return DEBITMEMO.toString();
                case 4:
                    return REFUND.toString();
                default:
                    return null;
            }
        }
    }

    public enum SaleType {

        TKTSALES(0), TKTPURCHASE(1), OTHERSALES(2);
        private int id;

        SaleType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return TKTSALES.toString();
                case 1:
                    return TKTPURCHASE.toString();
                case 2:
                    return OTHERSALES.toString();
                default:
                    return null;
            }
        }
    }

    public enum AcDocStatus {

        ACTIVE(0), ARCHIVE(1), VOID(2);
        private int id;

        AcDocStatus(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return ACTIVE.toString();
                case 1:
                    return ARCHIVE.toString();
                case 2:
                    return VOID.toString();
                default:
                    return null;
            }
        }
    }

    public enum CalculationType {

        FIXED(0), VARIABLE(1), PERCENT(2);
        private int id;

        CalculationType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return FIXED.toString();
                case 1:
                    return VARIABLE.toString();
                case 2:
                    return PERCENT.toString();
                default:
                    return null;
            }
        }
    }

    public enum TicketingType {

        IATA(0), THIRDPARY(1);
        private int id;

        TicketingType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return IATA.toString();
                case 1:
                    return THIRDPARY.toString();
                default:
                    return null;
            }
        }
    }

    public enum PaymentType {

        CASH(0), CHEQUE(1), CREDIT_CARD(2), DEBIT_CARD(3), BANKT_TANSFER(4), OTHER(5), CREDIT_TRANSFER(6);
        private int id;

        PaymentType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return CASH.toString();
                case 1:
                    return CHEQUE.toString();
                case 2:
                    return CREDIT_CARD.toString();
                case 3:
                    return DEBIT_CARD.toString();
                case 4:
                    return BANKT_TANSFER.toString();
                case 5:
                    return OTHER.toString();
                case 6:
                    return CREDIT_TRANSFER.toString();
                default:
                    return null;
            }
        }
    }

    public enum ClientSearchType {

        TICKETING_SALES_DUE_INVOICE(0), TICKETING_SALES_DUE_REFUND(1),
        TICKETING_PURCHASE_DUE_INVOICE(2), TICKETING_PURCHASE_DUE_REFUND(3),
        OTHER_SALES_DUE_INVOICE(4), OTHER_SALES_DUE_REFUND(5), ALL(6);
        private int id;

        ClientSearchType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static String valueOf(int id) {
            switch (id) {
                case 0:
                    return TICKETING_SALES_DUE_INVOICE.toString();
                case 1:
                    return TICKETING_SALES_DUE_REFUND.toString();
                case 2:
                    return TICKETING_PURCHASE_DUE_INVOICE.toString();
                case 3:
                    return TICKETING_PURCHASE_DUE_REFUND.toString();
                case 4:
                    return OTHER_SALES_DUE_INVOICE.toString();
                case 5:
                    return OTHER_SALES_DUE_REFUND.toString();
                case 6:
                    return ALL.toString();
                default:
                    return null;
            }
        }
    }

}
