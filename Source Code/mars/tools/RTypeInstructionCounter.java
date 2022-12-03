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
import mars.mips.hardware.Register;
import mars.mips.hardware.RegisterFile;
import mars.mips.instructions.BasicInstruction;
import mars.mips.instructions.BasicInstructionFormat;

/**
 * 
 * R-Type instruction counter tool. Can be used to know how many R-Type
 * instructions
 * were executed to complete a given program.
 * 
 * Code slightly based on InstructionCounter.
 * 
 * @author Adarsh Kumar <adarsh.in.lucknow@gmail.com>
 *
 */
// @SuppressWarnings("serial")
public class RTypeInstructionCounter extends AbstractMarsToolAndApplication {
	private static String name = "R-Type Instruction Counter";
	private static String version = "Version 1.0 (Adarsh Kumar)";
	private static String heading = "Counting The Number Of R-Type Instructions Executed";

	/**
	 * Total number of R-Type instructions executed until now.
	 */
	protected int counter = 0;
	private JTextField counterField;

	/**
	 * All R-type instructions can be viewed here:
	 * https://en.wikibooks.org/wiki/MIPS_Assembly/Instruction_Formats#R_Format
	 */
	protected int counterADD = 0;
	private JTextField counterADDField;
	private JProgressBar progressbarADD;

	protected int counterAND = 0;
	private JTextField counterANDField;
	private JProgressBar progressbarAND;

	protected int counterDIV = 0;
	private JTextField counterDIVField;
	private JProgressBar progressbarDIV;

	protected int counterJR = 0;
	private JTextField counterJRField;
	private JProgressBar progressbarJR;

	protected int counterMULT = 0;
	private JTextField counterMULTField;
	private JProgressBar progressbarMULT;

	protected int counterNOR = 0;
	private JTextField counterNORField;
	private JProgressBar progressbarNOR;

	protected int counterXOR = 0;
	private JTextField counterXORField;
	private JProgressBar progressbarXOR;

	protected int counterOR = 0;
	private JTextField counterORField;
	private JProgressBar progressbarOR;

	protected int counterSLT = 0;
	private JTextField counterSLTField;
	private JProgressBar progressbarSLT;

	protected int counterSLL = 0;
	private JTextField counterSLLField;
	private JProgressBar progressbarSLL;

	protected int counterSRL = 0;
	private JTextField counterSRLField;
	private JProgressBar progressbarSRL;

	protected int counterSUB = 0;
	private JTextField counterSUBField;
	private JProgressBar progressbarSUB;

	/**
	 * Other R-type instructions include:
	 * addu, divu, jalr, mfhi, mthi, mflo, mtlo, mfc0, multu, sltu, sra, subu
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
	public RTypeInstructionCounter(String title, String heading) {
		super(title, heading);
	}

	/**
	 * Simple construction, likely used by the MARS Tools menu mechanism.
	 */
	public RTypeInstructionCounter() {
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

		counterADDField = new JTextField("0", 10);
		counterADDField.setEditable(false);
		progressbarADD = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarADD.setStringPainted(true);

		counterANDField = new JTextField("0", 10);
		counterANDField.setEditable(false);
		progressbarAND = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarAND.setStringPainted(true);

		counterDIVField = new JTextField("0", 10);
		counterDIVField.setEditable(false);
		progressbarDIV = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarDIV.setStringPainted(true);

		counterJRField = new JTextField("0", 10);
		counterJRField.setEditable(false);
		progressbarJR = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarJR.setStringPainted(true);

		counterMULTField = new JTextField("0", 10);
		counterMULTField.setEditable(false);
		progressbarMULT = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarMULT.setStringPainted(true);

		counterNORField = new JTextField("0", 10);
		counterNORField.setEditable(false);
		progressbarNOR = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarNOR.setStringPainted(true);

		counterXORField = new JTextField("0", 10);
		counterXORField.setEditable(false);
		progressbarXOR = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarXOR.setStringPainted(true);

		counterORField = new JTextField("0", 10);
		counterORField.setEditable(false);
		progressbarOR = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarOR.setStringPainted(true);

		counterSLTField = new JTextField("0", 10);
		counterSLTField.setEditable(false);
		progressbarSLT = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarSLT.setStringPainted(true);

		counterSLLField = new JTextField("0", 10);
		counterSLLField.setEditable(false);
		progressbarSLL = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarSLL.setStringPainted(true);

		counterSRLField = new JTextField("0", 10);
		counterSRLField.setEditable(false);
		progressbarSRL = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarSRL.setStringPainted(true);

		counterSUBField = new JTextField("0", 10);
		counterSUBField.setEditable(false);
		progressbarSUB = new JProgressBar(JProgressBar.HORIZONTAL);
		progressbarSUB.setStringPainted(true);

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
		panel.add(counterADDField, c);

		c.gridy++;
		panel.add(counterANDField, c);

		c.gridy++;
		panel.add(counterDIVField, c);

		c.gridy++;
		panel.add(counterJRField, c);

		c.gridy++;
		panel.add(counterMULTField, c);

		c.gridy++;
		panel.add(counterNORField, c);

		c.gridy++;
		panel.add(counterXORField, c);

		c.gridy++;
		panel.add(counterORField, c);

		c.gridy++;
		panel.add(counterSLTField, c);

		c.gridy++;
		panel.add(counterSLLField, c);

		c.gridy++;
		panel.add(counterSRLField, c);

		c.gridy++;
		panel.add(counterSUBField, c);

		c.gridy++;
		panel.add(counterOTHERField, c);

		// Labels
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 1;
		c.gridwidth = 2;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 17, 0);
		panel.add(new JLabel("Total R-Type Instructions: "), c);

		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 2;
		c.gridwidth = 1;

		c.gridy++;
		panel.add(new JLabel("Add: "), c);

		c.gridy++;
		panel.add(new JLabel("Bitwise AND: "), c);

		c.gridy++;
		panel.add(new JLabel("Divide: "), c);

		c.gridy++;
		panel.add(new JLabel("Jump to Address in Register: "), c);

		c.gridy++;
		panel.add(new JLabel("Multiply: "), c);

		c.gridy++;
		panel.add(new JLabel("Bitwise NOR (NOT-OR): "), c);

		c.gridy++;
		panel.add(new JLabel("Bitwise XOR (Exclusive-OR): "), c);

		c.gridy++;
		panel.add(new JLabel("Bitwise OR: "), c);

		c.gridy++;
		panel.add(new JLabel("Set to 1 if Less Than: "), c);

		c.gridy++;
		panel.add(new JLabel("Logical Shift Left: "), c);

		c.gridy++;
		panel.add(new JLabel("Logical Shift Right (0-extended): "), c);

		c.gridy++;
		panel.add(new JLabel("Subtract: "), c);

		c.gridy++;
		panel.add(new JLabel("Other: "), c);

		// Progress bars
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 4;
		c.gridy = 2;

		panel.add(progressbarADD, c);

		c.gridy++;
		panel.add(progressbarAND, c);

		c.gridy++;
		panel.add(progressbarDIV, c);

		c.gridy++;
		panel.add(progressbarJR, c);

		c.gridy++;
		panel.add(progressbarMULT, c);

		c.gridy++;
		panel.add(progressbarNOR, c);

		c.gridy++;
		panel.add(progressbarXOR, c);

		c.gridy++;
		panel.add(progressbarOR, c);

		c.gridy++;
		panel.add(progressbarSLT, c);

		c.gridy++;
		panel.add(progressbarSLL, c);

		c.gridy++;
		panel.add(progressbarSRL, c);

		c.gridy++;
		panel.add(progressbarSUB, c);

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

			if (_mnemonics.equals("add"))
				counterADD++;
			else if (_mnemonics.equals("and"))
				counterAND++;
			else if (_mnemonics.equals("div"))
				counterDIV++;
			else if (_mnemonics.equals("jr"))
				counterJR++;
			else if (_mnemonics.equals("mult"))
				counterMULT++;
			else if (_mnemonics.equals("nor"))
				counterNOR++;
			else if (_mnemonics.equals("xor"))
				counterXOR++;
			else if (_mnemonics.equals("or"))
				counterOR++;
			else if (_mnemonics.equals("slt"))
				counterSLT++;
			else if (_mnemonics.equals("sll"))
				counterSLL++;
			else if (_mnemonics.equals("srl"))
				counterSRL++;
			else if (_mnemonics.equals("sub"))
				counterSUB++;
			else if (format == BasicInstructionFormat.R_FORMAT)
				counterOTHER++;

			if (format == BasicInstructionFormat.R_FORMAT)
				counter++;
		} catch (AddressErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateDisplay();
	}

	// @Override
	protected void initializePreGUI() {
		counter = counterADD = counterAND = counterDIV = counterJR = counterMULT = counterSUB = 0;
		counterNOR = counterXOR = counterOR = counterSLT = counterSLL = counterSRL = counterOTHER = 0;
		lastAddress = -1;
	}

	// @Override
	protected void reset() {
		counter = counterADD = counterAND = counterDIV = counterJR = counterMULT = counterSUB = 0;
		counterNOR = counterXOR = counterOR = counterSLT = counterSLL = counterSRL = counterOTHER = 0;
		lastAddress = -1;
		updateDisplay();
	}

	// @Override
	protected void updateDisplay() {
		counterField.setText(String.valueOf(counter));

		counterADDField.setText(String.valueOf(counterADD));
		progressbarADD.setMaximum(counter);
		progressbarADD.setValue(counterADD);

		counterANDField.setText(String.valueOf(counterAND));
		progressbarAND.setMaximum(counter);
		progressbarAND.setValue(counterAND);

		counterDIVField.setText(String.valueOf(counterDIV));
		progressbarDIV.setMaximum(counter);
		progressbarDIV.setValue(counterDIV);

		counterJRField.setText(String.valueOf(counterJR));
		progressbarJR.setMaximum(counter);
		progressbarJR.setValue(counterJR);

		counterMULTField.setText(String.valueOf(counterMULT));
		progressbarMULT.setMaximum(counter);
		progressbarMULT.setValue(counterMULT);

		counterNORField.setText(String.valueOf(counterNOR));
		progressbarNOR.setMaximum(counter);
		progressbarNOR.setValue(counterNOR);

		counterXORField.setText(String.valueOf(counterXOR));
		progressbarXOR.setMaximum(counter);
		progressbarXOR.setValue(counterXOR);

		counterORField.setText(String.valueOf(counterOR));
		progressbarOR.setMaximum(counter);
		progressbarOR.setValue(counterOR);

		counterSLTField.setText(String.valueOf(counterSLT));
		progressbarSLT.setMaximum(counter);
		progressbarSLT.setValue(counterSLT);

		counterSLLField.setText(String.valueOf(counterSLL));
		progressbarSLL.setMaximum(counter);
		progressbarSLL.setValue(counterSLL);

		counterSRLField.setText(String.valueOf(counterSRL));
		progressbarSRL.setMaximum(counter);
		progressbarSRL.setValue(counterSRL);

		counterSUBField.setText(String.valueOf(counterSUB));
		progressbarSUB.setMaximum(counter);
		progressbarSUB.setValue(counterSUB);

		counterOTHERField.setText(String.valueOf(counterOTHER));
		progressbarOTHER.setMaximum(counter);
		progressbarOTHER.setValue(counterOTHER);

		if (counter == 0) {
			progressbarADD.setString("0%");
			progressbarAND.setString("0%");
			progressbarDIV.setString("0%");
			progressbarJR.setString("0%");
			progressbarMULT.setString("0%");
			progressbarNOR.setString("0%");
			progressbarXOR.setString("0%");
			progressbarOR.setString("0%");
			progressbarSLT.setString("0%");
			progressbarSLL.setString("0%");
			progressbarSRL.setString("0%");
			progressbarSUB.setString("0%");
			progressbarOTHER.setString("0%");
		} else {
			progressbarADD.setString((counterADD * 100) / counter + "%");
			progressbarAND.setString((counterAND * 100) / counter + "%");
			progressbarDIV.setString((counterDIV * 100) / counter + "%");
			progressbarJR.setString((counterJR * 100) / counter + "%");
			progressbarMULT.setString((counterMULT * 100) / counter + "%");
			progressbarNOR.setString((counterNOR * 100) / counter + "%");
			progressbarXOR.setString((counterXOR * 100) / counter + "%");
			progressbarOR.setString((counterOR * 100) / counter + "%");
			progressbarSLT.setString((counterSLT * 100) / counter + "%");
			progressbarSLL.setString((counterSLL * 100) / counter + "%");
			progressbarSRL.setString((counterSRL * 100) / counter + "%");
			progressbarSUB.setString((counterSUB * 100) / counter + "%");
			progressbarOTHER.setString((counterOTHER * 100) / counter + "%");
		}
	}
}
