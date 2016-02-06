package xyz.pandawan.autobackup;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class AutoBackup{
	
	private JFrame frame;
	Float timeBetweenBackup;
	Float timeBeforeDeletion;
	Boolean delete;
	
	File saveFile;
	JLabel fileName;
	
	File backupFolder;
	JLabel folderName;
		
	JLabel finalNumT;
	int timeLeftM = 0;	
	int timeLeftH = 0;
	int timeH;
	int timeM;
	
	int maxFiles;
	
	Timer timer = new Timer ();
	TimerTask hourlyTask = new TimerTask () {
	    @Override
	    public void run () {	    	
	    	
	    	finalNumT.setText(getTimeLeft());
	    	System.out.println(timeLeftH + " " + timeLeftM);
	    
	    	//NEW ONE
	    	timeLeftM -=1;
	    	if (timeLeftM == 0) {
	    		if (timeLeftH >= 1) {
	    			timeLeftH -=1;
	    			timeLeftM = 59;
	    		}else if (timeLeftH == 0){
	    			timeLeftH = timeH;
	    			timeLeftM = timeM;
	    			backup();
	    		}
	    	}
	    	
	    }
	};


	
	public static void main(String[] args)
	  {
	    EventQueue.invokeLater(new Runnable() {
	      public void run() {
	        try {
	          AutoBackup window = new AutoBackup();
	          window.frame.setVisible(true);
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	    });
	  }
	
	public AutoBackup()
	{
		System.out.println("Hello World!");
		System.out.println("Thanks for downloading the Auto Backup by PandawanFr!");
		System.out.println("This will backup any file or folder automatically in an interval of time.");
		System.out.println("Thanks to Apache for the Common IO library");
		initialize();
	}
	
	private void initialize() {
		
		frame = new JFrame();
		frame.setTitle("Auto Backup - PandawanFr");
		frame.setBounds(100, 100, 800, 400);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);
		
		JOptionPane.showMessageDialog(frame, "Do not redistribute/copy/modify/claim it as yours! -Pandawan");
		
		JButton Btn = new JButton("File or folder to backup");
		Btn.setBounds(30, 50, 200, 25);
		Btn.setFont(new Font(Btn.getFont().getName(), Font.PLAIN, 13));
		Btn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	    	    fileChooser();
	        }
	    });
		frame.getContentPane().add(Btn);
		
		fileName = new JLabel(getFileName());
		fileName.setBounds(40, 75, 200, 25);
		fileName.setFont(new Font(fileName.getFont().getName(), Font.PLAIN, 13));
		frame.getContentPane().add(fileName);
		
		
		JLabel timeBackupTxt = new JLabel("Time between backups (H:M):");
		timeBackupTxt.setBounds(250, 50, 200, 25);
		timeBackupTxt.setFont(new Font(timeBackupTxt.getFont().getName(), Font.PLAIN, 13));
		frame.getContentPane().add(timeBackupTxt);

//		JFormattedTextField timeBField = new JFormattedTextField(mask);
//		timeBField.setColumns(10);
//		timeBField.setBounds(450, 50, 100, 25);
//		frame.getContentPane().add(timeBField);
		

		SpinnerNumberModel numberModel = new SpinnerNumberModel(
				new Integer(0), // value
				new Integer(0), // min
				new Integer(1000), // max
				new Integer(1) // step
				);
		JSpinner numberChooser = new JSpinner(numberModel);
		numberChooser.setBounds(435, 50, 65, 25);
		frame.getContentPane().add(numberChooser);
		
		JLabel twoDots = new JLabel(":");
		twoDots.setBounds(505, 50, 200, 25);
		twoDots.setFont(new Font(twoDots.getFont().getName(), Font.PLAIN, 15));
		frame.getContentPane().add(twoDots);
		
		SpinnerNumberModel numberModelM = new SpinnerNumberModel(
				new Integer(0), // value
				new Integer(0), // min
				new Integer(59), // max
				new Integer(1) // step
				);
		JSpinner minChooser = new JSpinner(numberModelM);
		minChooser.setBounds(510, 50, 65, 25);
		frame.getContentPane().add(minChooser);
		System.out.println("Time: " + numberChooser.getValue() + ":" + minChooser.getValue());
		
		SpinnerNumberModel numberOfFiles = new SpinnerNumberModel(
				new Integer(1), // value
				new Integer(1), // min
				new Integer(1000), // max
				new Integer(1) // step
				);
		JSpinner fileNChooser = new JSpinner(numberOfFiles);
		fileNChooser.setBounds(510, 150, 65, 25);
		frame.getContentPane().add(fileNChooser);
		
		JLabel textFileC = new JLabel("Keep how many backups?");
		textFileC.setBounds(345, 150, 250, 25);
		textFileC.setFont(new Font(textFileC.getFont().getName(), Font.PLAIN, 13));
		frame.getContentPane().add(textFileC);
		
		
		JButton Btn2 = new JButton("Folder where the backups go");
		Btn2.setBounds(30, 150, 225, 25);
		Btn2.setFont(new Font(Btn2.getFont().getName(), Font.PLAIN, 13));
		Btn2.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	    	    folderChooser();
	        }
	    });
		frame.getContentPane().add(Btn2);
		
		JButton BackupNowBtn = new JButton("Backup Now");
		BackupNowBtn.setBounds(300, 250, 150, 25);
		BackupNowBtn.setFont(new Font(BackupNowBtn.getFont().getName(), Font.PLAIN, 13));
		BackupNowBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	    	    backup();
	        }
	    });
		frame.getContentPane().add(BackupNowBtn);
		
		JButton BackupTimerBtn = new JButton("Start Backup Timer");
		BackupTimerBtn.setBounds(300, 275, 150, 25);
		BackupTimerBtn.setFont(new Font(BackupTimerBtn.getFont().getName(), Font.PLAIN, 13));
		BackupTimerBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	timeM = (Integer)numberModelM.getValue();
	        	timeH = (Integer)numberModel.getValue();
	        	if (timeM != 0 || timeH != 0) {
	    	    	timeLeftM = timeM;
	    	    	timeLeftH = timeH;
	    	    	maxFiles = (Integer)fileNChooser.getValue();
		        	timer.schedule (hourlyTask, 0l, 1000 * 60);
	        	}
	        }
	    });
		frame.getContentPane().add(BackupTimerBtn);
		
		finalNumT = new JLabel(getTimeLeft());
		finalNumT.setBounds(300, 225, 200, 25);
		finalNumT.setFont(new Font(finalNumT.getFont().getName(), Font.PLAIN, 13));
		frame.getContentPane().add(finalNumT);
		
		folderName = new JLabel(getFolderName());
		folderName.setBounds(40, 175, 200, 25);
		folderName.setFont(new Font(folderName.getFont().getName(), Font.PLAIN, 13));
		frame.getContentPane().add(folderName);

	}
	
	public String getTimeLeft () {
		return "Time left: " + timeLeftH + ":" + timeLeftM;
		//Change this to the actual time left instead of timer in mil
	}
	
	public String getFileName () {
		if (saveFile == null) {
			return "none";
		}else {
			return saveFile.getName();
		}
	}
	
	public String getFolderName () {
		if (backupFolder == null) {
			return "none";
		}else {
			return backupFolder.getName();
		}
	}
	
	public void fileChooser () {
		JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Choose a file or directory to backup");
	    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	String type;
	    	if (chooser.getSelectedFile().isDirectory()) {
	    		type = "Folder";
	    	}else if (chooser.getSelectedFile().isFile()) {
	    		type = "File";
	    	}else {
	    		type = "Error";
	    	}
	    	
	    	saveFile = chooser.getSelectedFile();
	    	fileName.setText("Selected " + type + ": " + getFileName());
	        //System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
	        System.out.println("Selected file: " + chooser.getSelectedFile());
	      } else {
	        System.out.println("No Selection!");
		    JOptionPane.showMessageDialog(frame, "No selection!", "Error", JOptionPane.ERROR_MESSAGE);
	      }
	    
	}
	
	public void folderChooser () {
		JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Choose directory where the backups will go to");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	String type;
	    	if (chooser.getSelectedFile().isDirectory()) {
	    		type = "Folder";
	    	}else {
	    		type = "Error";
	    	}
	    	
	    	backupFolder = chooser.getSelectedFile();
	    	folderName.setText("Selected " + type + ": " + getFolderName());
	        //System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
	        System.out.println("Selected folder: " + chooser.getSelectedFile());
	      } else {
	        System.out.println("No Selection!");
		    JOptionPane.showMessageDialog(frame, "No selection!", "Error", JOptionPane.ERROR_MESSAGE);
	      }
	    
	}
	
	public void backup () {
		
		System.out.println("Backing up!");
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd_HH-mm");
		Date date = new Date();
		String ext = FilenameUtils.getExtension(saveFile.getName());
		String fileNameWithoutExt = saveFile.getName().replace(ext,"");
		String fullPath;
		if (saveFile.isFile()) {
			fileNameWithoutExt = fileNameWithoutExt.substring(0,fileNameWithoutExt.length()-1);
			 fullPath = backupFolder.getPath() + "/" + fileNameWithoutExt + "_" + dateFormat.format(date);
			 fullPath = fullPath + "."+ ext;
		}else {
			 fullPath = backupFolder.getPath() + "/" + fileNameWithoutExt + "_" + dateFormat.format(date);
		}
		if (OsUtils.isWindows()) {
			OsUtils.MakeWinPath(fullPath);
		}
		
		System.out.println(fullPath);
		try {
			if (saveFile.isFile()) {
				removeExtraFile();
				FileUtils.copyFileToDirectory(saveFile, backupFolder);
			    System.out.println("Successfully backed up file!");
			    //JOptionPane.showMessageDialog(frame, "Successfully backed up file!");
			}else {
				removeExtraFile();
				FileUtils.copyDirectory(saveFile, new File(fullPath));
				System.out.println("Successfully backed up folder!");
			    //JOptionPane.showMessageDialog(frame, "Successfully backed up folder!");
			}
		} catch (IOException e) {
		    e.printStackTrace();
		    JOptionPane.showMessageDialog(frame, "Error, could not backup file or folder... ;(");
		}
	}
	
	public File[] getOldestFile () {
		String path = backupFolder.getPath() + "/";
		if (OsUtils.isWindows()) {
			OsUtils.MakeWinPath(path);
		}
		
		File directory = new File(path);
		
		
		ArrayList<File> filesList = new ArrayList<File>();
		
		for(File file : directory.listFiles()) {
		    if(file.getName().startsWith(saveFile.getName()))
		    	filesList.add(file);
		}
		File[] files = new File[filesList.size()];
		files = filesList.toArray(files);
		
		Arrays.sort(files, new Comparator<File>(){
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
		    } });
		return files;
	}
	
	public void removeExtraFile () {
		if (getOldestFile().length >= maxFiles) {
			deleteDirectory(getOldestFile()[0]);
		}
	}
	
	static public void deleteDirectory(File file) {
		if (file.isDirectory()) {//Dir
			File[] files = file.listFiles();
			if (files!=null) {
				for (File f: files) {
					if (f.isDirectory()) {
						deleteDirectory(f);
					}else {
						f.delete();
					}
				}
			}
		}else {//File
			file.delete();
		}
	}

}
