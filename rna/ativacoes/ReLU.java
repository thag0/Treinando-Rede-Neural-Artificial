package rna.ativacoes;

public class ReLU extends FuncaoAtivacao{
   
   @Override
   public double ativar(double x){
      if(x > 0) return x;
      return 0;
   }


   @Override
   public double derivada(double x){
      if(x > 0) return 1;
      return 0;
   }
}
