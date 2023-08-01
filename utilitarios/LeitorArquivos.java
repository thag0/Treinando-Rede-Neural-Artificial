package utilitarios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

class LeitorArquivos{
   
   public LeitorArquivos(){

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
}
