package rna.serializacao;

import rna.ativacoes.FuncaoAtivacao;
import rna.ativacoes.ReLU;
import rna.ativacoes.Sigmoid;

class DicionarioAtivacoes{
   public DicionarioAtivacoes(){

   }

   public FuncaoAtivacao obterAtivacao(String nome){
      switch(nome){
         case "ReLU":
            return new ReLU();

         case "Sigmoid":
            return new Sigmoid();

         default: throw new IllegalArgumentException("Função de ativação não encontada.");
      }
   }
}