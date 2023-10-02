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
      int id = 0;//indice de busca na lista de coeficientes
      for(int i = 0; i < redec.length; i++){
         for(int j = 0; j < redec[i].quantidadeNeuronios(); j++){

            Neuronio neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               g = neuronio.gradientes[k];

               acGradQuadrado[id] = (rho * acGradQuadrado[id]) + ((1 - rho) * (g*g));

               double delta = Math.sqrt(acAttQuadrado[id] + epsilon) / Math.sqrt(acGradQuadrado[id] + epsilon) * g;
               
               acAttQuadrado[id] = (rho * acAttQuadrado[id]) + ((1 - rho) * (delta*delta));
               
               neuronio.pesos[k] -= delta;
               id++;
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
