package com.seaglasslookandfeel.demo;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

public class TableTest 
{
	public class MyRenderer extends DefaultTableCellRenderer 
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{			
			if (value == null) 
				return this;
			
			Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if(column % 2 == 1)
				renderer.setBackground(Color.green);
			else 
				renderer.setBackground(Color.blue);
			
			return renderer;
		}
	}
	
	TableTest(String args[])
	{
		try {
			
			if(args.length > 0)
				UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			
			String headers[] = { "col 1", "col 2" };
			String rows[][] = {{"1","2"}, {"2","3"}, {"3","4"}, {"4","5"}};
			
			JFrame frame = new JFrame();		
			JTable table = new JTable(rows, headers);
			table.setDefaultRenderer(java.lang.Object.class, new MyRenderer());
			
			JScrollPane scrollPane = new JScrollPane(table);
			frame.add(scrollPane, BorderLayout.CENTER);
			frame.setSize(300, 150);
			frame.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static void main(String args[]) 
	{		
		new TableTest(args);
	}
}