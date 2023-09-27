package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Tangente Hiperbólica 
 * para uso dentro da {@code Rede Neural}.
 */
public class TanH extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Tangente Hiperbólica.
    */
   public TanH(){

   }

   @Override
   public void ativar(Neuronio[] neuronios){
      double x, exp, expn;
      for(int i = 0; i < neuronios.length; i++){
         x = neuronios[i].somatorio;
         exp = Math.exp(x); 
         expn = Math.exp(-x); 
         neuronios[i].saida = (exp - expn)/(exp + expn);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      double tanh;
      for(int i = 0; i < neuronios.length; i++){
         tanh = neuronios[i].saida;
         neuronios[i].derivada = 1 - (tanh * tanh);
      }
   }
}
