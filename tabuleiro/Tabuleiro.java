package tabuleiro;
import Jogador.Jogador;
public class Tabuleiro {

		 private Jogador[] jogadores;

		   public Tabuleiro(Jogador [] jogadores) {
			   this.jogadores = jogadores;
		   }
		   
		   public void mostrarTabuleiro() {
				
		    	for(int i = 1; i <= 40; i++) {
		    		StringBuilder cores = new StringBuilder();
		    				
					for( Jogador j : jogadores ) {
						if( j != null && j.getPosicao() == i) {
							if (cores.length() > 0) cores.append(", ");
			                cores.append(j.getCor());
						 }
					 }
					if (cores.length() > 0) 
						System.out.printf(" [ %-12s ] = = =  ", cores.toString());
					   else {
						   if(i==10||i==25||i==38||i==13||i==5||i==15||i==30||i==17||i==27||i==20||i==35)
							   System.out.printf(" *[ %-12d ]* = = =  ", i);
						   else	
						   System.out.printf("  [ %-12d ]  = = =  ", i);
					   }   
						   
					if (i % 4 == 0) 
						System.out.println("\n");	
			     }	
		    	System.out.print("-casas 10,25,38: casa não joga próxima rodada!");
		    	System.out.print("\n-casa 13: casa surpresa!");
		    	System.out.print("\n-casas 5,15,30: casas da sorte!");
		    	System.out.print("\n-casas 17,27: casa voltar ao início!");
		    	System.out.print("\n-casas 20,35: casas mágicas!");
		    }
}


