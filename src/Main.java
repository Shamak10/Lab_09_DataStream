import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class DataStreamsApp extends JFrame {
    private JTextArea[] displayTextAreas;
    private JTextArea[] searchResultTextAreas;
    private JTextField[] searchFields;
    private JButton[] loadFileButtons;
    private JButton[] searchButtons;
    private JButton quitButton;
    private Path[] loadedFilePaths;

    public DataStreamsApp() {
        setTitle("DataStreamsApp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 3));

        displayTextAreas = new JTextArea[3];
        searchResultTextAreas = new JTextArea[3];
        searchFields = new JTextField[3];
        loadFileButtons = new JButton[3];
        searchButtons = new JButton[3];

        loadedFilePaths = new Path[3];

        for (int i = 0; i < 3; i++) {
            JPanel filePanel = new JPanel();
            filePanel.setLayout(new BorderLayout());

            displayTextAreas[i] = new JTextArea(20, 20);
            displayTextAreas[i].setEditable(false);
            JScrollPane displayScrollPane = new JScrollPane(displayTextAreas[i]);
            filePanel.add(displayScrollPane, BorderLayout.WEST);

            searchResultTextAreas[i] = new JTextArea(20, 20);
            searchResultTextAreas[i].setEditable(false);
            JScrollPane searchResultScrollPane = new JScrollPane(searchResultTextAreas[i]);
            filePanel.add(searchResultScrollPane, BorderLayout.EAST);

            searchFields[i] = new JTextField(20);

            JPanel buttonPanel = new JPanel();
            loadFileButtons[i] = new JButton("Load File " + (i + 1));
            searchButtons[i] = new JButton("Search " + (i + 1));
            buttonPanel.add(loadFileButtons[i]);
            buttonPanel.add(searchFields[i]);
            buttonPanel.add(searchButtons[i]);

            mainPanel.add(filePanel);
            mainPanel.add(buttonPanel);
        }

        quitButton = new JButton("Quit");
        mainPanel.add(quitButton);

        for (int i = 0; i < 3; i++) {
            final int index = i;
            loadFileButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showOpenDialog(DataStreamsApp.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        loadedFilePaths[index] = fileChooser.getSelectedFile().toPath();
                        displayTextAreas[index].setText(""); // Clear the display area
                        searchResultTextAreas[index].setText(""); // Clear the search result area

                        try {
                            List<String> lines = Files.readAllLines(loadedFilePaths[index]);
                            lines.forEach(line -> displayTextAreas[index].append(line + "\n"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(DataStreamsApp.this, "Error loading the file.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            searchButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (loadedFilePaths[index] != null) {
                        String searchTerm = searchFields[index].getText().toLowerCase();
                        searchResultTextAreas[index].setText(""); // Clear the search result area

                        try {
                            List<String> filteredLines = Files.lines(loadedFilePaths[index])
                                    .filter(line -> line.toLowerCase().contains(searchTerm))
                                    .collect(Collectors.toList());

                            filteredLines.forEach(line -> searchResultTextAreas[index].append(line + "\n"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(DataStreamsApp.this, "Error searching the file.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DataStreamsApp().setVisible(true);
            }
        });
    }
}
