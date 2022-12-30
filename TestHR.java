import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.*;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodAndMin;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodOrMax;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;


public class TestHR {
	private static void createAndShowGUI(FIS fis) {
        JFrame frame = new JFrame("Heart Disease Diagnosis");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700,500));
        frame.setLayout(new GridLayout(0,1, 50, 50));
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        
        JSlider chestPainSlider = new JSlider(); 
        chestPainSlider.setMaximum(10);
        chestPainSlider.setMinimum(0);
        chestPainSlider.setMinorTickSpacing(1);
        chestPainSlider.setMajorTickSpacing(1);
        chestPainSlider.setPaintTicks(true);
        chestPainSlider.setPaintLabels(true);
        
        JSlider bloodPressureSlider = new JSlider(); 
        bloodPressureSlider.setMaximum(250);
        bloodPressureSlider.setMinimum(0);
        bloodPressureSlider.setMinorTickSpacing(10);
        bloodPressureSlider.setPaintTicks(true);
        bloodPressureSlider.setMajorTickSpacing(50);
        bloodPressureSlider.setPaintLabels(true);

        JSlider ageSlider = new JSlider(); 
        ageSlider.setMaximum(100);
        ageSlider.setMinimum(0);
        ageSlider.setMinorTickSpacing(10);
        ageSlider.setMajorTickSpacing(10);
        ageSlider.setPaintTicks(true);
        ageSlider.setPaintLabels(true);

        JSlider cholesterolSlider = new JSlider(); 
        cholesterolSlider.setMaximum(300);
        cholesterolSlider.setMinimum(0);
        cholesterolSlider.setMinorTickSpacing(10);
        cholesterolSlider.setPaintTicks(true);
        cholesterolSlider.setMajorTickSpacing(100);
        cholesterolSlider.setPaintLabels(true);

        JSlider diabeticSlider = new JSlider(); 
        diabeticSlider.setMaximum(10);
        diabeticSlider.setMinimum(0);
        diabeticSlider.setMinorTickSpacing(1);
        diabeticSlider.setPaintTicks(true);
        diabeticSlider.setMajorTickSpacing(1);
        diabeticSlider.setPaintLabels(true);
        diabeticSlider.setValue(0);

        JSlider ecgSlider = new JSlider(); 
        ecgSlider.setMaximum(10);
        ecgSlider.setMinimum(0);
        ecgSlider.setMinorTickSpacing(1);
        ecgSlider.setMajorTickSpacing(1);
        ecgSlider.setPaintTicks(true);
        ecgSlider.setPaintLabels(true);
        ecgSlider.setValue(0);
        
        JLabel ageLabel = new JLabel("Age", SwingConstants.CENTER);
        JLabel bpLabel = new JLabel("BloodPressure", SwingConstants.CENTER);
        JLabel choLabel = new JLabel("Cholestero", SwingConstants.CENTER);
        JLabel diaLabel = new JLabel("Diabetic", SwingConstants.CENTER);
        JLabel ecgLabel = new JLabel("ECG Significance Level", SwingConstants.CENTER);
        JLabel chestLabel = new JLabel("Chest Pain Level", SwingConstants.CENTER);
        JLabel resultLabel = new JLabel("Heart Disease Risk", SwingConstants.CENTER);
        resultLabel.setFont(new Font(resultLabel.getFont().getName(), Font.BOLD, 16));

        JButton evaluateButton = new JButton("Evaluate Risk");  
        evaluateButton.setPreferredSize(new Dimension(20,20));
        
        evaluateButton.addActionListener(new ActionListener(){  
        	public void actionPerformed(ActionEvent e){  
        		System.out.println("button pressed");
        		double result = evaluate(fis, chestPainSlider.getValue(), bloodPressureSlider.getValue(),
        				ageSlider.getValue(), cholesterolSlider.getValue(), ecgSlider.getValue(),
        				diabeticSlider.getValue());
        		resultLabel.setText(String.format("Heart Disease Risk is %.1f%%", result));
        	}  
        });
        
        panel.add(ageLabel);
        panel.add(ageSlider);
        panel.add(bpLabel);
        panel.add(bloodPressureSlider);
        panel.add(choLabel);
        panel.add(cholesterolSlider);
        panel.add(diaLabel);
        panel.add(diabeticSlider);
        panel.add(chestLabel);
        panel.add(chestPainSlider);
        panel.add(ecgLabel);
        panel.add(ecgSlider);
        panel.add(evaluateButton);
        panel.add(resultLabel);
        
        frame.add(panel);
        
        frame.pack();
        frame.setVisible(true);
    }
	
	public static double evaluate(FIS fis, double chestPain, double bloodPressure, double age, double cholesterol,
			double ecg, double diabetic) {
		// Set inputs
		FunctionBlock functionBlock = fis.getFunctionBlock("heartDisease");
		Variable result = functionBlock.getVariable("DiagnosisHeartDisease");
		
        fis.setVariable(functionBlock.getName(), "ChestPain", chestPain/10.0);
        fis.setVariable(functionBlock.getName(), "BloodPressure", bloodPressure);
        fis.setVariable(functionBlock.getName(), "Cholesterol", cholesterol);
        fis.setVariable(functionBlock.getName(), "Diabetic", diabetic/10.0);
        fis.setVariable(functionBlock.getName(), "Age", age);
        fis.setVariable(functionBlock.getName(), "ECG", ecg/10.0);
        
       // Evaluate
        fis.evaluate();

//        for(Rule r : functionBlock.getFuzzyRuleBlock("No1").getRules())
//          System.out.println(r);
        System.out.printf("Inputs %.1f %.1f %.1f %.1f %.1f %.1f\n",chestPain/10.0, bloodPressure, cholesterol, diabetic/10.0, age, ecg/10.0);
        System.out.printf("Defuzzified Chance of Heart Disease Risk  %.1f%%\n",result.getDefuzzifier().defuzzify()*100);
		return result.getDefuzzifier().defuzzify()*100;
	}
 
    public static void main(String[] args) throws Exception {
        // Load from 'FCL' file
        String fileName = "/home/dilshan/eclipse-workspace/fuzzy-logic/src/hr.fcl";
        FIS fis = FIS.load(fileName,true);

        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }

        FunctionBlock functionBlock = fis.getFunctionBlock("heartDisease");
        RuleBlock ruleBlock = functionBlock.getFuzzyRuleBlock("No1");
        
        /**
         * To show the variables defined in functionBlock tipper
         */
        JFuzzyChart.get().chart(functionBlock); 
        
        
        createAndShowGUI(fis);
    }
}