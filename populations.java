import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

// enumeration for gender
enum Gender
{ MALE, FEMALE }

// population class
class Populations implements Serializable {
    private String name;
    private int age;
    private int sid;
    private Gender gender;

    // constructor
    public Populations(String name, int age, int sid, Gender gender)
    {
        this.name = name;
        this.age = age;
        this.sid =sid;
        this.gender = gender;
    }

    // getters
    public String getName()
    {
        return name;
    }
    public int getAge()
    {
        return age;
    }
    public int getSid()
    {
        return sid;
    }
    public Gender getGender()
    {
        return gender;
    }

    public void add(Populations populations) {
    }
}

// GUI class
class PopulationsGUI extends JFrame implements ActionListener {
    private JTextField nameField;
    private JTextField ageField;
    private JTextField sidField;
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private JButton saveButton;
    private JButton viewButton;
    private ArrayList<Populations> populations;

    // constructor
    public PopulationsGUI() {
        // create GUI components
        nameField = new JTextField(20);
        ageField = new JTextField(20);
        sidField = new JTextField(20);
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        saveButton = new JButton("Save");
        viewButton = new JButton("View");

        // create gender button group
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        // create panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("SID:"));
        inputPanel.add(sidField);
        inputPanel.add(maleButton);
        inputPanel.add(femaleButton);

        // create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(viewButton);

        // set layout and add panels to frame
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // set action listeners for buttons
        saveButton.addActionListener(this);
        viewButton.addActionListener(this);

        // initialize array list for population
        populations = new ArrayList<Populations>();
    }

    // action performed event handler
    public void actionPerformed(ActionEvent event) {
        // save button clicked
        if (event.getSource() == saveButton) {
            // get input fields
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            int sid = Integer.parseInt(sidField.getText());
            Gender gender = maleButton.isSelected() ? Gender.MALE : Gender.FEMALE;

            // create new population object
            Populations population = new Populations(name, age, sid, gender);
            // add population to array list
            populations.add(population);

            // clear input fields
            nameField.setText("");
            ageField.setText("");
            sidField.setText("");
            maleButton.setSelected(false);
            femaleButton.setSelected(false);
        }
        // view button clicked
        else if (event.getSource() == viewButton) {
            // serialize array list of populations to file
            try {
                FileOutputStream fileOut = new FileOutputStream("population.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(populations);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // deserialize array list of populayions from file
            try {
                FileInputStream fileIn = new FileInputStream("population.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                populations = (ArrayList<Populations>) in.readObject();
                in.close();
                fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // create data for JTable
            String[][] data = new String[populations.size()][4];
            for (int i = 0; i < populations.size(); i++) {
                Populations p = populations.get(i);
                data[i][0] = p.getName();
                data[i][1] = Integer.toString(p.getAge());
                data[i][2] = Integer.toString(p.getSid());
                data[i][3] = p.getGender() == Gender.MALE ? "Male" : "Female";
            }
            // create column names for JTable
            String[] columnNames = { "Name", "Age","Sid", "Gender" };

            // create JTable with data and column names
            JTable table = new JTable(data, columnNames);

            // add JTable to scroll pane
            JScrollPane scrollPane = new JScrollPane(table);

            // add scroll pane to frame and show
            add(scrollPane, BorderLayout.CENTER);
            setVisible(true);
        }
    }

    public static void main(String[] args) {
        PopulationsGUI gui = new PopulationsGUI();
        gui.setTitle("Information Center");
        gui.setSize(1000, 300);
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
    }
}