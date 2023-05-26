import java.io.IOException;
import java.io.RandomAccessFile;


public class BoyerMoore {

    /*
     * esse metodo cria uma tabela de deslocamento 1 (tabela de bad character)
     * o boyer-moore usa essa tabela para deslocar o padrao para a direita
     * quando um caractere nao casar
     * 
     * @param padrao padrao a ser procurado
     */

    public static int[] badChar(char[] padrao) { // tabela de deslocamento 1 (tabela de bad character)
        int[] table = new int[255]; // tabela de deslocamento 1 (tabela de bad character)
        for (int i = 0; i < 255; i++) // esse for preenche a tabela com o tamanho do padrao
            table[i] = padrao.length; 
        for (int i = 0; i < padrao.length - 1; i++) // esse for preenche a tabela com o tamanho do padrao - 1
            table[padrao[i]] = padrao.length - 1 - i; // preenche a tabela com o tamanho do padrao - 1
        return table;
    }

    /*
     * esse metodo verifica se o padrao e um prefixo da palavra
     * 
     * @param palavra palavra a ser procurada
     */
    
    public static boolean ePrefixo(char[] palavra, int pos) {
        int sufixoaux = palavra.length - pos; // sufixoaux recebe o tamanho da palavra - pos
        for (int i = 0; i < sufixoaux; i++) // esse for verifica se o padrao e um prefixo da palavra
            if (palavra[i] != palavra[pos + i]) // se o padrao nao for um prefixo da palavra
                return false;
        return true;
    }

    /*
     * esse metodo verifica o tamanho do maior sufixo da palavra que e um prefixo do
     * padrao
     * 
     * @param palavra palavra a ser procurada
     */

    public static int sufixo_length(char[] palavra, int pos) {
        int i;
        for (i = 0; ((palavra[pos - i] == palavra[palavra.length - 1 - i]) && 
                (i < pos)); i++) {
        } //esse for verifica o tamanho do maior sufixo da palavra que e um prefixo do padrao
        return i;
    } 

    /*
     * esse metodo cria uma tabela de deslocamento 2 (tabela de good suffix)
     * o boyer-moore usa essa tabela para deslocar o padrao para a direita
     * quando um sufixo casar
     * 
     * @param padrao padrao a ser procurado
     */

    public static int[] makeD2(char[] padrao) {
        int[] delta2 = new int[padrao.length];
        int p;
        int last_prefix_index = padrao.length - 1; // last_prefix_index recebe o tamanho do padrao - 1
        for (p = padrao.length - 1; p >= 0; p--) { // esse for preenche a tabela com o tamanho do padrao
            if (ePrefixo(padrao, p + 1)) //se o padrao for um prefixo da palavra
                last_prefix_index = p + 1; // last_prefix_index recebe p + 1
            delta2[p] = last_prefix_index + (padrao.length - 1 - p); // preenche a tabela com o tamanho do padrao
        }
        for (p = 0; p < padrao.length - 1; p++) { // esse for preenche a tabela com o tamanho do padrao
            int slen = sufixo_length(padrao, p); // aqui ele verifica o tamanho do maior sufixo da palavra que e um prefixo do padrao
            if (padrao[p - slen] != padrao[padrao.length - 1 - slen]) // se o padrao for diferente do sufixo
                delta2[padrao.length - 1 - slen] = padrao.length - 1 - p + slen; // preenche a tabela com o tamanho do padrao
        }
        return delta2;
    }

    public static int bm(String prefix) throws IOException {

        long start = System.currentTimeMillis();

        RandomAccessFile raf2 = new RandomAccessFile("teste.txt", "r"); // arquivo txt criado para procurar o padrao
        String dados = new String();

        // leitura do arquivo txt auxiliar e armazenamento em uma string
        while (raf2.getFilePointer() < raf2.length()) {
            dados += raf2.readLine();
        }

        int[] d1 = badChar(prefix.toCharArray());
        int[] d2 = makeD2(prefix.toCharArray());
        int i = prefix.length() - 1;
        int comparacoes = 0;
        while (i < dados.length()) {
            int j = prefix.length() - 1;

            /*
             * esse metodo compara o padrao com o texto
             * se o padrao for igual ao texto, decrementa o i e o j
             * incrementa o numero de comparações
             * se o j for menor que 0, printa a posição do padrao encontrado
             * printa o tempo de execução
             */
            while (j >= 0 && (dados.charAt(i) == prefix.charAt(j))) { // compara o padrao com o texto
                i--; // decrementa o i
                j--; // decrementa o j
                comparacoes++; // incrementa o numero de comparações
            }
            if (j < 0) {
                long end = System.currentTimeMillis();
                System.out.println("Padrão encontrado na posição: " + (i + 1)); // printa a posição do padrao encontrado
                System.out.println("Tempo de execução: " + (end - start) + "ms"); // printa o tempo de execução
                System.out.println("Número de comparações: " + comparacoes); // printa o número de comparações
                return i + 1;
            }
            i += Math.max(d1[dados.charAt(i)], d2[j]);

        }

        int result = -1;

        return result;
    }

}
