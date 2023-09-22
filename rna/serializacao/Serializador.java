package rna.serializacao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;

/**
 * Classe responsável por tratar da serialização/desserialização de objetos
 * da {@Rede Neural}.
 * <p>
 *    Manipula os arquivos {@code .txt} baseados na rede para escrita e leitura, 
 *    possibilitando mais portabilidade de Redes Neurais treinadas.
 * </p>
 * Os pesos salvos são do tipo double (8 bytes), caso seja necessário mais economia
 * de memória pode ser recomendável converter os arquivos escritos para o tipo float 
 * (4 bytes).
 */
public class Serializador{

   /**
    * Salva as informações mais essenciais sobre a Rede Neural incluindo arquitetura,
    * funções de ativação de todas as camadas, bias configurado e o mais importante que
    * são os pesos de cada neurônio da rede.
    * <p>
    *    <strong> Reforçando</strong>: as informações sobre o otimizador e todas suas 
    *    configurações, treino, nome e outras pequenas coisas que não afetam diretamente 
    *    o funcionamento da rede serão perdidas.
    * </p>
    * <p>
    *    O arquivo deve ser salvo no formato {@code .txt}
    * </p>
    * @param rede instância de uma Rede Neural.
    * @param caminho caminho onde o arquivo da rede será salvo.
    */
   public static void salvar(RedeNeural rede, String caminho){
      try(BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))){

         //arquitetura da rede
         int[] arq = rede.obterArquitetura();
         for(int i = 0; i < arq.length; i++){
            writer.write(arq[i] + " ");
         }
         writer.newLine();

         //bias
         writer.write(Boolean.toString(rede.temBias()));
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

         //pesos das camadas ocultas
         for(int i = 0; i < numOcultas; i++){

            Camada camada = rede.obterCamadaOculta(i);
            for(int j = 0; j < camada.quantidadeNeuroniosSemBias(); j++){

               Neuronio neuronio = camada.neuronio(j);
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

   /**
    * Lê o arquivo de uma {@code Rede Neural} serializada e converter numa
    * instância pré configurada.
    * <p>
    *    Configurações mantidas: 
    * </p> 
    * <ul>
    *    <li>
    *       Pesos de todos os neurônios da rede.
    *    </li>
    *    <li>
    *       Arquitetura.
    *    </li>
    *    <li>
    *       Funções de ativação de todas as camadas.
    *    </li>
    * </ul>
    * <strong>Demais configurações não são recuperadas</strong>.
    * @param caminho caminho onde está salvo o arquivo {@code .txt} da Rede Neural.
    * @return Instância de Rede Neural baseada nas configurações lidas pelo arquivo.
    */
   public static RedeNeural ler(String caminho){
      RedeNeural rede = null;
      DicionarioAtivacoes dicionario = new DicionarioAtivacoes();

      try(BufferedReader reader = new BufferedReader(new FileReader(caminho))){
         //arquitetura
         String[] arqStr = reader.readLine().split(" ");
         int[] arq = new int[arqStr.length];
         for(int i = 0; i < arqStr.length; i++){
            arq[i] = Integer.parseInt(arqStr[i]);
         }

         //bias
         boolean bias = Boolean.parseBoolean(reader.readLine());
         for(int i = 0; i < arq.length-1; i++){//desconsiderar saída
            arq[i] -= (bias) ? 1 : 0;
         }

         //funções de ativação
         String[] ativacoesStr = reader.readLine().split(" ");
         String[] ativacaoOcultas = new String[ativacoesStr.length-1];
         System.arraycopy(ativacoesStr, 0, ativacaoOcultas, 0, ativacoesStr.length - 1);
         String ativacaoSaida = ativacoesStr[ativacaoOcultas.length-1];

         //inicialização e configurações da rede
         rede = new RedeNeural(arq);
         rede.compilar();
         rede.configurarBias(bias);

         for(int i = 0; i < rede.obterQuantidadeOcultas(); i++){
            rede.configurarFuncaoAtivacao(rede.obterCamadaOculta(i), dicionario.obterAtivacao(ativacaoOcultas[i]));
         }
         rede.configurarFuncaoAtivacao(rede.obterCamadaSaida(), dicionario.obterAtivacao(ativacaoSaida));

         //preencher pesos lidos
         //camada de entrada
         for(int i = 0; i < rede.obterQuantidadeOcultas(); i++){

            Camada camada = rede.obterCamadaOculta(i);
            for(int j = 0; j < camada.quantidadeNeuroniosSemBias(); j++){
               
               Neuronio neuronio = camada.neuronio(j);
               for(int k = 0; k < neuronio.numConexoes(); k++){
                  neuronio.pesos[k] = Double.parseDouble(reader.readLine());
               }
            }
         }

         //camada de saída
         for(int i = 0; i < rede.obterCamadaSaida().quantidadeNeuronios(); i++){
            
            Neuronio neuronio = rede.obterCamadaSaida().neuronio(i);
            for(int j = 0; j < neuronio.numConexoes(); j++){
               neuronio.pesos[j] = Double.parseDouble(reader.readLine());
            }
         }

      }catch(Exception e){
         System.out.println("Houve um erro ao ler o arquivo de Rede Neural.");
         e.printStackTrace();
         System.exit(0);
      }

      return rede;
   }
}
