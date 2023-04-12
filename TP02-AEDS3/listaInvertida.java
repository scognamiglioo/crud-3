import java.io.IOException;
import java.io.RandomAccessFile;


//dois tipos de lista invertida, uma por valor e outra por genero
//a valor é listado por meio de um float e a quantidade de generos por meio de um int
//valor lista todos os jogos com valor igual ao passado por parametro
//genero lista todos os jogos com quantidade de generos igual ao passado por parametro, 
//ou seja, se passar 2, lista todos os jogos que tem 2 generos


public class listaInvertida {

    // ------------------ Lista Invertida ------------------

    public static boolean listarValor(RandomAccessFile raf, float valor) throws IOException {

        RandomAccessFile lista = new RandomAccessFile("listaInvertidaValor.bin", "rw");
        if(lista.length() != 0){ // Se o arquivo não estiver vazio
            lista.setLength(0); // Zera o arquivo
        }

        System.out.println("\nValor " + valor);
        lista.writeFloat(valor); // Escreve o nome no arquivo

        raf.seek(4); // Pula o cabeçalho
        int contador = 0; // Contador de registros
        while (raf.length() != raf.getFilePointer()) { // Enquanto não chegar ao final do arquivo
            double pointer = raf.getFilePointer(); // Pega o ponteiro atual
            if(raf.readByte() == 0) { // Se o registro estiver ativo
                raf.readInt(); // tamanho 
                int id = raf.readInt(); // id
                raf.readUTF(); //id do jogo
                raf.readUTF(); // nome
                raf.readUTF(); // data
                int qntGeneros=raf.readInt(); // quantidade de generos
                for(int i=0;i<qntGeneros;i++){
                    raf.readUTF(); // generos
                }
                
                float n = raf.readFloat(); // preco
                
                if (n == valor) { // Se a valor for igual a passada por parâmetro
                    System.out.println("ID: " + id + " - Posição: " + (int) pointer); // Imprime o ID e a posição do registro
                    lista.writeInt(id); // Escreve o ID no arquivo de lista invertida
                    lista.writeDouble(pointer); // Escreve a posição do registro no arquivo de lista invertida
                    contador++;
                }
            }else { // Se o registro estiver inativo
                raf.skipBytes(raf.readInt()); // Pula o registro
            }
           
        }

        System.out.println("Quantidade de registros: " + contador);
        lista.writeInt(contador); // Escreve a quantidade de registros no arquivo de lista invertida

        lista.close();
        return true;
    }

    public static boolean listarGenero(RandomAccessFile raf, int qntGen) throws IOException {

        RandomAccessFile lista = new RandomAccessFile("listaInvertidaGenero.bin", "rw");
        if(lista.length() != 0){ // Se o arquivo não estiver vazio
            lista.setLength(0); // Zera o arquivo
        }

        System.out.println("\nQuantidade de genero: " + qntGen);
        lista.writeInt(qntGen); // Escreve o nome da idGame no arquivo

        raf.seek(4); // Pula o cabeçalho
        int contador = 0; // Contador de registros
        while (raf.length() != raf.getFilePointer()) { // Enquanto não chegar ao final do arquivo
            double pointer = raf.getFilePointer(); // Pega o ponteiro atual
            if(raf.readByte() == 0) { // Se o registro estiver ativo
                raf.readInt(); // tamanho 
                int id = raf.readInt(); // id
                raf.readUTF(); //id do jogo
                raf.readUTF(); // nome
                raf.readUTF(); // data
                int qntGeneros=raf.readInt(); // quantidade de generos
                for(int i=0;i<qntGeneros;i++){
                    raf.readUTF(); // generos
                }
                
                raf.readFloat(); // preco
                if (qntGeneros == qntGen) { // Se a idGame for igual a passada por parâmetro
                    System.out.println("ID: " + id + " - Posição: " + (int) pointer); // Imprime o ID e a posição do registro
                    lista.writeInt(id); // Escreve o ID no arquivo de lista invertida
                    lista.writeDouble(contador); // Escreve a posição do registro no arquivo de lista invertida
                    contador++;
                }
            }else { // Se o registro estiver inativo
                raf.skipBytes(raf.readInt()); // Pula o registro
            }
           
        }

        System.out.println("Quantidade de registros: " + contador);
        lista.writeInt(contador); // Escreve a quantidade de registros no arquivo de lista invertida

        lista.close();
        return true;
    }
}