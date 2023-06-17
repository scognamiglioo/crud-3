import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.function.Predicate;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

/*  o método de compressão retorna um arraylist de strings com os tokens
* o método de descompressão retorna um arraylist de strings com os dados descomprimidos
* os tokens são armazenados em um arquivo binário
* os dados descomprimidos são armazenados em um arquivo txt
* o arquivo txt auxiliar é criado para facilitar a leitura dos dados do arquivo binário
* o arquivo txt auxiliar é criado a partir da leitura do arquivo de dados (em binário)
* os tokens são utilizados para armazenar posiçao do prefixo e o simbolo mais a direita 
*  do prefixo caso haja falha na busca do prefixo no dicionario 
*/

public class lzw {

    public static void createTxt(String fileName) throws IOException { // metodo para criar um arquivo txt a partir da
                                                                       // leitura do arquivo de dados (em binário)
        RandomAccessFile raf = new RandomAccessFile(fileName, "r");
        RandomAccessFile arq = new RandomAccessFile("copia.txt", "rw");

        String[] generos;

        raf.seek(4); // pula o cabeçalho
        while (raf.getFilePointer() < raf.length()) { // enquanto não chegar ao fim do arquivo
            byte lapide = raf.readByte();
            if (lapide == 0) { // se o registro não estiver excluído
                int tam = raf.readInt(); // lê o tamanho do registro
                arq.writeChars(Integer.toString(tam)); // escreve o tamanho do registro no arquivo txt
                arq.writeChar(' ');
                int id = raf.readInt();
                arq.writeChars(Integer.toString(id));
                arq.writeChar(' ');
                String idJogo = raf.readUTF();
                arq.writeChars(idJogo);
                arq.writeChar(' ');
                String nome = raf.readUTF();
                arq.writeChars(nome);
                arq.writeChar(' ');
                String data = raf.readUTF();
                arq.writeChars(data);
                arq.writeChar(' ');
                int qtdGenero = raf.readInt();
                arq.writeChars(Integer.toString(qtdGenero));
                arq.writeChar(' ');
                generos = new String[qtdGenero];
                for (int i = 0; i < qtdGenero; i++) {
                    generos[i] = raf.readUTF();
                    arq.writeChars(generos[i]);
                    arq.writeChar(' ');
                }

                float preco = raf.readFloat();
                arq.writeChars(Float.toString(preco));
                arq.writeChar(' ');
                arq.writeChars("|");

            } else { // se o registro estiver excluído
                raf.skipBytes(raf.readInt());
            }
        }

        raf.close();
        arq.close();

    }

    public static class token { // classe token para armazenar a posição e o simbolo do token caso haja
                                // falha na busca do prefixo no dicionario
        int posic;
        String simbolo;

        public token(int posic, String simbolo) { // construtor
            this.posic = posic;
            this.simbolo = simbolo;
        }

    }

    public static ArrayList<String> criaDicionario() { // metodo que cria o dicionario com os 256 caracteres da tabela
                                                       // ASCII
        ArrayList<String> dicionario = new ArrayList<String>();
        for (int i = 0; i < 256; i++) {
            dicionario.add(Character.toString((char) i));
        }

        return dicionario;
    }

    public static ArrayList<String> compressao(String fileName) throws IOException { // recebe o nome do arquivo de
                                                                                     // dados e retorna um arraylist
                                                                                     // com os tokens
        long start = System.currentTimeMillis();
        createTxt(fileName); // cria o arquivo txt auxiliar
        RandomAccessFile raf2 = new RandomAccessFile("copia.txt", "r"); // arquivo txt auxiliar

        RandomAccessFile raf3 = new RandomAccessFile(fileName + "_comprimido.bin", "rw"); // arquivo binario de saida
        String dados = new String();
        while (raf2.getFilePointer() < raf2.length()) { // le o arquivo txt auxiliar e armazena em uma string
            dados += raf2.readChar();
        }
        // System.out.println(dados);
        ArrayList<String> dicionario = criaDicionario(); // cria o dicionario

        int i = 0; // posição inicial da string de dados
        int comecoPrefixo = 0; // posição inicial do prefixo
        int finalPrefixo = 1; // posição final do prefixo
        String prefixo = "";
      
        String prefixoValido = ""; // prefixo valido (prefixo que está no dicionario)
        String dadosAux = dados; // string auxiliar para percorrer a string de dados
        String simbolo = ""; // simbolo mais a direita do prefixo (simbolo que não está no dicionario)
        String arquivoFinal = new String();

        String token = new String();

        while (i < dados.length()) { // pecorre a string de dados

            prefixo = dadosAux.substring(comecoPrefixo, finalPrefixo); // Prefixo a ser buscado no dicionario
            simbolo = Character.toString(prefixo.charAt(prefixo.length() - 1)); // Simbolo mais a direita do prefixo

            if (dicionario.contains(prefixo) == true) { // verifica se o dicionario contém o prefixo
                finalPrefixo++; // caso o dicionario contenha o prefixo, o prefixo é incrementado
            } else { // caso o dicionario não contenha o prefixo (falha na busca do prefixo)

                dicionario.add(prefixo); // adiciona o prefixo ao dicionario
                prefixoValido = dadosAux.substring(comecoPrefixo, finalPrefixo); // ultimo prefixo encontrado no
                                                                                 // dicionario
                token falha = new token(dicionario.indexOf(prefixoValido), simbolo); // gera um token para falha

                raf3.writeShort(falha.posic); // escreve no arquivo compactado a posição da falha
                raf3.writeShort(dicionario.indexOf(simbolo)); // escreve no arquivo compactado a posição do simbolo

                arquivoFinal += Integer.toString(falha.posic) + ",";
                arquivoFinal += Integer.toString(dicionario.indexOf(simbolo)) + ",";
                comecoPrefixo = finalPrefixo; // novo prefixo começa na posição final do prefixo anterior
                finalPrefixo = comecoPrefixo + 1; // aumenta em uma posição o final do prefixo para que o primeiro
                                                  // prefixo comparado tenha sempre tamanho =1

            }
            i++;
        }
        raf2.close();
        raf3.close();
        long time = (System.currentTimeMillis() - start);
        System.out.println("Resultado após a compressão:");
        System.out.println(arquivoFinal);
        System.out.println("\nTempo de execução: " + time + " /ms");

        float tamanhoOriginal = new File(fileName).length(); // calcula o tamanho do arquivo original
        float tamanhoFinal = new File(fileName + "_comprimido.bin").length(); // calcula o tamanho do arquivo compactado

        System.out.println("Tamanho do arquivo original: " + tamanhoOriginal + " bytes"); // imprime o tamanho do
                                                                                          // arquivo original
        System.out.println("Tamanho do arquivo compactado: " + tamanhoFinal + " bytes"); // imprime o tamanho do arquivo
                                                                                         // compactado
        System.out.println("Taxa de compressão: " + (tamanhoOriginal / tamanhoFinal) + " vezes"); // imprime a taxa de
                                                                                                  // compressão

        File copia = new File("copia.txt");
        copia.delete();
        return dicionario; // Retorna o resultado resultante após a compressão
    }

    public static void decompressao(String fileName, ArrayList<String> dicionario) throws IOException { // Método para
                                                                                                        // decompressão
                                                                                                        // que recebe o
                                                                                                        // nome do
                                                                                                        // arquivo
                                                                                                        // original
        long start = System.currentTimeMillis();
        RandomAccessFile raf = new RandomAccessFile(fileName + "_comprimido.bin", "r");
        String dadosOriginais = new String(); // string para armazenar os dados originais (após decompressão)
        int posic = 0;

        while (raf.getFilePointer() < raf.length()) { // le o arquivo comprimido buscando no dicionario pela posição
                                                      // lida
            posic = raf.readShort();
            dadosOriginais += dicionario.get(posic); // gera a string final a partir das buscas no dicionário
        }
        System.out.println("Resultado após a decompressão:");
        System.out.println(dadosOriginais);
        raf.close();
        long time = (System.currentTimeMillis() - start);
        System.out.println("\nTempo de execução: " + time + " /ms");
    }

    public static void mostraDicionario(ArrayList<String> dicionario) { // metodo que mostra o dicionario
        for (int i = 0; i < dicionario.size(); i++) {
            System.out.println("[" + i + ";" + dicionario.get(i) + "]");
        }
    }

}