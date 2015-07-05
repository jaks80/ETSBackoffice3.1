package com.ets.air;

/**
 *
 * @author Yusuf
 */
public class AIRLineParser {

    /*
     AIR-BLK207;7A;;247;0100046934;GW4489451;001001
     */
    public static String[] parseAIRLine(String line) {        
        line = line.replace("AIR-", "").trim();
        String[] _AIR = splitter(line);
        return _AIR;
    }

    /*
     AMD 0100038767;1/1;
     */
    public static String[] parseAMDLine(String line) {
        line = line.replace("AMD", "").trim();
        return splitter(line);
    }

    /*
    MUC1A 3LIZMK015;0101;LONU123IT;91279053;LONU123IT;91279053;LONU123IT;91279053;LONU123IT;91279053;;;;;;;;;;;;;;;;;;;;;;IY CSBGQF
    */
    public static String[] parseMUCLine(String line) {
        line = line.replace("MUC1A", "").trim();
        return splitter(line);
    }

    /*
     A-ETIHAD AIRWAYS;EY 6075
     */
    public static String[] parseALine(String line) {
        line = line.replace("A-", "").trim();
        return splitter(line);
    }

    /*
     B-TTP
     */
    public static String[] parseBLine(String line) {
        line = line.replace("B-", "").trim();
        return splitter(line);
    }

    /**
     * C-7906/ 2008ITSU-2731IMGS-B-N--
     * @param line
     * @return 
     */
    public static String[] parseCLine(String line) {
        line = line.replace("C-", "").trim();
        String[] temp = line.split("-");
        String ticketingAgent = temp[1];
        String bookingAgent = temp[0].substring(5).trim();
        String[] agentSines = {bookingAgent,ticketingAgent};
        return agentSines;
    }
    /*     
      D-080625;080703;080703
     */
    public static String[] parseDLine(String line) {
        line = line.replace("D-", "").trim();
        return splitter(line);
    }

    /*
    H-016;005OLHR;LONDON LHR       ;AUH;ABU DHABI        ;EY    0012 Q Q 13JUL1030 2035 13JUL;OK02;HK02;M ;0;346;;;20K;3 ;XXXX;ET;0705 ;N;3425;GB;AE;1
     */
    public static String[] parseHLine(String line) {
        line = line.replace("H-", "").trim();
        return splitter(line);
    }

    public static String[] parseULine(String line) {
        line = line.replace("U-", "").trim();
        return splitter(line);
    }

    /*

     */
    public static String[] parseKLine(String line) {
        line = line.replace("K-", "").trim();
        return splitter(line);
    }

    /*
     KN-IGBP525.00     ;;;;;;;;;;;;GBP843.11     ;;;
     */
    public static String[] parseKNLine(String line) {
        line = line.replace("KN-", "").trim();
        return splitter(line);
    }

    /*
     KS-IGBP525.00     ;;;;;;;;;;;;GBP843.11     ;;;
     */
    public static String[] parseKSLine(String line) {
        line = line.replace("KS-", "").trim();
        return splitter(line);
    }

    /*
     I-001;01CHOUDHURY/RAHELA MRS;;APLON 0207 387 8264 - JUMBO TRAVEL - A//LON 02073750800 - IMRAN TRAVELS - A;;
     */
    public static String[] parseILine(String line) {
        line = line.replace("I-", "").trim();
        return splitter(line);
    }

    /*
     T-K157-5262536751-52
     */
    public static String[] parseTLine(String line) {
        line = line.replace("T-", "").trim();
        return line.split("-");
    }

    /*
     R-997-4785932038;28OCT13
     */
    public static String[] parseRLine(String line) {
        line = line.replace("R-", "").trim();
        return splitter(line);
    }

    /*
     RFDM;13AUG13;I;GBP370.00;343.00;27.00;;;;;;XT80.30;107.30;
     */
    public static String[] parseRFDLine(String line) {
        line = line.replace("RFD-", "").trim();
        return splitter(line);
    }

    /*
     Represent original tkt no incase of re-issue
     FO065-2885483792LON03JUL13/91279053/065-28854837926E1234;P3
     */
    public static String[] parseFOLine(String line) {
        line = line.replace("FO", "").trim();
        return splitter(line);
    }

    /*
     TKXL01DEC/0910/LONU123IT
     TKOK27JUN/LONU123IT
     TKOK01AUG/LONU121DN//ETEK 3rd party issue
     This line is used only for Ticket agent office ID for 3rd party issue file.
     */
    public static String[] parseTKLine(String line) {
        line = line.replace("TK", "").trim();
        return line.split("/");
    }

    /*
     FEVALID ON QR ONLY NON END/NON REF;S7-12;P1-6
     */
    public static String[] parseFELine(String line) {
        line = line.replace("FE", "").trim();
        return splitter(line);
    }

    /*
     RM PAX MOB 07909120055
     */
    public static String[] parseRMLine(String line) {
        line = line.replace("RM", "").trim();
        return splitter(line);
    }

    private static String[] splitter(String line) {
        if(line != null && line.length() > 2 ){
         return line.split("\\s*;\\s*");
        }else{
         return null;
        }      
    }
}
