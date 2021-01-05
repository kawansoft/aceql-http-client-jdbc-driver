/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2021,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aceql.sdk.jdbc.examples.blob;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import com.aceql.jdbc.commons.AceQLConnection;

/**
 *
 * This class is a demo for inserting a Blob using a
 * <code>ProgressMonitor</code>.&nbsp;
 * <p>
 * It works exactly like the <a href=
 * "http://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#ProgressMonitorDemo"
 * >ProgressMonitorDemo</a> class of the Java Tutorial in the chapter <a href=
 * "http://docs.oracle.com/javase/tutorial/uiswing/components/progress.html"
 * >How to Use Progress Bars</a>.
 * <p>
 * To run it, just modify the settings between the two lines in doInsert(): <br>
 * // BEGIN MODIFY WITH YOUR VALUES <br>
 * // END MODIFY WITH YOUR VALUES
 *
 * @since 1.0
 */

public class SqlProgressMonitorDemo extends JPanel implements ActionListener,
PropertyChangeListener {

    /** For serialization */
    private static final long serialVersionUID = -3482760023137893766L;

    private ProgressMonitor progressMonitor;
    private JButton startButton;
    private JTextArea taskOutput;
    private Task task;

    /** Progress value between 0 and 100. Will be used by progress monitors. */
    private AtomicInteger progress = new AtomicInteger();

    /** Says if user has cancelled the Blob/Clob upload or download */
    private AtomicBoolean cancelled = new AtomicBoolean();

    class Task extends SwingWorker<Void, Void> {

	@Override
	public Void doInBackground() {
	    cancelled.set(false);
	    progress.set(0);

	    setProgress(0);

	    while (progress.get() < 100) {
		try {
		    Thread.sleep(0);
		} catch (InterruptedException ignore) {
		}

		if (isCancelled()) {
		    // If end user cancels the task, say it to mutable & shareable
		    // cancelled.
		    // cancelled will be read by RemoteConnection to
		    // interrupt blob upload
		    cancelled.set(true);
		    break;
		}

		// Get the progress value between 0 and 100 that
		// is updated by doInsert in background thread
		setProgress(Math.min(progress.get(), 100));
	    }

	    return null;
	}

	@Override
	public void done() {
	    Toolkit.getDefaultToolkit().beep();
	    startButton.setEnabled(true);
	    progressMonitor.setProgress(0);
	    progressMonitor.close();
	}
    }

    public SqlProgressMonitorDemo() {
	super(new BorderLayout());

	// Create the demo's UI.
	startButton = new JButton("Start");
	startButton.setActionCommand("start");
	startButton.addActionListener(this);

	taskOutput = new JTextArea(5, 20);
	taskOutput.setMargin(new Insets(5, 5, 5, 5));
	taskOutput.setEditable(false);

	add(startButton, BorderLayout.PAGE_START);
	add(new JScrollPane(taskOutput), BorderLayout.CENTER);
	setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Invoked when the user presses the start button.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
	progressMonitor = new ProgressMonitor(SqlProgressMonitorDemo.this,
		"Insert with BLOB upload", "", 0, 100);
	progressMonitor.setProgress(0);

	Thread t = new Thread() {
	    @Override
	    public void run() {
		doInsert();
	    }
	};

	t.start();

	task = new Task();
	task.addPropertyChangeListener(this);
	task.execute();
	startButton.setEnabled(false);
    }

    /**
     * Invoked when task's progress property changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	if ("progress".contentEquals(evt.getPropertyName())) {
	    int progress = (Integer) evt.getNewValue();
	    progressMonitor.setProgress(progress);
	    String message = String.format("Completed %d%%.\n", progress);
	    progressMonitor.setNote(message);
	    taskOutput.append(message);
	    if (progressMonitor.isCanceled() || task.isDone()) {
		Toolkit.getDefaultToolkit().beep();
		if (progressMonitor.isCanceled()) {
		    task.cancel(true);
		    taskOutput.append("Task canceled.\n");
		} else {
		    taskOutput.append("Task completed.\n");
		}
		startButton.setEnabled(true);
	    }
	}
    }

    /**
     * SQL insert with BLOB column
     */
    private void doInsert() {
	try {
	    // BEGIN MODIFY WITH YOUR VALUES
	    String userHome = System.getProperty("user.home");

	    // Port number is the port number used to start the Web Server:
	    String url = "http://localhost:9090/aceql";
	    String database = "sampledb";
	    String username = "username";
	    char [] password = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
	    File imageFile =
		    new File(userHome + File.separator + "image_1.jpg");
	    // END MODIFY WITH YOUR VALUES

	    // Attempts to establish a connection to the remote database:
	    Connection connection = new AceQLConnection(url, database,
		    username, password);

	    // Pass the mutable & shareable progress and canceled to the
	    // underlying RemoteConnection.
	    // - progress value will be updated by the RemoteConnection and
	    // retrieved by SwingWorker to increment the progress.
	    // - cancelled value will be updated to true if user cancels the
	    // task and RemoteConnection will interrupt the Blob upload.

	    ((AceQLConnection) connection).setProgress(progress);
	    ((AceQLConnection) connection).setCancelled(cancelled);

	    // Now run our insert
	    BlobExample blobExample = new BlobExample(connection);

	    // Delete if duplicate
	    blobExample.deleteOrderlog(1, 1);

	    blobExample.insertOrderWithImage(1, 1, "description",
		    new BigDecimal("99.99"), imageFile);

	    System.out.println("Blob upload done.");

	} catch (Exception e) {

	    if (e instanceof SQLException && e.getCause() != null
		    && e.getCause() instanceof InterruptedException) {
		System.out.println(e.getMessage());
		return;
	    }
	    e.printStackTrace();
	} finally {
	    // Always set progress to maximum/end value to close the progress
	    // monitor
	    progress.set(100);
	}
    }


    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
	// Create and set up the window.
	JFrame frame = new JFrame("SqlProgressMonitorDemo");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Create and set up the content pane.
	JComponent newContentPane = new SqlProgressMonitorDemo();
	newContentPane.setOpaque(true); // content panes must be opaque
	frame.setContentPane(newContentPane);

	// Display the window.
	frame.pack();
	frame.setVisible(true);
    }

    public static void main(String[] args) {
	// Schedule a job for the event-dispatching thread:
	// creating and showing this application's GUI.
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		createAndShowGUI();
	    }
	});
    }
}
