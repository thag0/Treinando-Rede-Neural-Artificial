package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;


/**
 * Classe genérica para implementações de otimizadores do treino da rede neural.
 * <p>
 *      Para adicionar um novo otimizador, é preciso extender a classe Otimizador e implementar
 *      o método {@code atualizar()}.
 * </p>
 */
public abstract class Otimizador{

   /**
    * Treina a rede neural de acordo com o otimizador configurado.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param taxaAprendizagem valor de taxa de aprendizagem da Rede Neural.
    * @param momentum valor de taxa de momentum da Rede Neural.
    */
    public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){
        throw new java.lang.UnsupportedOperationException("Método de atualização do otimizador não foi implementado.");
    }
}
