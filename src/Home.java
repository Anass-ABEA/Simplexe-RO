/**
 This is a Java based application mad by Anass AIT BEN EL ARBI 
 Transformed from a console based application developped using Python to a Java with GUI
  **/




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Home {

	private JFrame frmProgrammationLinaire;
	private int x;
	
	private ArrayList<ArrayList> list;
	private JTextField var_count;
	private JTextField eq_count;
	private ArrayList<Float> max_min= null;
	private ArrayList<Float> inf_sup= null;
	private ArrayList<ArrayList> matrix= null;
	private boolean maximize = true;
	private ArrayList<ArrayList> list_addit= null;
	private ArrayList<Integer> cmb_box=null;
	private String comment ="";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home window = new Home(2,2);
					window.frmProgrammationLinaire.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home(int x, int y) {
		initialize(x,y);
	}
	public Home(int x, int y,ArrayList<ArrayList> listx, ArrayList<Float> max_min2, ArrayList<Float> inf_sup2, ArrayList<Integer> cmbbox) {
		
		this.matrix = listx;
		this.max_min = max_min2;
		this.inf_sup= inf_sup2;
		this.cmb_box=cmbbox;
		initialize(x,y);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int var, int eqt) {
		frmProgrammationLinaire = new JFrame();
		frmProgrammationLinaire.setTitle("programmation Lin\u00E9aire - LE PL -");
		frmProgrammationLinaire.setBounds(100, 100, 1151, 741);
		
		frmProgrammationLinaire.getContentPane().setLayout(null);
		this.x= var;
		JPanel panel = new JPanel();
		panel.setBounds(12, 83, 1109, 453);
		frmProgrammationLinaire.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));


		JLabel msg = new JLabel("Votre PL PRIMAL");
		msg.setFont(new Font("Tahoma", Font.BOLD, 15));
		msg.setBounds(790, 7, 230, 39);
		frmProgrammationLinaire.getContentPane().add(msg);
		msg.setVisible(false);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		
		JButton btnNewButton_2 = new JButton("Primal => Dual");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msg.setVisible(true);
				set_cmbboxes(var,eqt);
				fillTheMatrix();
				
				force_transpose(var,eqt);
				
				//System.out.println(list_forced.size()+" "+ list_forced.get(0).size()+" "+list_forced.get(1).size());
				//new Home(var,eqt,list_forced,this);
			}
		});
		btnNewButton_2.setBounds(483, 634, 162, 47);
		frmProgrammationLinaire.getContentPane().add(btnNewButton_2);
		
		JButton solve = new JButton("R\u00E9soudre le PL");
		solve.setBounds(959, 634, 162, 47);
		frmProgrammationLinaire.getContentPane().add(solve);
		solve.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "La résolution se fait uniquement pour le probleme\n de maximisation et pour toutes les contraitnes <=", "Resultat !", JOptionPane.INFORMATION_MESSAGE);
				fillTheMatrix();
				solve_now();
				
			}
			
		});
		
		JButton optimal = new JButton("V\u00E9rifier l'optimalit\u00E9");
		optimal.setBounds(12, 634, 162, 47);
		frmProgrammationLinaire.getContentPane().add(optimal);
		optimal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int xx = getvar_count();
				float[] testScore = new float[xx];
				for (int i = 0; i < testScore.length; i++) {
				   testScore[i] = getfloatTB(JOptionPane.showInputDialog("Entrez le  X" + (i+1) + ": "));
				}
				fillTheMatrix();
				verify_optimum(testScore);
				
			}
			
		});
		
		
		
		
		
		
		
		
		

		if (this.matrix!=null && this.max_min != null && this.maximize && inf_sup !=null) {
			frmProgrammationLinaire.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			msg.setVisible(true);
			msg.setText("Votre PL DUAL");
			ArrayList<Integer> cmb_box_new = new ArrayList<Integer>();
			
			cmb_box_new.add(0, 1-this.cmb_box.get(0));
			
			for (int i = var+1; i <this.cmb_box.size() ; i++) {
				cmb_box_new.add(this.cmb_box.get(i));
			}
			btnNewButton_2.setVisible(false);
			optimal.setVisible(false);
			solve.setVisible(false);
			for (int i = 1; i <=var ; i++) {
				if (this.cmb_box.get(i)==1) {
					cmb_box_new.add(2);
				}else {
					if (this.cmb_box.get(i)==2) {
						cmb_box_new.add(1);
					}else {
						cmb_box_new.add(0);
					}
				}
				
			}
			this.cmb_box= cmb_box_new;
			System.out.println(this.cmb_box.size());
			fill_grid(panel_1,var,eqt,"Y");
			fill_boxes(panel_1,var,eqt);
			set_cmbboxes(var,eqt);
		}else {
			frmProgrammationLinaire.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			fill_grid(panel_1,var,eqt,"X");
			
		}
		
		
		
		//fill(panel_1,x,y);
		if(eqt>6) {
			panel_1.setLayout(new GridLayout(0, (var+1)*2, 0, 5));
		}else {
			panel_1.setLayout(new GridLayout(0, (var+1)*2, 0, 30));
		}
		
		
		JLabel lblNewLabel = new JLabel("Nombre de variables");
		lblNewLabel.setBounds(58, 13, 169, 29);
		frmProgrammationLinaire.getContentPane().add(lblNewLabel);
		
		var_count = new JTextField();
		var_count.setText(""+var);
		var_count.setBounds(193, 8, 65, 39);
		frmProgrammationLinaire.getContentPane().add(var_count);
		var_count.setColumns(10);
		
		eq_count = new JTextField();
		eq_count.setText(""+eqt);
		eq_count.setColumns(10);
		eq_count.setBounds(457, 8, 65, 39);
		frmProgrammationLinaire.getContentPane().add(eq_count);
		
		JLabel lblNombreDquations = new JLabel("Nombre d'\u00E9quations");
		lblNombreDquations.setBounds(321, 18, 169, 29);
		frmProgrammationLinaire.getContentPane().add(lblNombreDquations);
		
		
		
		
		JButton btnNewButton_1 = new JButton("Appliquer");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = var_count.getText();
				//panel_1.removeAll();
				//panel_1.repaint();
				boolean set1=false;
				boolean set2=false;
				int r1 = 0;
				if (text.length()>0) {
					
						
					r1 = getInteger(text);
						
						
					if(r1== -1){
						var_count.setBorder(new LineBorder(Color.RED, 2, true));
						set1=false;
					}else {
						sety(r1);
						var_count.setText(""+r1);
						set1=true;
					}
						
					
				}
				int r2 = 0 ;
				String text2 = eq_count.getText();
				if (text2.length()>0) {
				 r2 = getInteger(text2);
					
					
					if(r2 == -1){
						eq_count.setBorder(new LineBorder(Color.RED, 2, true));
						set2=false;
					}else {
						setx(r2);
						eq_count.setText(""+r2);
						set2=true;
					}
						
				
			}
				
				System.out.println(set1+" "+set2);
				if(set1&&set2) {
						
						openNew(r1,r2);
				}else {
					if(set1) {
						//ffrmProgrammationLinaire.dispose();
						openNew(3,r1);
					}
					if(set2) {
						//ffrmProgrammationLinaire.dispose();
						openNew(r2,3);
					}
				}
				panel_1.setVisible(true);
		}

			private int getInteger(String text) {
				String num="";
				for (char s :text.toCharArray() ) {
					if ("0123456789".contains(String.valueOf(s))) {
						num=num+String.valueOf(s);
					}
				}
				if (num.length()==0) {
					return -1;
				}
				return Integer.parseInt(num);
			}
				
			});
		btnNewButton_1.setBounds(548, 8, 97, 39);
		frmProgrammationLinaire.getContentPane().add(btnNewButton_1);
		
		JButton example = new JButton("Exemple");
		example.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fill_matrix(5,4);
			}
		});
		
		example.setVisible(false);
		if (var == 5 && eqt == 4) {
			example.setVisible(true);
		}
		example.setBounds(1024, 15, 97, 55);
		frmProgrammationLinaire.getContentPane().add(example);
		frmProgrammationLinaire.setVisible(true);
		//fill_matrix2(var, eqt);
	}

	protected void solve_now() {
		
		
		
		double[] c = new double[this.max_min.size()];
		int index = 0;
		for (float f : this.max_min) {
			c[index]= (double) f;
			index++;
		}
		double[] b = new double[this.inf_sup.size()];
		index = 0;
		for (float f : this.inf_sup) {
			b[index]= (double) f;
			index++;
		}
		double[][] a = new double[this.matrix.size()][this.matrix.get(0).size()];
		index = 0;
		
		for (ArrayList<Float> f : this.matrix) {
			for (int e = 0; e <f.size();e++) {
				a[index][e] = f.get(e);
			}
			index ++;
		}
		String res= LinearProgramming.test(a,b,c);
		
		JOptionPane.showMessageDialog(null, res, "Resultat !", JOptionPane.INFORMATION_MESSAGE);
	}

	protected void verify_optimum(float[] testScore) {
		comment = "";
		ArrayList<Integer> yi_nulles = new ArrayList<Integer>();
		ArrayList<Integer> saturee = new ArrayList<Integer>();
		for (int i = 0; i<this.matrix.size();i++) {
			System.out.print("variable de test = "+testScore[i]);
			float sum = 0;
			for (int j = 0; j<this.matrix.get(0).size();j++) {
				sum = sum + ((float)this.matrix.get(i).get(j))*(testScore[j]);
			}
			System.out.println("  l'équations a une sol = "+sum);
			if (Math.abs(sum - this.inf_sup.get(i))<0.001) {
				
			}else {
				yi_nulles.add(i);
			}
		}
		comment=comment+"\t";
		for (int j = 0;j<yi_nulles.size();j++) {
			comment=comment + "L'équation "+(yi_nulles.get(j)+1)+" n'est pas saturé donc Y"+(yi_nulles.get(j)+1)+"=0\n";
		}
		for (int j = 0;j<testScore.length;j++) {
			
			if(testScore[j]>0.0) {
				saturee.add(j);
			}
			
		}
		comment=comment+"\t";
		for (int j = 0;j<saturee.size();j++) {
			comment=comment + "X"+(saturee.get(j)+1)+" est >0 donc la contrainte "+(saturee.get(j)+1)+" est saturée\n";
		}
		ArrayList<ArrayList> matrix_transpose = transpose(this.matrix);
		//ArrayList<ArrayList> solve_this = new ArrayList<ArrayList>();
		double[][] solve_this = new double[saturee.size()][this.matrix.size()-yi_nulles.size()];
		int f = 0;
		for (int i = 0; i<matrix_transpose.size();i++) {
			int g = 0;
			if (saturee.contains(i)) {
			for (int j = 0; j<matrix_transpose.get(0).size();j++) {
				
					if (!yi_nulles.contains(j)) {
						solve_this[f][g]=(float) ( matrix_transpose.get(i).get(j));
						g++;
					}
					
				}
			f++;
			}
			
			
			
		}
		double[] b = new double[saturee.size()];
		int e = 0;
		for (int i :saturee) {
				
				b[e]=((double) this.max_min.get(i));
				e++;
		}
		System.out.println(comment);
		//show(solve_this);
		double[] sol = solve_problem(solve_this,b);
		//show(solve_this);
		//System.out.println(comment);
		double[] sol_totale = new double[this.max_min.size()];
		
		for(int index = 0; index <yi_nulles.size();index++) {
			System.out.println("yi nulles :"+yi_nulles.get(index));
		}
		
		
		int r = 0;
		for (int g = 0; g <this.matrix.size();g++) {
			System.out.println("r = "+r);
			if(yi_nulles.contains(g)) {
				sol_totale[g]= 0;
			}else {
				sol_totale[g]= sol[r]; 
				r++;
			}
		}
		boolean broke = false;
		for (int i = 0; i<matrix_transpose.size();i++) {
			System.out.print("variable de test = "+sol_totale[i]);
			float sum = 0;
			for (int j = 0; j<matrix_transpose.get(0).size();j++) {
				sum = (float) (sum + ((float)matrix_transpose.get(i).get(j))*(sol_totale[j]));
			}
			
			int sel = ((JComboBox)(this.list.get(testScore.length+1).get(i))).getSelectedIndex();
			switch (sel) {
			case 0:
				System.out.print("compare to "+this.max_min.get(i));
				if (Math.abs(sum - this.max_min.get(i))<0.001) {
					
				}else {
					if (!broke) {
						comment = comment+"Une des équations ne vérifie pas la solution des Yi qui est (";
						for (int g = 0; g <sol_totale.length;g++) {
							comment=comment+sol_totale[g]+" ";
						} 
						comment= comment + ")";
						broke = true;
					}
					
				}
				break;
			case 1:
				System.out.print("compare to "+this.max_min.get(i));
				if (sum <= this.max_min.get(i)) {
					
				}else {
					if (!broke) {
						comment = comment+"Une des équations ne vérifie pas la solution des Yi qui est (";
						for (int g = 0; g <sol_totale.length;g++) {
							comment=comment+sol_totale[g]+" ";
						} 
						comment= comment + ")";
						broke = true;
					}
					
				}
				break;
			case 2:
				System.out.print("compare to "+this.max_min.get(i));
				if (sum >= this.max_min.get(i)) {
					
				}else {
					if (!broke) {
						comment = comment+"Une des équations ne vérifie pas la solution des Yi qui est (";
						for (int g = 0; g <sol_totale.length;g++) {
							comment=comment+sol_totale[g]+" ";
						} 
						comment= comment + ")";
						broke = true;
					}
					
				}
				break;
			}
			System.out.println("  l'équations a une sol = "+sum);
		}
		if (broke) {
			JOptionPane.showMessageDialog(null, comment, "Resultat !", JOptionPane.INFORMATION_MESSAGE);
		}else {
			
		
		float minW = calculateZ (inf_sup, sol_totale);
		float maxZ = calculateZ (max_min, testScore);
		
		if (Math.abs(minW-maxZ)<0.001) {
			JOptionPane.showMessageDialog(null, comment+"\nSolution est optimale\n Max Z = "+maxZ+"\nMin W = "+minW, "Resultat !", JOptionPane.INFORMATION_MESSAGE);
		}else {
			JOptionPane.showMessageDialog(null,  comment+"\nSolution n'est pas optimale\n Max Z = "+maxZ+"\nMin W = "+minW, "Resultat !", JOptionPane.INFORMATION_MESSAGE);
		}
		
		}
	}

	private float calculateZ(ArrayList<Float> max_min2, float[] testScore) {
		float res = 0;	
		
		for (int i = 0; i<max_min2.size();i++){
			res = (float) (res + max_min2.get(i)*testScore[i]);			
		}
		
		return res;
	}

	private float calculateZ(ArrayList<Float> inf_sup2, double[] sol_totale) {
		float res = 0;
		
		for (int i = 0; i<inf_sup2.size();i++){
			res = (float) (res + inf_sup2.get(i)*sol_totale[i]);			
		}
		
		return res;
	}

	private void fill_matrix(int var, int eqt) {
		ArrayList<Object> obj = this.list.get(0);
		
			((JTextField)(obj.get(1))).setText(""+7);
			((JTextField)(obj.get(2))).setText(""+6);
			((JTextField)(obj.get(3))).setText(""+5);
			((JTextField)(obj.get(4))).setText("-2");
			((JTextField)(obj.get(5))).setText(""+3);
		obj = this.list.get(1);
		((JTextField)(obj.get(0))).setText(""+1);
		((JTextField)(obj.get(1))).setText(""+3);
		((JTextField)(obj.get(2))).setText(""+5);
		((JTextField)(obj.get(3))).setText("-2");
		((JTextField)(obj.get(4))).setText("2");
		((JTextField)(obj.get(6))).setText(""+4);
		obj = this.list.get(2);
		((JTextField)(obj.get(0))).setText(""+4);
		((JTextField)(obj.get(1))).setText(""+2);
		((JTextField)(obj.get(2))).setText("-2");
		((JTextField)(obj.get(3))).setText("1");
		((JTextField)(obj.get(4))).setText("1");
		((JTextField)(obj.get(6))).setText(""+3);
		 obj = this.list.get(3);
		((JTextField)(obj.get(0))).setText(""+2);
		((JTextField)(obj.get(1))).setText(""+4);
		((JTextField)(obj.get(2))).setText("4");
		((JTextField)(obj.get(3))).setText("-2");
		((JTextField)(obj.get(4))).setText("5");
		((JTextField)(obj.get(6))).setText(""+5);
		 obj = this.list.get(4);
			((JTextField)(obj.get(0))).setText(""+3);
			((JTextField)(obj.get(1))).setText(""+1);
			((JTextField)(obj.get(2))).setText("2");
			((JTextField)(obj.get(3))).setText("-1");
			((JTextField)(obj.get(4))).setText("-2");
			((JTextField)(obj.get(6))).setText(""+1);
		
		
	}
	@SuppressWarnings("unchecked")
	private void fill_matrix2(int var, int eqt) {
		ArrayList<Object> obj = this.list.get(0);
		
			((JTextField)(obj.get(1))).setText(""+4);
			((JTextField)(obj.get(2))).setText(""+5);
			((JTextField)(obj.get(3))).setText(""+1);
			((JTextField)(obj.get(4))).setText("3");
			((JTextField)(obj.get(5))).setText("-5");
			((JTextField)(obj.get(6))).setText(""+8);
			obj = this.list.get(1);
			((JTextField)(obj.get(0))).setText(""+1);
			((JTextField)(obj.get(1))).setText("0");
			((JTextField)(obj.get(2))).setText("-4");
			((JTextField)(obj.get(3))).setText(""+3);
			((JTextField)(obj.get(4))).setText(""+1);
			((JTextField)(obj.get(5))).setText(""+1);
			((JTextField)(obj.get(7))).setText(""+1);
			obj = this.list.get(2);
			((JTextField)(obj.get(0))).setText(""+5);
			((JTextField)(obj.get(1))).setText(""+3);
			((JTextField)(obj.get(2))).setText(""+1);
			((JTextField)(obj.get(3))).setText("0");
			((JTextField)(obj.get(4))).setText("-5");
			((JTextField)(obj.get(5))).setText(""+3);
			((JTextField)(obj.get(7))).setText(""+4);
			
			 obj = this.list.get(3);
			((JTextField)(obj.get(0))).setText(""+4);
			((JTextField)(obj.get(1))).setText("5");
			((JTextField)(obj.get(2))).setText("-3");
			((JTextField)(obj.get(3))).setText("3");
			((JTextField)(obj.get(4))).setText("'-4");
			((JTextField)(obj.get(5))).setText("1");
			((JTextField)(obj.get(7))).setText(""+4);
			 obj = this.list.get(4);
				((JTextField)(obj.get(0))).setText("0");
				((JTextField)(obj.get(1))).setText("-1");
				((JTextField)(obj.get(2))).setText("0");
				((JTextField)(obj.get(3))).setText("2");
				((JTextField)(obj.get(4))).setText("1");
				((JTextField)(obj.get(5))).setText("-5");
				((JTextField)(obj.get(7))).setText(""+5);
				 obj = this.list.get(5);
					((JTextField)(obj.get(0))).setText("-2");
					((JTextField)(obj.get(1))).setText("1");
					((JTextField)(obj.get(2))).setText("1");
					((JTextField)(obj.get(3))).setText("1");
					((JTextField)(obj.get(4))).setText("2");
					((JTextField)(obj.get(5))).setText("2");
					((JTextField)(obj.get(7))).setText(""+7);
					 obj = this.list.get(6);
						((JTextField)(obj.get(0))).setText(""+2);
						((JTextField)(obj.get(1))).setText("-3");
						((JTextField)(obj.get(2))).setText("2");
						((JTextField)(obj.get(3))).setText("-1");
						((JTextField)(obj.get(4))).setText("4");
						((JTextField)(obj.get(5))).setText(""+5);
						((JTextField)(obj.get(7))).setText(""+5);
		
		
	}

	private double[] solve_problem(double[][] solve_this, double[] b) {
		RealVector solution=null;
		try {
			RealMatrix coefficients =new Array2DRowRealMatrix(solve_this,false);
			DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
			RealVector constants = new ArrayRealVector(b, false);
			solution = solver.solve(constants);
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Impossible de résoudre le problème\n nombre de variable > nombre d'équations", "ERREUR !", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		double[]  sol = new double[b.length];
		for (int xx = 0; xx<solution.getDimension();xx++) {
			sol[xx]= solution.getEntry(xx);
		}
		return sol;
	}

	protected int getvar_count() {
		return this.x;
		
	}

	protected void set_cmbboxes(int var, int eqt) {
		this.cmb_box= new ArrayList<Integer>();
		this.cmb_box.add((((JComboBox)(this.list.get(0).get(0))).getSelectedIndex()));
		for (int i = 0; i <eqt ; i++) {
			this.cmb_box.add((((JComboBox)(this.list.get(i+1).get(var))).getSelectedIndex()));
		}
		for (int i = 0; i <var ; i++) {
			this.cmb_box.add((((JComboBox)(this.list.get(eqt+1).get(i))).getSelectedIndex()));
		}
		
	}

	private void fill_boxes(JPanel panel_1,int var,int r1) {
		int count_cmb = 0;
		((JComboBox)(this.list.get(0).get(0))).setSelectedIndex(this.cmb_box.get(count_cmb));
		count_cmb++;
		for (int e = 0; e<this.inf_sup.size() ;e++) {
			((JTextField)(this.list.get(0).get(1+e))).setText(""+this.inf_sup.get(e));
			//primary.add(getfloatTB(str));
			
		
		}
		//this.max_min=primary;
		for(int e = 1; e<this.list.size() -1;e++) {
			
			ArrayList<Object> obj =this.list.get(e);
			for(int f = 0; f<obj.size() -2;f++) {
				((JTextField)(obj.get(f))).setText(""+this.matrix.get(e-1).get(f));
				//temp.add(getfloatTB(str));
			}
			((JComboBox)(obj.get(obj.size()-2))).setSelectedIndex(this.cmb_box.get(count_cmb));
			count_cmb++;
			
			((JTextField)(obj.get(obj.size()-1))).setText(""+this.max_min.get(e-1));
			//secondary.add(getfloatTB(str));
			//lst.add(temp);
		}
		for(int e = 0; e<var;e++) {
			int c = this.cmb_box.get(count_cmb);
			
			((JComboBox)(this.list.get(r1+1).get(e))).setSelectedIndex(c);
			count_cmb++;
			
		}
		/*
		ArrayList<ArrayList> liste = this.list_addit;
		
		
		
		panel_1.add(((JComboBox)list_addit.get(0).get(0)));
		
		
		JLabel lbl = new JLabel("=");
		lbl.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lbl);
		
		JLabel lblNewLabel_1_3;
		for (int e = 0;e<var;e++) {
			
			
			panel_1.add(((JTextField)(liste.get(0).get(e+1))));
			
			
			
			lblNewLabel_1_3 = new JLabel("X"+(e+1));
			lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
			panel_1.add(lblNewLabel_1_3);
		}
		
		
		for (int i = 0; i<r1;i++) {
			for (int e = 0;e<var;e++) {
				
				System.out.println(((JTextField)list.get(e+1).get(i)).getText());
				panel_1.add(((JTextField)list.get(e+1).get(i)));
				
				
				
				lblNewLabel_1_3 = new JLabel("X"+(e+1));
				lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
				panel_1.add(lblNewLabel_1_3);
			}
			
			
			panel_1.add(((JComboBox)list.get(i+1).get(var)));
			
			
			
			panel_1.add(((JTextField)list.get(i+1).get(var+1)));
			
			
		}
		lblNewLabel_1_3 = new JLabel("cndt : ");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblNewLabel_1_3);
		for (int e = 0;e<var;e++) {
			
			lblNewLabel_1_3 = new JLabel("X"+(e+1)+"  ");
			lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.RIGHT);
			panel_1.add(lblNewLabel_1_3);
			
			
			panel_1.add(((JComboBox)list.get(r1+1).get(e)));
			
		}
		lblNewLabel_1_3 = new JLabel("");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblNewLabel_1_3);
		

		
		panel_1.repaint();
		
		*/
		
		
	}

	protected void force_transpose(int var, int eqt) {
		ArrayList<ArrayList> temp = prepare_list(eqt,var);
		ArrayList<ArrayList> matrix_transpose= transpose(this.matrix);
		ArrayList<Integer> temps = new ArrayList<Integer>();
		set_cmbboxes(var,eqt);
		new Home(eqt,var,matrix_transpose,this.max_min,this.inf_sup,this.cmb_box);
		
		
		
		
	}

	private ArrayList<ArrayList>  transpose(ArrayList<ArrayList> table) {
		ArrayList<ArrayList> ret = new ArrayList<ArrayList>();
	        final int N = table.get(0).size();
	        for (int i = 0; i < N; i++) {
	        	ArrayList<Float> col = new ArrayList<Float>();
	            for (ArrayList<Float> row : table) {
	                col.add(row.get(i));
	            }
	            ret.add(col);
	        }
	        return ret;
	}

	protected void fillTheMatrix() {
		ArrayList<ArrayList> lst = new ArrayList<ArrayList>();
		ArrayList<Float> secondary = new ArrayList<Float>();
		ArrayList<Float> primary = new ArrayList<Float>();
		
		for (int e = 1; e<this.list.get(0).size() ;e++) {
			String str = ((JTextField)(this.list.get(0).get(e))).getText();
			primary.add(getfloatTB(str));
			
		
		}
		this.max_min=primary;
		for(int e = 1; e<this.list.size() -1;e++) {
			ArrayList<Float> temp = new ArrayList<Float>();
			ArrayList<Object> obj =list.get(e);
			for(int f = 0; f<obj.size() -2;f++) {
				String str = ((JTextField)(obj.get(f))).getText();
				temp.add(getfloatTB(str));
			}
			String str = ((JTextField)(obj.get(obj.size()-1))).getText();
			secondary.add(getfloatTB(str));
			lst.add(temp);
		}
		this.inf_sup=secondary;
		this.matrix = lst;
		
		
	}
	private void show(double[][] solve_this) {
		for (double[] lst : solve_this) {
			for (double f : lst) {
				
				System.out.print(f+ " ");
			}
			System.out.println("");
		}
		
	}

	protected float getfloatTB(String str) {
		String s = "0";
		if (str.length()==0) {
			return 0;
		}
		String d  = "";
		
		boolean divide = false;
		for (char c: str.toCharArray()) {
			
			if("\\".equals(String.valueOf(c)) ||"/".equals(String.valueOf(c)) ) {
				divide = true;
			}
			if ("0123456789.".contains(String.valueOf(c))) {
				
					if (divide) {
						d=d+String.valueOf(c);
					}else {
						s=s+String.valueOf(c);
					}
				
			}
		}
		if(divide) {
			float x = Float.parseFloat(s);
			float y = Float.parseFloat(d);
			if(str.contains("-")) {
				return - x/y;
			}else {
				return x/y;
			}
			
		}else {
			if(str.contains("-")) {
				return - Float.parseFloat(s);
			}else {
				return Float.parseFloat(s);
			}
			
		}
		
		
	}

	protected void openNew(int r1, int r2) {
		frmProgrammationLinaire.dispose();
		new Home(r1,r2);
		
	}

	protected void fill_grid(JPanel panel_1, int x2, int r1,String var_name) {
		ArrayList<ArrayList> liste = new ArrayList<ArrayList>();
		ArrayList<Object> elements ;
		elements = new ArrayList<Object>();
		elements.add( new JComboBox());
		if ("X".equals(var_name)) {
			((JComboBox)elements.get(elements.size()-1)).setModel(new DefaultComboBoxModel(new String[] {"Max Z", "Min Z"}));
		}else {
			((JComboBox)elements.get(elements.size()-1)).setModel(new DefaultComboBoxModel(new String[] {"Max W", "Min W"}));
		}
		
		((JComboBox)elements.get(elements.size()-1)).setSelectedIndex(0);
		panel_1.add(((JComboBox)elements.get(elements.size()-1)));
		
		
		JLabel lbl = new JLabel("=");
		lbl.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lbl);
		
		JLabel lblNewLabel_1_3;
		for (int e = 0;e<x2;e++) {
			elements.add( new JTextField());
			
			panel_1.add(((JTextField)elements.get(elements.size()-1)));
			((JTextField)elements.get(elements.size()-1)).setColumns(10);
			
			
			lblNewLabel_1_3 = new JLabel(var_name+(e+1));
			lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
			panel_1.add(lblNewLabel_1_3);
		}
		liste.add(elements);
		elements = new ArrayList<Object>();
		for (int i = 0; i<r1;i++) {
			for (int e = 0;e<x2;e++) {
				elements.add( new JTextField());
				
				panel_1.add(((JTextField)elements.get(elements.size()-1)));
				((JTextField)elements.get(elements.size()-1)).setColumns(10);
				
				
				lblNewLabel_1_3 = new JLabel(var_name+(e+1));
				lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
				panel_1.add(lblNewLabel_1_3);
			}
			
			elements.add( new JComboBox());
			((JComboBox)elements.get(elements.size()-1)).setModel(new DefaultComboBoxModel(new String[] {"=","<=", ">="}));
			((JComboBox)elements.get(elements.size()-1)).setSelectedIndex(0);
			panel_1.add(((JComboBox)elements.get(elements.size()-1)));
			
			elements.add( new JTextField());
			
			panel_1.add(((JTextField)elements.get(elements.size()-1)));
			((JTextField)elements.get(elements.size()-1)).setColumns(10);
			liste.add(elements);
			elements = new ArrayList<Object>();
			
		}
		lblNewLabel_1_3 = new JLabel("cndt : ");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblNewLabel_1_3);
		for (int e = 0;e<x2;e++) {
			
			lblNewLabel_1_3 = new JLabel(var_name+(e+1)+"  ");
			lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.RIGHT);
			panel_1.add(lblNewLabel_1_3);
			
			
			elements.add(new JComboBox());
			((JComboBox)elements.get(elements.size()-1)).setModel(new DefaultComboBoxModel(new String[] {"qlq","<=0", ">=0"}));
			((JComboBox)elements.get(elements.size()-1)).setSelectedIndex(0);
			
			panel_1.add(((JComboBox)elements.get(elements.size()-1)));
			
		}
		lblNewLabel_1_3 = new JLabel("");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblNewLabel_1_3);
		
		
		liste.add(elements);
		elements = new ArrayList<Object>();
		
		panel_1.repaint();
		
		this.list = liste;
	}
	
	
	protected ArrayList<ArrayList> prepare_list(int x2, int r1) {
		ArrayList<ArrayList> liste = new ArrayList<ArrayList>();
		ArrayList<Object> elements ;
		elements = new ArrayList<Object>();
		elements.add( new JComboBox());
		((JComboBox)elements.get(elements.size()-1)).setModel(new DefaultComboBoxModel(new String[] {"Max Z", "Min Z"}));
		((JComboBox)elements.get(elements.size()-1)).setSelectedIndex(0);
		
		
		
		JLabel lbl = new JLabel("=");
		lbl.setHorizontalAlignment(SwingConstants.LEFT);
		
		
		JLabel lblNewLabel_1_3;
		for (int e = 0;e<x2;e++) {
			elements.add( new JTextField());
			
			
			((JTextField)elements.get(elements.size()-1)).setColumns(10);
			
			
			lblNewLabel_1_3 = new JLabel("X"+(e+1));
			lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
			
		}
		liste.add(elements);
		elements = new ArrayList<Object>();
		for (int i = 0; i<r1;i++) {
			for (int e = 0;e<x2;e++) {
				elements.add( new JTextField());
				
				
				((JTextField)elements.get(elements.size()-1)).setColumns(10);
				
				
				lblNewLabel_1_3 = new JLabel("X"+(e+1));
				lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
				
			}
			
			elements.add( new JComboBox());
			((JComboBox)elements.get(elements.size()-1)).setModel(new DefaultComboBoxModel(new String[] {"=","<=", ">="}));
			((JComboBox)elements.get(elements.size()-1)).setSelectedIndex(0);
			
			
			elements.add( new JTextField());
			
			
			((JTextField)elements.get(elements.size()-1)).setColumns(10);
			liste.add(elements);
			elements = new ArrayList<Object>();
			
		}
		lblNewLabel_1_3 = new JLabel("cndt : ");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
		
		for (int e = 0;e<x2;e++) {
			
			lblNewLabel_1_3 = new JLabel("X"+(e+1)+"  ");
			lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.RIGHT);
			
			
			
			elements.add(new JComboBox());
			((JComboBox)elements.get(elements.size()-1)).setModel(new DefaultComboBoxModel(new String[] {"qlq","<=0", ">=0"}));
			((JComboBox)elements.get(elements.size()-1)).setSelectedIndex(0);
			
			
			
		}
		lblNewLabel_1_3 = new JLabel("");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.LEFT);
		
		
		liste.add(elements);
		
		
		
		
		return liste;
	}

	protected void setx(int parseInt) {
		this.x=parseInt;
		eq_count.setBorder((new JTextField()).getBorder());
	}

	protected void sety(int parseInt) {
		var_count.setBorder((new JTextField()).getBorder());
	}

	private void fill(JPanel panelx, int x2, int y2) {
		ArrayList<Box> column = new ArrayList<Box>();
		ArrayList<Object> elements ;
		ArrayList<ArrayList> liste = new ArrayList<ArrayList>();
		
		for (int i = 0;i<x2;i++) {
			elements = get_column(y2);
			liste.add(elements);
			
				
		}
		
		this.list = liste;
	}

	private ArrayList<Object> get_column(int y2) {
		ArrayList<Object> elements ;
		elements =  new ArrayList<Object>();
		for (int j = 0;j<y2;j++) {
			elements.add(new JTextField ());
			((JTextField)elements.get(elements.size()-1)).setBounds(0, j*60, 70, 50);
		}
		return elements;
		
	}
}
