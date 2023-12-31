/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.records;

import arkanoid.score.FormScore;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author KEVIN ANDRES GOMEZ M
 */
public class RecordManager {
    private float scoreValue;
    private int numBalls;
    private String speedType;
    private float time;

    public RecordManager(int scoreValue, int numBalls, String speedType, float time) {
        this.scoreValue = scoreValue / time;
        this.numBalls = numBalls;
        this.speedType = speedType;
        this.time = time;
    }
    
    public void manageScore() {
        if(this.isNewRecord()) {
            FormScore fs = new FormScore(scoreValue, numBalls, speedType);
            fs.setLocationRelativeTo(null);
            fs.setVisible(true);
        }
    }
    
    static public Record[] getRecordsFromFile() {
        try {
            Record records[] = new Record[5];
            File f = new File("data\\records.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));
            
            // Salta los headers.
            br.readLine(); 
            
            // Carga en memoria los records.
            String line;
            int itemsFounds = 0;
            
            // Recorre la linea.
            while((line=br.readLine()) != null){
                if(itemsFounds < 5) {
                    String fLine[] = line.split(",");
                    Record record = new Record(
                            fLine[0], 
                            Float.parseFloat(fLine[1]),
                            Integer.parseInt(fLine[2]),
                            fLine[3]
                        );
                    records[itemsFounds] = record;
                    itemsFounds++;
                } else break;
            }
            
            // Ordenar el arreglo
            for(int i = 0; i < itemsFounds - 1; i++) {
                for(int j = 0; j < itemsFounds - i - 1; i++) {
                    if (records[j].getScore() < records[j + 1].getScore()) {
                        Record temp = records[j];
                        records[j] = records[j + 1];
                        records[j + 1] = temp;
                    }
                }
            }
            
            return records;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return new Record[5];
        }
    }
    
    public boolean isNewRecord() {
        Record records[] = RecordManager.getRecordsFromFile();
        float s = (float) scoreValue / time;       
        
        for (Record record : records) {
            if (record != null && s > record.getScore()) {
                return true;
            }
            if (record == null) {
                return true;
            }
        }
        return false;
    }
    
    static public void saveNewRecord(Record r) {
        Record records[] = RecordManager.getRecordsFromFile();
        
        
        int itemsFounds = 0;
        for(Record rec: records) {
            if(rec != null) itemsFounds++;
        }
        
        // Si hay menos de 5 elementos lo añade de ultimo
        if(itemsFounds < 5) records[itemsFounds] = r;
        
        // Si el arreglo esta completo entonces reemplaza al ultimo elemento.
        else records[4] = r;
        
        // Organiza el arreglo.
        for(int i = 0; i < itemsFounds - 1; i++) {
            for(int j = 0; j < itemsFounds - i - 1; i++) {
                if (records[j].getScore() < records[j + 1].getScore()) {
                    Record temp = records[j];
                    records[j] = records[j + 1];
                    records[j + 1] = temp;
                }
            }
        }
        
        // Escribe en el archivo.
        try {
            FileWriter fileWriter = new FileWriter("data//records.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Escribe el header.
            bufferedWriter.write("initials,score,balls,speed");
            
            for (Record rec: records) {
                if(rec != null) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(rec.getInitials() + "," + rec.getScore() + "," + rec.getNumBalls() + "," + rec.getSpeedType());
                }
            }

            bufferedWriter.close();
            System.out.println("Líneas escritas exitosamente en el archivo.");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al escribir en el archivo: " + e.getMessage());
        }
    }
}
