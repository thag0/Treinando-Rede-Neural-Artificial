package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Nao conhecia, ainda to pesquisando melhor
 */
public class Lion extends Otimizador{
   
   private double taxaAprendizagem;
   
   private double beta1;
   
   private double beta2;

   private double[] momentum;

   public Lion(double tA, double beta1, double beta2){
      this.taxaAprendizagem = tA;
      this.beta1 = beta1;
      this.beta2 = beta2;
   }

   public Lion(){
      this(0.0001, 0.9, 0.99);
   }

   @Override
   public void inicializar(int parametros){
      this.momentum = new double[parametros];
   }

   @Override
   public void atualizar(Camada[] redec){
      double g;
		int id = 0;//indice de busca na lista de coeficientes
      for(int i = 0; i < redec.length; i++){
         for(int j = 0; j < redec[i].quantidadeNeuronios(); j++){   
            
            Neuronio neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               g = neuronio.gradientes[k];
               
               neuronio.pesos[k] -= taxaAprendizagem * Math.signum((momentum[id] * beta1) + (g * (1 - beta1)));
               momentum[id] = (momentum[id] * beta2) + (g * (1 - beta2));

               id++;
            }
         }
      }
   }

   @Override
   public String info(){
      String espacamento = "    ";
      String buffer = "";

      buffer += espacamento + "TaxaAprendizagem: " + this.taxaAprendizagem + "\n";
      buffer += espacamento + "Beta1: " + this.beta1 + "\n";
      buffer += espacamento + "Beta2: " + this.beta2 + "\n";

      return buffer;
   }
}
