import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Main extends CRUD {
    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("jogos.bin", "rw"); // Cria o arquivo
        Scanner sc = new Scanner(System.in); 
        Jogo jogo = new Jogo();

        int opcao = 0; // Variavel para armazenar a opcao dos menus
        boolean loop = true; // Variavel para controlar o loop do menu principal

        if (raf.length() == 0) raf.writeInt(0); // Se o arquivo estiver vazio escreve 0 no inicio do arquivo para indicar o ultimo id utilizado
        raf.seek(0); // Volta para o inicio do arquivo
        ultimoId = raf.readInt(); // Le o ultimo id utilizado

        while(loop) { // Loop do menu principal
            System.out.println("____________MENU____________");
            System.out.println("|                          |");
            System.out.println("|0 - Sair                  |");
            System.out.println("|1 - Cadastrar jogo        |");
            System.out.println("|2 - Ver lista de jogos    |");
            System.out.println("|3 - Atualizar jogo        |");
            System.out.println("|4 - Deletar jogo          |");
            System.out.println("|__________________________|\n");
            System.out.print("-> ");
            do { 
                try {
                    opcao = sc.nextInt();
                    if(opcao < 0 || opcao > 9) System.out.println("-> Opção inválida!"); 
                } catch (Exception e) { // Se a opcao não for um numero
                    System.out.println("-> Digite um número:");
                    sc.nextLine();
                    break;
                }
            } while (opcao < 0 || opcao > 9); // Enquanto a opcao for invalida continua no loop

            switch (opcao) {
                case 1: //criar
                    jogo = new Jogo();
                    System.out.println("\n____________CADASTRAR JOGO____________");
                    String in = "";
                    //ler dados
                    if(raf.length() > 5) { // Se o arquivo estiver vazio 
                        do {
                            System.out.print("ID Jogo: ");
                            in = sc.next();
                        } while (readIdJogo(raf, in).getIdJogo().equals(in)); // Enquanto o usuario já existir continua no loop
                        jogo.setIdJogo(in);
                    }else {
                        System.out.print("ID Jogo: ");
                        in = sc.next();
                        jogo.setIdJogo(in);
                    }

                    System.out.print("Nome: ");
                    jogo.setNome(sc.next());

                    System.out.print("Data: ");
                    jogo.setDataRegistro(sc.next());

                    System.out.print("Quantidade de idiomas: ");
                    jogo.setQntIdiomas(sc.nextInt());
                    String[] idiomas = new String[jogo.getQntIdiomas()];
                    for(int i = 0; i < jogo.getQntIdiomas(); i++) {
                        System.out.print("Idioma " + (i + 1) + ": ");
                        idiomas[i] = sc.next();
                    }
                    jogo.setIdiomas(idiomas);

                    System.out.print("Valor: ");
                    jogo.setValor(sc.nextFloat());

                    jogo.setId(++ultimoId); // Incrementa o ultimo id utilizado e atribui ao id 

                    // Cria a no arquivo
                    if(create(raf, jogo)) System.out.println("\nJogo cadastrado com sucesso!"); 
                    else System.out.println("\nErro ao cadastrar jogo!");
                    
                    break;
                    
                    case 2:
                    System.out.println("____________PESQUISAR UM JOGO____________");

                    System.out.print("Informe o ID: ");
                    int pesquisa = sc.nextInt();

                    jogo = readById(raf, pesquisa); // Criar uma conta com os dados do registro lido

                    if(jogo == null) System.out.println("Jogo não encontrado!");
                    else { 
                        // Imprime os dados do jogo
                        System.out.println("\nAchamos!");
                        System.out.println("-> ID: " + jogo.getIdJogo());
                        System.out.println("-> Nome: " + jogo.getNome());
                        System.out.println("-> Data: " + jogo.getDataRegistro());
                        System.out.println("-> Idiomas que foi traduzido: ");
                        for(int i = 0; i < jogo.getQntIdiomas(); i++) {
                            System.out.println("-> " + jogo.getIdiomas()[i]);
                        }
                        
                        System.out.println("-> Valor na Steam: " + jogo.getValor());
                        
                    }
                    break;
                case 3: // Atualizar registro
                    System.out.println("____________ATUALIZAR JOGO CADASTRADO____________");
                    System.out.print("-> ID do jogo: ");
                    String idjogo = sc.next();

                    jogo = readIdJogo(raf, idjogo); // 

                    if(jogo == null) System.out.println("-> Não achamos seu jogo!");
                    else {
                        // Imprime os dados da conta
                        System.out.println("\n-> Conta encontrada!");
                        System.out.println("-> ID: " + jogo.getIdJogo());
                        System.out.println("-> Nome: " + jogo.getNome());
                        System.out.println("-> Data: " + jogo.getDataRegistro());
                        System.out.println("-> Idiomas que foi traduzido: ");
                        for(int i = 0; i < jogo.getQntIdiomas(); i++) {
                            System.out.println("-> " + jogo.getIdiomas()[i]);
                        }
                        
                        System.out.println("-> Valor na Steam: " + jogo.getValor());

                        System.out.println("__________________________________________\n");

                        // Escolha do que atualizar
                        System.out.println("____________ATUALIZAR REGISTRO____________");
                        System.out.println("Qual campo deseja atualizar?");
                        System.out.println("-> 1 - ID do jogo");
                        System.out.println("-> 2 - Nome");
                        System.out.println("-> 3 - Data");
                        System.out.println("-> 4 - Idiomas");
                        System.out.println("-> 5 - Valor");
                        System.out.println("-> 6 - Cancelar");
                        System.out.print("-> ");

                        do {
                            try {
                                opcao = sc.nextInt();
                                if(opcao < 1 || opcao > 6) System.out.println("-> Opção inválida!");
                            } catch (Exception e) {
                                System.out.println("-> Digite um número!");
                                sc.next();
                                break;
                            }
                        } while (opcao < 1 || opcao > 6); // Enquanto a opção for inválida continua no loop

                        switch (opcao) { // Atualiza o campo escolhido
                            case 1: // Id
                                System.out.print("Novo ID: ");
                                jogo.setIdJogo(sc.next());
                                break;
                            case 2: // Nome
                                System.out.print("Novo nome: ");
                                jogo.setNome(sc.next());
                                break;
                            case 3: // Cidade
                                System.out.print("-> Nova data: ");
                                jogo.setDataRegistro(sc.next());
                                break;
                            case 4: // Email
                                System.out.print("Nova quantidade : ");
                                jogo.setQntIdiomas(sc.nextInt());
                                String[] idioma = new String[jogo.getQntIdiomas()];
                                for(int i = 0; i < jogo.getQntIdiomas(); i++) {
                                    System.out.print("-> Novo idioma " + (i + 1) + ": ");
                                    idioma[i] = sc.next();
                                }
                                jogo.setIdiomas(idioma);
                                break;
                            case 5: // Usuário
                                System.out.print("Valor: ");
                                jogo.setValor(sc.nextFloat());
                                break;
                            case 6: // Cancelar
                                System.out.println("-> Cancelado!\n");
                                break;
                        }

                        if(opcao != 6) { // Se a opção for diferente de 8, atualiza o registro
                            if(update(raf, jogo)) System.out.println("-> Atualizado com sucesso!");
                            else System.out.println("-> Erro ao atualizar!");
                        }
                    }
                    break;
                case 5: // Deletar registro
                    System.out.println("____________DELETAR JOGO____________");
                    System.out.print("-> ID: ");
                    pesquisa = sc.nextInt();

                    jogo = readById(raf, pesquisa); // Criar uma conta com os dados do registro lido
                    if(jogo == null) System.out.println("-> Jogo não encontrado!");
                    else { // Se a conta for encontrada, imprime os dados
                        System.out.println("\n-> Conta encontrada!");
                        System.out.println("-> ID: " + jogo.getId());
                        System.out.println("-> ID jogo: " + jogo.getIdJogo());
                        System.out.println("-> Nome do jogo: " + jogo.getNome());
                        System.out.println("-> Data de lançamento: " + jogo.getDataRegistro());
                        System.out.println("-> Idiomas: ");
                        for(int i = 0; i < jogo.getQntIdiomas(); i++) {
                            System.out.println("-> " + jogo.getIdiomas()[i]);
                        }
                        System.out.println("-> Valor: " + jogo.getValor());
                        System.out.println("________________________________________\n");

                        // Confirmação de exclusão
                        System.out.println("-> Deseja realmente deletar essa conta?");
                        System.out.println("-> 1 - Sim");
                        System.out.println("-> 2 - Não");
                        System.out.print("-> ");

                        do {
                            try {
                                opcao = sc.nextInt();
                                if(opcao < 1 || opcao > 2) System.out.println("-> Opção inválida!");
                            } catch (Exception e) {
                                System.out.println("-> Digite um número!");
                                sc.next();
                                break;
                            }
                        } while (opcao < 1 || opcao > 2); // Enquanto a opção for inválida continua no loop

                        if(opcao == 1) { // Se a opção for 1, deleta o registro
                            if(delete(raf, jogo)) System.out.println("-> Deletado com sucesso!");
                            else System.out.println("-> Erro ao deletar!");
                        }
                        else System.out.println("-> Cancelado!\n");
                    }
                    break;
            }
        }
    }
}