package utilitarios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class LeitorCsv{

   public LeitorCsv(){

   }

   /**
    * Lê o arquivo .csv de acordo com o caminho especificado.
    * @param caminho caminha absoluto ou relativo do arquivo
    * @return arquivo lido
    * @throws IllegalArgumentException caso não encontre o diretório fornecido
    */
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


   /**
    * Exibe os elementos contidos na lista 
    * @param lista lista com os dados
    */
   public void imprimirCsv(ArrayList<String[]> lista){
      String separador = ",";
      for(String linha[] : lista){
         for(int i = 0; i < linha.length; i++){
            if(i == 0) System.out.print(linha[i]);
            else System.out.print(separador + " " + linha[i]);
         }
         System.out.println();
      }
   }

}
