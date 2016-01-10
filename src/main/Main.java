package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;

class Main {
    
    public static class Puzzle implements Comparable<Puzzle>{
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
            
            this.custoTotal = custoHeuristica + altura;
            this.hash = this.toHash();
            this.pai = "Primeiro";
        }
        
        public Puzzle(int[][] tab, int altura, String pai){
            this.tabuleiro = tab;
            this.altura = altura;
            this.posicaoZero = this.procuraZero();
            this.custoHeuristica = this.calculaHeuristica3();
            
            this.custoTotal = custoHeuristica + altura;
            
            this.hash = this.toHash();
            this.pai = pai;
            
        } 
        
        private int calculaHeuristica1(){
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

            for(int l = 1; l < 16; l++){
                if(vetor[l] != vetor[l-1] + 1){
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
                    retorno += dist;
                    
                }
            }
            
            return retorno;
        }
        
        private int calculaHeuristica4(){
            int h1 = this.calculaHeuristica1();
            int h2 = this.calculaHeuristica2();
            int h3 = this.calculaHeuristica3();
            
            double aux = (0.03*h1 + 0.02*h2 + 0.95*h3);
            int retorno;
            aux = Math.ceil(aux);
            retorno = (int) aux;
            
            return retorno;
            
        }
        
        private int calculaHeuristica5(){
            int h1 = this.calculaHeuristica1();
            int h2 = this.calculaHeuristica2();
            int h3 = this.calculaHeuristica3();
            
            int aux = Math.max(h1, h2);
            int retorno = Math.max(aux, h3);
            
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
            System.out.println(this.hash);
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
                    retorno = retorno + "-";
                    retorno = retorno + this.tabuleiro[i][j];
                    
                }
            }
            return retorno;
        }
        
         @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.hash);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Puzzle other = (Puzzle) obj;
            if (!Objects.equals(this.hash, other.hash)) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(Puzzle p){
            if (this.hashCode() == p.hashCode()){
                return 0;
            }
            int f1 = this.custoHeuristica + this.altura;
            int f2 = p.custoHeuristica + p.altura;
            if(f1 == f2){
                return -1;
            }
            return f1-f2;
        }
        
        public ArrayList<Puzzle> geraSucessor(){
            ArrayList<Puzzle> listaSucessor = new ArrayList<>();
            int auxTroca;
            int linha = this.posicaoZero[0];
            int coluna = this.posicaoZero[1];
            int[][] formato1 = new int[4][4];
            for(int i = 0; i < 4; i++){
                System.arraycopy(this.tabuleiro[i], 0, formato1[i], 0, 4);
            }
            if(coluna < 3){
                int[][] formatoNovo = new int[4][4];
                for(int i = 0; i < 4; i++){
                    System.arraycopy(formato1[i], 0, formatoNovo[i], 0, 4);
                }
                auxTroca = formatoNovo[linha][coluna];
                formatoNovo[linha][coluna] = formatoNovo[linha][coluna+1];
                formatoNovo[linha][coluna+1] = auxTroca;
                Puzzle novo1 = new Puzzle(formatoNovo, this.altura+1, this.hash);
                listaSucessor.add(novo1);

            }
            if(coluna > 0){
                int[][] formatoNovo = new int[4][4];
                for(int i = 0; i < 4; i++){
                    System.arraycopy(formato1[i], 0, formatoNovo[i], 0, 4);
                }
                auxTroca = formatoNovo[linha][coluna];
                formatoNovo[linha][coluna] = formatoNovo[linha][coluna-1];
                formatoNovo[linha][coluna-1] = auxTroca;
                Puzzle novo2 = new Puzzle(formatoNovo, this.altura+1, this.hash);
                listaSucessor.add(novo2);
            }
            if(linha > 0){
                int[][] formatoNovo = new int[4][4];
                for(int i = 0; i < 4; i++){
                    System.arraycopy(formato1[i], 0, formatoNovo[i], 0, 4);
                }
                auxTroca = formatoNovo[linha][coluna];
                formatoNovo[linha][coluna] = formatoNovo[linha-1][coluna];
                formatoNovo[linha-1][coluna] = auxTroca;
                Puzzle novo3 = new Puzzle(formatoNovo, this.altura+1, this.hash);
                listaSucessor.add(novo3);
            }
            if(linha < 3){
                int[][] formatoNovo = new int[4][4];
                for(int i = 0; i < 4; i++){
                    System.arraycopy(formato1[i], 0, formatoNovo[i], 0, 4);
                }
                auxTroca = formatoNovo[linha][coluna];
                formatoNovo[linha][coluna] = formatoNovo[linha+1][coluna];
                formatoNovo[linha+1][coluna] = auxTroca;
                Puzzle novo4 = new Puzzle(formatoNovo, this.altura+1, this.hash);
                listaSucessor.add(novo4);
            }
            return listaSucessor;
        }
        
    }
    
    
    public static int algoritmoAEstrela(Puzzle estadoInicial) throws InterruptedException{
        int[][] estadoFinal = new int[][] {{1, 2, 3, 4},{12, 13, 14, 5},{11, 0, 15, 6}, {10, 9, 8, 7}};
        
        //TreeMap<String, Puzzle> listaAberto = new TreeMap<>();
        TreeSet<Puzzle> listaAberto = new TreeSet<>();
        HashMap<String, Integer> hashG = new HashMap<>(); 
        HashMap<String, Puzzle> listaFechado = new HashMap<>();
        
        listaAberto.add(estadoInicial);
        hashG.put(estadoInicial.hash, estadoInicial.altura);
        
        //listaAberto.put(estadoInicial.hash, estadoInicial);
        int it = 0;
        while(!listaAberto.isEmpty()){
            it++;
            //int tam = listaAberto.size();
            /*if(tam > 4){
                tam = 4;
            }
            //System.out.println("Iteracao" +it+ "tamanho: " + tam);
            if(listaAberto.size() > 25000){
                Puzzle r = listaAberto.pollLast();
                hashG.remove(r.hash);
                //tam--;
            }*/
            
            
            //System.out.println(it);
            //Puzzle v = listaAberto.pollFirst();
            Puzzle v = listaAberto.first();
            listaAberto.remove(v);
            hashG.remove(v.hash);
            //String str = listaAberto.firstKey();
            //Puzzle v = listaAberto.get(str);
            //listaAberto.remove(str);

            listaFechado.put(v.hash, v);
            
            ArrayList<Puzzle> vSucessor = new ArrayList<>();
            if(v.verifica()){
                System.out.println("it: " + it);
                //Thread.sleep(10000);
                return v.custoTotal;
            } else {
               vSucessor = v.geraSucessor();
               if(vSucessor.isEmpty()){
                    continue;
               }
               
               for(Puzzle m : vSucessor){
                    if(listaFechado.containsKey(m.hash)){
                        continue;
                    }

                    int antigo_g = hashG.containsKey(m.hash) ? hashG.get(m.hash) : Integer.MAX_VALUE;
                    if (m.altura >= antigo_g){
                        continue;
                    }
                    
                    listaAberto.remove(m);
                    listaAberto.add(m);
                    hashG.put(m.hash, m.altura);
                   /*if((!listaAberto.contains(m) && !listaFechado.containsKey(m.hash)) || ((listaAberto.contains(m) || listaFechado.containsKey(m.hash)) && m.altura <= v.altura)){
                        listaAberto.add(m);
                        if(listaFechado.containsKey(m.hash)){
                            listaFechado.remove(m.hash);
                        }
                   }*/
                }
            }
           
        }
        
        return -1;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        
        //Tempo inicial
        long tempoIn = System.currentTimeMillis();
        
        //==============================================================//
        //===========================LEITURA============================//
        String nomeArquivo = "Caso7.txt";
        FileInputStream stream = new FileInputStream(nomeArquivo);
        InputStreamReader reader = new InputStreamReader(stream);
        //InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(reader);
        
        int[][] entrada = new int[4][4];
        String linha  = br.readLine();
        int[] vetor;
        String sublinha[];
        sublinha = linha.split(" ");
        int k = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                String texto = sublinha[k];
                while(texto.equals("")){
                    k++;
                    texto = sublinha[k];
                }
                entrada[i][j] = Integer.parseInt(texto);
                k++;
            }
        }
        
        
        //=============================================================//
        Puzzle estadoInicial = new Puzzle(entrada);
        System.out.println(algoritmoAEstrela(estadoInicial));
        
        long tempoTotal = System.currentTimeMillis() - tempoIn;
        
        
        System.out.println("Tempo gasto: " + tempoTotal);
        
    }



}
