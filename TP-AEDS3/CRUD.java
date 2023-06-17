import java.io.RandomAccessFile;

public class CRUD {
     // --------------- Atributos ---------------

     static int ultimoId = 0;

     public static int getUltimoId(){
        return ultimoId;
       }
     
     // --------------- Create ---------------
 
     public static boolean create(RandomAccessFile raf, Jogo jogos) { // Cria uma jogo
 
         try {
             raf.seek(raf.length()); // Posiciona o ponteiro no final do arquivo
             raf.writeByte(0); // Escreve a lapide (0 = ativo)
             raf.writeInt(jogos.toByteArray().length); // Escreve o tamanho do registro
             raf.writeInt(jogos.getId()); // Escreve o id 
             raf.writeUTF(jogos.getIdJogo());
             raf.writeUTF(jogos.getNome()); // Escreve o nome
             raf.writeUTF(jogos.getDataRegistro()); // Escreve data
             raf.writeInt(jogos.getQntGeneros()); // Escreve a quantidade de generos
             for (int i = 0; i < jogos.getQntGeneros(); i++) { // Escreve os generos
                 raf.writeUTF(jogos.getGeneros()[i]); 
             }
             
             raf.writeFloat(jogos.getValor()); // Escreve o valor
 
             raf.seek(0); // Posiciona o ponteiro no inicio do arquivo
             raf.writeInt(ultimoId); // Atualiza o ultimo id
 
             return true;
         } catch (Exception e) {
             System.out.println("Erro de criação!");
             return false;
         }
    }

     // --------------- Read ---------------

     public static Jogo readById(RandomAccessFile raf, int pesquisa) { // Pesquisa um jogo pelo id
        try {
            Jogo jogos = new Jogo();
            int idBusca = 0;

            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo

            while (raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if (raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();

                    idBusca = raf.readInt();

                    if (idBusca == pesquisa) { // Se o id do jogo for igual ao id pesquisado
                        jogos.setId(idBusca);
                        jogos.setIdJogo(raf.readUTF());
                        jogos.setNome(raf.readUTF());
                        jogos.setDataRegistro(raf.readUTF());
                        jogos.setQntGeneros(raf.readInt());
                        String[] generos = new String[jogos.getQntGeneros()];
                        for (int i = 0; i < jogos.getQntGeneros(); i++) {
                            generos[i] = raf.readUTF();
                        }
                        jogos.setGeneros(generos);
                        jogos.setValor(raf.readFloat());

                        return jogos;
                    } else {
                        raf.skipBytes(tam - 4); // 
                    }
                } else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }

            return null;
        } catch (Exception e) {
            System.out.println("Erro ao ler registro!");
            return null;
        }

    }

    public static Jogo readIdJogo(RandomAccessFile raf, String idJogo) { //Busca por um nome e retorna um jogo com os dados correspondentes
        try{ 
            Jogo jogos = new Jogo();
             
             boolean status=false;
             raf.seek(4);
             while(raf.getFilePointer() < raf.length() && !status){
                 if(raf.readByte() == 0){
                     raf.readInt();
                     jogos.setId(raf.readInt());
                     jogos.setIdJogo(raf.readUTF());
                     jogos.setNome(raf.readUTF());
                     jogos.setDataRegistro(raf.readUTF());
                     jogos.setQntGeneros(raf.readInt());
                     String[] generos = new String[jogos.getQntGeneros()];
                     for (int i = 0; i < jogos.getQntGeneros(); i++) {
                         generos[i] = raf.readUTF();
                     }
                     jogos.setGeneros(generos);
                     jogos.setValor(raf.readFloat());
                     if(jogos.getIdJogo().compareTo(idJogo) == 0){
                         status = true;
                     }else{
                         jogos.setId(-1);
                     }
                 }else{
                     raf.skipBytes(raf.readInt());
                 }
             }
             return jogos;
        }catch(Exception e){
             System.out.println("erro :(");
             return null;
        }
     }


    // --------------- Update ---------------

    public static boolean update(RandomAccessFile raf, Jogo jogos) { // Atualiza um jogo
        try {
            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while (raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if (raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();

                    if (raf.readInt() == jogos.getId()) { // Se o id do jogo for igual ao id do jogo a ser atualizada
                        if (tam >= jogos.toByteArray().length) { // Se o tamanho do registro for maior ou igual ao tamanho do registro atualizado encaixar no mesmo registro
                            raf.writeUTF(jogos.getIdJogo());
                            raf.writeUTF(jogos.getNome());
                            raf.writeUTF(jogos.getDataRegistro());
                            raf.writeInt(jogos.getQntGeneros());
                            for (int i = 0; i < jogos.getQntGeneros(); i++) {
                                raf.writeUTF(jogos.getGeneros()[i]);
                            }
                            raf.writeFloat(jogos.getValor());

                            return true;
                        } else { // Se o tamanho do registro for menor que o tamanho do registro atualizado sera necessario criar um novo registro e excluir o antigo
                            raf.seek(raf.getFilePointer() - 9); // Volta o ponteiro para o inicio do registro
                            raf.writeByte(1); // Escreve a lapide 1 (excluido)
                            return create(raf, jogos); // Cria um novo registro
                        }
                    } else {
                        raf.skipBytes(tam - 4); // Pula o resto do registro
                    }
                } else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar registro!");
            return false;
        }
    }

     // --------------- Delete ---------------

     public static boolean delete(RandomAccessFile raf, Jogo jogos) { // Exclui um jogo
        try {
            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while(raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if(raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();
                    int id = raf.readInt();

                    if(id == jogos.getId()) { // Se o id da jogo for igual ao id da jogo a ser excluida
                        raf.seek(raf.getFilePointer() - 9); // Volta o ponteiro para o inicio do registro
                        raf.writeByte(1); // Escreve a lapide 1 (excluido)
                        return true;
                    }else {
                        raf.skipBytes(tam - 4); // Pula o resto do registro
                    }
                }else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }
            return false;
        }catch (Exception e) {
            System.out.println("Erro ao deletar registro!");
            return false;
        }
    }






}