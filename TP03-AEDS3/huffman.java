import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;



class HuffmanNode { // Classe que representa um nó da árvore de Huffman
    int frequencia; // Frequência do caractere
    char caracter; // Caractere
    HuffmanNode esquerda; // Nó da esquerda
    HuffmanNode direita; // Nó da direita
}

class Comparador implements Comparator<HuffmanNode> { // comparador para a fila de prioridade do algoritmo de Huffman
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.frequencia - y.frequencia; // comparando a frequência dos nós
    }
}

public class huffman { // arvore

    public static void deleteTxtFiles() { // metodo que deleta os arquivos .txt e .bin caso eles existam no diretório
        String[] files = { "copy.txt", "huffmanCode.txt", "comprimido.bin", "descomprimido.txt" };
        for (String file : files) {
            File f = new File(file); // cria um objeto File com o nome do arquivo
            if (f.exists()) { // se o arquivo existir no diretório ele deleta
                f.delete();
            }
        }
    }

    /*
     * esse metodo cria um arquivo .txt com os dados do arquivo .bin
     * o arquivo .txt é criado para que seja possível ler o arquivo .bin
     * o arquivo .txt é criado com o nome de copy.txt para evitar choque
     * com o arquivo de mesmo nome que é criado no método createHash()
     */

    public static void createTxt(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "r"); // le o arquivo .bin
        RandomAccessFile arq = new RandomAccessFile("copy.txt", "rw"); // cria um arquivo .txt

        String[] generos;

        // le e armazena no arquivo .txt
        raf.seek(0);
        int lastId = raf.readInt();
        arq.writeChars(Integer.toString(lastId));
        arq.writeChar(',');

        while (raf.getFilePointer() < raf.length()) {
            byte lapide = raf.readByte();
            arq.writeChars(Byte.toString(lapide));
            arq.writeChar(',');
            int tam = raf.readInt();
            arq.writeChars(Integer.toString(tam));
            arq.writeChar(',');
            int id = raf.readInt();
            arq.writeChars(Integer.toString(id));
            arq.writeChar(',');
            String idJogo = raf.readUTF();
            arq.writeChars(idJogo);
            arq.writeChar(',');
            String nome = raf.readUTF();
            arq.writeChars(nome);
            arq.writeChar(',');
            String data = raf.readUTF();
            arq.writeChars(data);
            arq.writeChar(',');
            int qtdGeneros = raf.readInt();
            arq.writeChars(Integer.toString(qtdGeneros));
            arq.writeChar(',');
            generos = new String[qtdGeneros];
            for (int i = 0; i < qtdGeneros; i++) {
                generos[i] = raf.readUTF();
                arq.writeChars(generos[i]);
                arq.writeChar(',');
            }

            float saldo = raf.readFloat();
            arq.writeChars(Float.toString(saldo));
            arq.writeChar(',');
            arq.writeChars(";");
        }

        raf.close();
        arq.close();
    }

    /*
     * esse metodo cria um HashMap com os caracteres e suas frequências
     * o HashMap é criado com o nome de copy.txt para evitar choque
     * com o arquivo de mesmo nome que é criado no método createTxt()
     */

    public static HashMap<Character, Integer> createHash() throws IOException {
        RandomAccessFile arq = new RandomAccessFile("copy.txt", "r");

        HashMap<Character, Integer> map = new HashMap<>(); // Cria um HashMap

        String line = arq.readLine();
        line = line.replaceAll(",", ""); // remove as vírgulas da linha

        for (int i = 0; i < line.length(); i++) { // para cada caractere da linha
            char c = line.charAt(i); // pega o caractere
            if (map.containsKey(c)) { // Se o caractere já estiver no HashMap
                map.put(c, map.get(c) + 1); // Incrementa a frequência do caractere e coloca no HashMap
            } else { // Se o caractere não estiver no HashMap
                map.put(c, 1); // Coloca o caractere no HashMap com frequência 1
            }
        }

        arq.close();

        char first = map.keySet().iterator().next(); // Pega o primeiro caractere do HashMap
        map.remove(first); // remove o primeiro caractere do HashMap (que é o id do último jogo)

        return map; // retorna o HashMap
    }


    /* 
     * esse metodo cria a árvore de Huffman
     * o método recebe um HashMap com os caracteres e suas frequências
     * o método retorna a raiz da árvore de Huffman
     */
   
    public static void printCode(HuffmanNode raiz, String code) throws IOException { 
        RandomAccessFile arq = new RandomAccessFile("huffmanCode.txt", "rw"); 

        if (raiz.esquerda == null && raiz.direita == null) { 
            arq.seek(arq.length()); // final do arquivo
            arq.writeChars(raiz.caracter + "=" + code); 
            arq.writeChars("\n"); //quebra de linha
            arq.close();
            return; 
        }

        printCode(raiz.esquerda, code + "0"); // chama o método recursivamente para o nó da esquerda com o código
                                              // concatenado com 0
        printCode(raiz.direita, code + "1"); // chama o método recursivamente para o nó da direita com o código
                                             // concatenado com 1
        arq.close();
    }


    /*
     * o metodo createTree() cria a árvore de Huffman
     * o método recebe um HashMap com os caracteres e suas frequências
     * o método retorna a raiz da árvore de Huffman
     * 
     */

    public static void createTree(HashMap<Character, Integer> map) throws IOException { // Método que cria a árvore de
                                                                                        // Huffman
        int n = map.size(); // Pega o tamanho do HashMap
        char[] charArray = new char[n]; // Cria um vetor de caracteres com o tamanho do HashMap
        int[] charfreq = new int[n]; // Cria um vetor de inteiros com o tamanho do HashMap

        int count = 0; // Inicializa um contador
        for (Character c : map.keySet()) { // Para cada caractere no HashMap
            charArray[count] = c; // Coloca o caractere no vetor de caracteres
            charfreq[count] = map.get(c); // Coloca a frequência do caractere no vetor de inteiros
            count++; // Incrementa o contador
        }

        PriorityQueue<HuffmanNode> q = new PriorityQueue<>(n, new Comparador()); // cria uma fila de prioridade com o
                                                                                 // tamanho do HashMap e o comparador do
                                                                                 // menor para o maior

        for (int i = 0; i < n; i++) { // Para cada caractere no vetor de caracteres
            HuffmanNode hn = new HuffmanNode(); // Cria um nó de Huffman

            hn.caracter = charArray[i]; // Coloca o caractere no nó de Huffman
            hn.frequencia = charfreq[i]; // Coloca a frequência do caractere no nó de Huffman

            hn.esquerda = null; // Coloca o nó da esquerda como nulo
            hn.direita = null; // Coloca o nó da direita como nulo

            q.add(hn); // Adiciona o nó de Huffman na fila de prioridade
        }

        HuffmanNode raiz = null; // Cria um nó de Huffman raiz

        while (q.size() > 1) { // Enquanto a fila de prioridade tiver mais de um nó (enquanto a árvore não
                               // estiver completa)
            HuffmanNode x = q.peek(); // Pega o primeiro nó da fila de prioridade
            q.poll(); // Remove o primeiro nó da fila de prioridade
            HuffmanNode y = q.peek(); // Pega o primeiro nó da fila de prioridade
            q.poll(); // Remove o primeiro nó da fila de prioridade

            HuffmanNode f = new HuffmanNode(); // Cria um nó de Huffman
            f.frequencia = x.frequencia + y.frequencia; // Soma as frequências dos dois nós
            f.caracter = '-'; // Coloca o caractere do nó como hífen
            f.esquerda = x; // Coloca o nó da esquerda como o primeiro nó da fila de prioridade
            f.direita = y; // Coloca o nó da direita como o primeiro nó da fila de prioridade

            raiz = f; // Coloca o nó de Huffman como raiz
            q.add(f); // Adiciona o nó de Huffman na fila de prioridade
        }

        printCode(raiz, ""); // Chama o método que cria o código de Huffman
    }

    /*
     * esse metodo cria um HashMap com o caractere e o código de Huffman
     * o método retorna o HashMap
     * o método é chamado no método main()
     * o método é chamado no método createTree()
     */

    public static HashMap<Character, String> createCode() throws IOException { 
        RandomAccessFile arq = new RandomAccessFile("huffmanCode.txt", "r"); 

        HashMap<Character, String> code = new HashMap<Character, String>(); // cria um HashMap com o caractere e o código de Huffman

        arq.seek(0); 
        while (arq.getFilePointer() < arq.length()) { 
            String line = arq.readLine(); 
            String[] split = line.split("="); // separa a linha em um vetor de Strings
            code.put(split[0].charAt(1), split[1]); // coloca o caractere e o código no HashMap
            split = null; // limpa o vetor 
        }

        arq.close();
        return code; // retorna o HashMap
    }


    public static String removeMetaDados(String cod) { 
        String codSemMeta = "";

        for (int i = 0; i < cod.length(); i++) { // para cada caractere no código de Huffman
            if (cod.charAt(i) == '1') { // se o caractere for 1
                codSemMeta += '1'; // adiciona 1 no código sem metadados
            } else if (cod.charAt(i) == '0') { // se o caractere for 0
                codSemMeta += '0'; // adiciona 0 no código sem metadados
            }
        }

        return codSemMeta; 
    }

    public static int stringToBinary(String codigo) { // metodo que converte o código de Huffman em binário
        int result = 0;

        for (int i = 0; i < codigo.length(); i++) { // para cada caractere no código de Huffman
            if (codigo.charAt(i) == '1') { // se o caractere for 1
                result += Math.pow(2, codigo.length() - i - 1); // adiciona 2 elevado ao tamanho do código menos o
                                                                // índice do caractere menos 1 (para começar do 0) no
                                                                // resultado
            }
        }

        return result; // Retorna o resultado
    }

    public static void compress(HashMap<Character, String> code) throws IOException { 
        RandomAccessFile arq = new RandomAccessFile("copy.txt", "r"); 
        RandomAccessFile arq2 = new RandomAccessFile("comprimido.bin", "rw"); // um arquivo .bin para armazenar o texto compactado

        arq.seek(0); // vai  para o início do arquivo .txt
        while (arq.getFilePointer() < arq.length()) { 
            String line = arq.readLine(); // Lê uma linha do arquivo .txt

            for (int i = 0; i < line.length(); i++) { // Para cada caractere na linha
                char c = line.charAt(i); // pega o caractere
                String cod = code.get(c); // pega o código de Huffman do caractere
                if (cod != null) { // se o código de Huffman não for nulo
                    String codigo = removeMetaDados(cod); // chama o método que remove os metadados do código de Huffman
                    int bin = stringToBinary(codigo); // chama o método que converte o código de Huffman em binário
                    arq2.write(bin); // escreve o binário no arquivo .bin
                }
            }
        }

        arq.close();
        arq2.close();
    }

    /*  
     * esse metodo compacta o arquivo
     * o método retorna true se o arquivo foi compactado com sucesso
     * 
     */
    public static boolean compactar(String fileName) throws IOException { 
        long start = System.currentTimeMillis(); // Variável que armazena o tempo inicial

        deleteTxtFiles(); // chama o método que deleta os arquivos .txt
        createTxt(fileName); // chama o método que cria os arquivos .txt

        HashMap<Character, Integer> map = createHash(); // define um HashMap com o caractere e a frequência
        createTree(map); 

        HashMap<Character, String> huff = createCode(); // define um HashMap com o caractere e o código de Huffman
        compress(huff); 

        File copia = new File("copy.txt"); // abre o arquivo .txt com a cópia do texto 
        copia.delete(); // deleta o arquivo .txt com a cópia do texto
        
        long time = (System.currentTimeMillis() - start); 
        float tamanhoOriginal = new File(fileName).length(); 
        float tamanho = new File("comprimido.bin").length(); 

        System.out.println("\nTempo de execução: " + time + " /ms"); // imprime o tempo de execução do programa
        System.out.println("Tamanho do arquivo original: " + tamanhoOriginal + " bytes"); // imprime o tamanho do arquivo original
        System.out.println("Tamanho do arquivo compactado: " + tamanho + " bytes"); // imprime o tamanho do arquivo compactado
        System.out.println("Taxa de compressão: " + (tamanhoOriginal / tamanho) + " vezes"); // imprime a taxa de compressão

        return true; // Retorna true
    }


    /* 
     * esse metodo remove os zeros a esquerda do código de Huffman
     * o método retorna o código de Huffman sem os zeros a esquerda
     * o método é chamado no método main()
     * se o código de Huffman for 0001, o método retorna 1
     * se o código de Huffman for 0000, o método retorna 0
     */
    public static String removeZeroEsq(String codigo) { 
        String cod = "";

        for (int i = 0; i < codigo.length(); i++) { 
            if (codigo.charAt(i) == '1') { 
                cod = codigo.substring(i); // remove
                break;
            }
        }

        return cod; //retorna código
    }

    /*
     * esse metodo descompacta o arquivo
     * o método retorna true se o arquivo foi descompactado com sucesso
     * o método retorna false se o arquivo não foi descompactado com sucesso
     * o método é chamado no método main(), removeZeroEsq(), stringToBinary(), descompress(), deleteTxtFiles(), createTxt(),
     * createHash(), createTree(), createCode()
     * 
     */

    public static boolean descompactar(String fileName) throws IOException { 
        long start = System.currentTimeMillis(); 

        HashMap<String, Character> map = new HashMap<String, Character>(); // cria um HashMap com o código de Huffman e
                                                                           // o caractere
        RandomAccessFile huff = new RandomAccessFile("huffmanCode.txt", "r"); //

        huff.seek(0); // vai para o início do arquivo .txt
        while (huff.getFilePointer() < huff.length()) { // enquanto não chegar no final do arquivo .txt
            String line = huff.readLine(); // lê uma linha do arquivo .txt
            String[] split = line.split("="); // separa a linha em um vetor de Strings
            String codigo = split[1];
            codigo = removeZeroEsq(codigo); 
            char c = split[0].charAt(1); // pega o caractere
            map.put(removeMetaDados(codigo), c); // adiciona o código de Huffman e o caractere no HashMap sem os
                                                 // metadados
            split = null; // limpa o vetor
        }

        huff.close();

        RandomAccessFile raf = new RandomAccessFile(fileName, "r"); // abre o arquivo .bin com o texto compactado
        RandomAccessFile arq = new RandomAccessFile("descomprimido.txt", "rw"); // cria um arquivo .txt para armazenar o
                                                                                // texto descompactado
        if (arq.length() > 0) { // se o arquivo .txt não estiver vazio
            arq.setLength(0); // limpa o arquivo
        }


        /*
         * converte o código de Huffman em binário e escreve o caractere no arquivo .txt
         * remove os metadados do código de Huffman
         * se o caractere for ;, escreve uma quebra de linha no arquivo .txt
         * se o caractere não for ;, escreve o caractere no arquivo .txt
         * 
         */
        raf.seek(0); // vai para o início do arquivo .bin
        while (raf.getFilePointer() < raf.length()) { 
            int bin = raf.read(); // lê um byte do arquivo .bin

            String codigo = Integer.toBinaryString(bin); 

            codigo = removeMetaDados(codigo); // cama o método que remove os metadados do código de Huffman

            if (map.get(codigo) != null) { // se o código de Huffman não for nulo
                if (map.get(codigo) == ';')
                    arq.writeChars("\n"); // se o caractere for ;, escreve uma quebra de linha no arquivo .txt
                else
                    arq.writeChar(map.get(codigo)); // escreve o caractere no arquivo .txt
            }
        }

        arq.close();
        raf.close();

        long time = (System.currentTimeMillis() - start); // calcula o tempo de execução do programa em milissegundos

        System.out.println("\nTempo de execução: " + time + " /ms"); // imprime o tempo de execução 

        return true;
    }
}