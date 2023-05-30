package rna;

public class RedeNeural implements Cloneable{
   public Camada entrada;
   public Camada[] ocultas;
   public Camada saida;
   
   public int qtdNeuroniosEntrada;
   public int qtdNeuroniosOcultas;
   public int qtdNeuroniosSaida;

   public int qtdCamadasOcultas;

   int BIAS = 1;
   double TAXA_APRENDIZAGEM = 0.3;

   //padronizar uso das funções de ativação
   final int ativacaoRelu = 1;
   final int ativacaoReluDx = 2;
   final int ativacaoSigmoid = 3;
   final int ativacaoSigmoidDx = 4;
   final int ativacaoTanH = 5;
   final int ativacaoTanHDx = 6;
   final int ativacaoLeakyRelu = 7;
   
   int funcaoAtivacao = ativacaoRelu;
   int funcaoAtivacaoSaida = ativacaoReluDx;

   int i, j, k;//contadores

   public RedeNeural(int qtdNeuroniosEntrada, int qtdNeuroniosOcultas, int qtdNeuroniosSaida, int qtdCamadasOcultas){
      if(qtdNeuroniosEntrada < 1 || qtdNeuroniosOcultas < 1 || qtdNeuroniosSaida < 1 || qtdCamadasOcultas < 1){
         throw new IllegalArgumentException("Os valores devem ser maiores ou iguais a um.");
      }

      this.qtdNeuroniosEntrada = qtdNeuroniosEntrada;
      this.qtdNeuroniosOcultas = qtdNeuroniosOcultas;
      this.qtdNeuroniosSaida = qtdNeuroniosSaida;

      this.qtdCamadasOcultas = qtdCamadasOcultas;

      criarRede();
   }


   private void criarRede(){
      //inicializar camada de entrada
      entrada = new Camada();
      entrada.neuronios = new Neuronio[qtdNeuroniosEntrada];
      for(int i = 0; i < qtdNeuroniosEntrada; i++){
         entrada.neuronios[i] = new Neuronio(qtdNeuroniosOcultas);
      }

      //inicializar camadas ocultas
      ocultas = new Camada[qtdCamadasOcultas];
      for (int i = 0; i < qtdCamadasOcultas; i++) {
         Camada novaOculta = new Camada();
         novaOculta.neuronios = new Neuronio[qtdNeuroniosOcultas];
      
         for (int j = 0; j < qtdNeuroniosOcultas; j++) {
            if (i == (qtdCamadasOcultas-1)){
               novaOculta.neuronios[j] = new Neuronio(qtdNeuroniosSaida);
            
            }else{
               novaOculta.neuronios[j] = new Neuronio(qtdNeuroniosOcultas);
            }
         }
         ocultas[i] = novaOculta;
      }

      //inicializar camada de saída
      saida = new Camada();
      saida.neuronios = new Neuronio[qtdNeuroniosSaida];
      for(int i = 0; i < qtdNeuroniosSaida; i++){
         saida.neuronios[i] = new Neuronio(qtdNeuroniosSaida);
      }

   }


   public void calcularSaida(double[] dados){
      if(dados.length != this.entrada.neuronios.length){
         throw new IllegalArgumentException("As dimensões dos dados de entrada com os neurônios de entrada da rede não são iguais");
      }

      //entrada
      for(i = 0; i < this.qtdNeuroniosEntrada; i++){
         this.entrada.neuronios[i].saida = dados[i];
      }

      //ocultas
      double soma = 0.0;
      for(i = 0; i < this.qtdCamadasOcultas; i++){//percorrer camadas ocultas

         Camada camadaAtual = this.ocultas[i];
         Camada camadaAnterior;
         if(i == 0) camadaAnterior = this.entrada;
         else camadaAnterior = this.ocultas[i-1];

         for(j = 0; j < camadaAtual.neuronios.length; j++){//percorrer cada neuronio da camada atual
            //saída é o somatorio dos pesos com os valores dos neuronios
            //aplicado na função de ativação
            soma = 0.0;
            for(k = 0; k < camadaAnterior.neuronios.length; k++){
               soma += camadaAnterior.neuronios[k].saida * camadaAnterior.neuronios[k].pesos[j];
            }
            soma += BIAS;
            camadaAtual.neuronios[j].saida = funcaoAtivacao(soma);
         }
      }

      //saída
      for(i = 0; i < this.saida.neuronios.length; i++){
         soma = 0.0;
         for(j = 0; j < (this.ocultas[this.qtdCamadasOcultas-1].neuronios.length); j++){
            soma += (
               this.ocultas[this.qtdCamadasOcultas-1].neuronios[j].saida *
               this.ocultas[this.qtdCamadasOcultas-1].neuronios[j].pesos[i]
            ); 
         }
         soma += BIAS;
         this.saida.neuronios[i].saida = funcaoAtivacaoSaida(soma);
      }
   }

   
   public double calcularPrecisao(double[][] dados){
      double precisao = 0;

      double[] dados_treino = new double[dados[0].length-1];
      double[] classe_treino = new double[1];
      int acertosTotais = 0;
      int acertosSaida = 0;

      for(int i = 0; i < dados.length; i++){
         dados_treino[0] = dados[i][0];
         dados_treino[1] = dados[i][1];
         dados_treino[2] = dados[i][2];
         classe_treino[0]= dados[i][3];

         this.calcularSaida(dados_treino);

         for(int j = 0; j < this.saida.neuronios.length; j++){
            if(this.saida.neuronios[j].saida == classe_treino[j]){
               acertosSaida ++;
            }
         }
         
         if(acertosSaida == (this.saida.neuronios.length)) acertosTotais++;

      }

      precisao = (double) acertosTotais/dados.length;
      return (precisao*100);
   }


   public void backpropagation(double[] dados, double[] saidaEsperada){
      if(saidaEsperada.length != this.saida.neuronios.length){
         System.out.println("imcompatibilidade de dimensões");
         return;
      }

      //calcular saída para aplicar o erro
      this.calcularSaida(dados);

      //CALCULANDO OS ERRROS DAS CAMADAS
      //calcular erros da saída
      for(int i = 0; i < this.saida.neuronios.length; i++){
         this.saida.neuronios[i].erro = (this.saida.neuronios[i].saida - saidaEsperada[i]);
         this.saida.neuronios[i].erro = reluDx(this.saida.neuronios[i].erro);
      }

      //propagar o erros para as outras camadas
      for(int i = (this.ocultas.length-1); i >= 0; i--){
         Camada camadaAtual = this.ocultas[i];
         Camada proximaCamada;
         if(i == (this.ocultas.length-1)) proximaCamada = this.saida;
         else proximaCamada = this.ocultas[i+1];

         //percorrer neuronios da camada atual
         for(int j = 0; j < camadaAtual.neuronios.length; j++){
            double erro = 0.0;
            
            for(int k = 0; k < proximaCamada.neuronios.length; k++){
               erro += (proximaCamada.neuronios[k].erro * camadaAtual.neuronios[j].pesos[k]);
               camadaAtual.neuronios[j].erro = erro * reluDx(camadaAtual.neuronios[j].saida);
            }
         }
      }

      //feito pelo chat, não muito confiavel ainda
      //atualizando os pesos
      for (int i = 0; i < ocultas.length; i++) {
         Camada camadaAtual = ocultas[i];
         Camada proximaCamada;
         if (i == ocultas.length - 1)
            proximaCamada = saida;
         else
            proximaCamada = ocultas[i + 1];
      
         for (int j = 0; j < camadaAtual.neuronios.length; j++) {
            Neuronio neuronioAtual = camadaAtual.neuronios[j];
      
            // Atualizar os pesos do neurônio atual
            for (int k = 0; k < proximaCamada.neuronios.length; k++) {
               Neuronio neuronioProximaCamada = proximaCamada.neuronios[k];
      
               // Calcular o ajuste do peso usando a regra do Gradiente Descendente
               double ajustePeso = TAXA_APRENDIZAGEM * neuronioProximaCamada.erro * neuronioAtual.saida;
               neuronioAtual.pesos[k] -= ajustePeso;
            }
         }
      }
      
      // Atualizar os pesos da camada de saída
      for (int i = 0; i < saida.neuronios.length; i++) {
         Neuronio neuronioSaida = saida.neuronios[i];
      
         // Calcular o ajuste do peso usando a regra do Gradiente Descendente
         double ajustePeso = TAXA_APRENDIZAGEM * neuronioSaida.erro * neuronioSaida.saida;
         for (int j = 0; j < neuronioSaida.pesos.length; j++) {
            neuronioSaida.pesos[j] -= ajustePeso;
         }
      }
   }


   /**
    * 1 - ReLu.
    * 2 - ReLu derivada.
    * 3 - Sigmoide.
    * 4 - Sigmoid derivada.
    * 5 - Tangente hiperbolica.
    * 6 - Tangente hiperbolica derivada.
    * 7 - Leaky ReLu.
    * Por padrão será usado ReLu e Relu derivada, respectivamente.
    * @param ocultas função de ativação das camadas ocultas
    * @param saida função de ativação da ultima camada oculta para a saída
    */
   public void configurarFuncaoAtivacao(int ocultas, int saida){
      if(ocultas < 0 || ocultas > 7) funcaoAtivacao = 1;
      if(saida < 0 || saida > 7) funcaoAtivacaoSaida = 2;

      funcaoAtivacao = ocultas;
      funcaoAtivacaoSaida = saida;
   }


   //FUNÇÕES DE ATIVAÇÃO
   private double funcaoAtivacao(double valor){
      if(funcaoAtivacao == ativacaoRelu) return relu(valor);
      if(funcaoAtivacao == ativacaoReluDx) return reluDx(valor);
      if(funcaoAtivacao == ativacaoSigmoid) return sigmoid(valor);
      if(funcaoAtivacao == ativacaoSigmoidDx) return sigmoidDx(valor);
      if(funcaoAtivacao == ativacaoTanH) return tanH(valor);
      if(funcaoAtivacao == ativacaoTanHDx) return tanHDx(valor);
      if(funcaoAtivacao == ativacaoLeakyRelu) return leakyRelu(valor);

      else return valor;
   }


   private double funcaoAtivacaoSaida(double valor){
      if(funcaoAtivacaoSaida == ativacaoRelu) return relu(valor);
      if(funcaoAtivacaoSaida == ativacaoReluDx) return reluDx(valor);
      if(funcaoAtivacaoSaida == ativacaoSigmoid) return sigmoid(valor);
      if(funcaoAtivacaoSaida == ativacaoSigmoidDx) return sigmoidDx(valor);
      if(funcaoAtivacaoSaida == ativacaoTanH) return tanH(valor);
      if(funcaoAtivacaoSaida == ativacaoTanHDx) return tanHDx(valor);
      if(funcaoAtivacaoSaida == ativacaoLeakyRelu) return leakyRelu(valor);

      else return valor;
   }


   private double relu(double valor){
      if(valor < 0) return 0;
      return valor;
   }


   private double reluDx(double valor){
      if(valor < 0) return 0;
      return 1;     
   }


   private double sigmoid(double valor){
      return 1 / (1 + Math.exp(-valor));
   }


   private double sigmoidDx(double valor){
      return (sigmoid(valor) * (1-sigmoid(valor)));
   }


   private double tanH(double valor){
      return Math.tanh(valor);
   }


   private double tanHDx(double valor){
      double resultado = Math.tanh(valor);
      return (1 - Math.pow(resultado, 2));
   }


   private double leakyRelu(double valor){
      if(valor >= 0) return valor;
      else return (0.01) * valor;
   }


   //clonar a rede
   @Override
   public RedeNeural clone(){
      try {
         RedeNeural clone = (RedeNeural) super.clone();

         // Clonar dados importantes
         clone.qtdNeuroniosEntrada = this.qtdNeuroniosEntrada;
         clone.qtdNeuroniosOcultas = this.qtdNeuroniosOcultas;
         clone.qtdNeuroniosSaida = this.qtdNeuroniosSaida;
         clone.qtdCamadasOcultas = this.qtdCamadasOcultas;
         clone.BIAS = this.BIAS;
         clone.TAXA_APRENDIZAGEM = this.TAXA_APRENDIZAGEM;

         // Clonar camada de entrada
         clone.entrada = cloneCamada(this.entrada);

         // Clonar camadas ocultas
         clone.ocultas = new Camada[qtdCamadasOcultas];
         for (int i = 0; i < qtdCamadasOcultas; i++) {
            clone.ocultas[i] = cloneCamada(this.ocultas[i]);
         }

         // Clonar camada de saída
         clone.saida = cloneCamada(this.saida);

         return clone;
      } catch (CloneNotSupportedException e){
         throw new RuntimeException(e);
      }
   }


   private Camada cloneCamada(Camada camada){
      Camada clone = new Camada();
      clone.neuronios = new Neuronio[camada.neuronios.length];

      for (int i = 0; i < camada.neuronios.length; i++) {
         clone.neuronios[i] = cloneNeuronio(camada.neuronios[i], camada.neuronios[i].qtdLigacoes, camada.neuronios[i].pesos);
      }

      return clone;
   }


   private Neuronio cloneNeuronio(Neuronio neuronio, int qtdLigacoes, double[] pesos){
      Neuronio clone = new Neuronio(neuronio.pesos.length);

      double pesosClone[] = new double[qtdLigacoes];

      for(int i = 0; i < pesos.length; i++){
         pesosClone[i] = pesos[i];
      }

      clone.pesos = pesosClone;
      clone.qtdLigacoes = qtdLigacoes;

      return clone;
   }
}
