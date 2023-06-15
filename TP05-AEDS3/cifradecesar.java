import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class cifradecesar {

    /*
     * metodo para cifrar e atualizar o arquivo original com os dados cifrados
     * 
     * @param nomeArquivo nome do arquivo a ser cifrado
     * 
     */
    public static void cifraCesar(String nomeArquivo) throws IOException {
        RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo + ".bin", "rw");
        List<String> lista = new ArrayList<>();
        String linha;
        int chave = 3;
        int i = 0;
        while ((linha = arquivo.readLine()) != null) {
            lista.add(linha);
            i++;
        }
        arquivo.seek(0);
        for (int j = 0; j < i; j++) {
            String linhaCifrada = cifra(lista.get(j), chave);
            arquivo.writeBytes(linhaCifrada + "\n");
        }
        arquivo.close();
    }

    public static String cifra(String linha, int chave) {
        String linhaCifrada = "";
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c >= 'a' && c <= 'z') {
                c = (char) (c + chave);
                if (c > 'z') {
                    c = (char) (c - 26);
                }
            } else if (c >= 'A' && c <= 'Z') {
                c = (char) (c + chave);
                if (c > 'Z') {
                    c = (char) (c - 26);
                }
            }
            linhaCifrada += c;
        }
        return linhaCifrada;
    }

    /*
     * metodo para descifrar e atualizar o arquivo original com os dados descifrados
     * 
     * @param nomeArquivo nome do arquivo a ser descifrado
     * 
     */
    public static void descifraCesar(String nomeArquivo) throws IOException {
        RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw");
        List<String> lista = new ArrayList<>();
        String linha;
        int chave = 3;
        int i = 0;
        while ((linha = arquivo.readLine()) != null) {
            lista.add(linha);
            i++;
        }
        arquivo.seek(0);
        for (int j = 0; j < i; j++) {
            String linhaDescifrada = descifra(lista.get(j), chave);
            arquivo.writeBytes(linhaDescifrada + "\n");
        }
        arquivo.close();
    }

    public static String descifra(String linha, int chave) {
        String linhaDescifrada = "";
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c >= 'a' && c <= 'z') {
                c = (char) (c - chave);
                if (c < 'a') {
                    c = (char) (c + 26);
                }
            } else if (c >= 'A' && c <= 'Z') {
                c = (char) (c - chave);
                if (c < 'A') {
                    c = (char) (c + 26);
                }
            }
            linhaDescifrada += c;
        }
        return linhaDescifrada;
    }

}
