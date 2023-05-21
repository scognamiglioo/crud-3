import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends CRUD {
    public static void main(String[] args) throws IOException {

        RandomAccessFile raf = new RandomAccessFile("teste.bin", "rw"); // Cria o arquivo
        RandomAccessFile arvoreBindices = new RandomAccessFile("arvoreBindices", "rw"); // Arquivo de índice Arvore B+
                                                                                        // de ordem 8
        // arvoreBM arvoreDeIndices = new arvoreBM(8); // Estrutura de arvire para
        // armazenar o índice localmente
        hashExtensivel.tabelaBuckets indiceHash = new hashExtensivel.tabelaBuckets(); // Estrutura de tabela de buckets
                                                                                      // para armazenar o índice
                                                                                      // localmente
       

        Scanner sc = new Scanner(System.in);
        Jogo jogo = new Jogo();

        int opcao = 0; // Variavel para armazenar a opcao dos menus
        String entrada = new String();
        boolean loop = true; // Variavel para controlar o loop do menu principal
        ArrayList<String> dicionario = new ArrayList<String>(); // dicionario para armazenar os nomes dos jogos

        if (raf.length() == 0)
            raf.writeInt(0); // Se o arquivo estiver vazio escreve 0 no inicio do arquivo para indicar o
                             // ultimo id utilizado
        raf.seek(0); // Volta para o inicio do arquivo
        ultimoId = raf.readInt(); // Le o ultimo id utilizado

        while (loop) { // Loop do menu principal
            System.out.println("____________MENU____________");
            System.out.println("|                          |");
            System.out.println("|0 - Sair                  |");
            System.out.println("|1 - Cadastrar jogo        |");
            System.out.println("|2 - Ver lista de jogos    |");
            System.out.println("|3 - Atualizar jogo        |");
            System.out.println("|4 - Deletar jogo          |");
            System.out.println("|5 - Ordenação jogo        |");
            System.out.println("|6 - Indices               |");
            System.out.println("|7 - Compressão            |");
            System.out.println("|8 - Descompressão         |");
            System.out.println("|__________________________|\n");
            System.out.print("-> ");
            do {
                try {
                    opcao = sc.nextInt();
                    if (opcao < 0 || opcao > 9)
                        System.out.println("-> Opção inválida!");
                } catch (Exception e) { // Se a opcao não for um numero
                    System.out.println("-> Digite um número:");
                    sc.nextLine();
                    break;
                }
            } while (opcao < 0 || opcao > 9); // Enquanto a opcao for invalida continua no loop

            switch (opcao) {
                case 0:
                    loop = false;
                    break;

                case 1: // criar
                    jogo = new Jogo();
                    System.out.println("\n____________CADASTRAR JOGO____________");
                    String in = "";
                    // ler dados
                    if (raf.length() > 5) { // Se o arquivo estiver vazio
                        do {
                            System.out.print("ID Jogo: ");
                            in = sc.next();
                        } while (readIdJogo(raf, in).getIdJogo().equals(in)); // Enquanto o usuario já existir continua
                                                                              // no loop
                        jogo.setIdJogo(in);
                    } else {
                        System.out.print("ID Jogo: ");
                        in = sc.next();
                        jogo.setIdJogo(in);
                    }

                    sc.nextLine();

                    System.out.print("Nome: ");
                    jogo.setNome(sc.nextLine());

                    System.out.print("Data: ");
                    jogo.setDataRegistro(sc.next());

                    sc.nextLine();

                    System.out.print("Quantidade de gêneros: ");
                    jogo.setQntGeneros(sc.nextInt());
                    sc.nextLine(); // Captura a quebra de linha deixada pelo nextInt()

                    String[] generos = new String[jogo.getQntGeneros()];
                    for (int i = 0; i < jogo.getQntGeneros(); i++) {
                        System.out.print("Gênero " + (i + 1) + ": ");
                        generos[i] = sc.nextLine();
                    }
                    jogo.setGeneros(generos);
                    System.out.print("Valor: ");
                    entrada = sc.next();
                    float valor = Float.parseFloat(entrada);
                    jogo.setValor(valor);

                    jogo.setId(++ultimoId); // Incrementa o ultimo id utilizado e atribui ao id

                    // Cria a no arquivo
                    if (create(raf, jogo))
                        System.out.println("\nJogo cadastrado com sucesso!");
                    else
                        System.out.println("\nErro ao cadastrar jogo!");

                    // arvoreDeIndices = indice.indiceArvore(raf); //Atualiza o arquivo de índices
                    break;

                case 2:
                    System.out.println("____________PESQUISAR UM JOGO____________");

                    System.out.print("Informe o ID do arquivo: "); // Aqui pede o arquivo, na ordem que ele foi colocado
                                                                   // e não o ID do jogo como no csv
                    int pesquisa = sc.nextInt();

                    jogo = readById(raf, pesquisa); // Criar uma jogo com os dados do registro lido

                    if (jogo == null)
                        System.out.println("Jogo não encontrado!");
                    else {
                        // Imprime os dados do jogo
                        System.out.println("\nAchamos!");
                        System.out.println("-> ID: " + jogo.getIdJogo());
                        System.out.println("-> Nome: " + jogo.getNome());
                        System.out.println("-> Data: " + jogo.getDataRegistro());
                        System.out.print("-> Genêros:");
                        for (int i = 0; i < jogo.getQntGeneros(); i++) {
                            System.out.print(" " + jogo.getGeneros()[i]);
                        }
                        System.out.println();
                        System.out.println("-> Valor na Steam: " + jogo.getValor());

                    }
                    break;
                case 3: // Atualizar registro
                    System.out.println("____________ATUALIZAR JOGO CADASTRADO____________");
                    int caso = 0;
                    String idjogo = new String();
                    System.out.println("Digite o ID do jogo que deseja atualizar:"); // Aqui é com base no valor do ID
                                                                                     // no csv e não da ordem no arquivo
                    idjogo = sc.next();
                    Jogo jogoNovo = CRUD.readIdJogo(raf, idjogo);

                    System.out.println("Qual campo deseja atualizar?");
                    System.out.println("-> 1 - ID do jogo");
                    System.out.println("-> 2 - Nome");
                    System.out.println("-> 3 - Data");
                    System.out.println("-> 4 - Genêros");
                    System.out.println("-> 5 - Valor");
                    System.out.println("-> 6 - Cancelar");
                    entrada = sc.next();
                    caso = Integer.parseInt(entrada);

                    if (caso == 1) {
                        System.out.println("---- Atualizando ID jogo ----");
                        System.out.println("Digite o novo valor:");
                        entrada = sc.next();
                        jogoNovo.setIdJogo(entrada);
                        CRUD.update(raf, jogoNovo);

                    } else if (caso == 2) {
                        System.out.println("---- Atualizando nome do jogo ----");
                        System.out.println("Digite o novo valor:");
                        entrada = sc.next();
                        jogoNovo.setNome(entrada);
                        CRUD.update(raf, jogoNovo);
                    } else if (caso == 3) {
                        System.out.println("---- Atualizando data ----");
                        System.out.println("Digite o novo valor:");
                        entrada = sc.next();
                        jogoNovo.setDataRegistro(entrada);
                        CRUD.update(raf, jogoNovo);
                    } else if (caso == 4) {
                        System.out.println("---- Atualizando Genero ----");
                        System.out.println("Digite a nova quantidade de genêros:");
                        entrada = sc.next();
                        jogoNovo.setQntGeneros(Integer.parseInt(entrada));
                        String[] genero = new String[jogoNovo.getQntGeneros()];
                        for (int i = 0; i < jogoNovo.getQntGeneros(); i++) {
                            System.out.println("Digite o " + (i + 1) + " genêro:");
                            entrada = sc.next();
                            genero[i] = entrada;
                        }
                        jogoNovo.setGeneros(genero);
                        CRUD.update(raf, jogoNovo);

                    } else if (caso == 5) {
                        System.out.println("---- Atualizando valor ----");
                        System.out.println("Digite o novo valor:");
                        entrada = sc.next();
                        // jogoNovo.setValor(entrada);
                        CRUD.update(raf, jogoNovo);
                    }

                    break;
                case 4: // Deletar registro
                    System.out.println("____________DELETAR JOGO____________");
                    System.out.println("Digite o ID no arquivo do registro que deseja deletar:");
                    entrada = sc.next();
                    CRUD.delete(raf, CRUD.readById(raf, Integer.parseInt(entrada)));
                    // arvoreDeIndices = indice.indiceArvore(raf);
                    break;
                case 5: // ordenação
                    int casoAux = 0;
                    System.out.println("____________ORDERNAR JOGO____________");
                    System.out.println("Qual tipo de ordenação deseja fazer?");
                    System.out.println("-> 1 - Intercalação balanceada comum");
                    System.out.println("-> 2 - Intercalação balanceada com blocos de tamanho variável");
                    System.out.println("-> 3 - Intercalação balanceada com seleção por substituição");
                    System.out.println("-> 4 - Voltar ao menu principal");

                    entrada = sc.next();
                    casoAux = Integer.parseInt(entrada);

                    switch (casoAux) {
                        case 1:
                            if (ordenacao.intercalacao(raf) == true) {
                                System.out.println("Ordenação realizada com sucesso!");
                            } else {
                                System.out.println("Erro na ordenação!");
                            }
                            break;

                        case 2: // não está funcionando
                            if (ordenacao.ordenacaoIntercalacaoBalanceada(raf) == true) {
                                System.out.println("Ordenação realizada com sucesso!");
                            } else {
                                System.out.println("Erro na ordenação!");
                            }
                            break;

                    }
                    break;

                // ordem dos arquivos para video:
                // 1 - indice.java jogos.bin
                // 2 - arvoreBM.java jogos.bin
                // 3 - hashExtensivel.java bdHashExtensivel.bin
                // hash ja acessa o proprio arquivo bin quando voce chama ele, n precisa mudar

                // 4 - listaInvertida.java jogos.bin
                case 6: // Índice
                    System.out.println("____________ÍNDICES____________");
                    int tipo = 0;
                    System.out.println("Escolha o tipo de índice:");
                    System.out.println("1) Árvore B+ de ordem 8");
                    System.out.println("2) Hash Estendido");
                    System.out.println("3) Lista Invertida");
                    tipo = sc.nextInt();

                    switch (tipo) {
                        case 1:
                            // arvoreDeIndices = indice.indiceArvore(raf);
                            break;

                        case 2:
                            indiceHash = hashExtensivel.indiceHash(raf);
                            break;
                        case 3:
                            System.out.println("Escolha o tipo de lista:");
                            System.out.println("1) Pelo valor");
                            System.out.println("2) Pelo gênero");

                            int tipoLista = sc.nextInt();

                            if (tipoLista == 1) {
                                System.out.print("\n-> Digite o valor: ");
                                float valorAux = sc.nextFloat();
                                if (listaInvertida.listarValor(raf, valorAux))
                                    System.out.println("\n-> Listado com sucesso!");
                                else
                                    System.out.println("\n-> Erro ao listar!");
                            } else if (tipoLista == 2) {
                                System.out.print("\n-> Digite o ID do jogo: ");
                                int genero = sc.nextInt();
                                if (listaInvertida.listarGenero(raf, genero))
                                    System.out.println("\n-> Listado com sucesso!");
                                else
                                    System.out.println("\n-> Erro ao listar!");
                            } else {
                                System.out.println("2) Pelo ID do jogo");
                            }
                            break;
                    }
                    break;
                 

                case 7: // Compactação
                    System.out.println("____________COMPACTAÇÃO____________");
                    tipo = 0;
                    System.out.println("Escolha o tipo de compactação:");
                    System.out.println("1) Huffman");
                    System.out.println("2) LZW");
                    tipo = sc.nextInt();
                    if (tipo == 1) {
                        System.out.println("____________HUFFMAN____________");
                        System.out.println("Digite o nome do arquivo de dados:");
                        String nomeArquivo = sc.next();
                        huffman.compactar(nomeArquivo);
                    } else if (tipo == 2) {
                        System.out.println("____________LZW____________");
                        System.out.println("Digite o nome do arquivo de dados:");
                        String nomeArquivo = sc.next();
                        dicionario = lzw.compressao(nomeArquivo);
                    }
                    break;
                case 8:
                    System.out.println("____________DESCOMPACTAÇÃO____________");
                    tipo = 0;
                    System.out.println("Escolha o tipo de descompactação:");
                    System.out.println("1) Huffman");
                    System.out.println("2) LZW");
                    tipo = sc.nextInt();

                    if (tipo == 1) {
                        System.out.println("____________HUFFMAN____________");
                        System.out.println("Digite o nome do arquivo de dados:");
                        String nomeArquivo = sc.next();
                        huffman.descompactar(nomeArquivo);

                    } else if(tipo==2){
                        System.out.println("____________LZW____________");
                        System.out.println("Digite o nome do arquivo de dados:");
                        String nomeArquivo = sc.next();
                        //lzw.decompressao(nomeArquivo,lzw.compressao(nomeArquivo));
                        lzw.decompressao(nomeArquivo,dicionario);
                    } 
                    break;

            }
            
        }
        raf.close();
    }
}