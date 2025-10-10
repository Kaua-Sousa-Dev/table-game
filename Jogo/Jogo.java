package Jogo;

// Importações necessárias
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Importa as classes de Jogador
import Jogador.Jogador;
import Jogador.JogadorSortudo;
import Jogador.JogadorAzarado;
import Jogador.JogadorNormal;

public class Jogo {
    private Jogador[] jogadores = new Jogador[6];
    private boolean modoDebug = false;
    private Scanner sc = new Scanner(System.in);
    private Random random = new Random();
    private boolean acabou = false;

    public Jogo() {}
    public void iniciar() {
        System.out.println("===== JOGO DE TABULEIRO =====");

        int qtd;
        do {
            System.out.print("Quantos jogadores (2–6)? ");
            qtd = sc.nextInt();
            sc.nextLine();
            if (qtd < 2 || qtd > 6) {
                System.out.println("Número inválido. Digite um valor entre 2 e 6.");
            }
        } while (qtd < 2 || qtd > 6);

        for (int i = 0; i < qtd; i++) {
            System.out.println("\n--- Jogador " + (i + 1) + " ---");
            System.out.print("Nome: ");
            String nome = sc.nextLine();
            System.out.print("Cor: ");
            String cor = sc.nextLine();

            int tipoEscolhido = 0;
            do {
                System.out.println("1 - Sortudo");
                System.out.println("2 - Azarado");
                System.out.println("3 - Normal");
                System.out.print("Tipo: ");
                tipoEscolhido = sc.nextInt();
                sc.nextLine();
            } while (tipoEscolhido < 1 || tipoEscolhido > 3);

            if (tipoEscolhido == 1) {
                jogadores[i] = new JogadorSortudo(nome, cor);
            } else if (tipoEscolhido == 2) {
                jogadores[i] = new JogadorAzarado(nome, cor);
            } else {
                jogadores[i] = new JogadorNormal(nome, cor);
            }
        }

        System.out.print("\nAtivar modo debug? (s/n): ");
        String resp = sc.nextLine();
        if (resp.equalsIgnoreCase("s")) {
            modoDebug = true;
        }

        System.out.println("\nJogadores criados!");
        mostrarPosicoes();

        while (!acabou) {
            jogarRodada();
        }

        mostrarResultadoFinal();
    }

    private void jogarRodada() {
        // Só aparece no modo de jogo normal.
        if (!modoDebug) {
            System.out.println("\n Pressione ENTER para iniciar a próxima rodada...");
            sc.nextLine();
        }

        System.out.println("\n=============== INICIANDO NOVA RODADA ===============");
        pausar(1500); // Uma pausa inicial para o título da rodada.

        for (int i = 0; i < jogadores.length; i++) {
            // Se o jogo acabou no meio da rodada, interrompe o loop.
            if (acabou) {
                break;
            }

            Jogador j = jogadores[i];
            if (j == null) continue;

            if (j.devePularRodada()) {
                System.out.println("\n" + j.getNome() + " vai pular a rodada!");
                j.setPularRodada(false);
                pausar(2500); // Pausa para ler a mensagem
                continue;
            }

            System.out.println("\n--- É a vez de " + j.getNome() + " (" + j.getCor() + ") ---");
            System.out.println("Posição atual: " + j.getPosicao());
            pausar(1500); // Pausa antes da ação

            if (modoDebug) {
                System.out.print(">> MODO DEBUG: Digite a casa de destino para " + j.getNome() + ": ");
                int destino = sc.nextInt();
                sc.nextLine();
                j.setPosicao(destino);
                System.out.println(j.getNome() + " foi para a casa " + destino);
            } else {
                int soma = j.jogarDados();
                System.out.println(j.getNome() + " andou " + soma + " casas!");
                j.mover(soma);
            }

            j.contarJogada();
            System.out.println("Nova posição: " + j.getPosicao());
            pausar(1500); // Pausa para ver a nova posição

            aplicarRegraEspecial(i);
            j = jogadores[i]; // Atualiza a referência do jogador após possíveis mudanças

            if (j.getPosicao() >= 40) {
                System.out.println("\n" + j.getNome() + " venceu o jogo!");
                acabou = true;
                break; // Sai do loop da rodada
            }

            if (!modoDebug && j.tirouDuplo()) {
                System.out.println("Tirou números iguais! Joga novamente!");
                i--;
            }


            System.out.println("-------------------------------------------------");
            pausar(3000); // Pausa final da vez do jogador
        }

        if (!acabou) {
            mostrarPosicoes();
        }
    }

    private void pausar(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private void aplicarRegraEspecial(int indiceJogador) {
        Jogador jogador = jogadores[indiceJogador]; // Pega o jogador pelo índice
        int pos = jogador.getPosicao();

        switch (pos) {
            case 10: case 25: case 38:
                System.out.println(">> REGRA ESPECIAL: " + jogador.getNome() + " caiu na casa 'Perca a Vez' e não jogará na próxima rodada.");
                jogador.setPularRodada(true);
                break;
            case 13:
                System.out.println(">> REGRA ESPECIAL: " + jogador.getNome() + " caiu na Casa Surpresa! Uma carta é virada...");
                pausar(1500);
                mudarTipoJogador(indiceJogador);
                break;
            case 5: case 15: case 30:
                System.out.println(">> REGRA ESPECIAL: " + jogador.getNome() + " caiu na Casa da Sorte!");
                if (!jogador.getSorte().equalsIgnoreCase("azarado")) {
                    System.out.println("   Por não ser Azarado, andou +3 casas!");
                    jogador.mover(3);
                    aplicarRegraEspecial(indiceJogador); // Recursão usa o índice
                } else {
                    System.out.println("   Por ser Azarado, nada acontece.");
                }
                break;
            case 17: case 27:
                System.out.println(">> REGRA ESPECIAL: " + jogador.getNome() + " caiu na casa 'Mandar para o Início'!");
                escolherJogadorParaInicio(jogador);
                break;
            case 20: case 35:
                System.out.println(">> REGRA ESPECIAL: " + jogador.getNome() + " caiu na Casa Mágica e vai trocar de lugar com o último!");
                trocarComUltimoColocado(jogador);
                break;
        }
    }

    private void mudarTipoJogador(int indiceJogador) {
        Jogador jogadorAtual = jogadores[indiceJogador];
        System.out.println("   O tipo atual de " + jogadorAtual.getNome() + " é: " + jogadorAtual.getSorte());

        // Cria uma lista de tipos possíveis para sortear
        List<String> tiposPossiveis = new ArrayList<>();
        tiposPossiveis.add("sortudo");
        tiposPossiveis.add("azarado");
        tiposPossiveis.add("normal");

        // Remove o tipo atual da lista para garantir que o tipo mude
        tiposPossiveis.remove(jogadorAtual.getSorte());

        // Sorteia um novo tipo entre os restantes
        String novoTipo = tiposPossiveis.get(random.nextInt(tiposPossiveis.size()));

        System.out.println("   A carta revela que seu novo tipo é... " + novoTipo.toUpperCase() + "!");
        pausar(2000);

        // Substitui o jogador no array por um novo do tipo sorteado, usando os construtores de cópia
        switch (novoTipo) {
            case "sortudo":
                jogadores[indiceJogador] = new JogadorSortudo(jogadorAtual);
                break;
            case "azarado":
                jogadores[indiceJogador] = new JogadorAzarado(jogadorAtual);
                break;
            case "normal":
                jogadores[indiceJogador] = new JogadorNormal(jogadorAtual);
                break;
        }
    }

    private void escolherJogadorParaInicio(Jogador jogadorAtual) {
        List<Jogador> outrosJogadores = new ArrayList<>();
        for (Jogador j : jogadores) {
            if (j != null && !j.equals(jogadorAtual)) {
                outrosJogadores.add(j);
            }
        }

        if (outrosJogadores.isEmpty()) {
            System.out.println("Não há outros jogadores para enviar ao início.");
            return;
        }

        System.out.println("Escolha o número do jogador para enviar ao início:");
        for (int i = 0; i < outrosJogadores.size(); i++) {
            System.out.println((i + 1) + ": " + outrosJogadores.get(i).getNome());
        }

        int escolha = sc.nextInt() - 1;
        sc.nextLine();

        if (escolha >= 0 && escolha < outrosJogadores.size()) {
            Jogador escolhido = outrosJogadores.get(escolha);
            System.out.println(">> " + escolhido.getNome() + " foi enviado para o início! <<");
            escolhido.setPosicao(0);
        } else {
            System.out.println("Escolha inválida. Ninguém foi movido.");
        }
    }

    private void trocarComUltimoColocado(Jogador jogadorAtual) {
        Jogador ultimo = null;
        int contagemJogadores = 0;
        for (Jogador j : jogadores) {
            if (j != null) {
                contagemJogadores++;
                if (ultimo == null || j.getPosicao() < ultimo.getPosicao()) {
                    ultimo = j;
                }
            }
        }
        if (contagemJogadores < 2 || ultimo == null) return;

        if (jogadorAtual.equals(ultimo)) {
            System.out.println(jogadorAtual.getNome() + " já é o último, então nada acontece.");
        } else {
            System.out.println(jogadorAtual.getNome() + " troca de lugar com " + ultimo.getNome() + "!");
            int posAtual = jogadorAtual.getPosicao();
            jogadorAtual.setPosicao(ultimo.getPosicao());
            ultimo.setPosicao(posAtual);
        }
    }

    private void mostrarPosicoes() {
        System.out.println("\nPosições atuais:");
        for (Jogador j : jogadores) {
            if (j != null) j.mostrarPosicao();
        }
    }

    private void mostrarResultadoFinal() {
        System.out.println("\n=== RESULTADO FINAL ===");
        for (Jogador j : jogadores) {
            if (j != null) {
                System.out.println(j.getNome() + " terminou na casa "
                        + j.getPosicao() + " com "
                        + j.getJogadas() + " jogadas.");
            }
        }
    }
}
