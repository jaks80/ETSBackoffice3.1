package com.amadeus.air;

import com.ets.air.AIRLineParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Yusuf
 */
public class AIRLineParserTest {
    
    public AIRLineParserTest() {
        System.out.println("Line parser test...");
    }

    @Test
    public void testParseAIRLine() {
        System.out.println("parseAIRLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseAIRLine(line);
        assertArrayEquals(expResult, result);
        
    }

    @Test
    public void testParseAMDLine() {
        System.out.println("parseAMDLine");
        String line = "AMD 2800030221;1/1;    29OCT;ITSU";
        String[] expResult = {"2800030221","1/1","29OCT","ITSU"};
        String[] result = AIRLineParser.parseAMDLine(line);
        assertEquals(expResult[0], result[0]);
        assertEquals(expResult[1], result[1]);
        assertEquals(expResult[2], result[2]);
        assertEquals(expResult[3], result[3]);        
    }

    @Test
    public void testParseMUCLine() {
        System.out.println("parseMUCLine");
        String line = "MUC1A 3LIZMK015;0101;LONU123IT;91279053;LONU123IK;91279054;LONU123IJ;91279055;LONU123IK;91279056;;;;;;;;;;;;;;;;;;;;;;IY CSBGQF";
        String[] expResult = {"3LIZMK015","0101","LONU123IT","91279053","LONU123IK","91279054","LONU123IJ","91279055","LONU123IK","91279056",
            "","","","","","","","","","","","","","","","","","","","","","IY CSBGQF"};
        String[] result = AIRLineParser.parseMUCLine(line);
        
        assertEquals(expResult[0], result[0]);
        assertEquals(expResult[1], result[1]);
        assertEquals(expResult[2], result[2]);
        assertEquals(expResult[3], result[3]);
        assertEquals(expResult[4], result[4]);
        assertEquals(expResult[5], result[5]);
        assertEquals(expResult[6], result[6]);
        assertEquals(expResult[7], result[7]);
        assertEquals(expResult[8], result[8]);
        assertEquals(expResult[9], result[9]);
        assertEquals(expResult[10], result[10]);
        assertEquals(expResult[30], result[20]);        
    }

    //@Test
    public void testParseALine() {
        System.out.println("parseALine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseALine(line);
        assertArrayEquals(expResult, result);
        
    }

    @Test
    public void testParseBLine() {
        System.out.println("parseBLine");
        String line = "B-TTP";
        String[] expResult = {"TTP"};
        String[] result = AIRLineParser.parseBLine(line);
        assertArrayEquals(expResult, result);
        
    }

    @Test
    public void testParseCLine() {
        System.out.println("parseBLine");
        String line = "C-7906/ 2008ITSU-2731IMGS-B-N--";
        String[] expResult = {"2008ITSU","2731IMGS"};
        String[] result = AIRLineParser.parseCLine(line);
        assertArrayEquals(expResult, result);
        
    }
    
    @Test
    public void testParseDLine() {
        System.out.println("parseDLine");
        String line = "D-080625;080703;080703";
        String[] expResult = {"080625","080703","080703"};
        String[] result = AIRLineParser.parseDLine(line);
        assertArrayEquals(expResult, result);
        
    }

    @Test
    public void testParseHLine() {
        System.out.println("parseHLine");
        String line = "H-016;005OLHR;LONDON LHR       ;AUH;ABU DHABI        ;EY    0012 Q Q 13JUL1030 2035 13JUL;OK02;HK02;M ;0;346;;;20K;3 ;XXXX;ET;0705 ;N;3425;GB;AE;1";
        String[] expResult = {"016","005OLHR","LONDON LHR","AUH","ABU DHABI",
                              "EY    0012 Q Q 13JUL1030 2035 13JUL","OK02","HK02","M",
                              "0","346","","","20K",
                              "3","XXXX","ET","0705","N","3425",
                              "GB","AE","1"};
        String[] result = AIRLineParser.parseHLine(line);
        assertArrayEquals(expResult, result);        
    }

    //@Test
    public void testParseULine() {
        System.out.println("parseULine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseULine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseKLine() {
        System.out.println("parseKLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseKLine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseKNLine() {
        System.out.println("parseKNLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseKNLine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseKSLine() {
        System.out.println("parseKSLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseKSLine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseILine() {
        System.out.println("parseILine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseILine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseTLine() {
        System.out.println("parseTLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseTLine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseRLine() {
        System.out.println("parseRLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseRLine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseRFDLine() {
        System.out.println("parseRFDLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseRFDLine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseFOLine() {
        System.out.println("parseFOLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseFOLine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseTKLine() {
        System.out.println("parseTKLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseTKLine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseFELine() {
        System.out.println("parseFELine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseFELine(line);
        assertArrayEquals(expResult, result);
        
    }

    //@Test
    public void testParseRMLine() {
        System.out.println("parseRMLine");
        String line = "";
        String[] expResult = null;
        String[] result = AIRLineParser.parseRMLine(line);
        assertArrayEquals(expResult, result);
        
    }
    
}
