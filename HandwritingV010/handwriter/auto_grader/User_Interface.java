package auto_grader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Toolkit;

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
}
