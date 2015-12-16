package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Main {
    
    public static Puzzle buscaMenor(ArrayList<Puzzle> lista){
        int compara = Integer.MAX_VALUE;
        Puzzle puzzleRetorno = null;
        for(Puzzle puzzle : lista){
            if(puzzle.custoTotal < compara){
                compara = puzzle.custoTotal;
                puzzleRetorno = puzzle;
            }
        }
        return puzzleRetorno;
    }
    
    public static class Puzzle{
        int[][] tabuleiro = new int[4][4];
        int altura;             //G(x)
        int custoHeuristica;    //H'(X)
        int custoTotal;         //F(X)
        int[] posicaoZero;
        String hash;
        String pai;
        
        public Puzzle(int[][] tab){
            this.tabuleiro = tab;
            this.altura = 0;
            this.posicaoZero = this.procuraZero();
            this.custoHeuristica = this.calculaHeuristica3();
            
            //System.out.println(this.custoHeuristica);
            this.custoTotal = custoHeuristica + altura;
            //this.mostraFormato();
            this.hash = this.toHash();
            this.pai = "Primeiro";
        }
        
        public Puzzle(int[][] tab, int altura, String pai){
            this.tabuleiro = tab;
            this.altura = altura;
            this.posicaoZero = this.procuraZero();
            this.custoHeuristica = this.calculaHeuristica3();
            
            //System.out.println(this.custoHeuristica);
            this.custoTotal = custoHeuristica + altura;
            //this.mostraFormato();
            this.hash = this.toHash();
            this.pai = pai;
        }
        
        private int calculaHeuristica(){
            int[][] estadoFinal = new int[][] {{1, 2, 3, 4},{12, 13, 14, 5},{11, 0, 15, 6}, {10, 9, 8, 7}};
            int soma = 0;
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if(this.tabuleiro[i][j] != estadoFinal[i][j]){
                        soma += 1;
                    }
                }
            }
            return soma;
        }
        
        private int calculaHeuristica2(){
            int retorno = 0;
            int[] vetor = new int[16];
            vetor[0] = this.tabuleiro[0][0];
            vetor[1] = this.tabuleiro[0][1];
            vetor[2] = this.tabuleiro[0][2];
            vetor[3] = this.tabuleiro[0][3];
            vetor[4] = this.tabuleiro[1][3];
            vetor[5] = this.tabuleiro[2][3];
            vetor[6] = this.tabuleiro[3][3];
            vetor[7] = this.tabuleiro[3][2];
            vetor[8] = this.tabuleiro[3][1];
            vetor[9] = this.tabuleiro[3][0];
            vetor[10] = this.tabuleiro[2][0];
            vetor[11] = this.tabuleiro[1][0];
            vetor[12] = this.tabuleiro[1][1];
            vetor[13] = this.tabuleiro[1][2];
            vetor[14] = this.tabuleiro[2][2];
            vetor[15] = this.tabuleiro[2][1];

            //System.out.print(vetor[0] + " ");
            for(int l = 1; l < 16; l++){
                //System.out.print(vetor[l] + " ");
                if(vetor[l] != vetor[l-1] + 1){
                    //System.out.print(vetor[l] + " ");
                    retorno++;
                }
            }
            return retorno - 1;
        }
        
        private int calculaHeuristica3(){
            int retorno = 0;
            int[][] matriz = new int[16][2];
            
            matriz[0][0] = 2;
            matriz[0][1] = 1;
            
            matriz[1][0] = 0;
            matriz[1][1] = 0;
            
            matriz[2][0] = 0;
            matriz[2][1] = 1;
            
            matriz[3][0] = 0;
            matriz[3][1] = 2;
            
            matriz[4][0] = 0;
            matriz[4][1] = 3;
            
            matriz[5][0] = 1;
            matriz[5][1] = 3;
            
            matriz[6][0] = 2;
            matriz[6][1] = 3;
            
            matriz[7][0] = 3;
            matriz[7][1] = 3;
            
            matriz[8][0] = 3;
            matriz[8][1] = 2;
            
            matriz[9][0] = 3;
            matriz[9][1] = 1;
            
            matriz[10][0] = 3;
            matriz[10][1] = 0;
            
            matriz[11][0] = 2;
            matriz[11][1] = 0;
            
            matriz[12][0] = 1;
            matriz[12][1] = 0;
            
            matriz[13][0] = 1;
            matriz[13][1] = 1;
            
            matriz[14][0] = 1;
            matriz[14][1] = 2;
            
            matriz[15][0] = 2;
            matriz[15][1] = 2;
            
            
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    int numero = this.tabuleiro[i][j];
                    int linha = matriz[numero][0];
                    int coluna = matriz[numero][1];
                    int dist = Math.abs(linha - i) + Math.abs(coluna - j);
                    //System.out.println(retorno);
                    retorno += dist;
                    
                }
            }
            
            return retorno;
        }
        
        private int[] procuraZero(){
            int[] retorno = new int[2];
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if(this.tabuleiro[i][j] == 0){
                        retorno[0] = i;
                        retorno[1] = j;
                    }
                }
            }
            return retorno;
        }
        
        public void mostraFormato(){
        //System.out.println("HASH = " + this.hash);
        //System.out.println("PAI = " + this.pai);
        System.out.println("Custo = " + this.altura + " -- Heuristica = " + this.custoHeuristica);
        System.out.println("CUSTO TOTAL = " + this.custoTotal);
        for(int i = 0; i < 4; i++){            
            for(int j = 0; j < 4; j++){
                System.out.print(this.tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
        
        public boolean verifica(){
        int[][] resposta = {{1, 2, 3, 4},{12, 13, 14, 5},{11, 0, 15, 6}, {10, 9, 8, 7}};
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(this.tabuleiro[i][j] != resposta[i][j]){
                    return false;
                }
            }
        }
        return true;
        }
        
        private String toHash(){
            String retorno = "";
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    retorno = retorno + this.tabuleiro[i][j];
                }
            }
            return retorno;
        }
        
        public ArrayList<Puzzle> geraSucessor(){
        ArrayList<Puzzle> listaSucessor = new ArrayList<>();
        int auxTroca;
        //Troca a col pela col+1
        int linha = this.posicaoZero[0];
        int coluna = this.posicaoZero[1];
        int[][] formato1 = new int[4][4];
        for(int i = 0; i < 4; i++){
            System.arraycopy(this.tabuleiro[i], 0, formato1[i], 0, 4);
        }
        //System.out.println(this.formato[linha][coluna]);
        if(coluna < 3){
            int[][] formatoNovo = new int[4][4];
            for(int i = 0; i < 4; i++){
                System.arraycopy(formato1[i], 0, formatoNovo[i], 0, 4);
            }
            auxTroca = formatoNovo[linha][coluna];
            formatoNovo[linha][coluna] = formatoNovo[linha][coluna+1];
            formatoNovo[linha][coluna+1] = auxTroca;
            Puzzle novo1 = new Puzzle(formatoNovo, this.altura+1, this.hash);
            //novo1.mostraFormato();
            listaSucessor.add(novo1);
            
        }
        //System.out.println(this.formato[linha][coluna]);
        if(coluna > 0){
            int[][] formatoNovo = new int[4][4];
            for(int i = 0; i < 4; i++){
                System.arraycopy(formato1[i], 0, formatoNovo[i], 0, 4);
            }
            auxTroca = formatoNovo[linha][coluna];
            formatoNovo[linha][coluna] = formatoNovo[linha][coluna-1];
            formatoNovo[linha][coluna-1] = auxTroca;
            Puzzle novo2 = new Puzzle(formatoNovo, this.altura+1, this.hash);
            //novo2.mostraFormato();
            listaSucessor.add(novo2);
        }
        //System.out.println(this.formato[linha][coluna]);
        if(linha > 0){
            int[][] formatoNovo = new int[4][4];
            for(int i = 0; i < 4; i++){
                System.arraycopy(formato1[i], 0, formatoNovo[i], 0, 4);
            }
            auxTroca = formatoNovo[linha][coluna];
            formatoNovo[linha][coluna] = formatoNovo[linha-1][coluna];
            formatoNovo[linha-1][coluna] = auxTroca;
            Puzzle novo3 = new Puzzle(formatoNovo, this.altura+1, this.hash);
            //novo3.mostraFormato();
            listaSucessor.add(novo3);
        }
        //System.out.println(this.formato[linha][coluna]);
        if(linha < 3){
            int[][] formatoNovo = new int[4][4];
            for(int i = 0; i < 4; i++){
                System.arraycopy(formato1[i], 0, formatoNovo[i], 0, 4);
            }
            auxTroca = formatoNovo[linha][coluna];
            formatoNovo[linha][coluna] = formatoNovo[linha+1][coluna];
            formatoNovo[linha+1][coluna] = auxTroca;
            Puzzle novo4 = new Puzzle(formatoNovo, this.altura+1, this.hash);
            //novo4.mostraFormato();
            listaSucessor.add(novo4);
        }
        return listaSucessor;
    }
        
    }
    
    
    public static int algoritmoAEstrela(Puzzle estadoInicial){
        int[][] estadoFinal = new int[][] {{1, 2, 3, 4},{12, 13, 14, 5},{11, 0, 15, 6}, {10, 9, 8, 7}};
        ArrayList<Puzzle> listaAberto = new ArrayList<>();
        ArrayList<Puzzle> listaFechado = new ArrayList<>();
        
        listaAberto.add(estadoInicial);
        int it = 0;
        while(!listaAberto.isEmpty()){
           //System.out.println(++it);
           Puzzle v = buscaMenor(listaAberto); 
           listaAberto.remove(v);
           listaFechado.add(v);
           ArrayList<Puzzle> vSucessor = new ArrayList<>();
           //v.mostraFormato();
           if(v.verifica()){
               return v.custoTotal;
           } else {
               vSucessor = v.geraSucessor();
               if(vSucessor.isEmpty()){
                   //return -1;
                   continue;
               }
               
               for(Puzzle m : vSucessor){
                   if((!listaAberto.contains(m) && !listaFechado.contains(m)) || ((listaAberto.contains(m) || listaFechado.contains(m)) && m.altura <= v.altura)){
                        listaAberto.add(m);
                        if(listaFechado.contains(m)){
                            //listaAberto.add(m);
                            listaFechado.remove(m);
                        }
                   }
               }
           }
           
        }
        
        return -1;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        //==============================================================//
        //===========================LEITURA============================//
        //String nomeArquivo = "teste.txt";
        //FileInputStream stream = new FileInputStream(nomeArquivo);
        //InputStreamReader reader = new InputStreamReader(stream);
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(reader);
        
        int[][] entrada = new int[4][4];
        String linha  = br.readLine();
        int[] vetor;
        String sublinha[];
        sublinha = linha.split(" ");
        int k = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                entrada[i][j] = Integer.parseInt(sublinha[k]);
                k++;
                //System.out.println(entrada[i][j]);
            }
        }
        
        
        //=============================================================//
        Puzzle estadoInicial = new Puzzle(entrada);
        System.out.println(algoritmoAEstrela(estadoInicial));
        //System.out.println(estadoInicial.custoHeuristica);        
        
        
        
    }



}
