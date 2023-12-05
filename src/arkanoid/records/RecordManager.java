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
import java.util.Arrays;
import java.util.Comparator;
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
        System.out.println("Es un nuevo record?: " + (this.isNewRecord() ? "Si" : "No"));
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
            
            // Carga en memoria los records.
            for(int i = 0; i < 5; i++) {
                String line;
                br.readLine();
                while((line=br.readLine()) != null){
                    String fLine[] = line.split(",");
                    if(fLine.length == 4) {
                        Record record = new Record(
                                fLine[0], 
                                Float.parseFloat(fLine[1]),
                                Integer.parseInt(fLine[2]),
                                fLine[3]
                            );
                        records[i] = record;
                    }
                }
            }
            
            // Ordenar el arreglo
            
            return records;
            
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return new Record[5];
        }
    }
    
    public boolean isNewRecord() {
        Record records[] = RecordManager.getRecordsFromFile();
        float s = (float) scoreValue / time;       
        
        for(int i = 0; i < records.length; i++) {
            System.out.println("Record " + (i + 1) + ": " + records[i]);
            if(records[i] != null && s > records[i].getScore()) return true;
            if(records[i] == null) return true;
        }
        return false;
    }
    
    static public void saveNewRecord(Record r) {
        System.out.println("PASADO POR PARAMETROS: " + r.getInitials() + ", PUNTOS: " + r.getScore());
        
        Record records[] = RecordManager.getRecordsFromFile();
        // Reemplaza el record con menor puntuacion en el arreglo.
        records[records.length - 1] = r;
        
        // LLena el arreglo para valores nulos 
        for(int i = 0; i < records.length; i++) {
            if(records[i] == null) records[i] = new Record("", 0, 0, "");
        }
        
        // Organiza el arreglo.
        for(int i = 0; i < records.length - 1; i++) {
            for(int j = 0; j < records.length - i - 1; i++) {
                if (records[j].getScore() > records[j + 1].getScore()) {
                    Record temp = records[j];
                    records[j] = records[j + 1];
                    records[j + 1] = temp;
                }
            }
        }
        
        for(int i = 0; i < 5; i++) {
            System.out.println((records[i].getScore() == 0 ? "VOID" : records[i].getInitials())  + ": " + records[i].getScore());
        }
        
        
        // Escribe en el archivo.
        
        try {
            // Crear un objeto FileWriter para escribir en el archivo
            FileWriter fileWriter = new FileWriter("data//records.csv");

            // Envolver FileWriter en un BufferedWriter para mejorar la eficiencia de escritura
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Escribir cada línea en el archivo
            bufferedWriter.write("initials,score,balls,speed");
            for (Record rec: records) {
                bufferedWriter.newLine();
                bufferedWriter.write(rec.getInitials() + "," + rec.getScore() + "," + rec.getNumBalls() + "," + rec.getSpeedType());
            }

            // Cerrar el BufferedWriter después de escribir
            bufferedWriter.close();
            System.out.println("Líneas escritas exitosamente en el archivo.");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al escribir en el archivo: " + e.getMessage());
        }
    }
}
