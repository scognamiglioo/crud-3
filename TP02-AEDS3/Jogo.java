import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Jogo  {
    // --------------- Atributos ---------------
    private int id;
    private String idJogo;
    private String nome;
    private String dataRegistro;
    private int qntGeneros;
    private String[] generos;
    private float valor;

    // --------------- Construtores ---------------
    public Jogo() {
        this.id = -1;
        this.idJogo = null;
        this.nome = null;
        this.dataRegistro = null;
        this.qntGeneros = 0;
        this.generos = new String[qntGeneros];
        this.valor = -1;

    }

    public Jogo(int id, String idJogo, String nome, String dataRegistro, int qntGeneros, String[] generos, float valor) {
        this.id = id;
        this.idJogo = idJogo;
        this.nome = nome;
        this.dataRegistro = dataRegistro;
        this.qntGeneros = qntGeneros;
        this.generos = generos;
        this.valor = valor; 
    }

    // --------------- Get/Set ---------------

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getIdJogo() {return idJogo;}
    public void setIdJogo(String idJogo) {this.idJogo = idJogo;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getDataRegistro() {return dataRegistro;}
    public void setDataRegistro(String dataRegistro) {this.dataRegistro = dataRegistro;}

    public int getQntGeneros() {return qntGeneros;}
    public void setQntGeneros(int qntGeneros) {this.qntGeneros = qntGeneros;}

    public String[] getGeneros() {return generos;}
    public void setGeneros(String[] generos) {this.generos = generos;}

    public float getValor() {return valor;}
    public void setValor(float valor) {this.valor = valor;}

    /*
     * metodo para imprimir as informações
     * @return String com dados 
     */

    public void print(){
            System.out.println("---- INFORMAÇÕES DO JOGO ----");
            System.out.println("ID: " + this.idJogo);
            System.out.println("Nome: " + this.nome);
            System.out.println("Data de lançamento: " + this.dataRegistro);
            System.out.println("Quantidade de generos traduzido: " + this.qntGeneros);
            System.out.println("Genêros: ");
            System.out.println("[ ");
            for (int i = 0; i < this.qntGeneros; i++) {
                if (i != qntGeneros - 1) {
                    System.out.print(generos[i] + " ; ");
                } else {
                    System.out.print(generos[i]);
                }
            }
           
            System.out.println("Valor do jogo na Steam: " + this.valor);
        }

        
        /**
        * função: esse metodo serve para converter um objeto Jogo em um arranjo de bytes.
        * @return Arranjo de bytes.
        * @throws IOException Excecao das classes ByteArrayOutputStream e DataOutputStream, que sera tratada no metodo que chamou este metodo.
        */

    public byte[] toByteArray() throws IOException {  //Método para transformar uma jogo em um array de bytes para facilitar operações no arquivo de dados
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeInt(this.getId()); // Escreve o id do jogo no array de bytes
            dos.writeUTF(this.getIdJogo());
            dos.writeUTF(this.getNome()); // Escreve o nome do jogo no array de bytes
            dos.writeUTF(this.getDataRegistro()); //Escreve data de lançamento
            dos.writeInt(this.getQntGeneros()); // Escreve a quantidade de generos no array de bytes
            for(int i = 0; i < this.getQntGeneros(); i++){ // Escreve os idomas no array de bytes
                dos.writeUTF(this.getGeneros()[i]);
            }
            dos.writeFloat(this.getValor()); // Escreve o valor na steam no array de bytes
    
            dos.close();
            baos.close();
    
            return baos.toByteArray(); // Retorna o array de bytes
    }

    /**
     * função: esse metodo serve para converter um arranjo de bytes em um objeto Jogo.
     * @param b Arranjo de bytes.
     * @throws IOException Excecao das classes ByteArrayInputStream e DataInputStream, que sera tratada no metodo que chamou este metodo.
     */
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba); // Cria um array de bytes
        DataInputStream dis = new DataInputStream(bais); // Cria um fluxo de dados

        this.id = dis.readInt(); // Le o id do array de bytes
        this.idJogo = dis.readUTF();
        this.nome = dis.readUTF(); // Le o nome do jogo do array de bytes
        this.dataRegistro = dis.readUTF(); // Le a data de lançamento do array de bytes
        this.qntGeneros = dis.readInt(); // Le a quantidade de generos do array de bytes
        for(int i = 0; i < this.getQntGeneros(); i++){ // Le os generos do array de bytes
            this.generos[i] = dis.readUTF();
        }
        this.valor = dis.readFloat(); // Le o valor do jogo do array de bytes
    }

    public short size() throws IOException {
        return (short)this.toByteArray().length;
    }



}
