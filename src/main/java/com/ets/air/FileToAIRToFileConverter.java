package com.ets.air;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yusuf
 */
public class FileToAIRToFileConverter {

    public FileToAIRToFileConverter() {

    }

    public AIR convert(File file) {

        AIR air = new AIR();
        BufferedReader bf = null;
        try {
                       
            bf = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bf.readLine()) != null) {
                air.addLine(line);
                
                if (line.startsWith("AIR")) {
                    String[] vals = AIRLineParser.parseAIRLine(line);
                    air.setVersion(vals[0]);
                } else if (line.startsWith("AMD")) {
                    String[] vals = AIRLineParser.parseAMDLine(line); 
                    air.setAirSequenceNumber(Long.valueOf(vals[0].trim()));
                    String[] pages = vals[1].split("/");
                    air.setCurrentPage(Integer.valueOf(pages[0]));
                    air.setTotalPages(Integer.valueOf(pages[1]));
                    if(vals.length > 2 && vals[2].contains("VOID")){
                        air.setType("VOID");
                    }
                } else if (line.startsWith("B-")) {                    
                    
                    if (line.contains("BT")) {
                        air.setType("BT");
                    } else if (line.contains("ET")) {
                        air.setType("ET");
                    }else if (line.contains("TTP")) {
                        air.setType("TTP");
                    } else if (line.contains("INV")) {
                        air.setType("INV");
                    } else if (line.contains("TRFP")) {
                        air.setType("TRFP");
                    }else{
                     return null;//System can not take risk or unknown type of file. So break here.
                    }
                } else if (line.startsWith("D")) {
                    String[] vals = AIRLineParser.parseDLine(line);
                    air.setCreationDate(vals[2]);
                }
            }   bf.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileToAIRToFileConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileToAIRToFileConverter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bf.close();
            } catch (IOException ex) {
                Logger.getLogger(FileToAIRToFileConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return air;
    }
}
