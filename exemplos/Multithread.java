package exemplos;

import java.util.concurrent.TimeUnit;

import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class Multithread {
   public static void main(String[] args){
      Ged ged = new Ged();

      int lin = 1024;
      int col = 1024;
      double[][] a = new double[lin][col];
      double[][] b = new double[lin][col];
      double[][] r = new double[lin][col];

      for(int i = 0; i < lin; i++){
         for(int j = 0; j < col; j++){
            a[i][j] = (j+1) * (i+1);
            b[i][j] = (i == j) ? 1 : 0;
         }
      }

      System.out.println("Multiplicando.");
   
      long t1 = System.nanoTime();
      r = ged.multiplicarMatrizes(a, b);
      long t2 = System.nanoTime();

      long t3 = TimeUnit.NANOSECONDS.toSeconds(t2 - t1);
      long segundos = t3 % 60;
      System.out.println("Terminado em " + segundos + "s");

      System.out.println(new Dados(r).shapeInfo());
   }



   static void mult(double[][] a, double[][] b, double[][] r){
      int iner = a[0].length;
      for(int i = 0; i < r.length; i++){
         for(int j = 0; j < r[i].length; j++){

            r[i][j] = 0;
            for(int k = 0; k < iner; k++){
               r[i][j] += a[i][k] * b[k][j];
            }
         }
      }
   }



   static void mult(double[][] a, double[][] b, double[][] r, int nThreads){
      int linA = a.length;
      int colA = a[0].length;
      int colB = b[0].length;
      int linPorThread = linA / nThreads;
      Thread[] threads = new Thread[nThreads];

      for(int t = 0; t < nThreads; t++){
         final int id = t;

         threads[t] = new Thread(() -> {
            int inicio = id * linPorThread;
            int fim = (id == nThreads - 1) ? linA : (id + 1) * linPorThread;

            for(int i = inicio; i < fim; i++){
               for(int j = 0; j < colB; j++){
                  r[i][j] = 0;
                  for(int k = 0; k < colA; k++){
                     r[i][j] += a[i][k] * b[k][j];
                  }
               }
            }
         });
         
         threads[t].start();
      }

      try{
         for(Thread thread : threads){
            thread.join();
         }
      }catch(InterruptedException e){
         e.printStackTrace();
      }
   }
}
