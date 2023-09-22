package rna.serializacao;

import rna.ativacoes.Argmax;
import rna.ativacoes.ELU;
import rna.ativacoes.FuncaoAtivacao;
import rna.ativacoes.GELU;
import rna.ativacoes.LeakyReLU;
import rna.ativacoes.Linear;
import rna.ativacoes.ReLU;
import rna.ativacoes.Seno;
import rna.ativacoes.Sigmoid;
import rna.ativacoes.SoftPlus;
import rna.ativacoes.Softmax;
import rna.ativacoes.Swish;
import rna.ativacoes.TanH;

class DicionarioAtivacoes{

   public DicionarioAtivacoes(){

   }

   public FuncaoAtivacao obterAtivacao(String nome){
      //essa provavelmente não é a melhor abordagem
      //mas é a mais fácil de implementar
      
      nome = nome.toLowerCase();
      switch(nome){
         case "argmax": return new Argmax();
         case "elu": return new ELU();
         case "gelu": return new GELU();
         case "leakyrelu": return new LeakyReLU();
         case "linear": return new Linear();
         case "relu": return new ReLU();
         case "seno": return new Seno();
         case "sigmoid": return new Sigmoid();
         case "softmax": return new Softmax();
         case "softplus": return new SoftPlus();
         case "swish": return new Swish();
         case "tanh": return new TanH();

         default: throw new IllegalArgumentException("Função de ativação não encontada.");
      }
   }
}