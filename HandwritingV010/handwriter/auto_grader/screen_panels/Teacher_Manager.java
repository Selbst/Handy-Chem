package auto_grader.screen_panels;

import java.awt.CardLayout;

import auto_grader.User_Interface;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author selbst
 */
public class Teacher_Manager extends javax.swing.JPanel {
	private User_Interface parent;
    /**
     * Creates new form Teacher_Manager
     */
    public Teacher_Manager(User_Interface parent) {
    	this.parent = parent;
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

        manager_teacher = new javax.swing.JTabbedPane();
        classes_tab = new javax.swing.JPanel();
        class_name_label = new javax.swing.JLabel();
        class_date_label = new javax.swing.JLabel();
        new_class_button = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        class_list = new javax.swing.JList();
        students_tab = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        student_list = new javax.swing.JList();
        student_average_label = new javax.swing.JLabel();
        student_name_label = new javax.swing.JLabel();
        open_student_button = new javax.swing.JButton();
        class_indicator_students = new javax.swing.JLabel();
        assignments_tab = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        assignment_list = new javax.swing.JList();
        class_indicator_assignments = new javax.swing.JLabel();
        assignment_name_label = new javax.swing.JLabel();
        create_assignment_button = new javax.swing.JButton();
        open_assignment_button = new javax.swing.JButton();

        manager_teacher.setBackground(new java.awt.Color(37, 86, 123));
        manager_teacher.setForeground(new java.awt.Color(37, 86, 123));
        manager_teacher.setFocusable(false);
        manager_teacher.setFont(new java.awt.Font("Purisa", 0, 36)); // NOI18N
        manager_teacher.setOpaque(true);

        classes_tab.setBackground(new java.awt.Color(102, 163, 210));
        classes_tab.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        class_name_label.setFont(new java.awt.Font("Purisa", 1, 24)); // NOI18N
        class_name_label.setText("Name");

        class_date_label.setFont(new java.awt.Font("Purisa", 1, 24)); // NOI18N
        class_date_label.setText("Date");

        new_class_button.setBackground(new java.awt.Color(11, 97, 164));
        new_class_button.setFont(new java.awt.Font("Purisa", 1, 36)); // NOI18N
        new_class_button.setForeground(new java.awt.Color(255, 255, 255));
        new_class_button.setText("Start New Class");
        new_class_button.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(37, 86, 123), 3));
        new_class_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new_class_buttonActionPerformed(evt);
            }
        });

        class_list.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        class_list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(class_list);

        javax.swing.GroupLayout classes_tabLayout = new javax.swing.GroupLayout(classes_tab);
        classes_tab.setLayout(classes_tabLayout);
        classes_tabLayout.setHorizontalGroup(
            classes_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classes_tabLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(classes_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(classes_tabLayout.createSequentialGroup()
                        .addComponent(new_class_button, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(classes_tabLayout.createSequentialGroup()
                        .addGroup(classes_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(classes_tabLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(class_name_label, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                .addGap(269, 269, 269)
                                .addComponent(class_date_label, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addGap(10, 10, 10))
                            .addComponent(jScrollPane2))
                        .addGap(159, 159, 159))))
        );
        classes_tabLayout.setVerticalGroup(
            classes_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classes_tabLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(classes_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(class_date_label, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(class_name_label, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(new_class_button, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addGap(39, 39, 39))
        );

        manager_teacher.addTab("classes ", classes_tab);

        students_tab.setBackground(new java.awt.Color(102, 163, 210));

        student_list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(student_list);

        student_average_label.setFont(new java.awt.Font("Purisa", 1, 24)); // NOI18N
        student_average_label.setText("Average");

        student_name_label.setFont(new java.awt.Font("Purisa", 1, 24)); // NOI18N
        student_name_label.setText("Name");

        open_student_button.setBackground(new java.awt.Color(11, 97, 164));
        open_student_button.setFont(new java.awt.Font("Purisa", 1, 36)); // NOI18N
        open_student_button.setForeground(new java.awt.Color(255, 255, 255));
        open_student_button.setText("Open Student Profile");
        open_student_button.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(37, 86, 123), 3));
        open_student_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                open_student_buttonActionPerformed(evt);
            }
        });

        class_indicator_students.setFont(new java.awt.Font("Purisa", 1, 24)); // NOI18N
        class_indicator_students.setText("No Class Selected:");

        javax.swing.GroupLayout students_tabLayout = new javax.swing.GroupLayout(students_tab);
        students_tab.setLayout(students_tabLayout);
        students_tabLayout.setHorizontalGroup(
            students_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, students_tabLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(students_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(students_tabLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 160, Short.MAX_VALUE))
                    .addGroup(students_tabLayout.createSequentialGroup()
                        .addComponent(open_student_button, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, students_tabLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(student_average_label)
                .addGap(171, 171, 171))
            .addGroup(students_tabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(class_indicator_students, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(students_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(students_tabLayout.createSequentialGroup()
                    .addGap(78, 78, 78)
                    .addComponent(student_name_label, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(515, Short.MAX_VALUE)))
        );
        students_tabLayout.setVerticalGroup(
            students_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(students_tabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(class_indicator_students, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(student_average_label, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(open_student_button, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addGap(41, 41, 41))
            .addGroup(students_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(students_tabLayout.createSequentialGroup()
                    .addGap(68, 68, 68)
                    .addComponent(student_name_label, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(429, Short.MAX_VALUE)))
        );

        manager_teacher.addTab("students ", students_tab);

        assignments_tab.setBackground(new java.awt.Color(102, 163, 210));

        assignment_list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(assignment_list);

        class_indicator_assignments.setFont(new java.awt.Font("Purisa", 1, 24)); // NOI18N
        class_indicator_assignments.setText("No Class Selected:");

        assignment_name_label.setFont(new java.awt.Font("Purisa", 1, 24)); // NOI18N
        assignment_name_label.setText("Name");

        create_assignment_button.setBackground(new java.awt.Color(11, 97, 164));
        create_assignment_button.setFont(new java.awt.Font("Purisa", 1, 36)); // NOI18N
        create_assignment_button.setForeground(new java.awt.Color(255, 255, 255));
        create_assignment_button.setText("Create");
        create_assignment_button.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(37, 86, 123), 3));
        create_assignment_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_assignment_buttonActionPerformed(evt);
            }
        });

        open_assignment_button.setBackground(new java.awt.Color(11, 97, 164));
        open_assignment_button.setFont(new java.awt.Font("Purisa", 1, 36)); // NOI18N
        open_assignment_button.setForeground(new java.awt.Color(255, 255, 255));
        open_assignment_button.setText("Open");
        open_assignment_button.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(37, 86, 123), 3));
        open_assignment_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                open_assignment_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout assignments_tabLayout = new javax.swing.GroupLayout(assignments_tab);
        assignments_tab.setLayout(assignments_tabLayout);
        assignments_tabLayout.setHorizontalGroup(
            assignments_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(assignments_tabLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(assignments_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(assignments_tabLayout.createSequentialGroup()
                        .addComponent(create_assignment_button, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(open_assignment_button, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
                .addGap(160, 160, 160))
            .addGroup(assignments_tabLayout.createSequentialGroup()
                .addGroup(assignments_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(assignments_tabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(class_indicator_assignments, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(assignments_tabLayout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(assignment_name_label, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        assignments_tabLayout.setVerticalGroup(
            assignments_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(assignments_tabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(class_indicator_assignments, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(assignment_name_label, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addGroup(assignments_tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(create_assignment_button, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(open_assignment_button, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addGap(40, 40, 40))
        );

        manager_teacher.addTab("assignments", assignments_tab);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 696, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(manager_teacher, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(manager_teacher, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>

    private void new_class_buttonActionPerformed(java.awt.event.ActionEvent evt) {
        ((CardLayout)parent.getContentPane().getLayout()).show(parent.getContentPane(), "start_class");
    }

    private void open_student_buttonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void create_assignment_buttonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void open_assignment_buttonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    // Variables declaration - do not modify
    private javax.swing.JList assignment_list;
    private javax.swing.JLabel assignment_name_label;
    private javax.swing.JPanel assignments_tab;
    private javax.swing.JLabel class_date_label;
    private javax.swing.JLabel class_indicator_assignments;
    private javax.swing.JLabel class_indicator_students;
    private javax.swing.JList class_list;
    private javax.swing.JLabel class_name_label;
    private javax.swing.JPanel classes_tab;
    private javax.swing.JButton create_assignment_button;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane manager_teacher;
    private javax.swing.JButton new_class_button;
    private javax.swing.JButton open_assignment_button;
    private javax.swing.JButton open_student_button;
    private javax.swing.JLabel student_average_label;
    private javax.swing.JList student_list;
    private javax.swing.JLabel student_name_label;
    private javax.swing.JPanel students_tab;
    // End of variables declaration
}
