/*
* Métodos para cifrar e decifrar arquivo através de substitução
* O método  utiliza uma tabela de substituição predefinida para mapear cada valor de byte para um novo valor cifrado
*A tabela de substituição consiste em todos os possíveis valores de byte, variando de 0 a 255.
*Cada valor de byte é substituído por outro valor da tabela de substituição cifrada, cada byte é deslocado sete 
*posições para a direita na tabela de substituição. 
*/


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class criptografiasubstituicao {

    private static final String TABELA_SUBSTITUICAO = criarTabelaSubstituicao();
    private static final String TABELA_SUBSTITUICAO_CIFRADA = criarTabelaSubstituicaoCifrada();

    /*
     * Método para criar a tabela de substituição
     *      
     */
    private static String criarTabelaSubstituicao() {
        StringBuilder tabela = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            tabela.append((char) i);
        }
        return tabela.toString();
    }

    /*
     * Método para criar a tabela de substituição cifrada.
     * A tabela de substituição cifrada consiste em todos os possíveis valores de byte, variando de 0 a 255.
     * cada valor de byte é substituído por outro valor da tabela de substituição cifrada, cada byte é deslocado sete
     * posições para a direita na tabela de substituição.
     */
    private static String criarTabelaSubstituicaoCifrada() {
        StringBuilder tabela = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            tabela.append((char) ((i + 7) % 256));
        }
        return tabela.toString();
    }

    /*
     * Método para cifrar um arquivo e salvar o resultado em um novo arquivo
     *
     * @param nomeArquivoOriginal nome do arquivo original a ser cifrado
     * @param nomeArquivoCifrado nome do arquivo cifrado a ser salvo
     */
    public static void cifrarArquivo(String nomeArquivoOriginal) throws IOException {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(nomeArquivoOriginal));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("cifrado.bin"))) {
            int byteLido;
            while ((byteLido = inputStream.read()) != -1) {
                byte byteCifrado = cifrarByte((byte) byteLido);
                outputStream.write(byteCifrado);
            }
        }
    }

    /*
     * Método para cifrar um byte utilizando a tabela de substituição cifrada
     * 
     *
     * @param b byte a ser cifrado
     * @return byte cifrado
     */
    public static byte cifrarByte(byte b) {
        int index = TABELA_SUBSTITUICAO.indexOf(b & 0xFF);
        if (index != -1) {
            return (byte) TABELA_SUBSTITUICAO_CIFRADA.charAt(index);
        }
        return b;
    }

    /*
     * Método para descifrar um arquivo cifrado e salvar o resultado em um novo arquivo
     *
     * @param nomeArquivoCifrado nome do arquivo cifrado a ser descifrado
     * @param nomeArquivoDescifrado nome do arquivo descifrado a ser salvo
     */
    public static void descifrarArquivo(String nomeArquivoDecifrado) throws IOException {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("cifrado.bin"));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(nomeArquivoDecifrado))) {
            int byteLido;
            while ((byteLido = inputStream.read()) != -1) {
                byte byteDescifrado = descifrarByte((byte) byteLido);
                outputStream.write(byteDescifrado);
            }
        }
    }

    /*
     * Método para descifrar um byte utilizando a tabela de substituição original
     *
     * @param b byte a ser descifrado
     * @return byte descifrado
     */
    public static byte descifrarByte(byte b) {
        int index = TABELA_SUBSTITUICAO_CIFRADA.indexOf(b & 0xFF);
        if (index != -1) {
            return (byte) TABELA_SUBSTITUICAO.charAt(index);
        }
        return b;
    }

   
}
