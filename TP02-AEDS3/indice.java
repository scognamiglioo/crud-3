import java.io.IOException;
import java.io.RandomAccessFile;

//arquivo para declarar pegar um indice na arvore e retornar o ponteiro do registro no arquivo de dados

public class indice extends arvoreBM {
    
    static arvoreBM indiceArvore(RandomAccessFile raf)throws IOException{
        arvoreBM arvore = new arvoreBM(8);
        double posicao=0;
        int tamanhoRegistro=0;
        int idChave=0;
        raf.seek(4);


        RandomAccessFile arvoreBIndices = new RandomAccessFile("arvoreBIndices.bin", "rw"); //arquivo para armazenar os indices da arvore B+ para serem usados no arquivo de dados
        while(raf.getFilePointer()<raf.length()){
            if(raf.readByte()==0){ 
                tamanhoRegistro=raf.readInt();
                idChave=raf.readInt();
                posicao=raf.getFilePointer();
                arvore.insert(idChave, posicao); //insere o indice na arvore B+
                System.out.println("idChave: "+idChave+" posicao: "+posicao); //imprime o indice na arvore B+
                arvoreBIndices.writeInt(idChave); //insere o indice no arquivo de indices da arvore B+
                arvoreBIndices.writeDouble(posicao); //insere o ponteiro no arquivo de indices da arvore B+
                raf.readUTF(); //idJogo
                raf.readUTF(); //nome
                raf.readUTF(); //data
                int qntGeneros=raf.readInt(); //qntGeneros
                for(int i=0;i<qntGeneros;i++){
                    raf.readUTF(); //genero
                }
                
                raf.readFloat(); //preco
            }else{
                raf.skipBytes(raf.readInt());
            }
        }
        return arvore;
    }

    
}
