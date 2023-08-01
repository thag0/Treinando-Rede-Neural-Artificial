package utilitarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;


/**
 * Responsável por manuseio de arquivos externos, por enquanto só csv
 */
class GerenciadorArquivos{
   
   public GerenciadorArquivos(){

   }


   public ArrayList<String[]> lerCsv(String caminho){
      ArrayList<String[]> dados = new ArrayList<>();
      String separador = ",";
      String linha = "";

      //diretório não existe
      if(!(new File(caminho).exists())){
         throw new IllegalArgumentException("O caminho especificado não existe ou não foi encontrado.");
      }

      try{
         BufferedReader br = new BufferedReader(new FileReader(caminho));
         while((linha = br.readLine()) != null){
            String linhaDados[] = linha.split(separador);
            dados.add(linhaDados);
         }
         br.close();

      }catch(Exception e){
         e.printStackTrace();
      }
      
      return dados;
   }


   public void exportarCsv(ArrayList<String[]> lista, String caminho){
      String separador = ",";

      try{
         BufferedWriter bw = new BufferedWriter(new FileWriter(caminho + ".csv"));
         
         for(String[] linha : lista){
            for(int i = 0; i < linha.length; i++){

               bw.write(linha[i]);
               if(i < linha.length - 1){
                  bw.write(separador);
               }
            }
            bw.newLine();
         }

         bw.close();

      }catch(Exception e){
         System.out.println("Houve um erro ao exportar o arquivo.");
         e.printStackTrace();
      }
   }
}
