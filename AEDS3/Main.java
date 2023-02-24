import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Main extends CRUD {
    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("jogos.bin", "rw"); // Cria o arquivo
        Scanner sc = new Scanner(System.in); 
        Game game = new Game();

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
                    System.out.println("-> Digite um número!");
                    sc.nextLine();
                    break;
                }
            } while (opcao < 0 || opcao > 9); // Enquanto a opcao for invalida continua no loop

        }
    }
}