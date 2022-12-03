/*
Copyright (c) 2022,  Adarsh Kumar

Developed by Adarsh Kumar (adarsh.in.lucknow@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining 
a copy of this software and associated documentation files (the 
"Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, 
distribute, sublicense, and/or sell copies of the Software, and to 
permit persons to whom the Software is furnished to do so, subject 
to the following conditions:

The above copyright notice and this permission notice shall be 
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION 
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(MIT license, http://www.opensource.org/licenses/mit-license.html)
 */
package mars.tools;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import mars.ProgramStatement;
import mars.mips.hardware.AccessNotice;
import mars.mips.hardware.AddressErrorException;
import mars.mips.hardware.Memory;
import mars.mips.hardware.MemoryAccessNotice;
import mars.mips.instructions.BasicInstruction;
import mars.mips.instructions.BasicInstructionFormat;

/**
 * 
 * I-Type instruction counter tool. Can be used to know how many I-Type
 * instructions
 * were executed to complete a given program.
 * 
 * Code slightly based on InstructionCounter.
 * 
 * @author Adarsh Kumar <adarsh.in.lucknow@gmail.com>
 *
 */
// @SuppressWarnings("serial")
public class ITypeInstructionCounter extends AbstractMarsToolAndApplication {
    private static String name = "I-Type Instruction Counter";
    private static String version = "Version 1.0 (Adarsh Kumar)";
    private static String heading = "Counting The Number Of I-Type Instructions Executed";

    /**
     * Total number of I-Type instructions executed until now.
     */
    protected int counter = 0;
    private JTextField counterField;

    /**
     * All I-type instructions can be viewed here:
     * https://en.wikibooks.org/wiki/MIPS_Assembly/Instruction_Formats#I_Format
     */
    protected int counterADDI = 0;
    private JTextField counterADDIField;
    private JProgressBar progressbarADDI;

    protected int counterANDI = 0;
    private JTextField counterANDIField;
    private JProgressBar progressbarANDI;

    protected int counterBEQ = 0;
    private JTextField counterBEQField;
    private JProgressBar progressbarBEQ;

    protected int counterBNE = 0;
    private JTextField counterBNEField;
    private JProgressBar progressbarBNE;

    protected int counterLB = 0;
    private JTextField counterLBField;
    private JProgressBar progressbarLB;

    protected int counterLW = 0;
    private JTextField counterLWField;
    private JProgressBar progressbarLW;

    protected int counterORI = 0;
    private JTextField counterORIField;
    private JProgressBar progressbarORI;

    protected int counterSB = 0;
    private JTextField counterSBField;
    private JProgressBar progressbarSB;

    protected int counterSLTI = 0;
    private JTextField counterSLTIField;
    private JProgressBar progressbarSLTI;

    protected int counterSW = 0;
    private JTextField counterSWField;
    private JProgressBar progressbarSW;

    /**
     * Other I-type instructions include:
     * addiu, blez, bgtz, lbu, lhu, lui, sh, sltiu
     */
    protected int counterOTHER = 0;
    private JTextField counterOTHERField;
    private JProgressBar progressbarOTHER;

    /**
     * The last address we saw. We ignore it because the only way for a
     * program to execute twice the same instruction is to enter an infinite
     * loop, which is not insteresting in the POV of counting instructions.
     */
    protected int lastAddress = -1;

    /**
     * Simple constructor, likely used to run a stand-alone memory reference
     * visualizer.
     * 
     * @param title   String containing title for title bar
     * @param heading String containing text for heading shown in upper part of
     *                window.
     */
    public ITypeInstructionCounter(String title, String heading) {
        super(title, heading);
    }

    /**
     * Simple construction, likely used by the MARS Tools menu mechanism.
     */
    public ITypeInstructionCounter() {
        super(name + ", " + version, heading);
    }

    // @Override
    public String getName() {
        return name;
    }

    // @Override
    protected JComponent buildMainDisplayArea() {
        // Create everything
        JPanel panel = new JPanel(new GridBagLayout());

        counterField = new JTextField("0", 10);
        counterField.setEditable(false);

        counterADDIField = new JTextField("0", 10);
        counterADDIField.setEditable(false);
        progressbarADDI = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarADDI.setStringPainted(true);

        counterANDIField = new JTextField("0", 10);
        counterANDIField.setEditable(false);
        progressbarANDI = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarANDI.setStringPainted(true);

        counterBEQField = new JTextField("0", 10);
        counterBEQField.setEditable(false);
        progressbarBEQ = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarBEQ.setStringPainted(true);

        counterBNEField = new JTextField("0", 10);
        counterBNEField.setEditable(false);
        progressbarBNE = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarBNE.setStringPainted(true);

        counterLBField = new JTextField("0", 10);
        counterLBField.setEditable(false);
        progressbarLB = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarLB.setStringPainted(true);

        counterLWField = new JTextField("0", 10);
        counterLWField.setEditable(false);
        progressbarLW = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarLW.setStringPainted(true);

        counterORIField = new JTextField("0", 10);
        counterORIField.setEditable(false);
        progressbarORI = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarORI.setStringPainted(true);

        counterSBField = new JTextField("0", 10);
        counterSBField.setEditable(false);
        progressbarSB = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarSB.setStringPainted(true);

        counterSLTIField = new JTextField("0", 10);
        counterSLTIField.setEditable(false);
        progressbarSLTI = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarSLTI.setStringPainted(true);

        counterSWField = new JTextField("0", 10);
        counterSWField.setEditable(false);
        progressbarSW = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarSW.setStringPainted(true);

        counterOTHERField = new JTextField("0", 10);
        counterOTHERField.setEditable(false);
        progressbarOTHER = new JProgressBar(JProgressBar.HORIZONTAL);
        progressbarOTHER.setStringPainted(true);

        // Add them to the panel

        // Fields
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.gridheight = c.gridwidth = 1;
        c.gridx = 3;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 17, 0);
        panel.add(counterField, c);

        c.insets = new Insets(0, 0, 0, 0);

        c.gridy++;
        panel.add(counterADDIField, c);

        c.gridy++;
        panel.add(counterANDIField, c);

        c.gridy++;
        panel.add(counterBEQField, c);

        c.gridy++;
        panel.add(counterBNEField, c);

        c.gridy++;
        panel.add(counterLBField, c);

        c.gridy++;
        panel.add(counterLWField, c);

        c.gridy++;
        panel.add(counterORIField, c);

        c.gridy++;
        panel.add(counterSBField, c);

        c.gridy++;
        panel.add(counterSLTIField, c);

        c.gridy++;
        panel.add(counterSWField, c);

        c.gridy++;
        panel.add(counterOTHERField, c);

        // Labels
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 1;
        c.gridwidth = 2;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 17, 0);
        panel.add(new JLabel("Total I-Type Instructions: "), c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 2;
        c.gridwidth = 1;

        c.gridy++;
        panel.add(new JLabel("Add Immediate: "), c);

        c.gridy++;
        panel.add(new JLabel("Bitwise AND Immediate: "), c);

        c.gridy++;
        panel.add(new JLabel("Branch if Equal: "), c);

        c.gridy++;
        panel.add(new JLabel("Branch if Not Equal: "), c);

        c.gridy++;
        panel.add(new JLabel("Load Byte: "), c);

        c.gridy++;
        panel.add(new JLabel("Load Word: "), c);

        c.gridy++;
        panel.add(new JLabel("Bitwise OR Immediate: "), c);

        c.gridy++;
        panel.add(new JLabel("Store Byte: "), c);

        c.gridy++;
        panel.add(new JLabel("Set to 1 if Less Than Immediate: "), c);

        c.gridy++;
        panel.add(new JLabel("Store Word: "), c);

        c.gridy++;
        panel.add(new JLabel("Other: "), c);

        // Progress bars
        c.insets = new Insets(3, 3, 3, 3);
        c.gridx = 4;
        c.gridy = 2;

        panel.add(progressbarADDI, c);

        c.gridy++;
        panel.add(progressbarANDI, c);

        c.gridy++;
        panel.add(progressbarBEQ, c);

        c.gridy++;
        panel.add(progressbarBNE, c);

        c.gridy++;
        panel.add(progressbarLB, c);

        c.gridy++;
        panel.add(progressbarLW, c);

        c.gridy++;
        panel.add(progressbarORI, c);

        c.gridy++;
        panel.add(progressbarSB, c);

        c.gridy++;
        panel.add(progressbarSLTI, c);

        c.gridy++;
        panel.add(progressbarSW, c);

        c.gridy++;
        panel.add(progressbarOTHER, c);

        return panel;
    }

    // @Override
    protected void addAsObserver() {
        addAsObserver(Memory.textBaseAddress, Memory.textLimitAddress);
    }

    // @Override
    protected void processMIPSUpdate(Observable resource, AccessNotice notice) {
        if (!notice.accessIsFromMIPS())
            return;
        if (notice.getAccessType() != AccessNotice.READ)
            return;
        MemoryAccessNotice m = (MemoryAccessNotice) notice;
        int a = m.getAddress();
        if (a == lastAddress)
            return;
        lastAddress = a;
        try {
            ProgramStatement stmt = Memory.getInstance().getStatement(a);
            BasicInstruction instr = (BasicInstruction) stmt.getInstruction();
            BasicInstructionFormat format = instr.getInstructionFormat();
            String _mnemonics = instr.getName().trim();

            if (_mnemonics.equals("addi"))
                counterADDI++;
            else if (_mnemonics.equals("andi"))
                counterANDI++;
            else if (_mnemonics.equals("beq"))
                counterBEQ++;
            else if (_mnemonics.equals("bne"))
                counterBNE++;
            else if (_mnemonics.equals("lb"))
                counterLB++;
            else if (_mnemonics.equals("lw"))
                counterLW++;
            else if (_mnemonics.equals("ori"))
                counterORI++;
            else if (_mnemonics.equals("sb"))
                counterSB++;
            else if (_mnemonics.equals("slti"))
                counterSLTI++;
            else if (_mnemonics.equals("sw"))
                counterSW++;
            else if (format == BasicInstructionFormat.I_FORMAT || format == BasicInstructionFormat.I_BRANCH_FORMAT)
                counterOTHER++;

            if (format == BasicInstructionFormat.I_FORMAT || format == BasicInstructionFormat.I_BRANCH_FORMAT)
                counter++;
        } catch (AddressErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        updateDisplay();
    }

    // @Override
    protected void initializePreGUI() {
        counter = counterADDI = counterANDI = counterBEQ = counterBNE = counterLB = counterLW = 0;
        counterORI = counterSB = counterSLTI = counterSW = counterOTHER = 0;
        lastAddress = -1;
    }

    // @Override
    protected void reset() {
        counter = counterADDI = counterANDI = counterBEQ = counterBNE = counterLB = counterLW = 0;
        counterORI = counterSB = counterSLTI = counterSW = counterOTHER = 0;
        lastAddress = -1;
        updateDisplay();
    }

    // @Override
    protected void updateDisplay() {
        counterField.setText(String.valueOf(counter));

        counterADDIField.setText(String.valueOf(counterADDI));
        progressbarADDI.setMaximum(counter);
        progressbarADDI.setValue(counterADDI);

        counterANDIField.setText(String.valueOf(counterANDI));
        progressbarANDI.setMaximum(counter);
        progressbarANDI.setValue(counterANDI);

        counterBEQField.setText(String.valueOf(counterBEQ));
        progressbarBEQ.setMaximum(counter);
        progressbarBEQ.setValue(counterBEQ);

        counterBNEField.setText(String.valueOf(counterBNE));
        progressbarBNE.setMaximum(counter);
        progressbarBNE.setValue(counterBNE);

        counterLBField.setText(String.valueOf(counterLB));
        progressbarLB.setMaximum(counter);
        progressbarLB.setValue(counterLB);

        counterLWField.setText(String.valueOf(counterLW));
        progressbarLW.setMaximum(counter);
        progressbarLW.setValue(counterLW);

        counterORIField.setText(String.valueOf(counterORI));
        progressbarORI.setMaximum(counter);
        progressbarORI.setValue(counterORI);

        counterSBField.setText(String.valueOf(counterSB));
        progressbarSB.setMaximum(counter);
        progressbarSB.setValue(counterSB);

        counterSLTIField.setText(String.valueOf(counterSLTI));
        progressbarSLTI.setMaximum(counter);
        progressbarSLTI.setValue(counterSLTI);

        counterSWField.setText(String.valueOf(counterSW));
        progressbarSW.setMaximum(counter);
        progressbarSW.setValue(counterSW);

        counterOTHERField.setText(String.valueOf(counterOTHER));
        progressbarOTHER.setMaximum(counter);
        progressbarOTHER.setValue(counterOTHER);

        if (counter == 0) {
            progressbarADDI.setString("0%");
            progressbarANDI.setString("0%");
            progressbarBEQ.setString("0%");
            progressbarBNE.setString("0%");
            progressbarLB.setString("0%");
            progressbarLW.setString("0%");
            progressbarORI.setString("0%");
            progressbarSB.setString("0%");
            progressbarSLTI.setString("0%");
            progressbarSW.setString("0%");
            progressbarOTHER.setString("0%");
        } else {
            progressbarADDI.setString((counterADDI * 100) / counter + "%");
            progressbarANDI.setString((counterANDI * 100) / counter + "%");
            progressbarBEQ.setString((counterBEQ * 100) / counter + "%");
            progressbarBNE.setString((counterBNE * 100) / counter + "%");
            progressbarLB.setString((counterLB * 100) / counter + "%");
            progressbarLW.setString((counterLW * 100) / counter + "%");
            progressbarORI.setString((counterORI * 100) / counter + "%");
            progressbarSB.setString((counterSB * 100) / counter + "%");
            progressbarSLTI.setString((counterSLTI * 100) / counter + "%");
            progressbarSW.setString((counterSW * 100) / counter + "%");
            progressbarOTHER.setString((counterOTHER * 100) / counter + "%");
        }
    }
}
