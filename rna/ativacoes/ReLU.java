package rna.ativacoes;

public class ReLU extends FuncaoAtivacao{
   
   @Override
   public double ativar(double x){
      return (x > 0) ? x : 0;
   }


   @Override
   public double derivada(double x){
      return (x > 0) ? 1 : 0;
   }
}
