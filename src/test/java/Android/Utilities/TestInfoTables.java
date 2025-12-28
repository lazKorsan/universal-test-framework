package Android.Utilities;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TestInfoTables {
    public static void main(String[] args) {

        String patternChoice = "honeycomb"; // "honeycomb" veya "fishscale"

        // JFrame oluştur
        JFrame frame = new JFrame("JPanel Örneği - Desenli Panel (6 saniye sonra kapanacak)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ekran boyutunu al
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Pencere boyutlarını belirle (x ve y ekseni)
        int width = 550;  // x ekseni genişlik
        int height = 300; // y ekseni yükseklik

        // Pencereyi ekranın ortasına yerleştir
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;

        frame.setBounds(x, y, width, height);

        // Özel desenli JPanel oluştur
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if ("honeycomb".equals(patternChoice)) {
                    drawHoneycombPattern(g, getWidth(), getHeight());
                } else if ("fishscale".equals(patternChoice)) {
                    drawFishScalePattern(g, getWidth(), getHeight());
                }
            }
        };

        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Kenarlık

        // Panel'i frame'e ekle
        frame.add(panel);

        // Frame'i görünür yap
        frame.setVisible(true);

        // 6 saniye sonra otomatik kapanma timer'ı
        Timer timer = new Timer(6000, e -> {
            frame.dispose(); // Pencereyi kapat
            System.out.println("Panel 6 saniye sonra otomatik olarak kapatıldı.");
        });
        timer.setRepeats(false); // Sadece bir kere çalışsın
        timer.start(); // Timer'ı başlat
    }

    // Bal peteği deseni çizme metodu - Bal rengi tonları
    private static void drawHoneycombPattern(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color honeyFill = new Color(251, 220, 149); // Petek içi rengi
        Color honeyBorder = new Color(225, 180, 90, 150); // Petek kenar rengi (daha az belirgin)

        int hexSize = 20; // Altıgen boyutu
        int hexWidth = (int) (hexSize * Math.sqrt(3));
        int hexHeight = 2 * hexSize;

        for (int row = 0; row < height + hexHeight; row += hexHeight * 3 / 4) {
            for (int col = 0; col < width + hexWidth; col += hexWidth) {
                int x = col + ((row / (hexHeight * 3 / 4)) % 2 == 0 ? 0 : hexWidth / 2);
                int y = row;
                drawHexagon(g2d, x, y, hexSize, honeyFill, honeyBorder);
            }
        }
    }

    // Balık sırtı deseni çizme metodu
    private static void drawFishScalePattern(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color lightBlue = new Color(173, 216, 230);
        Color darkBlue = new Color(100, 149, 237);

        int scaleWidth = 30;
        int scaleHeight = 20;

        for (int row = 0; row < height + scaleHeight; row += scaleHeight) {
            for (int col = 0; col < width + scaleWidth; col += scaleWidth) {
                int x = col + ((row / scaleHeight) % 2 == 0 ? 0 : scaleWidth / 2);
                int y = row;
                drawFishScale(g2d, x, y, scaleWidth, scaleHeight, lightBlue, darkBlue);
            }
        }
    }

    // Tek bir altıgen çizme metodu
    private static void drawHexagon(Graphics2D g2d, int x, int y, int size, Color fillColor, Color borderColor) {
        Polygon hexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * i;
            int pointX = x + (int) (size * Math.cos(angle));
            int pointY = y + (int) (size * Math.sin(angle));
            hexagon.addPoint(pointX, pointY);
        }
        g2d.setColor(fillColor);
        g2d.fill(hexagon);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(0.8f)); // Çizgi kalınlığı azaltıldı
        g2d.draw(hexagon);
    }

    // Tek bir balık pulu çizme metodu
    private static void drawFishScale(Graphics2D g2d, int x, int y, int width, int height, Color fillColor, Color borderColor) {
        int[] xPoints = {x, x + width / 2, x + width, x + width / 2};
        int[] yPoints = {y + height / 2, y, y + height / 2, y + height};
        Polygon scale = new Polygon(xPoints, yPoints, 4);
        g2d.setColor(fillColor);
        g2d.fill(scale);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.draw(scale);
    }

    public static void showHoneycombPanel() {
        String[] args = {"honeycomb"};
        main(args);
    }

    public static void showFishScalePanel() {
        String[] args = {"fishscale"};
        main(args);
    }

    public static JPanel InProgress(String... lines) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Bilgi Paneli");
        dialog.setModal(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setMinimumSize(new Dimension(550, 300)); // TABLONUN MİNİMUM AÇILIŞ BOYUTLARI (Genişlik, Yükseklik)

        // Ana panel (arka plan desenli)
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) { // Ortalamak için GridBagLayout
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawHoneycombPattern(g, getWidth(), getHeight());
            }
        };
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        backgroundPanel.setBorder(lineBorder);

        // İçerik paneli (yazıları tutan)
        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 0, 5)); // Satırları aynı boyutta yapar
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Color cellColor = new Color(152, 251, 152, 170);
        Border textBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
        Border textPadding = BorderFactory.createEmptyBorder(5, 10, 5, 10);

        for (int i = 0; i < lines.length; i++) {
            JLabel lineLabel;
            if (i == 0) {
                lineLabel = new JLabel(lines[i], SwingConstants.CENTER);
                lineLabel.setFont(new Font("Arial", Font.BOLD, 16)); // BAŞLIK YAZI TİPİ BOYUTU (16)
            } else {
                lineLabel = new JLabel(lines[i], SwingConstants.LEFT);
                lineLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // DİĞER SATIRLARIN YAZI TİPİ BOYUTU (12)
            }
            lineLabel.setOpaque(true);
            lineLabel.setBackground(cellColor);
            lineLabel.setBorder(BorderFactory.createCompoundBorder(textBorder, textPadding));
            contentPanel.add(lineLabel);
        }

        backgroundPanel.add(contentPanel); // İçerik panelini arka plan paneline ekle
        dialog.add(backgroundPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        Timer timer = new Timer(5000, e -> {
            dialog.dispose();
            System.out.println("Panel 5 saniye sonra otomatik olarak kapatıldı.");
        });
        timer.setRepeats(false);
        timer.start();

        return backgroundPanel;
    }
}
