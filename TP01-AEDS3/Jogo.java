import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Jogo  {
    // --------------- Atributos ---------------
    private int id;
    private String nome;
    private String dataRegistro;
    private int qntIdiomas;
    private String[] idiomas;
    private float valor;

    // --------------- Construtores ---------------
    public Jogo() {
        this.id = -1;
        this.nome = null;
        this.dataRegistro = null;
        this.qntIdiomas = 0;
        this.idiomas = new String[qntIdiomas];
        this.valor = -1;

    }

    public Jogo(int id, String nome, String dataRegistro, int qntIdiomas, String[] idiomas, float valor) {
        this.id = id;
        this.nome = nome;
        this.dataRegistro = dataRegistro;
        this.qntIdiomas = qntIdiomas;
        this.idiomas = idiomas;
        this.valor = valor; 
    }

    // --------------- Get/Set ---------------

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getDataRegistro() {return dataRegistro;}
    public void setDataRegistro(String dataRegistro) {this.dataRegistro = dataRegistro;}

    public int getQntIdiomas() {return qntIdiomas;}
    public void setQntIdiomas(int qntIdiomas) {this.qntIdiomas = qntIdiomas;}

    public String[] getIdiomas() {return idiomas;}
    public void setIdiomas(String[] idiomas) {this.idiomas = idiomas;}

    public float getValor() {return valor;}
    public void setValor(float valor) {this.valor = valor;}

    /*
     * metodo para imprimir as informações
     * @return String com dados 
     */

    public void print(){
            System.out.println("---- INFORMAÇÕES DO JOGO ----");
            System.out.println("Nome: " + this.nome);
            System.out.println("Data de lançamento: " + this.dataRegistro);
            System.out.println("Quantidade de idiomas traduzido: " + this.qntIdiomas);
            System.out.println("Idiomas: ");
            System.out.println("[ ");
            for (int i = 0; i < this.qntIdiomas; i++) {
                if (i != qntIdiomas - 1) {
                    System.out.print(idiomas[i] + " / ");
                } else {
                    System.out.print(idiomas[i]);
                }
            }
           
            System.out.println("Valor do jogo na Steam: " + this.valor);
        }

        
        /**
        * função: esse metodo serve para converter um objeto Jogo em um arranjo de bytes.
        * @return Arranjo de bytes.
        * @throws IOException Excecao das classes ByteArrayOutputStream e DataOutputStream, que sera tratada no metodo que chamou este metodo.
        */

    public byte[] toByteArray() throws IOException {  //Método para transformar uma conta em um array de bytes para facilitar operações no arquivo de dados
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeInt(this.getId()); // Escreve o id do jogo no array de bytes
            dos.writeUTF(this.getNome()); // Escreve o nome do jogo no array de bytes
            dos.writeUTF(this.getDataRegistro()); //Escreve data de lançamento
            dos.writeInt(this.getQntIdiomas()); // Escreve a quantidade de idiomas no array de bytes
            for(int i = 0; i < this.getQntIdiomas(); i++){ // Escreve os idomas no array de bytes
                dos.writeUTF(this.getIdiomas()[i]);
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

        this.id = dis.readInt(); // Le o id da conta do array de bytes
        this.nome = dis.readUTF(); // Le o nome do jogo do array de bytes
        this.dataRegistro = dis.readUTF(); // Le a data de lançamento do array de bytes
        this.qntIdiomas = dis.readInt(); // Le a quantidade de idiomas do array de bytes
        for(int i = 0; i < this.getQntIdiomas(); i++){ // Le os idiomas do array de bytes
            this.idiomas[i] = dis.readUTF();
        }
        this.valor = dis.readFloat(); // Le o valor do jogo do array de bytes
    }

    public short size() throws IOException {
        return (short)this.toByteArray().length;
    }



}