import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.text.Position;

// hash extensivel com 4 chaves por bucket e profundidade inicial 1 (2 buckets) e aumenta a profundidade exponencialmente 
// de acordo com a função hash fornecida no enunciado
// a profundidade dobra a cada aumento e o numero de buckets aumenta em uma unidade
// a indexação é feita por meio de um arquivo de buckets com o campo ID 
// e ele le ID + endereço para colocar no bucket correspondente 

// o arquivo CSV aqui é o bdHasH.csv pois contem x % 5 da base inicial

public class hashExtensivel { 

    public static int funcaoHash(int k, int p) { //Função hash fornecida no enunciado
        int posicao=0;
        posicao=k%( (int)Math.pow(2 , p));
        return posicao;
    }

    public static class chaveEndereco { //Classe que armazena os conjuntos de chave-endereço (ID e posição no arquivo de dados)
        int chave;
        double endereco;

        public chaveEndereco(int chave, double endereco) {
            this.chave = chave;
            this.endereco = endereco;
        }

        public chaveEndereco() {
            this.chave = -1;
            this.endereco = -1;
        }

        public int getChave() {
            return chave;
        }
    }

    public static class bucket{ //Bucket corresponde a cada "linha" do arquivo de buckets 
        ArrayList<chaveEndereco> bucket; 

        public bucket() {
            this.bucket = new ArrayList<chaveEndereco>();
        }
    }

    public static class tabelaBuckets { //corresponde a todo o arquivo de índice que conterá os conjuntos de chave-endereço (4 por bucket)
        ArrayList<bucket> tabelaBukets; //Implementação na forma de um arraylist de buckets (que é um array list de chave-endereço)
        int p=1; //Profundidade inicial
        int m=4; //Numero de chave-endereço por bucket
        int numeroBuckets=1;
        
        public int getProfundidade(){
            return this.p;
        }
       
        public tabelaBuckets() { //Construtor da tabela de buckets
            tabelaBukets=new ArrayList<bucket>();
            bucket b=new bucket();
            tabelaBukets.add(b); //Adiciona o primeiro bucket de 4 posições na tabela
            p=1;
        }

        public void inserir(chaveEndereco c) { //Recebe uma chave endereço
            int posicaoHash = funcaoHash(c.chave, p);
            int posicaoInsercao= posicaoHash%numeroBuckets; //Posição de inserção no arquivo de buckets é diferente da fornecida pela função hash uma vez que a profundidade dobra a cada aumento (exponencial) e o numero de buckets aumenta em uma unidade (linear)
            System.out.println("Inserindo a chave ("+c.chave+" , "+c.endereco+") no bucket "+posicaoInsercao);

            if(tabelaBukets.get(posicaoInsercao).bucket.size()<4){ //Insere no bucket-destino caso ele tenha menos de 4 elementos
                tabelaBukets.get(posicaoInsercao).bucket.add(c);
            }else{
                aumentaProfundidade(posicaoInsercao); //Caso contrário aumenta a profundidade do arquivo de buckets
                inserir(c); //Após o aumento de profundidade é necessário inserir o elemento responsável por chamar o aumento
            }
            
        }

        public void aumentaProfundidade (int bucketCheio){ //Recebe a posição do bucket cheio
            System.out.println("Aumento de profundidade: "+p+" -> "+(p+1));
            bucket b=new bucket();
            tabelaBukets.add(b); //Adiciona um novo bucket no arquivo de buckets
            ++p; //Aumenta a profundidade e o numero de buckets
            ++numeroBuckets;
            for(int i=0;i<m;i++){ //Redistribui os elementos do bucket cheio 
                chaveEndereco remanejado=new chaveEndereco(); 
                remanejado=tabelaBukets.get(bucketCheio).bucket.get(0);
                tabelaBukets.get(bucketCheio).bucket.remove(0);
                int posicaoHash=funcaoHash(remanejado.getChave(), p); 
                //Insere os elementos remanejados na estrutura novamente

                inserir(remanejado);
            }
        }

        public int find(int chave) { // busca por uma chave no arquivo de buckets

            int posicao = funcaoHash(chave, p);
            
            for(int i=0;i<tabelaBukets.get(posicao).bucket.size();i++){
                if(tabelaBukets.get(posicao).bucket.get(i).getChave()==chave){
                    return i;
                }
            }

            return -1;
        }

        public void remover(int chave) { // remove uma chave no arquivo de buckets
            int posicao = funcaoHash(chave, p);
            for(int i=0;i<tabelaBukets.get(posicao).bucket.size();i++){
                if(tabelaBukets.get(posicao).bucket.get(i).getChave()==chave){
                    tabelaBukets.get(posicao).bucket.remove(i);
                }
            }
        }

    }

    public static tabelaBuckets indiceHash (RandomAccessFile raf) throws IOException{ //Metodo que constroi o arquivo de buckets  
        tabelaBuckets indice = new tabelaBuckets(); //Foi necessario construir o índice em um arquivo separado para evitar conflitos com o índice de Arvore B+
        RandomAccessFile indicesHash = new RandomAccessFile("bdHashExtensivel.bin", "rw");
        raf.seek(4);
        while (raf.getFilePointer() < raf.length()) { // Percorre o arquivo de dados
            
            if (raf.readByte() == 0) {
                raf.readInt(); // tamanho
                int chave = raf.readInt(); // id
                double endereco = raf.getFilePointer(); // endereco
                
                raf.readUTF(); // id jogo
                raf.readUTF(); // nome
                raf.readUTF(); // data
                int qdt = raf.readInt(); // quantidade de genero
                for (int i = 0; i < qdt; i++) {
                    raf.readUTF(); // genero
                }
                
                raf.readFloat(); // preco
                chaveEndereco c=new chaveEndereco(chave,endereco);
                indicesHash.writeInt(chave);
                indicesHash.writeDouble(endereco);
                indice.inserir(c);
                
            } else {
                raf.skipBytes(raf.readInt());
            }
        }
        return indice;
    }

}