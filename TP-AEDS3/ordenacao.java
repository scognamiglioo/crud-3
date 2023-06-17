import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ordenacao {
    public static Jogo readFile(RandomAccessFile raf, int pesquisa) { // Lê o arquivo
        try {
            Jogo jogos = new Jogo();

            raf.seek(0); // Posiciona o ponteiro no inicio do arquivo
            while (raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if (raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();

                    jogos.setId(raf.readInt());

                    if (jogos.getId() == pesquisa) { // Se o id da jogos for igual ao id pesquisado
                        jogos.setIdJogo(raf.readUTF());
                        jogos.setNome(raf.readUTF());
                        jogos.setDataRegistro(raf.readUTF());
                        jogos.setQntGeneros(raf.readInt());
                        
                        String[] generos=new String[jogos.getQntGeneros()];
                        for (int i = 0; i < jogos.getQntGeneros(); i++) {
                            generos[i] = raf.readUTF();
                        }
                        jogos.setGeneros(generos);
                        
                        jogos.setValor(raf.readFloat());

                        return jogos;
                    } else {
                        raf.skipBytes(tam - 4); // Pula o resto do registro
                    }
                } else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }

            return null;
        } catch (Exception e) {
            System.out.println("-> Erro ao ler o arquivo!");
            return null;
        }
    }

    public static void deleteFiles() { // Deleta os arquivos já existentes
        File file1 = new File("arq1.bin");
        File file2 = new File("arq2.bin");
        File file3 = new File("arq3.bin");
        File file4 = new File("arq4.bin");
        File file5 = new File("arqFinal.bin");

        file1.delete();
        file2.delete();
        file3.delete();
        file4.delete();
        file5.delete();
    }

    public static void printFile(RandomAccessFile raf) throws IOException { // Imprime o arquivo
        raf.seek(0);
        while (raf.getFilePointer() < raf.length()) {
            raf.readByte();
            raf.readInt();
            int id = raf.readInt();
            String idJogo= raf.readUTF();
            String nome = raf.readUTF();
            String dataRegistro = raf.readUTF();
            int qtdGeneros = raf.readInt();
            String[] generos = new String[qtdGeneros];
            for (int i = 0; i < qtdGeneros; i++) {
                generos[i] = raf.readUTF();
            }
            float valor = raf.readFloat();

            System.out.println("ID: " + id + " \t|Id jogo: " + idJogo + " \t|Nome: " + nome);
        }
    }

    public static boolean intercalacao(RandomAccessFile raf)throws IOException{
        
        deleteFiles();
        System.out.println("\n-> Distribuindo ...");

        ArrayList<Jogo> jogoAux = new ArrayList<Jogo>();
        Jogo jogos = new Jogo();

        RandomAccessFile arq1 = new RandomAccessFile("arq1.bin", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("arq2.bin", "rw");

        
        raf.seek(4); 
        while(raf.getFilePointer() < raf.length()){
            if (raf.readByte() == 0) {
                raf.readInt();
                jogos = CRUD.readById(raf, raf.readInt());
                jogoAux.add(jogos);
            } else {
                raf.skipBytes(raf.readInt());
            }
        }

        ArrayList<Jogo> jogoAuxTmp = new ArrayList<Jogo>(); 
        int contador = 0; 
        while (jogoAux.size() > 0) { 
            for (int j = 0; j < 5; j++) { 
                if (jogoAux.size() > 0) { 
                    jogoAuxTmp.add(jogoAux.get(0)); 
                    jogoAux.remove(0); 
                }
            }

           jogoAuxTmp.sort((Jogo c1, Jogo c2) -> c1.getId() - c2.getId());

            contador++;

            if (contador % 2 != 0) { 
                for (Jogo c : jogoAuxTmp) {
                    arq1.writeByte(0);
                    arq1.writeInt(c.toByteArray().length);
                    arq1.write(c.toByteArray());
                }
            } else { 
                for (Jogo c : jogoAuxTmp) {
                    arq2.writeByte(0);
                    arq2.writeInt(c.toByteArray().length);
                    arq2.write(c.toByteArray());
                }
            }

            jogoAuxTmp.clear(); 
        }

      

        ArrayList<Jogo> jogoAux1 = new ArrayList<Jogo>();
        ArrayList<Jogo> jogoAux2 = new ArrayList<Jogo>();

        System.out.println("\n-> Intercalação 1 ...");

        arq1.seek(0); 
        while (arq1.getFilePointer() < arq1.length()) { 
            arq1.readByte();
            arq1.readInt();
            jogos = readFile(arq1, arq1.readInt()); 
            jogoAux1.add(jogos); 
        }

        arq2.seek(0); 
        while (arq2.getFilePointer() < arq2.length()) { 
            arq2.readByte();
            arq2.readInt();
            jogos = readFile(arq2, arq2.readInt()); 
            jogoAux2.add(jogos);
        }

        RandomAccessFile arq3 = new RandomAccessFile("arq3.bin", "rw");
        RandomAccessFile arq4 = new RandomAccessFile("arq4.bin", "rw");

        contador = 0; 
        jogoAuxTmp.clear(); 
        int m = 5;

        while (jogoAux1.size() > 0 || jogoAux2.size() > 0) { 
                                                           
            for (int i = 0; i < m; i++) {
                if (jogoAux1.size() > 0) { 
                                          
                    jogoAuxTmp.add(jogoAux1.get(0));
                    jogoAux1.remove(0);
                }
                if (jogoAux2.size() > 0) { 
                                          
                    jogoAuxTmp.add(jogoAux2.get(0));
                    jogoAux2.remove(0);
                }
            }

            jogoAuxTmp.sort((Jogo c1, Jogo c2) -> c1.getId() - c2.getId()); 

            contador++;

            if (contador % 2 != 0) { 
                for (Jogo c : jogoAuxTmp) {
                    arq3.writeByte(0);
                    arq3.writeInt(c.toByteArray().length);
                    arq3.write(c.toByteArray());
                }
            } else { 
                for (Jogo c : jogoAuxTmp) {
                    arq4.writeByte(0);
                    arq4.writeInt(c.toByteArray().length);
                    arq4.write(c.toByteArray());
                }
            }

            jogoAuxTmp.clear(); 
        }


        int qdt = 2; 
        while (arq2.length() > 0) { 

            System.out.println("\n-> Intercalação " + qdt + " ..."); 
            arq3.seek(0);
            while (arq3.getFilePointer() < arq3.length()) { 
                                                            
                arq3.readByte();
                arq3.readInt();
                jogos = readFile(arq3, arq3.readInt());
                jogoAux1.add(jogos);
            }

            arq4.seek(0);
            while (arq4.getFilePointer() < arq4.length()) { 
                                                            
                arq4.readByte();
                arq4.readInt();
                jogos = readFile(arq4, arq4.readInt());
                jogoAux2.add(jogos);
            }

            arq1.setLength(0);
            arq2.setLength(0); 

            contador = 0;
            jogoAuxTmp.clear();
            m *= 2; 
            while (jogoAux1.size() > 0 || jogoAux2.size() > 0) { 
                                                               
                for (int i = 0; i < m; i++) { 
                    if (jogoAux1.size() > 0) { 
                                              
                        jogoAuxTmp.add(jogoAux1.get(0));
                        jogoAux1.remove(0);
                    }
                    if (jogoAux2.size() > 0) { 
                                              
                        jogoAuxTmp.add(jogoAux2.get(0));
                        jogoAux2.remove(0);
                    }
                }

                jogoAuxTmp.sort((Jogo c1, Jogo c2) -> c1.getId() - c2.getId()); 

                contador++;

                if (contador % 2 != 0) { 
                    for (Jogo c : jogoAuxTmp) {
                        arq1.writeByte(0);
                        arq1.writeInt(c.toByteArray().length);
                        arq1.write(c.toByteArray());
                    }
                } else { 
                    for (Jogo c : jogoAuxTmp) {
                        arq2.writeByte(0);
                        arq2.writeInt(c.toByteArray().length);
                        arq2.write(c.toByteArray());
                    }
                }

                jogoAuxTmp.clear(); 
            }


            qdt++;
        }

        // ------------------------------------------------------------------- //
        
        RandomAccessFile arqFinal = new RandomAccessFile("arqFinal.bin", "rw");
        arqFinal.seek(0); 
        arqFinal.writeInt(CRUD.getUltimoId()); 

        arqFinal.seek(4); 
        arq1.seek(0); 
        while (arq1.getFilePointer() < arq1.length()) { 
            arq1.readByte(); 
            arq1.readInt(); 
            jogos = readFile(arq1, arq1.readInt()); 
            arqFinal.writeByte(0); 
            arqFinal.writeInt(jogos.toByteArray().length); 
            arqFinal.write(jogos.toByteArray()); 
        }

        // Fecha os arquivos
        arq1.close(); 
        arq2.close();
        arq3.close();
        arq4.close();
        arqFinal.close();

        return true;
    }

    //metodo de ordenação externa intercalação balanceada com blocos de tamanho variável
    public static boolean ordenacaoIntercalacaoBalanceada(RandomAccessFile raf) throws IOException {
        ArrayList<Jogo> jogoAux = new ArrayList<Jogo>();
        ArrayList<Jogo> jogoAuxTmp = new ArrayList<Jogo>();
      
        Jogo jogos = new Jogo();

        RandomAccessFile arqV1 = new RandomAccessFile("arqV1.bin", "rw");
        RandomAccessFile arqV2 = new RandomAccessFile("arqV2.bin", "rw");

        int contador = 0; 
        int m = 5; 

        while (jogoAux.size() > 0) { 
            for (int i = 0; i < m; i++) { 
                if (jogoAux.size() > 0) { 
                    jogoAuxTmp.add(jogoAux.get(0));
                    jogoAux.remove(0);
                }
            }

            jogoAuxTmp.sort((Jogo c1, Jogo c2) -> c1.getId() - c2.getId()); 

            contador++;

            if (contador % 2 != 0) { 
                for (Jogo c : jogoAuxTmp) {
                    arqV1.writeByte(0);
                    arqV1.writeInt(c.toByteArray().length);
                    arqV1.write(c.toByteArray());
                }
            } else { 
                for (Jogo c : jogoAuxTmp) {
                    arqV2.writeByte(0);
                    arqV2.writeInt(c.toByteArray().length);
                    arqV2.write(c.toByteArray());
                }
            }

            jogoAuxTmp.clear(); 
        }

        System.out.println("\n-> Intercalação 2 ...");

        ArrayList<Jogo> jogoAux3 = new ArrayList<Jogo>();
        ArrayList<Jogo> jogoAux4 = new ArrayList<Jogo>();
        arqV1.seek(0); 
        while (arqV1.getFilePointer() < arqV1.length()) { 
            arqV1.readByte();
            arqV1.readInt();
            jogos = readFile(arqV1, arqV1.readInt()); 
            jogoAux3.add(jogos); 
        }

        arqV2.seek(0); 
        while (arqV2.getFilePointer() < arqV2.length()) { 
            arqV2.readByte();
            arqV2.readInt();
            jogos = readFile(arqV2, arqV2.readInt()); 
            jogoAux4.add(jogos);
        }

        RandomAccessFile arqV3 = new RandomAccessFile("arqV3.bin", "rw");

        RandomAccessFile arqFinal = new RandomAccessFile("arqFinalV.bin", "rw");
        arqFinal.seek(0); 
        arqFinal.writeInt(CRUD.getUltimoId()); 

        arqFinal.seek(4); 
        arqV1.seek(0); 
        while (arqV1.getFilePointer() < arqV1.length()) { 
            arqV1.readByte(); 
            arqV1.readInt(); 
            jogos = readFile(arqV1, arqV1.readInt()); 
            arqFinal.writeByte(0); 
            arqFinal.writeInt(jogos.toByteArray().length); 
            arqFinal.write(jogos.toByteArray()); 
        }

        // Fecha os arquivos
        arqV1.close(); 
        arqV2.close();
        arqV3.close();
        arqFinal.close();

        return true;
}
}