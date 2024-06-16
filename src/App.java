import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame {
    private JTextField nameField, numSubjectsField;
    private JButton startButton, calculateButton, exitButton, tryAgainButton;
    private JPanel inputPanel, resultPanel;
    private JLabel resultLabel;
    private JTextField[] marksFields;
    private String username;
    private static final Font LARGE_FONT = new Font("Arial", Font.PLAIN, 20);

    public App() {
        setTitle("Student Grade Calculator");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel nameLabel = new JLabel("Enter your name: ");
        nameLabel.setFont(LARGE_FONT);
        nameField = new JTextField(15);
        nameField.setFont(LARGE_FONT);
        nameField.setPreferredSize(new Dimension(200, 40));
        startButton = new JButton("Start");
        startButton.setFont(LARGE_FONT);
        startButton.setPreferredSize(new Dimension(150, 40));

        JLabel numSubjectsLabel = new JLabel("Enter the number of subjects: ");
        numSubjectsLabel.setFont(LARGE_FONT);
        numSubjectsField = new JTextField(5);
        numSubjectsField.setFont(LARGE_FONT);
        numSubjectsField.setPreferredSize(new Dimension(100, 40));
        calculateButton = new JButton("Calculate");
        calculateButton.setFont(LARGE_FONT);
        calculateButton.setPreferredSize(new Dimension(150, 40));
        exitButton = new JButton("Exit");
        exitButton.setFont(LARGE_FONT);
        exitButton.setPreferredSize(new Dimension(150, 40));
        tryAgainButton = new JButton("Try Again");
        tryAgainButton.setFont(LARGE_FONT);
        tryAgainButton.setPreferredSize(new Dimension(150, 40));

        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false); // Transparent background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        inputPanel.add(nameLabel, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(startButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(numSubjectsLabel, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(numSubjectsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(calculateButton, gbc);
        calculateButton.setEnabled(false);

        resultPanel = new JPanel();
        resultPanel.setOpaque(false); // Transparent background
        resultLabel = new JLabel("");
        resultLabel.setFont(LARGE_FONT);
        resultPanel.add(resultLabel);
        resultPanel.add(exitButton);
        resultPanel.add(tryAgainButton);
        exitButton.setVisible(false);
        tryAgainButton.setVisible(false);

        gradientPanel.add(inputPanel);
        gradientPanel.add(resultPanel);

        add(gradientPanel, BorderLayout.CENTER);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCalculation();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateGrades();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        tryAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
    }

    private void startCalculation() {
        username = nameField.getText();
        int numSubjects;
        try {
            numSubjects = Integer.parseInt(numSubjectsField.getText());
            if (numSubjects <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of subjects.");
            return;
        }

        inputPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        inputPanel.add(new JLabel("Hello, " + username + "! Enter your marks:"), gbc);

        marksFields = new JTextField[numSubjects];
        for (int i = 0; i < numSubjects; i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            JLabel subjectLabel = new JLabel("Subject " + (i + 1) + ":");
            subjectLabel.setFont(LARGE_FONT);
            inputPanel.add(subjectLabel, gbc);

            gbc.gridx = 1;
            marksFields[i] = new JTextField(5);
            marksFields[i].setFont(LARGE_FONT);
            marksFields[i].setPreferredSize(new Dimension(100, 40));
            inputPanel.add(marksFields[i], gbc);
        }

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        inputPanel.add(calculateButton, gbc);
        calculateButton.setEnabled(true);

        revalidate();
        repaint();
    }

    private void calculateGrades() {
        int numSubjects = marksFields.length;
        double[] marks = new double[numSubjects];
        double totalMarks = 0;

        for (int i = 0; i < numSubjects; i++) {
            try {
                marks[i] = Double.parseDouble(marksFields[i].getText());
                if (marks[i] < 0 || marks[i] > 100) throw new NumberFormatException();
                totalMarks += marks[i];
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid marks between 0 and 100 for all subjects.");
                return;
            }
        }

        double averagePercentage = totalMarks / numSubjects;
        char grade = calculateGrade(averagePercentage);

        resultLabel.setText("<html>Total Marks: " + totalMarks +
                "<br>Average Percentage: " + String.format("%.2f", averagePercentage) + "%" +
                "<br>Grade: " + grade + "</html>");

        exitButton.setVisible(true);
        tryAgainButton.setVisible(true);
    }

    public static char calculateGrade(double averagePercentage) {
        if (averagePercentage >= 90) {
            return 'A';
        } else if (averagePercentage >= 80) {
            return 'B';
        } else if (averagePercentage >= 70) {
            return 'C';
        } else if (averagePercentage >= 60) {
            return 'D';
        } else {
            return 'F';
        }
    }

    private void reset() {
        nameField.setText("");
        numSubjectsField.setText("");
        inputPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel nameLabel = new JLabel("Enter your name: ");
        nameLabel.setFont(LARGE_FONT);
        inputPanel.add(nameLabel, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel numSubjectsLabel = new JLabel("Enter the number of subjects: ");
        numSubjectsLabel.setFont(LARGE_FONT);
        inputPanel.add(numSubjectsLabel, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        inputPanel.add(numSubjectsField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        inputPanel.add(startButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(calculateButton, gbc);
        calculateButton.setEnabled(false);

        resultLabel.setText("");
        exitButton.setVisible(false);
        tryAgainButton.setVisible(false);
        revalidate();
        repaint();
    }

    private class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = Color.CYAN;
            Color color2 = new Color(128, 0, 128); // Purple
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}
