package rna;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


/**
 * Modelo de Rede Neural Multilayer Perceptron baseado em feedforward criado do zero. Possui um conjunto de camadas 
 * e cada camada possui um conjunto de neurônios artificiais.
 * <p>
 *    O modelo pode ser usado para problemas de regressão e classificação, contando com algoritmos de treino 
 *    atualmente baseados no backpropagation com adição da ideia de momentum na atualização dos pesos.
 * </p>
 * Possui opções de configuração tanto para hiperparâmetros como taxa de aprendizagem e momentun, quanto para
 * funções de ativações de camadas individuais e valor de alcance máximo e mínimo na aleatorização dos pesos iniciais. 
 * <p>
 * Após configurar as propriedades da rede, o modelo precisará ser compilado para efetivamente poder ser utilizado.
 * </p>
 * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, Campus Tucuruí.
 */
public class RedeNeural implements Cloneable, Serializable{
   public Camada entrada;
   public Camada[] ocultas;
   public Camada saida;

   private int[] arquitetura;
   
   private int neuroniosEntrada;
   private int neuroniosOcultas;
   private int neuroniosSaida;
   private int quantidadeOcultas;

   private double TAXA_APRENDIZAGEM = 0.01;
   private double TAXA_MOMENTUM = 0;
   private int BIAS = 1;
   private double alcancePeso = 1.0;
   private boolean modeloCompilado = false;


   /**
    * <p>
    *    Cria uma instância de rede neural artificial. A arquitetura da rede será baseada de acordo com cada posição do array,
    *    cada valor contido nele representará a quantidade de neurônios da camada correspondente.
    * </p> 
    * <p>
    *    A camada de entrada deverá ser especificada pelo indice 0, a camada de saída 
    *    será representada pelo último valor do array e as camadas ocultas serão representadas pelos valores intermediários.
    * </p>
    * Os valores de todos os parâmetros pedidos <strong>NÃO devem</strong> ser menores que 1.
    * <p>
    *    Após instanciar o modelo, é necessário compilar por meio da função "compilar()", certifique-se 
    *    de configurar as propriedades da rede por meio das funções de configuração fornecidas como, alcance
    *    dos pesos iniciais, taxa de aprendizagem e uso de bias. Caso não seja usada nenhuma das funções 
    *    de configuração, a rede será compilada com os valores padrão.
    * </p>
    * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, Campus Tucuruí.
    * @param arquitetura modelo de arquitetura específico da rede.
    * @throws IllegalArgumentException se o array de arquitetura não possuir, pelo menos, três elementos.
    * @throws IllegalArgumentException se os valores fornecidos forem menores que um.
    */
   public RedeNeural(int[] arquitetura){
      if(arquitetura.length < 3) throw new IllegalArgumentException("A arquitetura da rede não pode conter menos de três elementos");
      
      for(int i = 0; i < arquitetura.length; i++){
         if(arquitetura[i] < 1) throw new IllegalArgumentException("Os valores fornecidos devem ser maiores ou iguais a um.");
      }

      this.neuroniosEntrada = arquitetura[0];

      this.quantidadeOcultas = 0;//evitar problemas
      for(int i = 1; i < arquitetura.length-1; i++) this.quantidadeOcultas += 1;

      this.arquitetura = new int[1 + quantidadeOcultas + 1];
      this.arquitetura[0] = this.neuroniosEntrada;
      
      for(int i = 0; i < this.quantidadeOcultas; i++) this.arquitetura[i+1] = arquitetura[i+1];
      
      this.neuroniosSaida = arquitetura[arquitetura.length-1];
      this.arquitetura[this.arquitetura.length-1] = this.neuroniosSaida;
   }


   /**
    * Define o valor máximo e mínimo na hora de aleatorizar os pesos da rede 
    * para a compilação, os novos valores não podem ser menores ou iguais a zero.
    * <p>
    *    O valor padrão de alcance é 1.
    * </p>
    * @param alcancePesos novo valor máximo e mínimo.
    * @throws IllegalArgumentException se o novo valor for menor ou igual a zero.
    */
   public void configurarAlcancePesos(double alcancePesos){
      if(alcancePesos <= 0){
         throw new IllegalArgumentException("Os novos valores de alcance dos pesos não podem ser menores ou iguais a zero.");
      }
      this.alcancePeso = alcancePesos;
   }


   /**
    * Define se a rede neural usará um neurônio adicional como bias nas camadas da rede.
    * O bias não é adicionado na camada de saída.
    * <p>O valor padrão para uso do bias é true.</p>
    * @param usarBias novo valor para o uso do bias.
    */
   public void configurarBias(boolean usarBias){
      if(usarBias) this.BIAS = 1;
      else this.BIAS = 0;
   }


   /**
    * Define o novo valor de taxa de aprendizagem da rede. O valor é usado durante o método de treino.
    * O valor da taxa de aprendizagem difine "o quanto a rede vai aprender com o erro durante o treinamento".
    * Certifique-se de não usar valores muito altos ou muito baixos para não gerar resultados inesperados 
    * durante o treino.
    * <p>
    *    O valor padrão é 0.1.
    * </p>
    * @param taxaAprendizagem novo valor de taxa de aprendizagem.
    * @throws IllegalArgumentException caso o novo valor de taxa de aprendizagem seja igual a zero.
    */
   public void configurarTaxaAprendizagem(double taxaAprendizagem){
      if(taxaAprendizagem <= 0){
         throw new IllegalArgumentException("O valor da nova taxa de aprendizagem não pode ser menor ou igual a zero.");
      }
      this.TAXA_APRENDIZAGEM = taxaAprendizagem;
   }


   /**
    * Define o novo valor do momentum que vai ser usado no treinamento da rede.
    * <p>
    *    O momentum é uma técnica utilizada no treinamento de redes neurais para acelerar a convergência e
    *    melhorar a estabilidade das atualizações dos pesos. Ele introduz um termo de momento nas atualizações
    *    dos pesos, permitindo que as atualizações tenham inércia, acumulando uma fração dos gradientes das
    *    iterações anteriores.
    * </p>
    * Normalmente esse valor fica entre 0 e 1, onde 0 significa que o momentum não terá efeito  e 1 
    * significa que o momentum terá o máximo de inércia, acumulando totalmente os gradientes anteriores. 
    * <p>
    *    O valor padrão é 0, onde o momentum não é aplicado no treino.
    * </p>
    * @param momentum novo valor de momentum.
    * @throws IllegalArgumentException se o valor de momentum for menor que zero.
    */
   public void configurarMomentum(double momentum){
      if(momentum < 0){
         throw new IllegalArgumentException("O valor de momentum não pode ser menor que zero.");
      }
      this.TAXA_MOMENTUM = momentum;
   }


   /**
    * Configura a função de ativação da camada correspondente, a função de ativação padrão é a ReLU. É preciso
    * compilar o modelo previamente para poder configurar suas funções de ativação.
    * <p>segue a lista das funções disponíveis:</p>
    * <ul>
    *    <li>1 - ReLU.</li>
    *    <li>2 - Sigmoid.</li>
    *    <li>3 - Tangente Hiperbólica.</li>
    *    <li>4 - Leaky ReLU.</li>
    *    <li>5 - ELU.</li>
    *    <li>6 - Swish.</li>
    *    <li>7 - GELU.</li>
    *    <li>8 - Linear.</li>
    *    <li>9 - Seno.</li>
    *    <li>10 - Argmax.</li>
    *    <li>11 - Softmax.</li>
    * </ul>
    * @param camada camada que será configurada.
    * @param funcaoAtivacao valor relativo a lista de ativações disponíveis.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public void configurarFuncaoAtivacao(Camada camada, int funcaoAtivacao){
      modeloValido();
      if(camada.equals(this.entrada)){
         throw new IllegalArgumentException("Não é possível configurar função de ativação para a camada de entrada.");
      } 
      
      camada.configurarAtivacao(funcaoAtivacao);
   }


   /**
    * Configura a função de ativação de todas as camadas da rede, a função de ativação padrão é a ReLU. É preciso
    * compilar o modelo previamente para poder configurar suas funções de ativação
    * <p>segue a lista das funções disponíveis:</p>
    * <ul>
    *    <li>1 - ReLU.</li>
    *    <li>2 - Sigmoid.</li>
    *    <li>3 - Tangente Hiperbólica.</li>
    *    <li>4 - Leaky ReLU.</li>
    *    <li>5 - ELU.</li>
    *    <li>6 - Swish.</li>
    *    <li>7 - GELU.</li>
    *    <li>8 - Linear.</li>
    *    <li>9 - Seno.</li>
    *    <li>10 - Argmax.</li>
    *    <li>11 - Softmax.</li>
    * </ul>
    * @param funcaoAtivacao valor relativo a lista de ativações disponíveis.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public void configurarFuncaoAtivacao(int funcaoAtivacao){
      modeloValido();
      
      for(Camada camada : this.ocultas) camada.configurarAtivacao(funcaoAtivacao);
      this.saida.configurarAtivacao(funcaoAtivacao);
   }


   /**
    * Compila o modelo de rede baseado nos valores fornecidos. Antes da compilação é possível
    * informar alguns valores ajustáveis na inicialização da rede, como:
    * <ul>
    *    <li>Valor máximo e mínimo para os pesos gerados aleatoriamente.</li>
    *    <li>Funções de ativação para as camadas ocultas e para a camada de saída.</li>
    *    <li>Neurônios adicionais nas camadas atuando como bias.</li>
    *    <li>Taxa de aprendizagem.</li>
    *    <li>Taxa de momentum.</li>
    * </ul>
    * <p>
    *    Caso nenhuma configuração seja feita, a rede será inicializada com os valores padrão. 
    * </p>
    * Após a compilação o modelo está pronto para ser usado.
    */
   public void compilar(){
      boolean temBias = (this.BIAS == 1) ? true : false;

      //adicionando bias como neuronio adicional nas camadas
      for(int i = 0; i < arquitetura.length-1; i++){
         arquitetura[i] += BIAS;
      }
      
      //inicializar camada de entrada
      entrada = new Camada(temBias);
      entrada.neuronios = new Neuronio[arquitetura[0]];
      for(int i = 0; i < entrada.neuronios.length; i++){
         //pesos e entradas nao importam na camada de entrada
         entrada.neuronios[i] = new Neuronio(1, alcancePeso);
      }

      //inicializar camadas ocultas
      ocultas = new Camada[quantidadeOcultas];
      for(int i = 0; i < this.ocultas.length; i++){// percorrer ocultas
         Camada novaOculta = new Camada(temBias);
         novaOculta.neuronios = new Neuronio[arquitetura[i+1]];
      
         // inicializar neuronios da camada
         for (int j = 0; j < novaOculta.neuronios.length; j++){
            if (i == 0) novaOculta.neuronios[j] = new Neuronio(arquitetura[0], alcancePeso);// neuronios da entrada
            else novaOculta.neuronios[j] = new Neuronio(arquitetura[i], alcancePeso);// neuronios da oculta anterior
         }
         ocultas[i] = novaOculta;
      }

      //inicializar camada de saída
      saida = new Camada(false);
      saida.neuronios = new Neuronio[arquitetura[arquitetura.length-1]];
      for(int i = 0; i < this.saida.neuronios.length; i++){
         // neuronios da ultima oculta
         saida.neuronios[i] = new Neuronio(arquitetura[arquitetura.length-2], alcancePeso);
      }

      modeloCompilado = true;//modelo pode ser usado
   }


   /**
    * Verifica se o modelo já foi compilado para evitar problemas de uso indevido, bem como componentes nulos.
    * @throws IllegalArgumentException se o modelo não foi compilado.
    */
   public void modeloValido(){
      if(!this.modeloCompilado){
         throw new IllegalArgumentException("O modelo ainda não foi compilado");
      }
   }


   /**
    * Propaga os dados de entrada pela rede neural pelo método de feedforward.
    * @param dados dados usados para a camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos neurônios de entrada, excluindo o bias.
    */
   public void calcularSaida(double[] dados){
      modeloValido();
      
      if(dados.length != (this.entrada.neuronios.length-BIAS)){
         throw new IllegalArgumentException("As dimensões dos dados de entrada com os neurônios de entrada da rede não são iguais");
      }

      //carregar dados na camada de entrada
      for(int i = 0; i < (this.entrada.neuronios.length-BIAS); i++){
         this.entrada.neuronios[i].saida = dados[i];
      }
      
      //ocultas
      for(int i = 0; i < this.ocultas.length; i++){
         if(i == 0) this.ocultas[i].ativarNeuronios(this.entrada);
         else this.ocultas[i].ativarNeuronios(this.ocultas[i-1]);
      }

      //saída
      this.saida.ativarNeuronios(this.ocultas[this.ocultas.length-1]);
   }

   
   /**
    * Calcula a precisão da rede neural com base nos dados fornecidos.
    * A precisão é calculada como a média do erro absoluto entre a saída prevista pela rede e a saída fornecida.
    * Esse método pode ser adequado para tarefas de regressão, mas não é uma boa abordagem em problemas de classificação
    * ou quando as saídas são valores discretos ou categóricos.
    * @param dados matriz com os dados de entrada.
    * @param saida matriz com os dados de saída.
    * @return precisão obtida com base nos dados fornecidos, um valor entre 0 e 1, onde 1 representa a máxima precisão.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se a quantidade de linhas dos dados fornecidos for diferente da quantidade de linhas das saídas fornecidas.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos neurônios de entrada, excluindo o bias.
    * @throws IllegalArgumentException se o tamanho dos dados de saída for diferente do tamanho dos neurônios de saída.
    */
   public double calcularPrecisao(double[][] dados, double[][] saida){
      modeloValido();

      if(dados.length != saida.length){
         throw new IllegalArgumentException("A quantidade de linhas de dados e saídas são diferentes");
      }
      if(dados[0].length != (this.entrada.neuronios.length - BIAS)){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de entrada e os neurônios de entrada da rede");
      }
      if(saida[0].length != this.saida.neuronios.length){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de saída e os neurônios de saída da rede");
      }

      double[] dadosEntrada = new double[dados[0].length];
      double[] dadosSaida = new double[saida[0].length];
      double erroMedio = 0;

      for(int i = 0; i < dados.length; i++){ // Percorrer linhas dos dados
         for(int j = 0; j < dados[i].length; j++){ // Preencher dados de entrada
            dadosEntrada[j] = dados[i][j];
         }
         for(int j = 0; j < saida[i].length; j++){ // Preencher dados de saída desejada
            dadosSaida[j] = saida[i][j];
         }

         this.calcularSaida(dadosEntrada);

         for(int k = 0; k < this.saida.neuronios.length; k++){
            erroMedio += Math.abs(dadosSaida[k] - this.saida.neuronios[k].saida);
         }
      }

      erroMedio /= dados.length;
      return (1 - erroMedio); // Converter em um valor relativo a porcentagem
   }
   

   /**
    * Calcula a função de custo baseada nos dados de entrada e na saída esperada para eles por meio do erro médio quadrado.
    * @param dados matriz de dados de entrada.
    * @param saida matriz dos dados de saída.
    * @return valor de custo da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se a quantidade de linhas dos dados fornecidos for diferente da quantidade de linhas das saídas fornecidas.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos neurônios de entrada, excluindo o bias.
    * @throws IllegalArgumentException se o tamanho dos dados de saída for diferente do tamanho dos neurôniosde de saída.
    */
   public double funcaoDeCusto(double[][] dados, double[][] saida){
      modeloValido();
      
      if(dados.length != saida.length){
         throw new IllegalArgumentException("A quantidade de linhas de dados e saídas são diferentes");
      }
      if(dados[0].length != (this.entrada.neuronios.length-BIAS)){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de entrada e os neurônios de entrada da rede");
      }
      if(saida[0].length != this.saida.neuronios.length){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de saída e os neurônios de saída da rede");
      }

      double[] dados_entrada = new double[dados[0].length];//tamanho das colunas da entrada
      double[] dados_saida = new double[saida[0].length];//tamanho de colunas da saída
      
      int i, j, k;
      double diferenca;
      double custo = 0.0;
      for(i = 0; i < dados.length; i++){//percorrer as linhas da entrada
         for(j = 0; j < (this.entrada.neuronios.length - BIAS); j++){//passar os dados para a entrada da rede
            dados_entrada[j] = dados[i][j];
         }
         for(j = 0; j < this.saida.neuronios.length; j++){//passar os dados de saída desejada para o vetor
            dados_saida[j] = saida[i][j];
         }

         //calcular saída com base nos dados passados
         this.calcularSaida(dados_entrada);

         //calcular custo com base na saída
         for(k = 0; k < this.saida.neuronios.length; k++){
            diferenca = dados_saida[k] - this.saida.neuronios[k].saida;
            custo += (diferenca*diferenca);
         }
      }

      custo /= dados.length;

      return custo;
   }


   /**
    * Treina a rede neural usando o algoritmo Backpropagation.
    * @param dados matriz de dados de entrada. Cada linha representa um exemplo de entrada.
    * @param saida matriz de dados de saída esperados. Cada linha representa o valor de saída correspondente ao exemplo de entrada.
    * @param epochs número de épocas de treinamento. Uma época é um ciclo completo de treinamento em que todos os exemplos de treinamento são apresentados para a rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se a quantidade de linhas dos dados fornecidos for diferente da quantidade de linhas das saídas fornecidas.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos neurônios de entrada, excluindo o bias.
    * @throws IllegalArgumentException se o tamanho dos dados de saída for diferente do tamanho dos neurônios de saída da rede.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    */
   public void treinoBackpropagation(double[][] dados, double[][] saida, int epochs){
      modeloValido();

      if(dados.length != saida.length){
         throw new IllegalArgumentException("A quantidade de dados de entrada e saída são diferentes");
      }
      if(dados[0].length != (this.entrada.neuronios.length-BIAS)){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de entrada e os neurônios de entrada da rede");
      }
      if(saida[0].length != this.saida.neuronios.length){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de saída e os neurônios de saída da rede");
      }
      if(epochs < 1){
         throw new IllegalArgumentException("O valor de epochs não pode ser menor que um");
      }

      double[] dadosEntrada = new double[dados[0].length];//tamanho de colunas da entrada
      double[] dadosSaida = new double[saida[0].length];//tamanho de colunas da saída

      for(int i = 0; i < epochs; i++){//quantidade de épocas
         for(int j = 0; j < dados.length; j++){//percorrer linhas dos dados

            for(int k = 0; k < dados[0].length; k++){//preencher dados de entrada
               dadosEntrada[k] = dados[j][k];
            }
            for(int k = 0; k < dadosSaida.length; k++){//preencher dados de saída
               dadosSaida[k] = saida[j][k];
            }

            this.backpropagation(dadosEntrada, dadosSaida);
         }      
      }
   }


   /**
    * Treina a rede com a técnica do gradiente estocástico, onde embaralhamos os dados de entrada para tornar o treino "aleatório" mas que tende a
    * convergir mais rápido.
    * @param dados matriz de dados de entrada. Cada linha representa um exemplo de entrada.
    * @param saida matriz de dados de saída esperados. Cada linha representa o valor de saída correspondente ao exemplo de entrada.
    * @param epochs número de épocas de treinamento. Uma época é um ciclo completo de treinamento em que todos os exemplos de treinamento são apresentados para a rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se a quantidade de linhas dos dados fornecidos for diferente da quantidade de linhas das saídas fornecidas.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos neurônios de entrada, excluindo o bias.
    * @throws IllegalArgumentException se o tamanho dos dados de saída for diferente do tamanho dos neurônios de saída da rede.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    */
   public void treinoGradienteEstocastico(double[][] dados, double[][] saida, int epochs){
      modeloValido();
   
      if(dados.length != saida.length){
         throw new IllegalArgumentException("A quantidade de dados de entrada e saída são diferentes");
      }
      if(dados[0].length != (this.entrada.neuronios.length - BIAS)){
        throw new IllegalArgumentException("Incompatibilidade entre os dados de entrada e os neurônios de entrada da rede");
      }
      if(saida[0].length != this.saida.neuronios.length){
        throw new IllegalArgumentException("Incompatibilidade entre os dados de saída e os neurônios de saída da rede");
      }
      if(epochs < 1){
        throw new IllegalArgumentException("O valor de epochs não pode ser menor que um");
      }
   
      double[] dadosEntrada = new double[dados[0].length];//tamanho de colunas da entrada
      double[] dadosSaida = new double[saida[0].length];//tamanho de colunas da saída
      
      Random random = new Random();
      int[] indices = new int[dados.length];
   
      int i, j, k;
      for(i = 0; i < epochs; i++){// quantidade de épocas
         //embaralhar os dados antes de cada época
         for(j = 0; j < dados.length; j++){
            indices[j] = j;
         }
         for(j = 0; j < dados.length; j++){
            int indiceAleatorio = random.nextInt(dados.length);
            int temp = indices[j];
            indices[j] = indices[indiceAleatorio];
            indices[indiceAleatorio] = temp;
         }

         //treinar com os dados embaralhados
         for(j = 0; j < dados.length; j++){
            int indiceDados = indices[j];
            for(k = 0; k < dados[0].length; k++){// preencher dados de entrada
               dadosEntrada[k] = dados[indiceDados][k];
            }
            for(k = 0; k < dadosSaida.length; k++){// preencher dados de saída
               dadosSaida[k] = saida[indiceDados][k];
            }

            this.backpropagation(dadosEntrada, dadosSaida);
         }
      }
   }


   /**
    * Retropropaga o erro da rede neural de acordo com os dados de entrada e saída esperados, 
    * depois atualiza os pesos usando a técnica de gradiente descendente com momentum.
    *
    * @param entrada array com os dados de entrada das amostras.
    * @param saida array com as saídas esperadas das amostras.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos neurônios de entrada, excluindo o bias.
    * @throws IllegalArgumentException se o tamanho dos dados de saída for diferente do tamanho dos neurônios de saída da rede.
    */
   private void backpropagation(double[] entrada, double[] saida){
      modeloValido();

      if(entrada.length != (this.entrada.neuronios.length-BIAS)){
         throw new IllegalArgumentException("O tamanho dos dados de entrada não corresponde ao tamanho dos neurônios de entrada da rede, com exceção dos bias");
      }
      if(saida.length != this.saida.neuronios.length){
         throw new IllegalArgumentException("O tamanho dos dados de saída não corresponde ao tamanho dos neurônios de saída da rede");
      }

      //calcular saída para aplicar o erro
      this.calcularSaida(entrada);

      //transformar a rede num vetor de camadas pra facilitar minha vida
      ArrayList<Camada> redec = new ArrayList<>();
      redec.add(this.entrada);
      for(Camada camada : this.ocultas) redec.add(camada);
      redec.add(this.saida);

      //erro da saída
      for(int i = 0; i < this.saida.neuronios.length; i++){
         Neuronio neuronio = this.saida.neuronios[i];
         neuronio.erro = ((saida[i] - neuronio.saida) * this.saida.funcaoAtivacaoDx(neuronio.somatorio));
      }

      double somaErros = 0.0;
      //começar da ultima oculta
      for(int i = redec.size()-2; i >= 1; i--){// percorrer camadas ocultas de trás pra frente
         
         Camada camadaAtual = redec.get(i);
         int qNeuronioAtual = camadaAtual.neuronios.length;
         if(redec.get(i).temBias) qNeuronioAtual -= 1;
         for (int j = 0; j < qNeuronioAtual; j++){//percorrer neurônios da camada atual
         
            Neuronio neuronio = camadaAtual.neuronios[j];
            somaErros = 0.0;
            for(Neuronio neuronioProximo : redec.get(i+1).neuronios){//percorrer neurônios da camada seguinte
               somaErros += neuronioProximo.pesos[j] * neuronioProximo.erro;
            }
            neuronio.erro = somaErros * camadaAtual.funcaoAtivacaoDx(neuronio.somatorio);
         }
     }

      for(int i = 1; i < redec.size(); i++){//percorrer rede 
         
         Camada camadaAtual = redec.get(i);
         Camada camadaAnterior = redec.get(i-1);
         for(int j = 0; j < camadaAtual.neuronios.length; j++){//percorrer neurônios da camada atual
            
            Neuronio neuronio = camadaAtual.neuronios[j];
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradiente = TAXA_APRENDIZAGEM * neuronio.erro * camadaAnterior.neuronios[k].saida; 
               neuronio.momentum[k] = (TAXA_MOMENTUM * neuronio.momentum[k]) + neuronio.gradiente;
               neuronio.pesos[k] += neuronio.momentum[k];
            }
         }
      } 
   }


   /**
    * Método alternativo no treino da rede neural usando diferenciação finita (finite difference), que calcula a "derivada" da função de custo levando
    * a rede ao mínimo local dela. É importante encontrar um bom balanço entre a taxa de aprendizagem da rede e o valor de perturbação usado.
    * <p>
    *    Vale ressaltar que esse método é mais lento e menos eficiente que o backpropagation, em arquiteturas de rede maiores e que tenha uma grande 
    *    volume de dados de treino ou para problemas mais complexos ele pode demorar muito para convergir ou simplemente não funcionar como esperado.
    * </p>
    * <p>
    *    Ainda sim pode ser uma abordagem válida.
    * </p>
    * @param treinoEntrada matriz com os dados de entrada 
    * @param treinoSaida matriz com os dados de saída
    * @param eps valor de perturbação
    * @param epochs número de épocas do treinamento
    * @param custoMinimo valor de custo desejável, o treino será finalizado caso o valor de custo mínimo seja atingido. Caso o custo mínimo seja zero, o treino
    * irá continuar até o final das épocas fornecidas
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se a quantidade de linhas dos dados fornecidos for diferente da quantidade de linhas das saídas fornecidas.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada do treino for diferente da quantidade de neurônios de entrada da rede, excluindo o bias.
    * @throws IllegalArgumentException se o tamanho dos dados de saída do treino for diferente da quantidade de neurônios da saída da rede.
    * @throws IllegalArgumentException se o valor de perturbação for igual a zero.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    * @throws IllegalArgumentException se o valor de custo mínimo for menor que zero.
    */
   public void diferencaFinita(double[][] treinoEntrada, double[][] treinoSaida, double eps, int epochs, double custoMinimo){
      modeloValido();

      if(treinoEntrada.length != treinoSaida.length){
         throw new IllegalArgumentException("A quantidade de linhas de dados e saídas são diferentes");
      }
      if(treinoEntrada[0].length != (this.entrada.neuronios.length-BIAS)){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de entrada e os neurônios de entrada da rede.");
      }
      if(treinoSaida[0].length != this.saida.neuronios.length){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de saída e os neurônios de saída da rede");
      }
      if(eps == 0){
         throw new IllegalArgumentException("O valor de perturbação não pode ser igual a zero.");
      }
      if(epochs < 1){
         throw new IllegalArgumentException("O valor de epochs não pode ser menor que um.");
      }
      if(custoMinimo < 0){
         throw new IllegalArgumentException("O valor de custo mínimo não pode ser negativo.");
      }

      RedeNeural redeG = this.clone();//copia da rede para guardar os valores de gradiente
      
      ArrayList<Camada> camadasRede = new ArrayList<Camada>();//copia da rede para camadas
      ArrayList<Camada> camadasGradiente = new ArrayList<Camada>();//copia da rede gradiente para camadas
      
      //colocando a rede de forma sequencial
      camadasRede.add(this.entrada);
      for(Camada camada : this.ocultas) camadasRede.add(camada);
      camadasRede.add(this.saida);
      
      //colocando a rede gradiente de forma sequencial
      camadasGradiente.add(redeG.entrada);
      for(Camada camada : redeG.ocultas) camadasGradiente.add(camada);
      camadasGradiente.add(redeG.saida);

      for(int epocas = 0; epocas < epochs; epocas++){
         double custo = this.funcaoDeCusto(treinoEntrada, treinoSaida);
         if(custo < custoMinimo) break;

         double valorAnterior = 0;

         for(int i = 0; i < camadasRede.size(); i++){//percorrer camadas da rede
            for(int j = 0; j < camadasRede.get(i).neuronios.length; j++){//percorrer neuronios da camada
               for(int k = 0; k < camadasRede.get(i).neuronios[j].pesos.length; k++){//percorrer pesos do neuronio
                  valorAnterior = camadasRede.get(i).neuronios[j].pesos[k];
                  camadasRede.get(i).neuronios[j].pesos[k] += eps;
                  camadasGradiente.get(i).neuronios[j].pesos[k] = ((funcaoDeCusto(treinoEntrada, treinoSaida) - custo)/eps);//derivada da função de custo
                  camadasRede.get(i).neuronios[j].pesos[k] = valorAnterior;
               }
            }
         }

         //atualizar pesos
         for(int i = 0; i < camadasRede.size(); i++){
            for(int j = 0; j < camadasRede.get(i).neuronios.length; j++){
               for(int k = 0; k < camadasRede.get(i).neuronios[j].pesos.length; k++){
                  camadasRede.get(i).neuronios[j].pesos[k] -= TAXA_APRENDIZAGEM * camadasGradiente.get(i).neuronios[j].pesos[k];
               }
            }
         }
      }

   }


   /**
    * @return valor de taxa de aprendizagem da rede.
    */
   public double obterTaxaAprendizagem(){
      return this.TAXA_APRENDIZAGEM;
   }


   /**
    * @return valor de taxa de momentum da rede.
    */
   public double obterTaxaMomentum(){
      return this.TAXA_MOMENTUM;
   }


   /**
    * Retorna a camada de entrada da rede.
    * @return camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public Camada obterCamadaEntrada(){
      modeloValido();
      return this.entrada;
   }


   /**
    * Retorna a camada oculta correspondente a ao índice fornecido.
    * @param indice índice da busca.
    * @return camada oculta baseada na busca.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o índice estiver fora do alcance do tamanho das camadas ocultas.
    */
   public Camada obterCamadaOculta(int indice){
      modeloValido();
      if((indice < 0) || (indice > this.ocultas.length-1)){
         throw new IllegalArgumentException("O índice fornecido está fora do alcance das camadas disponíveis");
      }
   
      return this.ocultas[indice];
   }


   /**
    * Retorna a camada de saída da rede.
    * @return camada de saída.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public Camada obterCamadaSaida(){
      modeloValido();
      return this.saida;
   }


   /**
    * Copia os dados de saída de cada neurônio da camada de saída da rede neural para um vetor.
    * A ordem de cópia é crescente, do primeiro neurônio da saída ao último.
    * @return vetor com os dados das saídas da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public double[] obterSaidas(){
      modeloValido();

      double saida[] = new double[this.saida.neuronios.length];
      for(int i = 0; i < this.saida.neuronios.length; i++){
         saida[i] = this.saida.neuronios[i].saida;
      }

      return saida;
   }


   /**
    * Cria um array contendo os valores dos neuônios de cada camada da rede neural. Cada elementos do 
    * array representa uma camada da rede.
    * @return array com a arquitetura da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int[] obterArquitetura(){
      modeloValido();
      return this.arquitetura;
   }


   /**
    * Exibe as informações importantes da rede neural como:
    * <ul>
    *    <li>Contém bias como neurônio adicional.</li>
    *    <li>Valor da taxa de aprendizagem.</li>
    *    <li>Função de ativação de todas as camadas ocultas.</li>
    *    <li>Função de ativação da camada de saída.</li>
    *    <li>Arquitetura da rede.</li>
    * </ul>
    * @return buffer formatado contendo as informações.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public String obterInformacoes(){
      modeloValido();

      String buffer = "";
      String espacamento = "    ";
      System.out.println("\nInformações " + this.getClass().getSimpleName() + " = [");

      //bias
      if(this.BIAS == 1) buffer += espacamento + "Bias = " + "true\n";
      else buffer += espacamento + "Bias: " + "false\n";

      buffer += espacamento + "Taxa de aprendizgem: " + TAXA_APRENDIZAGEM + "\n";
      buffer += espacamento + "Taxa de momentum: " + TAXA_MOMENTUM + "\n\n";

      for(int i = 0; i < this.ocultas.length; i++){
         buffer += espacamento + "Ativação oculta " + i + " = " + this.ocultas[i].ativacao + "\n";
      }
      buffer += espacamento + "Ativação saída = " + this.saida.ativacao + "\n";

      //arquitetura
      buffer += "\n" + espacamento + "arquitetura = {" + this.arquitetura[0];
      for(int i = 0; i < this.ocultas.length; i++) buffer += ", " + this.arquitetura[i+1];
      buffer += ", " + this.arquitetura[this.arquitetura.length-1] + "}";

      buffer += "\n]\n";

      return buffer;
   }

   
   /**
    * Clona a instância da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @return Clone da rede
    */
   @Override
   public RedeNeural clone(){
      modeloValido();
      try{
         RedeNeural clone = (RedeNeural) super.clone();

         // Clonar dados importantes
         clone.neuroniosEntrada = this.neuroniosEntrada;
         clone.neuroniosOcultas = this.neuroniosOcultas;
         clone.neuroniosSaida = this.neuroniosSaida;
         clone.quantidadeOcultas = this.quantidadeOcultas;
         clone.BIAS = this.BIAS;
         clone.TAXA_APRENDIZAGEM = this.TAXA_APRENDIZAGEM;

         boolean temBias = (this.BIAS == 1) ? true : false;
         // Clonar camada de entrada
         clone.entrada = cloneCamada(this.entrada, temBias);

         // Clonar camadas ocultas
         clone.ocultas = new Camada[quantidadeOcultas];
         for (int i = 0; i < quantidadeOcultas; i++) {
            clone.ocultas[i] = cloneCamada(this.ocultas[i], temBias);
         }

         // Clonar camada de saída
         clone.saida = cloneCamada(this.saida, false);

         return clone;
      }catch(CloneNotSupportedException e){
         throw new RuntimeException(e);
      }
   }


   private Camada cloneCamada(Camada camada, boolean temBias){
      Camada clone = new Camada(temBias);
      clone.neuronios = new Neuronio[camada.neuronios.length];
      clone.ativacao = camada.ativacao;
      clone.temBias = camada.temBias;

      for (int i = 0; i < camada.neuronios.length; i++) {
         clone.neuronios[i] = cloneNeuronio(camada.neuronios[i], camada.neuronios[i].pesos.length, camada.neuronios[i].pesos);
      }

      return clone;
   }


   private Neuronio cloneNeuronio(Neuronio neuronio, int qtdLigacoes, double[] pesos){
      Neuronio clone = new Neuronio(neuronio.pesos.length, this.alcancePeso);

      double pesosClone[] = new double[qtdLigacoes];

      for(int i = 0; i < pesos.length; i++){
         pesosClone[i] = pesos[i];
      }

      clone.pesos = pesosClone;

      return clone;
   }


   /**
    * Salva a classe da rede em um arquivo especificado, o caminho não leva em consideração
    * o formato, de preferência deve ser .dat, caso seja especificado apenas o nome, o arquivo
    * será salvo no mesmo diretório que o arquivo principal.
    * @param caminho caminho de destino do arquivo que será salvo.
    */
   public void salvarArquivoRede(String caminho){
      try{
         FileOutputStream arquivo = new FileOutputStream(caminho);
         ObjectOutputStream objeto = new ObjectOutputStream(arquivo);

         objeto.writeObject(this);
         objeto.close();
         arquivo.close();

      }catch(Exception e){
         e.printStackTrace();
      }
   }


   /**
    * Lê um arquivo de rede neural no caminho especificado, o caminho não leva em consideração
    * o formato, logo precisa ser especificado.
    * @param caminho caminho do arquivo de rede salvo
    * @return Rede lida pelo arquivo.
    */
   public static RedeNeural lerArquivoRede(String caminho){
      RedeNeural rede = null;

      try{
         FileInputStream arquivo = new FileInputStream(caminho);
         ObjectInputStream objeto = new ObjectInputStream(arquivo);

         rede = (RedeNeural) objeto.readObject();
         objeto.close();
         arquivo.close();

      }catch(Exception e){
         e.printStackTrace();
      }

      return rede;
   }


   public String toString(){
      modeloValido();

      String buffer = "";
      String espacamento = "   ";
      String espacamentoDuplo = espacamento + espacamento;
      String espacamentoTriplo = espacamento + espacamento + espacamento;
      
      buffer += "\nArquitetura " + this.getClass().getSimpleName() + " = [\n";

      //ocultas
      for(int i = 0; i < this.ocultas.length; i++){
         buffer += espacamento + "Oculta " + i + " = [\n";
         for(int j = 0; j < this.ocultas[i].neuronios.length-BIAS; j++){
            
            buffer += espacamento + espacamento + "n" + j + " = [\n";
            
            for(int k = 0; k < this.ocultas[i].neuronios[j].pesos.length; k++){
               if(k == this.ocultas[i].neuronios[j].pesos.length-1 && (this.BIAS == 1)){
                  buffer += espacamentoTriplo + "pb" + " = " + this.ocultas[i].neuronios[j].pesos[k] + "\n";
               
               }else{
                  buffer += espacamentoTriplo + "p" + k + " = " + this.ocultas[i].neuronios[j].pesos[k] + "\n";
               }
            }
            buffer += espacamentoDuplo + "]\n";
         }
         buffer += espacamento + "]\n\n";
      }

      //saida
      buffer += espacamento + "Saída = [\n";
      for(int i = 0; i < this.saida.neuronios.length; i++){
         for(int j = 0; j < this.saida.neuronios[i].pesos.length; j++){
            if(j == this.saida.neuronios[i].pesos.length-1 && (this.BIAS == 1)){
               buffer += espacamentoDuplo + "pb" + " = " + this.saida.neuronios[i].pesos[j] + "\n";
            
            }else buffer += espacamentoDuplo + "p" + j + " = " + this.saida.neuronios[i].pesos[j] + "\n";
         }
      }
      buffer += espacamento + "]\n";

      buffer += "]\n";

      return buffer;
   }
}