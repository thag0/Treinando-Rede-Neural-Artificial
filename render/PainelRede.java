package render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import rna.Camada;
import rna.RedeNeural;

public class PainelRede extends JPanel{
   final int largura = 500;
   final int altura = 400;
   Graphics2D g2;
   
   public RedeNeural rede;

   //coordenadas de origem dos neuronios
   ArrayList<Coordenada> coordEntrada = new ArrayList<>();
   ArrayList<ArrayList<Coordenada>> coordOcultas = new ArrayList<>();//lista de lista de coordenadas
   ArrayList<Coordenada> coordSaida = new ArrayList<>();

   //auxilinar na inicialização
   Coordenada c = new Coordenada(0, 0, 0);
   //auxiliar no desenho das conexões
   Coordenada c1;
   Coordenada c2;

   //desenho
   int contador = 0;
   int contador2 = 0;
   int x0 = 0;//posição x base de desenho da rede
   int y0 = 0;//posição y base de desenho da rede
   int x = 0;
   int y = 0;
   int yCamadaEntrada = 0;
   int[] yCamadaOculta = new int[1];
   int yCamadaSaida = 0;
   int larguraDesenho = 26;
   int alturaDesenho = larguraDesenho;
   int espacoVerticalEntreNeuronio = 7;
   int espacoHorizontalEntreCamadas = (int)(larguraDesenho * 2.3);
   String texto = "";

   //informações
   int geracaoAtual = 0;
   double mediaPesos = 0;
   double melhorFitness = 0;
   double mediaFitness = 0;
   int geracoesStagnadas = 0;
   long redesQueGanharam = 0;
   int metodoEvolucao = 0;

   //cores
   int r = 150;
   int g = 180;
   int b = 210;
   Color corNeuronioAtivo = new Color(r, g, b);
   Color corNeuronioInativo = new Color((int)(r * 0.4), (int)(g * 0.4), (int)(b * 0.4));
   Color corBordaNeuronio = Color.BLACK;

   Color corConexaoAtiva = new Color((int)(r * 0.7), (int)(g * 0.7), (int)(b * 0.7));
   Color corConexaoInativa = new Color((int)(r * 0.25), (int)(g * 0.25), (int)(b * 0.25));

   Color corTexto = new Color(r, g, b);

   //auxiliares
   BasicStroke linhaDesenho = new BasicStroke(1.8f);
   File pastaRedes = new File("./melhores-redes/");


   public PainelRede(){
      setBackground(new Color(25, 25, 25));
      setPreferredSize(new Dimension(largura, altura));
      setFocusable(true);
      setDoubleBuffered(true);
      setEnabled(true);
      setVisible(true);

      int arq[] = {1, 1, 1};
      this.rede = new RedeNeural(arq);
      this.rede.compilar();
   }


   public void desenhar(RedeNeural rede){
      this.rede = rede;
      yCamadaOculta = new int[rede.obterQuantidadeOcultas()];

      Camada entrada = rede.obterCamadaEntrada();
      Camada saida = rede.obterCamadaSaida();

      //isso aqui com certeza pode ser melhorado

      int quantidadeCamadas = 1 + rede.obterQuantidadeOcultas() + 1;
      int tamanhoRede = (quantidadeCamadas * larguraDesenho) + (2*larguraDesenho/3);//quantidade de neuronios
      tamanhoRede += (espacoHorizontalEntreCamadas * (quantidadeCamadas-2));//quantidade de espaços entre as camadas
      x0 = (this.largura/2) - (tamanhoRede/2);

      //centralizar o desenho dos neuronios com base na altura da tela, no tamanho dos neuronios das camadas desenhadas
      //incluir o espaçamento estre os neurinios no calculo
      yCamadaEntrada = y0 + (altura/2) - (alturaDesenho * (entrada.obterQuantidadeNeuronios())) + (espacoVerticalEntreNeuronio * (entrada.obterQuantidadeNeuronios()+2));
      for(int i = 0; i < rede.obterQuantidadeOcultas(); i++){
         yCamadaOculta[i] = y0+(altura/2) - (alturaDesenho * (rede.obterCamadaOculta(i).obterQuantidadeNeuronios())) + (espacoVerticalEntreNeuronio * (rede.obterCamadaOculta(i).obterQuantidadeNeuronios()+2));
      }
      yCamadaSaida = y0 + (altura/2) - (alturaDesenho * (saida.obterQuantidadeNeuronios())) + (espacoVerticalEntreNeuronio * (saida.obterQuantidadeNeuronios()+1));

      repaint();
   }


   public void configurarRede(RedeNeural rede){
      this.rede = rede;
      this.yCamadaOculta = new int[rede.obterQuantidadeOcultas()];
   }

   
   @Override
   protected void paintComponent(Graphics g){
      super.paintComponent(g);
      g2 = (Graphics2D) g;

      g2.setFont(getFont().deriveFont(14.f));

      //desenho para calcular as cooredanas das conexões
      desenharCamadaEntrada(g2);    
      desenharCamadasOcultas(g2);
      desenharCamadaSaida(g2);
      
      //conexões entre os neuronios
      g2.setStroke(linhaDesenho);
      desenharConexoesEntrada(g2);
      desenharConexoesOcultas(g2);
      desenharConexoesSaida(g2);

      g2.dispose();
   }


   private void desenharConexoesEntrada(Graphics2D g2){
      //evitar muitas instanciações
      int i, j;
      int qNeuroniosOculta = rede.obterCamadaOculta(0).obterQuantidadeNeuronios();
      qNeuroniosOculta -= (rede.obterCamadaOculta(0).temBias()) ? 1 : 0;

      //entrada -> primeira oculta
      for(i = 0; i < coordEntrada.size(); i++){//percorrer neuronios da entrada
         for(j = 0; j < qNeuroniosOculta; j++){//percorrer a primeira oculta, excluir o bias
            c1 = coordEntrada.get(i);
            c2 = coordOcultas.get(0).get(j);

            if(c1.valor > 0 && c2.valor > 0) g2.setColor(corConexaoAtiva);
            else g2.setColor(corConexaoInativa);
            g2.drawLine(c1.x, c1.y, c2.x-larguraDesenho, c2.y);
         }
      }
      //reforçar as linhas ativas
      for(i = 0; i < coordEntrada.size(); i++){
         for(j = 0; j < coordOcultas.get(0).size()-1; j++){
            c1 = coordEntrada.get(i);
            c2 = coordOcultas.get(0).get(j);

            if(c1.valor > 0 && c2.valor > 0){
               g2.setColor(corConexaoAtiva);
               g2.drawLine(c1.x, c1.y, c2.x-larguraDesenho, c2.y);
            }
         }
      }
   }


   private void desenharConexoesOcultas(Graphics2D g2){
      int i, j, k;
      int qNeuroniosProxima;
      //primeira oculta -> ultima oculta
      for(i = 0; i < coordOcultas.size()-1; i++){//percorrer ocultas

         for(j = 0; j < coordOcultas.get(i).size(); j++){//percorrer neuronios da camada oculta atual

            //evitar exluir conexões quando nao tem bias
            if(rede.obterCamadaOculta(i+1).temBias()) qNeuroniosProxima = rede.obterCamadaOculta(i+1).obterQuantidadeNeuronios()-1;
            else qNeuroniosProxima = rede.obterCamadaOculta(i+1).obterQuantidadeNeuronios();
            for(k = 0; k < qNeuroniosProxima; k++){//percorrer neuronios da camada oculta na frente, excluir o bias
               c1 = coordOcultas.get(i).get(j);
               c2 = coordOcultas.get(i+1).get(k);

               if(c1.valor > 0 && c2.valor > 0) g2.setColor(corConexaoAtiva);
               else g2.setColor(corConexaoInativa);
               g2.drawLine(c1.x, c1.y, c2.x-larguraDesenho, c2.y);
            }
         }
      }
      //reforçar as linhas ativas
      for(i = 0; i < coordOcultas.size()-1; i++){
         for(j = 0; j < coordOcultas.get(i).size(); j++){
            for(k = 0; k < coordOcultas.get(i+1).size()-1; k++){
               c1 = coordOcultas.get(i).get(j);
               c2 = coordOcultas.get(i+1).get(k);

               if(c1.valor > 0 && c2.valor > 0){
                  g2.setColor(corConexaoAtiva);
                  g2.drawLine(c1.x, c1.y, c2.x-larguraDesenho, c2.y);
               }
            }
         }
      }      
   }


   private void desenharConexoesSaida(Graphics2D g2){
      //ultima oculta -> saída
      int i, j;
      for(i = 0; i < coordOcultas.get(coordOcultas.size()-1).size(); i++){//percorrer neuronios da ultima oculta
         for(j = 0; j < coordSaida.size(); j++){//percorrer neuronios da saida
            c1 = coordOcultas.get(coordOcultas.size()-1).get(i);
            c2 = coordSaida.get(j);

            if(c1.valor > 0 && c2.valor > 0) g2.setColor(corConexaoAtiva);
            else g2.setColor(corConexaoInativa);
            g2.drawLine(c1.x, c1.y, c2.x-larguraDesenho, c2.y);
         }
      }
      //reforçar as linhas ativas
      for(i = 0; i < coordOcultas.get(coordOcultas.size()-1).size(); i++){
         for(j = 0; j < coordSaida.size(); j++){
            c1 = coordOcultas.get(coordOcultas.size()-1).get(i);
            c2 = coordSaida.get(j);

            if(c1.valor > 0 && c2.valor > 0){
               g2.setColor(corConexaoAtiva);
               g2.drawLine(c1.x, c1.y, c2.x-larguraDesenho, c2.y);
            }
         }
      }
   }
   
   
   private void desenharCamadaEntrada(Graphics2D g2){
      x = x0;
      y = yCamadaEntrada;

      Camada entrada = rede.obterCamadaEntrada();
      
      coordEntrada.clear();
      for(contador = 0; contador < entrada.obterQuantidadeNeuronios(); contador++){
         
         if(entrada.neuronio(contador).saida > 0) g2.setColor(corNeuronioAtivo);
         else g2.setColor(corNeuronioInativo);
         
         int xTexto = -30;
         int yTexto = 18;
         if(entrada.temBias() && (contador < entrada.obterQuantidadeNeuronios()-1)) g2.drawString(("e" + contador), (x+xTexto), (y+yTexto));
         if((!entrada.temBias())) g2.drawString(("e" + contador), (x+xTexto), (y+yTexto));

         //salvar coordenada do centro do desenho do neuronio
         coordEntrada.add(new Coordenada(x+larguraDesenho, y+(larguraDesenho/2), entrada.neuronio(contador).saida));

         if(entrada.temBias() && contador == entrada.obterQuantidadeNeuronios()-1) desenharNeuronioBias(g2, x, y);
         else desenharNeuronio(g2, x, y, entrada.neuronio(contador).saida);

         y += larguraDesenho + espacoVerticalEntreNeuronio;
      }
      
   }
   
   
   private void desenharCamadasOcultas(Graphics2D g2){
      x += espacoHorizontalEntreCamadas;
      
      coordOcultas.clear();
      for(contador = 0; contador < rede.obterQuantidadeOcultas(); contador++){//percorrer ocultas
         y = yCamadaOculta[contador];
         
         // coordOcultas.get(contador).clear();//limpar lista da camada atual pra não estourar a memória
         coordOcultas.add(new ArrayList<>());
         for(contador2 = 0; contador2 < rede.obterCamadaOculta(contador).obterQuantidadeNeuronios(); contador2++){//percorrer neuronios de uma oculta
            
            //salvar coordenada do centro do desenho do neuronio
            coordOcultas.get(contador).add(new Coordenada(x+larguraDesenho, y+(larguraDesenho/2), rede.obterCamadaOculta(contador).neuronio(contador2).saida));
            
            if(rede.obterCamadaOculta(contador).temBias() && contador2 ==  rede.obterCamadaOculta(contador).obterQuantidadeNeuronios()-1) desenharNeuronioBias(g2, x, y);
            else desenharNeuronio(g2, x, y, rede.obterCamadaOculta(contador).neuronio(contador2).saida);

            y += larguraDesenho + espacoVerticalEntreNeuronio;
         }
         x += espacoHorizontalEntreCamadas;
      }
   }

   
   private void desenharCamadaSaida(Graphics2D g2){
      y = yCamadaSaida;
      
      Camada saida = rede.obterCamadaSaida();
      coordSaida.clear();
      for(contador = 0; contador < saida.obterQuantidadeNeuronios(); contador++){
         coordSaida.add(new Coordenada(x+larguraDesenho, y+(larguraDesenho/2), saida.neuronio(contador).saida));
         desenharNeuronio(g2, x, y, saida.neuronio(contador).saida);
         
         g2.drawString(("s" + contador + " = " + (float)saida.neuronio(contador).saida), (x+40), (y+16));

         y += larguraDesenho + espacoVerticalEntreNeuronio; 
      }
   }
   
   
   private void desenharNeuronio(Graphics2D g2, int x, int y, double valorSaida){
      g2.setColor(corBordaNeuronio);
      g2.fillOval(x, y, larguraDesenho, alturaDesenho);

      int borda = 3;

      if(valorSaida > 0) g2.setColor(corNeuronioAtivo);
      else g2.setColor(corNeuronioInativo);
      g2.fillOval(x+borda, y+borda, larguraDesenho-(borda*2), alturaDesenho-(borda*2));  
   }
   
   
   private void desenharNeuronioBias(Graphics2D g2, int x, int y){
      g2.setColor(corBordaNeuronio);
      g2.fillOval(x, y, larguraDesenho, alturaDesenho);

      int borda = 3;

      g2.setColor(corNeuronioAtivo);
      g2.fillOval(x+borda, y+borda, larguraDesenho-(borda*2), alturaDesenho-(borda*2));

      g2.setColor(Color.black);

      //levar em consideração o tamanho da fonte
      int xTexto = x + (g2.getFont().getSize()/2);
      int yTexto = y + g2.getFont().getSize() + 3;
      
      g2.drawString("B", xTexto, yTexto);
      g2.drawString("B", xTexto, yTexto+1);//deixar o desenho mais espesso
   }
}