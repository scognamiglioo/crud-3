import java.io.RandomAccessFile;

public class CRUD {

    // --------------- Atributos ---------------

    static int ultimoId;

    // --------------- Create ---------------

    public static boolean create(RandomAccessFile raf, Game game) { // Cria uma game

        try {
            raf.seek(raf.length()); // Posiciona o ponteiro no final do arquivo
            raf.writeByte(0); // Escreve a lapide (0 = ativo)
            raf.writeInt(game.toByteArray().length); // Escreve o tamanho do registro
            raf.writeInt(game.getAppId()); // Escreve o id da game
            raf.writeUTF(game.getName()); // Escreve o nome da pessoa
            raf.writeFloat(game.getPrice()); // Escreve o saldo da game

            raf.seek(0); // Posiciona o ponteiro no inicio do arquivo
            raf.writeInt(ultimoId); // Atualiza o ultimo id

            return true;
        } catch (Exception e) {
            System.out.println("-> Erro ao cadastrar novo jogo!");
            return false;
        }
    }

    // --------------- Read ---------------

    /*
     * 
        dos.writeInt(this.getAppId());
        dos.writeUTF(this.getName());
        dos.writeUTF(this.getReleaseDate());
        dos.writeFloat(this.getPrice());
     */

    public static Game readById(RandomAccessFile raf, int pesquisa) { // Pesquisa uma game pelo id
        try {
            Game game = new Game();

            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while (raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if (raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();

                    game.setAppId(raf.readInt()); 

                    if (game.getAppId() == pesquisa) { // Se o id da game for igual ao id pesquisado
                        game.setName(raf.readUTF());
                        game.setReleaseDate(raf.readUTF());
                        game.setPrice(raf.readFloat());

                        return game;
                    } else {
                        raf.skipBytes(tam - 4); // Pula o resto do registro
                    }
                } else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }

            return null;
        } catch (Exception e) {
            System.out.println("-> Erro ao ler registro!");
            return null;
        }
    }

    public static Game readByUser(RandomAccessFile raf, String pesquisa) { // Pesquisa uma Game pelo nome de usuario
        try {
            Game game = null; 
            //String[] emails; 
            boolean achado = false; // Variavel para verificar se o nome de usuario foi encontrado

            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while (raf.getFilePointer() < raf.length() && !achado) { // enquanto não chegar no final do arquivo
                if (raf.readByte() == 0) { // se o registro não estiver excluído
                    game = new Game(); 
                    raf.readInt();

                    game.setAppId(raf.readInt());
                    game.setName(raf.readUTF());
                    game.setReleaseDate(raf.readUTF());
                    game.setPrice(raf.readFloat());


                    if(game.getName().equals(pesquisa)){ // se o nome de usuario for igual ao nome de usuario pesquisado
                        achado = true;
                    }
                } else { // se o registro estiver excluído
                    raf.skipBytes(raf.readInt());
                }
            }

            return game;
        } catch (Exception e) {
            System.out.println("-> Erro ao ler registro!");
            return null;
        }
    }

    // --------------- Update (!) VERIFICAR ---------------

    public static boolean update(RandomAccessFile raf, Game game) { // Atualiza uma game
        try {
            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while (raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if (raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();

                    if (raf.readInt() == game.getAppId()) { // Se o id da game for igual ao id da game a ser atualizada
                        if (tam >= game.toByteArray().length) { // Se o tamanho do registro for maior ou igual ao tamanho do registro atualizado encaixar no mesmo registro

                            raf.writeUTF(game.getName());
                            raf.writeUTF(game.getReleaseDate());
                            raf.writeFloat(game.getPrice());

                            return true;
                        } else { // Se o tamanho do registro for menor que o tamanho do registro atualizado sera necessario criar um novo registro e excluir o antigo
                            raf.seek(raf.getFilePointer() - 9); // Volta o ponteiro para o inicio do registro
                            raf.writeByte(1); // Escreve a lapide 1 (excluido)
                            return create(raf, game); // Cria um novo registro
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
            System.out.println("-> Erro ao atualizar registro!");
            return false;
        }
    }

    // --------------- Delete ---------------

    public static boolean delete(RandomAccessFile raf, Game game) { // Exclui uma game
        try {
            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while(raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if(raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();
                    int id = raf.readInt();

                    if(id == game.getAppId()) { // Se o id da game for igual ao id da game a ser excluida
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
            System.out.println("-> Erro ao deletar registro!");
            return false;
        }
    }
}
