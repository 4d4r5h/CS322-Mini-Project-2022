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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import mars.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mars.ProgramStatement;
import mars.mips.hardware.AccessNotice;
import mars.mips.hardware.AddressErrorException;
import mars.mips.hardware.Memory;
import mars.mips.hardware.MemoryAccessNotice;
import mars.mips.instructions.BasicInstruction;
import mars.mips.instructions.BasicInstructionFormat;
import mars.mips.hardware.RegisterFile;

// @SuppressWarnings("serial")
public class ChangeInRegister extends AbstractMarsToolAndApplication {
    private static String name = "Change in Register's Value";
    private static String version = "Version 1.0 (Adarsh Kumar)";
    private static String heading = "Displaying The Initial & Final Value Of The Register";

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
    public ChangeInRegister(String title, String heading) {
        super(title, heading);
    }

    /**
     * Simple construction, likely used by the MARS Tools menu mechanism.
     */
    public ChangeInRegister() {
        super(name + ", " + version, heading);
    }

    // @Override
    public String getName() {
        return name;
    }

    private JTextArea message = new JTextArea();
    private String code;

    // @Override
    protected JComponent buildMainDisplayArea() {
        message.setEditable(false);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        message.setFont(new Font("Ariel", Font.PLAIN, 14));
        message.setRows(30);
        message.setColumns(75);
        message.setCaretPosition(0); // Assure first line is visible and at top of scroll pane.
        return new JScrollPane(message);
    }

    // @Override
    protected void addAsObserver() {
        addAsObserver(Memory.textBaseAddress, Memory.textLimitAddress);
    }

    public static int getDecimal(int binary) {
        int decimal = 0;
        int n = 0;
        while (true) {
            if (binary == 0) {
                break;
            } else {
                int temp = binary % 10;
                decimal += temp * Math.pow(2, n);
                binary = binary / 10;
                n++;
            }
        }
        return decimal;
    }

    public static String rightPadding(String input, char ch, int L) {
        String result = String

                // First right pad the string with space up to length L
                .format("%" + (-L) + "s", input)

                // Then replace all the spaces with the given character ch
                .replace(' ', ch);

        // Return the resultant string
        return result;
    }

    public static String centerPadding(String str, char paddingChar, int maxPadding) {
        int length = str.length();
        int padding = (maxPadding - length) / 2; // Decide left and right padding
        if (padding <= 0) {
            return str; // Return actual String if padding is less than or equal to 0
        }

        String empty = "", hash = "#"; // Hash is used as a place holder

        // Extra character in case of String with even length
        int extra = (length % 2 == 0) ? 1 : 0;

        String leftPadding = "%" + padding + "s";
        String rightPadding = "%" + (padding - extra) + "s";

        String strFormat = leftPadding + "%s" + rightPadding;
        String formattedString = String.format(strFormat, empty, hash, empty);

        // Replace space with * and hash with the provided String
        String paddedString = formattedString.replace(' ', paddingChar).replace(hash, str);
        return paddedString;
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
        post_instruction_execution();
        lastAddress = a;
        try {
            ProgramStatement stmt = Memory.getInstance().getStatement(a);
            BasicInstruction instr = (BasicInstruction) stmt.getInstruction();
            BasicInstructionFormat format = instr.getInstructionFormat();
            String source = stmt.getSource().trim();
            String machineStatement = stmt.getMachineStatement().trim();
            code += centerPadding(machineStatement, ' ', 32) + "       " + rightPadding(source, ' ', 25);

            if (format == BasicInstructionFormat.R_FORMAT ||
                    format == BasicInstructionFormat.I_FORMAT) {

                int k = 16;
                if (format == BasicInstructionFormat.I_FORMAT)
                    k = 11;
                String regnumber = "";
                for (int i = 0; i < machineStatement.length(); i++) {
                    if (i >= k && i <= k + 4) {
                        regnumber += machineStatement.charAt(i);
                    }
                }

                int binarycode = Integer.parseInt(regnumber);
                int n = getDecimal(binarycode);
                int val = RegisterFile.getValue(n);
                String name = RegisterFile.getName(n).trim();
                code += "       ";
                code += rightPadding(name + " " + String.valueOf(val), ' ', 25);
            }
        } catch (AddressErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        updateDisplay();
    }

    private void post_instruction_execution() {
        if (lastAddress == -1)
            return;
        try {
            ProgramStatement stmt = Memory.getInstance().getStatement(lastAddress);
            BasicInstruction instr = (BasicInstruction) stmt.getInstruction();
            BasicInstructionFormat format = instr.getInstructionFormat();
            String source = stmt.getSource();
            String machineStatement = stmt.getMachineStatement();

            if (format == BasicInstructionFormat.R_FORMAT ||
                    format == BasicInstructionFormat.I_FORMAT) {

                int k = 16;
                if (format == BasicInstructionFormat.I_FORMAT)
                    k = 11;
                String regnumber = "";
                for (int i = 0; i < machineStatement.length(); i++) {
                    if (i >= k && i <= k + 4) {
                        regnumber += machineStatement.charAt(i);
                    }
                }

                int binarycode = Integer.parseInt(regnumber);
                int n = getDecimal(binarycode);
                int val = RegisterFile.getValue(n);
                String name = RegisterFile.getName(n).trim();
                code += "       ";
                code += rightPadding(name + " " + String.valueOf(val), ' ', 25);
            }
            code += "\n";

        } catch (AddressErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // @Override
    protected void initializePreGUI() {
        code = centerPadding("Instruction Code", ' ', 50) + "       " +
                centerPadding("Instruction", ' ', 25) + "       " +
                centerPadding("Initial Value", ' ', 25) + "       " +
                centerPadding("Updated Value", ' ', 25) +
                "\n\n";
        lastAddress = -1;
    }

    // @Override
    protected void reset() {
        message.setText("");
        code = centerPadding("Instruction Code", ' ', 50) + "       " +
                centerPadding("Instruction", ' ', 25) + "       " +
                centerPadding("Initial Value", ' ', 25) + "       " +
                centerPadding("Updated Value", ' ', 25) +
                "\n\n";
        lastAddress = -1;
        updateDisplay();
    }

    // @Override
    protected void updateDisplay() {
        message.setText(code);
    }
}
