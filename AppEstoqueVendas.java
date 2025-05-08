import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

class Produto {
    private String nome;
    private double preco;
    private int quantidade;

    public Produto(String nome, double preco, int quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }

    public void vender(int quantidadeVendida) {
        this.quantidade -= quantidadeVendida;
    }

    public void adicionarEstoque(int qtd) {
        this.quantidade += qtd;
    }
}

class Venda {
    private Produto produto;
    private int quantidade;
    private double total;

    public Venda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.total = produto.getPreco() * quantidade;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getTotal() { return total; }
}

public class AppEstoqueVendas {
    private static ArrayList<Produto> produtos = new ArrayList<>();
    private static ArrayList<Venda> vendas = new ArrayList<>();
    private static JTable tabelaProdutos;
    private static JTable tabelaVendas;
    private static DefaultTableModel modeloProdutos;
    private static DefaultTableModel modeloVendas;
    private static JLabel labelTotalVendas = new JLabel("Total de Vendas: R$ 0.00");

    public static void main(String[] args) {
        // Visual Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Não foi possível aplicar o tema Nimbus.");
        }

        JFrame frame = new JFrame("📦 Controle de Estoque e Vendas");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Tabela Produtos
        modeloProdutos = new DefaultTableModel(new Object[]{"Nome", "Preço", "Quantidade"}, 0);
        tabelaProdutos = new JTable(modeloProdutos);
        tabelaProdutos.setRowHeight(22);
        JScrollPane scrollProdutos = new JScrollPane(tabelaProdutos);

        // Tabela Vendas
        modeloVendas = new DefaultTableModel(new Object[]{"Produto", "Quantidade", "Total"}, 0);
        tabelaVendas = new JTable(modeloVendas);
        tabelaVendas.setRowHeight(22);
        JScrollPane scrollVendas = new JScrollPane(tabelaVendas);

        // Botões
        JButton btnAdicionar = new JButton("➕ Adicionar Produto");
        JButton btnVender = new JButton("💸 Vender Produto");
        JButton btnExcluirVenda = new JButton("❌ Excluir Venda");

        // Estilo
        Font fonte = new Font("Segoe UI", Font.BOLD, 14);
        btnAdicionar.setBackground(new Color(46, 204, 113));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(fonte);

        btnVender.setBackground(new Color(52, 152, 219));
        btnVender.setForeground(Color.WHITE);
        btnVender.setFont(fonte);

        btnExcluirVenda.setBackground(new Color(231, 76, 60));
        btnExcluirVenda.setForeground(Color.WHITE);
        btnExcluirVenda.setFont(fonte);

        labelTotalVendas.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Painéis
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnVender);
        painelBotoes.add(btnExcluirVenda);

        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelTotal.add(labelTotalVendas);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(painelBotoes, BorderLayout.WEST);
        painelInferior.add(painelTotal, BorderLayout.EAST);

        // Layout principal
        JPanel centro = new JPanel(new GridLayout(2, 1));
        centro.add(scrollProdutos);
        centro.add(scrollVendas);

        frame.add(centro, BorderLayout.CENTER);
        frame.add(painelInferior, BorderLayout.SOUTH);

        // Ações
        btnAdicionar.addActionListener(e -> {
            try {
                String nome = JOptionPane.showInputDialog("Nome do Produto:");
                String precoStr = JOptionPane.showInputDialog("Preço do Produto:");
                String qtdStr = JOptionPane.showInputDialog("Quantidade em Estoque:");

                if (nome == null || precoStr == null || qtdStr == null || nome.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Entrada inválida.");
                    return;
                }

                double preco = Double.parseDouble(precoStr);
                int quantidade = Integer.parseInt(qtdStr);

                Produto novo = new Produto(nome, preco, quantidade);
                produtos.add(novo);
                atualizarTabelaProdutos();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Preço ou quantidade inválidos.");
            }
        });

        btnVender.addActionListener(e -> {
            int linha = tabelaProdutos.getSelectedRow();
            if (linha >= 0) {
                Produto produto = produtos.get(linha);
                String qtdStr = JOptionPane.showInputDialog("Quantidade a vender:");
                try {
                    int quantidade = Integer.parseInt(qtdStr);
                    if (quantidade <= produto.getQuantidade() && quantidade > 0) {
                        produto.vender(quantidade);
                        vendas.add(new Venda(produto, quantidade));
                        atualizarTabelaProdutos();
                        atualizarTabelaVendas();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Quantidade inválida ou insuficiente.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Quantidade inválida.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione um produto para vender.");
            }
        });

        btnExcluirVenda.addActionListener(e -> {
            int linha = tabelaVendas.getSelectedRow();
            if (linha >= 0) {
                vendas.remove(linha);
                atualizarTabelaVendas();
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione uma venda para excluir.");
            }
        });

        frame.setVisible(true);
    }

    private static void atualizarTabelaProdutos() {
        modeloProdutos.setRowCount(0);
        for (Produto p : produtos) {
            modeloProdutos.addRow(new Object[]{p.getNome(), p.getPreco(), p.getQuantidade()});
        }
    }

    private static void atualizarTabelaVendas() {
        modeloVendas.setRowCount(0);
        double totalGeral = 0;
        for (Venda v : vendas) {
            modeloVendas.addRow(new Object[]{v.getProduto().getNome(), v.getQuantidade(), v.getTotal()});
            totalGeral += v.getTotal();
        }
        labelTotalVendas.setText(String.format("Total de Vendas: R$ %.2f", totalGeral));
    }
}
