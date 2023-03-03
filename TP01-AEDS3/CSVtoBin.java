import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class CSVtoBin {

    public static void main(String[] args) throws IOException {
        String arquivoCsv = "games.csv"; // Arquivo CSV de entrada
        String arquivoBin = "jogos.bin"; // Arquivo binário de saída
        List<String[]> dadosCsv = lerCsv(arquivoCsv); // Lê os dados do arquivo CSV
        gravarBinario(arquivoBin, dadosCsv); // Grava os dados no arquivo binário

    }

    // Lê os dados do arquivo CSV
    private static List<String[]> lerCsv(String arquivoCsv) throws IOException {
        List<String[]> dadosCsv = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(arquivoCsv));
        String linha;
        while ((linha = reader.readLine()) != null) {
            String[] dadosLinha = linha.split(";");

            dadosCsv.add(dadosLinha);
        }
        reader.close();
        return dadosCsv;
    }

    // Grava os dados no arquivo binário
    private static void gravarBinario(String arquivoBin, List<String[]> dadosCsv) throws IOException {
        FileOutputStream fos = new FileOutputStream(arquivoBin);
        DataOutputStream dos = new DataOutputStream(fos);

        dos.writeInt(dadosCsv.size()); // Grava a quantidade de registros
      
        for (String[] dadosLinha : dadosCsv) {
            dos.writeUTF(dadosLinha[0]); // Grava o primeiro campo como uma String
            dos.writeUTF(dadosLinha[1]); // Grava o segundo campo como uma String
            dos.writeUTF(dadosLinha[2]); // Grava o terceiro campo como uma String

            // Grava o quarto campo como um inteiro
            int intValue = 0;
            if (!dadosLinha[3].isEmpty()) {
                intValue = Integer.parseInt(dadosLinha[3]);
            }
            byte[] intBytes = ByteBuffer.allocate(4).putInt(intValue).array();
            dos.write(intBytes);

            dos.writeUTF(dadosLinha[4]); // Grava o quinto campo como uma String
          // System.out.println("cheguei");
            String toint = "";
            if (!dadosLinha[5].isEmpty()) {
                toint = (dadosLinha[5]);
            } 
                           
          dos.writeUTF(toint);
          
            dos.writeUTF(dadosLinha[6]); // Grava o sétimo campo como uma String
            dos.writeFloat(Float.parseFloat(dadosLinha[7])); // Grava o oitavo campo como um Float
            // System.out.print(dadosLinha[0]);

        }
        dos.close();
        fos.close();

    }
}
