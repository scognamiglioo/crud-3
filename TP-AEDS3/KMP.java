import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * o algoritmo KMP examina os caracteres de txt um a um, 
 * da esquerda para a direita, sem nunca retroceder. 
 * em cada iteração, o algoritmo sabe qual posição  k  de pat deve ser emparelhada com a próxima posição i+1 de txt.  
 * em outras palavras, no fim de cada iteração, o algoritmo sabe qual índice  k  
 * deve fazer o papel de j na próxima iteração.
 */
public class KMP {


    /* essa funçaõ calcula o array lps que guarda o tamanho do maior prefixo que também é sufixo
     * para cada subpadrao do padrao
     * 
    */
    public static int[] computeLPSArray(char[] padrao) {
        int[] lps = new int[padrao.length]; // cria um array de inteiros com o tamanho do padrao
        int len = 0; // tamanho do padrao
        int i = 1; // indice do padrao

        /* 
         * aqui o array lps é preenchido 
         * o array lps guarda o tamanho do maior prefixo que também é sufixo
         * para cada subpadrao do padrao
         */
        while (i < padrao.length) { // enquanto o indice for menor que o tamanho do padrao
            if (padrao[i] == padrao[len]) { // se o padrao na posicao i for igual ao padrao na posicao len
                len++; // incrementa o tamanho do padrao
                lps[i] = len; // o array lps na posicao i recebe o tamanho do padrao
                i++; // incrementa o indice
            } else {
                if (len != 0) { // se o tamanho do padrao for diferente de 0
                    len = lps[len - 1]; // o tamanho do padrao recebe o array lps na posicao len - 1
                } else {
                    lps[i] = 0; // o array lps na posicao i recebe 0
                    i++; // incrementa o indice
                }
            }
        }
        return lps;
    }

    /* essa funcao recebe o padrao e retorna a posicao do padrao no texto
     * 
     */

    public static int kmp(String padrao) throws IOException {
        long start = System.currentTimeMillis();

        RandomAccessFile raf2 = new RandomAccessFile("teste.txt", "r");
        StringBuilder sb = new StringBuilder();

        // leitura do arquivo txt auxiliar e armazenamento em uma string
        String line;
        while ((line = raf2.readLine()) != null) {
            sb.append(line);
        }
        String text = sb.toString();

        char[] padraoArr = padrao.toCharArray();
        char[] textArr = text.toCharArray();
        int[] lps = computeLPSArray(padraoArr);

        int i = 0;
        int j = 0;
        int comparacoes = 0;

        /*
         * aqui esse metodo compara o padrao com o texto, se o padrao for igual ao texto
         * na posicao i, incrementa o tamanho do padrao e o array lps na posicao i
         * recebe o tamanho do padrao, se o tamanho do padrao for diferente de 0, o
         * tamanho do padrao recebe o array lps na posicao len - 1, se o tamanho do
         * padrao for igual a 0, o array lps na posicao i recebe 0
         */

        while (i < textArr.length) { // enquanto o tamanho do texto for maior que o tamanho do padrao
            if (padraoArr[j] == textArr[i]) {
                j++;
                i++;
                comparacoes++;
            }
            if (j == padraoArr.length) { // se o tamanho do padrao for igual ao tamanho do texto
                long end = System.currentTimeMillis();
                System.out.println("Padrão encontrado na posição: " + (i - j));
                System.out.println("Tempo de execução: " + (end - start) + "ms");
                System.out.println("Número de comparações: " + comparacoes);
                return i - j;
            } else if (i < textArr.length && padraoArr[j] != textArr[i]) { // se o tamanho do padrao for menor que o
                                                                           // tamanho do texto e o padrao for diferente
                                                                           // do texto na posicao i
                if (j != 0) { // se o tamanho do padrao for diferente de 0
                    j = lps[j - 1]; // o tamanho do padrao recebe o array lps na posicao j - 1
                } else {
                    i++; // incrementa o tamanho do texto
                }
            }
        }

        int result = -1;
        return result;
    }

}
