import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class CSVtoBin extends CRUD {

    public static void main(String[] args) throws IOException {
        String arquivoCsv = "games.csv"; // Arquivo CSV de entrada
        // String arquivoBin = "jogos.bin"; // Arquivo binário de saída
        RandomAccessFile raf = new RandomAccessFile("jogos.bin", "rw"); // Cria o arquivo
        List<String[]> dadosCsv = lerCsv(arquivoCsv); // Lê os dados do arquivo CSV
        gravarBinario(raf, dadosCsv); // Grava os dados no arquivo binário

    }

    // Lê os dados do arquivo CSV
    private static List<String[]> lerCsv(String arquivoCsv) throws IOException {
        List<String[]> dadosCsv = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(arquivoCsv));
        String linha;
        while ((linha = reader.readLine()) != null) {

            String[] dadosLinha = linha.split(";");
            // replace espaços em branco
            dadosLinha[1] = dadosLinha[1].replace(" ", "");

            dadosCsv.add(dadosLinha);
        }
        reader.close();
        return dadosCsv;
    }

    // Grava os dados no arquivo binário
    private static void gravarBinario(RandomAccessFile raf, List<String[]> dadosCsv) throws IOException {

        Jogo jogos = new Jogo();

        raf.writeInt(dadosCsv.size()); // Grava a quantidade de registros

        for (String[] dadosLinha : dadosCsv) {
            raf.seek(raf.length()); // Posiciona o ponteiro no final do arquivo
            raf.writeByte(0); // Escreve a lapide (0 = ativo)
            
            raf.writeInt(dadosLinha[0].length() + dadosLinha[1].length() + dadosLinha[2].length()
                    + dadosLinha[3].length() + dadosLinha[4].length() + dadosLinha[5].length() + dadosLinha[6].length()
                    + dadosLinha[7].length());
                    
            raf.writeUTF(dadosLinha[0]); // Grava o primeiro campo como uma String
            raf.writeUTF(dadosLinha[1]); // Grava o segundo campo como uma String
            raf.writeUTF(dadosLinha[2]); // Grava o terceiro campo como uma String

            // Grava o quarto campo como um inteiro
            int intValue = 0;
            if (!dadosLinha[3].isEmpty()) {
                intValue = Integer.parseInt(dadosLinha[3]);
            }
            byte[] intBytes = ByteBuffer.allocate(4).putInt(intValue).array();
            raf.write(intBytes);

            raf.writeUTF(dadosLinha[4]); // Grava o quinto campo como uma String
            // System.out.println("cheguei");
            String toint = "";
            if (!dadosLinha[5].isEmpty()) {
                toint = (dadosLinha[5]);
            }

            raf.writeUTF(toint);

            raf.writeUTF(dadosLinha[6]); // Grava o sétimo campo como uma String
            raf.writeFloat(Float.parseFloat(dadosLinha[7])); // Grava o oitavo campo como um Float

            raf.seek(0); // Posiciona o ponteiro no inicio do arquivo
            raf.writeInt(ultimoId); // Atualiza o ultimo id

        }
        raf.close();

    }
}