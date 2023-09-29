package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

public class Adadelta extends Otimizador {

   private double rho;
   private double epsilon;
   private double[] acGradQuadrado;
   private double[] acAttQuadrado;

   public Adadelta(double beta2, double epsilon){
      this.rho = beta2;
      this.epsilon = epsilon;
   }

   public Adadelta(){
      this(0.99, 1e-6);
   }

   @Override
   public void inicializar(int parametros){
      acGradQuadrado = new double[parametros];
      acAttQuadrado = new double[parametros];
   }

   @Override
   public void atualizar(Camada[] redec){
      double g;
      Neuronio neuronio;

      int indice = 0;
      for(int i = 0; i < redec.length; i++){

         int nNeuronios = redec[i].quantidadeNeuronios();
         for(int j = 0; j < nNeuronios; j++){

            neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               g = neuronio.gradiente[k];

               acGradQuadrado[indice] = (rho * acGradQuadrado[indice]) + ((1 - rho) * (g*g));

               double delta = Math.sqrt(acAttQuadrado[indice] + epsilon) / Math.sqrt(acGradQuadrado[indice] + epsilon) * g;
               
               acAttQuadrado[indice] = (rho * acAttQuadrado[indice]) + ((1 - rho) * (delta*delta));
               
               neuronio.pesos[k] -= delta;
               indice++;
            }
         }
      }
   }

   @Override
   public String info() {
      String buffer = "";

      String espacamento = "    ";
      buffer += espacamento + "Rho: " + this.rho + "\n";
      buffer += espacamento + "Epsilon: " + this.epsilon + "\n";

      return buffer;
   }
}
