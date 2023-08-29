package utilitarios.ged;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * Classe criada para centralizar um tipo de dado 
 * específico para uso dentro do Ged.
 */
public class Dados{

   /**
    * <p>
    *    Região crítica.
    * </p>
    * Estrutura que armazena o conteúdo de dados lidos
    */
   private ArrayList<String[]> conteudo;


   /**
    * Inicializa um objeto do tipo Dados com seu conteúdo vazio.
    */
   public Dados(){
      this.conteudo = new ArrayList<>();
      this.conteudo.add(new String[0]);
   }


   /**
    * Retorna o item correspondente pela linha e coluna fornecidos.
    * @param lin linha para busca.
    * @param col coluna para busca.
    * @return valor contido com base na linha e coluna.
    */
   public String obterItem(int lin, int col){
      return this.conteudo.get(lin)[col];
   }


   /**
    * Edita o valor contido no espaço indicado pela linha e coluna.
    * @param lin linha para edição.
    * @param col coluna para edição.
    * @param valor novo valor.
    * @throws IllegalArgumentException se os índices fornecidos forem inválidos.
    */
   public void editarItem(int lin, int col, String valor){
      if(lin < 0 || lin >= this.conteudo.size()){
         throw new IllegalArgumentException("Índice de linha fornecido é inválido.");
      }
      if(col < 0 || col >= this.conteudo.get(lin).length){
         throw new IllegalArgumentException("Índice de coluna fornecido é inválido.");
      }

      String[] linha = this.conteudo.get(lin);
      linha[col] = valor;
      this.conteudo.set(lin, linha);
   }


   /**
    * Edita o valor em todas as linhas de acordo com a coluna especificada.
    * @param lin linha para edição.
    * @param col coluna para edição.
    * @param busca valor alvo.
    * @param valor novo valor.
    */
   public void editarItem(int col, String busca, String valor){
      for(String[] linha : this.conteudo){
         if(linha[col].contains(busca)){
            linha[col] = valor;
         }
      }
   }


   /**
    * Substitui todo o conteúdo atual de Dados pela nova lista.
    * @param lista lista com os novos dados.
    */
   public void atribuir(ArrayList<String[]> lista){
      if(lista != null) this.conteudo = lista;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * @param matriz matriz com os dados.
    */
   public void atribuir(int[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = Integer.toString(matriz[i][j]);
         }
         lista.add(linha);
      }

      this.conteudo = lista;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * @param matriz matriz com os dados.
    */
   public void atribuir(float[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = Float.toString(matriz[i][j]);
         }
         lista.add(linha);
      }

      this.conteudo = lista;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * @param matriz matriz com os dados.
    */
   public void atribuir(double[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = Double.toString(matriz[i][j]);
         }
         lista.add(linha);
      }

      this.conteudo = lista;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * @param matriz matriz com os dados.
    */
   public void atribuir(String[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = matriz[i][j];
         }
         lista.add(linha);
      }

      this.conteudo = lista;
   }


   /**
    * Retorna todo o conteúdo presente nos dados.
    * @return estrutura {@code ArrayList<String[]>} que armazena o conteúdo dos dados.
    */
   public ArrayList<String[]> conteudo(){
      return this.conteudo;
   }


   /**
    * Verifica se o conteúdo do conjunto de dados está vazio.
    * <p>
    *    Ele é considerado vazio se não conter nenhuma linha ou 
    *    se todas as linhas estiverem vazias.
    * </p>
    * @return true se o conjunto de dados estiver vazio, false caso contrário.
    */
   public boolean estaVazio(){
      if(this.conteudo.isEmpty()) return true;
      
      for(int i = 0; i < this.conteudo.size(); i++){
         if(this.conteudo.get(i).length > 0) return false;
      }

      return true;
   }


   /**
    * Calula a média do conteúdo que pode ser transformado para valor numérico 
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return média dos elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double media(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      double soma = 0;
      int contador = 0;//contador de ocasiôes transformadas

      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         
         try{
            double valorTransformado = Double.parseDouble(valor);
            soma += valorTransformado;
            contador++;

         }catch(Exception e){

         }
      }

      if(contador == 0) return 0;

      return soma / contador;
   }


   /**
    * Calcula a mediana do conteúdo que pode ser transformado para valor numérico
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return valor da mediana dos elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double mediana(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      ArrayList<Double> valoresNumericos = new ArrayList<>();

      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         try{
            double valorTransformado = Double.parseDouble(valor);
            valoresNumericos.add(valorTransformado);
         
         }catch(NumberFormatException e){
         
         }
      }  

      if(valoresNumericos.isEmpty()) return 0;

      Collections.sort(valoresNumericos);
      int meio = valoresNumericos.size() / 2;

      if(valoresNumericos.size() % 2 == 0){
         return (valoresNumericos.get(meio-1) + valoresNumericos.get(meio))/2;
      
      }else{
         return valoresNumericos.get(meio);
      }
   }


   /**
    * Calcula a moda do conteúdo que pode ser transformado para valor numérico
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return valor da moda dos elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double moda(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      HashMap<String, Integer> frequenciaElementos = new HashMap<>();

      //frequência de cada valor na coluna
      for(String[] linha : this.conteudo){
         
         String valor = linha[idCol].trim();
         if(!valor.isEmpty()){
            try{
               Double.parseDouble(valor);
               if(frequenciaElementos.containsKey(valor)){//elemento existente
                  frequenciaElementos.put(valor, frequenciaElementos.get(valor) + 1);
                  
               }else{//elemento novo
                  frequenciaElementos.put(valor, 1);
               }

            }catch(Exception e){

            }
         }
      }

      if (frequenciaElementos.isEmpty()) return 0;

      String moda = "";
      int maxFrequencia = 0;

      // Encontra o valor com maior frequência
      for(String valor : frequenciaElementos.keySet()){
         
         int frequencia = frequenciaElementos.get(valor);
         if(frequencia > maxFrequencia){
            maxFrequencia = frequencia;
            moda = valor;
         }
      }

      if(moda.isEmpty()) return 0;

      try{
         return Double.parseDouble(moda);
      
      }catch(NumberFormatException e){
         return 0;
      }
   }


   /**
    * Calcula o maior valor do conteúdo que pode ser transformado para valor numérico
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return valor máximo entre os elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double maximo(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      ArrayList<Double> valoresNumericos = new ArrayList<>();
  
      //transformando o que dá em valor
      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         try{
            double num = Double.parseDouble(valor);
            valoresNumericos.add(num);
         
         }catch(NumberFormatException e){
            
         }
      }
  
      if(valoresNumericos.isEmpty()) return 0;
      double valorMaximo = valoresNumericos.get(0);

      //procurando valor maior
      for(int i = 1; i < valoresNumericos.size(); i++){
         if(valoresNumericos.get(i) > valorMaximo){
            valorMaximo = valoresNumericos.get(i);
         }
      }
  
      return valorMaximo;
   }


   /**
    * Calcula o menor valor do conteúdo que pode ser transformado para valor numérico
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return valor mínimo entre os elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double minimo(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      ArrayList<Double> valoresNumericos = new ArrayList<>();
  
      //transformando o que dá em valor
      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         try{
            double num = Double.parseDouble(valor);
            valoresNumericos.add(num);
         
         }catch(NumberFormatException e){
            
         }
      }
  
      if(valoresNumericos.isEmpty()) return 0;
      double valorMaximo = valoresNumericos.get(0);

      //procurando valor menor
      for(int i = 1; i < valoresNumericos.size(); i++){
         if(valoresNumericos.get(i) < valorMaximo){
            valorMaximo = valoresNumericos.get(i);
         }
      }
  
      return valorMaximo;
   }


   /**
    * Retorna um array contendo as linhas e colunas do conteúdo dos dados.
    * <p>
    *    {@code shape[0] = linhas}
    * </p>
    * <p>
    *    {@code shape[1] = colunas}
    * </p>
    * @return estrutura contendo o formato da lista, considerando que ela é simétrica.
    * @throws IllegalArgumentException se o conteúdo estiver vazio.
    * @throws IllegalArgumentException se o conteúdo não for simétrico.
    */
   public int[] shape(){
      if(this.estaVazio()){
         throw new IllegalArgumentException("O conteúdo dos dados está vazio.");
      }

      if(!this.dadosSimetricos()){
         throw new IllegalArgumentException("O conteúdo dos dados não é.");
      }

      int[] shape = {
         this.conteudo.size(), 
         this.conteudo.get(0).length
      };
      return shape;
   }


   /**
    * Retorna um buffer contendo as informações do conteúdo dos dados, onde:
    * <p>
    *    {@code shape = [linhas, colunas]}
    * </p>
    * @return estrutura contendo o formato da lista, considerando que ela é simétrica.
    */
   public String shapeInfo(){
      if(this.estaVazio()){
         return "Shape = [ (Vazio) ]";
      }

      int[] shape = {
         this.conteudo.size(), 
         this.conteudo.get(0).length
      };

      return "[" + Integer.toString(shape[0]) + ", " + Integer.toString(shape[1]) + "]";
   }


   /**
    * Método de impressão básico, via console, do conteúdo
    * contido em formato de tabela.
    * <p>
    *    Caso os dados sejam simétricos, também é exibido o formato do conteúdo.
    * </p>
    */
   public void imprimir(){
      String espacamento = "   ";

      if(this.estaVazio()){
         System.out.println("Dados = [");
         System.out.println(espacamento + "(Vazio)");
         
      }else{
         if(this.dadosSimetricos()){
            int[] shape = this.shape();
            System.out.println("Dados (" + shape[0] + ", " + shape[1] + ") = [");

         }else{
            System.out.println("Dados = [");
         }

         for(String linha[] : this.conteudo){
            for(int i = 0; i < linha.length; i++){
               System.out.print(espacamento + linha[i] + "\t");
            }
            System.out.println();
         }
      }
      
      System.out.println("]\n");
   }


   /**
    * Exibe uma visão geral das informações da coluna especificada.
    * <p>
    *    As informações incluem:
    * </p>
    * <ul>
    *    <li>Média;</li>
    *    <li>Mediana;</li>
    *    <li>Valor máximo;</li>
    *    <li>Valor mínimo;</li>
    *    <li>Moda;</li>
    * </ul>
    * @param idCol índice da coluna desejada.
    * @return buffer formatado contendo informações da coluna.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public String informacoesColuna(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      String espacamento = "   ";

      String buffer = "Coluna " + idCol + ": [\n";
      buffer += espacamento + "Média: \t" + media(idCol) + "\n";
      buffer += espacamento + "Mediana: \t" + mediana(idCol) + "\n";
      buffer += espacamento + "Máximo: \t" + maximo(idCol) + "\n";
      buffer += espacamento + "Mínimo: \t" + minimo(idCol) + "\n";
      buffer += espacamento + "Moda: \t" + moda(idCol) + "\n";
      buffer += espacamento + "Numéricos: \t" + (!contemNaoNumericos(idCol) ? "sim" : "não") + "\n";
      buffer += espacamento + "Ausentes: \t" + valoresAusentes(idCol) + "\n";
      buffer += "]\n";

      return buffer;
   }


   /**
    * Exibe algumas informações sobre o conjunto de dados no geral.
    * <p>
    *    As informações incluem:
    * </p>
    * <ul>
    *    <li>Formato dos dados;</li>
    *    <li>Existência de valores não numéricos;</li>
    *    <li>Quantidade de valores ausentes;</li>
    * </ul>
    * @return buffer formatado contendo as informações.
    */
   public String informacoes(){
      String espacamento = "    ";
      String formatacao = "\t\t";

      String buffer = "Informações dos Dados = [\n";

      if(this.estaVazio()){
         buffer += espacamento + "Conteúdo vazio.\n"; 
      
      }else{
         if(dadosSimetricos()){
            int[] shape = this.shape();
            buffer += espacamento + "Linhas: " + formatacao + shape[0] + "\n";
            buffer += espacamento + "Colunas: " + formatacao + shape[1] + "\n";
         
         }else{
            buffer += espacamento + "Tamanho inconsistente \n";
         }
      }
      buffer += "\n";


      boolean apenasNumericos = true;
      for(int i = 0; i < this.conteudo.get(0).length; i++){
         if(contemNaoNumericos(i)){
            apenasNumericos = false;
            break;
         }
      }
      buffer += espacamento + "Numéricos:" + formatacao + ((apenasNumericos) ? "sim" : "não") + "\n";

      boolean temValoresAusentes = false;
      for(int i = 0; i < this.conteudo.get(0).length; i++){
         int valoresAusentes = valoresAusentes(i);
         if (valoresAusentes > 0) {
            buffer += espacamento + "Ausentes coluna " + i + ": \t" + valoresAusentes + "\n";
            temValoresAusentes = true;
         }
      }
  
      if(!temValoresAusentes){
         buffer += espacamento + "Valores Ausentes: \tnão\n";
      }
      
      buffer += "]\n";

      return buffer;
   }


   /**
    * Verifica se a coluna indicada possui algum valor que não possa
    * ser convertido para um valor numérico.
    * @param idCol índice da coluna desejada.
    * @return verdadeiro caso a coluna possua valores que não possam ser convertidos, falso caso contrário.
    */
   private boolean contemNaoNumericos(int idCol){
      if (idCol < 0 || idCol >= this.conteudo.get(0).length) {
          throw new IllegalArgumentException("Índice fornecido inválido.");
      }
  
      for (String[] linha : this.conteudo) {
         String valor = linha[idCol];
         try{
            Double.parseDouble(valor);
         
         }catch(NumberFormatException e){
            return true;
         }
      }
  
      return false;
   }


   /**
    * Verifica a quantidade de valores ausentes presentes na coluna especificada.
    * <p>
    *    São considerados dados ausentes quaisquer valores que sejam {@code vazio},
    *    {@code em branco} ou {@code com "?"}
    * </p>
    * @param idCol índice da coluna desejada.
    * @return quantidade de valores considerados ausentes.
    */
   public int valoresAusentes(int idCol){
      if(idCol < 0 || idCol >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }
  
      int contador = 0;
      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         if(valor.equals("?") || valor.trim().isEmpty()){
            contador++;
         }
      }
  
      return contador;
  }
 

   /**
    * Verifica se o conteúdo dos dados é simetrico. A simetria leva em conta 
    * se todas as colunas têm o mesmo tamanho.
    * <p>
    *    A simetria também leva em conta se o conteúdo dos dados possui elementos, 
    *    caso o tamanho seja zero será considerada como não simétrica.
    * <p>
    * @param dados conjunto de dados.
    * @return true caso os dados sejam simétricos, false caso contrário.
    * @throws IllegalArgumentException se o conteúdo dos dados for nulo.
    */
   public boolean dadosSimetricos(){
      ArrayList<String[]> lista = this.conteudo();

      if(lista == null) throw new IllegalArgumentException("O conteúdo dos dados é nulo.");
      
      //lista sem dados é considerada como não simétrica
      if(lista.size() == 0) return false;

      int colunas = lista.get(0).length;// tamanho base
      for(int i = 0; i < lista.size(); i++){
         if(lista.get(i).length != colunas) return false;
      }

      return true;
   }


   /**
    * Clona o conteúdo em uma nova estrutura, devolvendo um novo objeto
    * de {@code Dados} com o mesmo conteúdo.
    * @return novo objeto do tipo {@code Dados} com a cópia do conteúdo.
    */
   public Dados clonar(){
      Dados cloneDados = new Dados();
      ArrayList<String[]> cloneConteudo = new ArrayList<>();

      for(String[] linha : this.conteudo){
         cloneConteudo.add(linha);
      }
      cloneDados.atribuir(cloneConteudo);

      return cloneDados;
   }
}
