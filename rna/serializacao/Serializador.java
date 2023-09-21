package rna.serializacao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;

public class Serializador{

   public Serializador(){

   }

   public void salvar(RedeNeural rede, String caminho){
      try(BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))){

         //arquitetura da rede
         int[] arq = rede.obterArquitetura();
         for(int i = 0; i < arq.length; i++){
            writer.write(arq[i] + " ");
         }
         writer.newLine();

         //funções de ativação
         int numOcultas = rede.obterQuantidadeOcultas();
         writer.write(rede.obterCamadaOculta(0).obterAtivacao().getClass().getSimpleName());
         writer.write(" ");
         for(int i = 1; i < numOcultas; i++){
            writer.write(rede.obterCamadaOculta(i).obterAtivacao().getClass().getSimpleName() + " ");
         }
         writer.write(rede.obterCamadaSaida().obterAtivacao().getClass().getSimpleName());
         writer.newLine();

         //bias
         writer.write(Boolean.toString(rede.temBias()));
         writer.newLine();

         //pesos das camadas ocultas
         for(int i = 0; i < numOcultas; i++){

            Camada camada = rede.obterCamadaOculta(i);
            for(int j = 0; j < camada.quantidadeNeuronios(); j++){

               Neuronio neuronio = camada.neuronio(i);
               for(int k = 0; k < neuronio.numConexoes(); k++){
                  double peso = neuronio.pesos[k];
                  writer.write(Double.toString((float)peso));
                  writer.newLine();
               }
            }
         }

         //pesos da saída
         for(int i = 0; i < rede.obterCamadaSaida().quantidadeNeuronios(); i++){
            Neuronio neuronio = rede.obterCamadaSaida().neuronio(i);
            for(int k = 0; k < neuronio.numConexoes(); k++){
               double peso = neuronio.pesos[k];
               writer.write(Double.toString((float)peso));
               writer.newLine();
            }
         }

      }catch(Exception e){
         e.printStackTrace();
      }
   }

   public RedeNeural ler(String caminho){
      RedeNeural rede = null;
      DicionarioAtivacoes dicionario = new DicionarioAtivacoes();

      try(BufferedReader reader = new BufferedReader(new FileReader(caminho))){
         //arquitetura
         String[] arqStr = reader.readLine().split(" ");
         int[] arq = new int[arqStr.length];
         for(int i = 0; i < arqStr.length; i++){
            arq[i] = Integer.parseInt(arqStr[i]);
         }

         //funções de ativação
         String[] ativacoesStr = reader.readLine().split(" ");
         String[] ativacaoOcultas = new String[ativacoesStr.length-1];
         System.arraycopy(ativacoesStr, 0, ativacaoOcultas, 0, ativacoesStr.length - 1);
         String ativacaoSaida = ativacoesStr[ativacaoOcultas.length-1];

         //bias
         boolean bias = Boolean.parseBoolean(reader.readLine());
         for(int i = 0; i < arq.length-1; i++){//desconsiderar saída
            arq[i] -= (bias) ? 1 : 0;
         }

         System.out.println("b = " + bias);
         System.out.println(arq[0] + " " + arq[1] + " " + arq[2] + " " + arq[3]);
         System.out.println(ativacoesStr[0] + " " + ativacoesStr[1] + " " + ativacoesStr[2]);

         //inicialização e configurações da rede
         //TODO finalizar configuração completa
         rede = new RedeNeural(arq);
         rede.compilar();
         rede.configurarBias(bias);

         for(int i = 0; i < rede.obterQuantidadeOcultas(); i++){
            rede.configurarFuncaoAtivacao(rede.obterCamadaOculta(i), dicionario.obterAtivacao(ativacaoOcultas[i]));
         }
         rede.configurarFuncaoAtivacao(rede.obterCamadaSaida(), dicionario.obterAtivacao(ativacaoSaida));

      }catch(Exception e){
         e.printStackTrace();
         System.exit(0);
      }

      return rede;
   }
}
