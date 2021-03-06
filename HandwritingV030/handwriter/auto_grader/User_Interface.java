package auto_grader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import auto_grader.screen_panels.Add_Assignment_Panel;
import auto_grader.screen_panels.Edit_Question_Panel;
import auto_grader.screen_panels.Login_Panel;
import auto_grader.screen_panels.Questions_Manager_Panel;
import auto_grader.screen_panels.Start_Class_Panel;
import auto_grader.screen_panels.Teacher_Manager;
import auto_grader.screen_panels.Title_Panel;

/**
 *
 * @author selbst
 */
public class User_Interface extends javax.swing.JFrame {
    Toolkit tk;
	public String username;
	public String[] classes;
	public String[][] students;
	public String[][] assignments;

	public String current_class;
	public String current_student;
	public String current_assignment;
	public String current_question;
	
    /**
     * Creates new form NewJFrame
     */
    public User_Interface() {
        tk = Toolkit.getDefaultToolkit();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        title = new Title_Panel(this);
        login = new Login_Panel(this);
        teacher_manager = new Teacher_Manager(this);
        start_class = new Start_Class_Panel(this);
        add_assignment = new Add_Assignment_Panel(this);
        questions_manager = new Questions_Manager_Panel(this);
        edit_question = new Edit_Question_Panel(this);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(86, 176, 244));
        setMinimumSize(new java.awt.Dimension(696, 640));
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        getContentPane().add(title, "title");

        getContentPane().add(login, "login");

        getContentPane().add(teacher_manager, "teacher_manager");
        teacher_manager.getAccessibleContext().setAccessibleName("");

        getContentPane().add(start_class, "start_class");
        getContentPane().add(add_assignment, "add_assignment");
        getContentPane().add(questions_manager, "questions_manager");
        getContentPane().add(edit_question, "edit_question");
        pack();
    }// </editor-fold>                                                            
    public void load_users() {                                                             
    	((Login_Panel) login).get_users();
    }
    public void load_classes() {
    	LinkedList<String> temp_classes = new LinkedList<String>();
		try {
			BufferedReader reader  = new BufferedReader(new FileReader("handwriter/auto_grader/user_interface/instructor/classes/" + username + ".save"));
			String this_class;
			while ((this_class = reader.readLine()) != null) {
				temp_classes.add(this_class);
			}
			classes = classes_array(temp_classes);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String s : classes) {
			System.out.println(s);
		}
		
		teacher_manager.set_class_list(classes);
		
    	students = new String[classes.length][];
    	
    	int current_class = -1;
    	LinkedList<String> temp_students = new LinkedList<String>();
		try {
			BufferedReader reader  = new BufferedReader(new FileReader("handwriter/auto_grader/user_interface/instructor/students/" + username + ".save"));
			String this_line;
			while ((this_line = reader.readLine()) != null) {
				if (this_line.equals("_class")) {
					if (current_class != -1) {
						students[current_class] = classes_array(temp_students);
						temp_students.clear();
					}
					current_class = find_class_index(reader.readLine());
					System.out.println(current_class);
				} else {
					temp_students.add(this_line);
					System.out.println(this_line);
				}
			}
			students[current_class] = classes_array(temp_students);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		teacher_manager.set_student_list(classes[0]);
		
    	assignments = new String[classes.length][];
    	
    	current_class = -1;
    	LinkedList<String> temp_assignments = new LinkedList<String>();
		try {
			BufferedReader reader  = new BufferedReader(new FileReader("handwriter/auto_grader/user_interface/instructor/assignments/" + username + ".save"));
			String this_line;
			while ((this_line = reader.readLine()) != null) {
				if (this_line.equals("_class")) {
					if (current_class != -1) {
						assignments[current_class] = classes_array(temp_assignments);
						temp_assignments.clear();
					}
					current_class = find_class_index(reader.readLine());
					System.out.println(current_class);
				} else {
					temp_assignments.add(this_line);
					System.out.println(this_line);
				}
			}
			assignments[current_class] = classes_array(temp_assignments);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		teacher_manager.set_assignment_list(classes[0]);
		
		
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(User_Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User_Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User_Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User_Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        Runnable frame_runner = new Runnable() {
            User_Interface ui;
            public void run() {
                ui = new User_Interface();
                ui.setVisible(true);
            }   
        };
        
        
        java.awt.EventQueue.invokeLater(frame_runner);

    }
    // Variables declaration - do not modify                     
    private Teacher_Manager teacher_manager;
    private javax.swing.JPanel login;
    private javax.swing.JPanel title;
    private javax.swing.JPanel start_class;
    private javax.swing.JPanel add_assignment;
    private javax.swing.JPanel questions_manager;
    private javax.swing.JPanel edit_question;
    // End of variables declaration                   

	public void set_username(String username) {
		this.username = username;
	}

	public static String[] classes_array(LinkedList<String> temp_classes) {
		String[] return_classes = new String[temp_classes.size()];
		int count=0;
		for (String s : temp_classes) {
			return_classes[count] = s;
			count++;
		}
		return return_classes;
	}
	public int find_class_index(String class_name) {
		for (int k=0; k < classes.length; k++) {
			if (class_name.equals(classes[k])) {
				return k;
			}
		}
		return 0;
	}

	public void set_questions() {
		((Questions_Manager_Panel) questions_manager).set_questions(current_assignment, current_class, username);
	}

}
