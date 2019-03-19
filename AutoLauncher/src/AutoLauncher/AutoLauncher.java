package AutoLauncher;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.tools.FileObject;

/**
 * textfield 에디트 텍스트 textarea 텍스트 뷰(넓은 공간) label 텍스트 뷰
 */
public class AutoLauncher extends Frame implements WindowListener, ActionListener {
	private SystemTray systemTray;
	private PopupMenu mPopup;
	private MenuItem mItemLaunch, mItemChange, mItemShow, mItemHide, mItemExit, mItemStop, mItemLog, mItemSetting;

	Frame frame = new Frame("Auto Launcher v1.0");
	Panel p1 = new Panel();
	Panel p2 = new Panel();
	Panel p3 = new Panel();
	static TextArea ta = new TextArea(15, 75);
	static String addr;
	static Label time_now = new Label();
	static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

	int count = 0;

	Button launch = new Button();
	Button end_btn = new Button();
	Button path_chg = new Button();
	Button clr = new Button();
	Button Set_time = new Button();
	Button Show_Hide = new Button();
	Button Stop = new Button();
	Button Log = new Button();

	static File f = new File("");

	Timer m_timer = null;

	String start = "Launch";
	String end = "Exit";
	String chg = "Path Change";
	String clear = "Clear";
	String show_hide = "Show/Hide";
	String hide = "Hide";
	String show = "Show";
	String stop = "Stop";
	String log = "Log";
	String set_time = "Setting";
	static Path path;
	static String aps_path;

	static String log_name;
	static String log_path;
	static String gettime;
	public static int time_set;
	static String now;

	public static void main(String[] args) throws IOException, URISyntaxException {
		AutoLauncher atl = new AutoLauncher();
		get_time();
		time_set = 3600000;
		path=Paths.get("").toAbsolutePath();	
		log_path = path.toString().replace("\\", "/")+ "/Log/";
		now = time_calc(time_set / 1000);
		aps_path = f.getAbsolutePath() + "\\Log\\";
		addr=path.toString();
		atl.createFrame();
		atl.TestSystemTrayIcon();
	}

	public static String get_time() {
		Calendar time = Calendar.getInstance();
		gettime = format1.format(time.getTime());
		log_name = format2.format(time.getTime());
		return gettime;
	}

	public static void Write_log(String str) throws IOException {
		File fo = new File(aps_path);
		if(!fo.exists()) fo.mkdirs();
		BufferedOutputStream bs = null;
		try { //"C:/Users/hyoye/eclipse-workspace/Auto Launcher/src/Auto_Launcher/LOG/test.txt"
			bs = new BufferedOutputStream(new FileOutputStream(log_path + log_name + "_Auto_Launcher.txt", true));
			bs.write(str.getBytes()); // Byte형으로만 넣을 수 있음
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			bs.close(); // 반드시 닫는다.
		}
		fo.delete();
	}

	public void TestSystemTrayIcon() {
		try {
			initSystemTrayIcon();
		} catch (AWTException awte) {
			System.out.println("##### Error occurred during create UI!!!");
			System.out.println(awte.toString());
			System.exit(0);
			try {
				Write_log(get_time() + " / ##### Error occurred during create UI!!! Program Exit\n");
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	public void initSystemTrayIcon() throws AWTException {
		if (SystemTray.isSupported()) {
			mPopup = new PopupMenu();
			mItemLaunch = new MenuItem("Launch");
			mItemStop = new MenuItem("Stop");
			mItemChange = new MenuItem("Path Change");
			mItemShow = new MenuItem("Show");
			mItemHide = new MenuItem("Hide");
			mItemExit = new MenuItem("Exit");
			mItemSetting = new MenuItem("Setting");
			mItemLog = new MenuItem("Log");

			mItemLaunch.addActionListener(this);
			mItemStop.addActionListener(this);
			mItemChange.addActionListener(this);
			mItemShow.addActionListener(this);
			mItemHide.addActionListener(this);
			mItemExit.addActionListener(this);
			mItemSetting.addActionListener(this);
			mItemLog.addActionListener(this);

			mPopup.add(mItemLaunch);
			mPopup.add(mItemStop);
			mPopup.add(mItemChange);
			mPopup.addSeparator();
			mPopup.add(mItemSetting);
			mPopup.add(mItemLog);
			mPopup.addSeparator();
			mPopup.add(mItemShow);
			mPopup.add(mItemHide);
			mPopup.addSeparator();
			mPopup.add(mItemExit);
			

			File fo = new File( f.getAbsolutePath() + "\\bin\\values\\drawable");
			if(!fo.exists()) fo.mkdirs();

			Image image = Toolkit.getDefaultToolkit().getImage("./bin/values/drawable/Auto Launcher.png");
			TrayIcon trayIcon = new TrayIcon(image, "Auto Launch", mPopup);
			trayIcon.setImageAutoSize(true);
			frame.setIconImage(image);

			systemTray = SystemTray.getSystemTray();
			systemTray.add(trayIcon);
		}
	}

	public static String time_calc(int time) {
		int hour, min, sec;
		hour = time / 3600;
		time %= 3600;
		min = time / 60;
		sec = time % 60;
		return "Time: " + hour + "h " + min + "m " + sec + "s";
	}

	public void createFrame() {

		launch.setLabel(start);
		end_btn.setLabel(end);
		Stop.setLabel(stop);
		path_chg.setLabel(chg);
		clr.setLabel(clear);
		Show_Hide.setLabel(hide);
		Set_time.setLabel(set_time);
		Log.setLabel(log);
		time_now.setText(time_calc(time_set/1000));

		p1.add(time_now); // label
		p1.add(launch);
		p1.add(Stop);
		p1.add(path_chg);
		p1.add(Set_time);

		p2.add(ta);

		p3.add(Log);
		p3.add(clr);
		p3.add(Show_Hide);
		p3.add(end_btn);

		Set_component();

		try {
			Write_log("==================================\n" + get_time() + " / Program Strat\n");
			ta.append("Program Strat...\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void Set_component() {
		frame.setFocusable(true);
		frame.setResizable(false);

//		launch.setFocusable(false);
//		Stop.setFocusable(false);
//		end_btn.setFocusable(false);
//		path_chg.setFocusable(false);
//		Show_Hide.setFocusable(false);
//		clr.setFocusable(false);
//		ta.setFocusable(false);
		ta.setEditable(false);

		launch.addActionListener(this);
		Log.addActionListener(this);
		path_chg.addActionListener(this);
		Stop.addActionListener(this);
		end_btn.addActionListener(this);
		Show_Hide.addActionListener(this);
		clr.addActionListener(this);
		Set_time.addActionListener(this);
		frame.addWindowListener(this);

		frame.add(p1, BorderLayout.NORTH);
		frame.add(p2, BorderLayout.CENTER);
		frame.add(p3, BorderLayout.SOUTH);

		frame.setBounds(500, 300, 300, 300);
		frame.setSize(600, 400);

		frame.pack();
		frame.setVisible(true);
	}

	public void Open_File(String address) {
		String str = "";
		Process process = null;
		String[] cmd = new String[] { "rundll32", "url.dll", "FileProtocolHandler", address };

		try {
			// 프로세스 빌더를 통하여 외부 프로그램 실행
			process = new ProcessBuilder(cmd).start();

			// 외부 프로그램의 표준출력 상태 버퍼에 저장
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));

			// 표준출력 상태를 출력
			while ((str = stdOut.readLine()) != null) {
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TimerTask m_task = new TimerTask() {
			@Override
			public void run() {
				count++;
				ta.append(get_time() + " / Launch: Count= " + count + ", Path= " + addr + "\n");
				Open_File(addr);
				try {
					Write_log(get_time() + " / Launch: Count= " + count + ", Path= " + addr + "\n");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		String name = e.getActionCommand();

		if (name.equals(start)) { // 시간 설정만 만들면 끝. 새창띄워서 시간, 경로 설정 할 수 있도록
			if (m_timer == null) {
				m_timer = new Timer();
				m_timer.schedule(m_task, 100, time_set); // 2번째는 딜레이, 3번째는 반복 주기
				ta.append(get_time() + " / Launch Start: " + addr + "\n"); // 실행 시간 추가할 것( 몇번째로 실행된 건지도 추가)
				ta.append(get_time() + " / Launch Time Cycle: " + (time_set / 1000) + " sec\n"); // 실행 시간 추가할 것( 몇번째로
																									// 실행된 건지도 추가)
				try {
					Write_log(get_time() + " / Launch Start: " + addr + "\n" + get_time() + " / Launch Time Cycle: "
							+ (time_set / 1000) + " sec\n");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} else {
				ta.append(get_time() + " / Launch error: Already running\n");
			}
		} else if (name.equals(stop)) {
			count=0;
			if (m_timer != null) {
				m_timer.cancel();
				m_timer = null;
				ta.append(get_time() + " / Launch: Task Stop\n");
				try {
					Write_log(get_time() + " / Launch: Task Stop\n");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} else {
				ta.append(get_time() + " / Launch error: Not running anything now\n");
			}
		} else if (name.equals(end)) {
			ta.setText(get_time() + " / Program Exit\n");
			try {
				Write_log(get_time() + " / Program Exit\n");
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			System.exit(0);
		} else if (name.equals(chg)) {
			if (m_timer != null) {
				m_timer.cancel();
				m_timer = null;
				ta.append(get_time() + " / Launch: Task Stop\n");
				try {
					Write_log(get_time() + " / Launch: Task Stop\n");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			FileDialog fileOpen = new FileDialog(frame, "file open", FileDialog.LOAD);
			fileOpen.setDirectory(path.toString());
			fileOpen.setVisible(true);
			if (fileOpen.getFile() == null) {
				ta.append(get_time() + " / Path Change Error: " + "Wrong path\n");
			} else {
				ta.append(get_time() + " / Path Change: " + fileOpen.getDirectory() + fileOpen.getFile() + "\n");
				addr = fileOpen.getDirectory() + fileOpen.getFile();
				count = 0;
				try {
					Write_log(get_time() + " / Path Change: " + fileOpen.getDirectory() + fileOpen.getFile() + "\n");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}

		} else if (name.equals(hide))
			frame.setVisible(false);
		else if (name.equals(show))
			frame.setVisible(true);
		else if (name.equals(clear)) {
			ta.setText(""); // 실행 시간 추가할 것( 몇번째로 실행된 건지도 추가)
		} else if (name.equals(set_time)) {
			new Sub_Frame("Setting");
		} else if (name.equals(log)) {
			Open_File(log_path + log_name + "_Auto_Launcher.txt");
		}
	}

	public void launch() {

	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			Write_log(get_time() + " / Program Exit\n");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}

class Sub_Frame extends Frame implements ActionListener {
	Button Save = new Button();
	int hour, min, sec;
	int time;

	JTextField tf = new JTextField() {
		public void processKeyEvent(KeyEvent ev) {
			char c = ev.getKeyChar();
			try {
				// Ignore all non-printable characters. Just check the printable ones.
				if (c > 31 && c < 127) {
					Integer.parseInt(c + "");
				}
				super.processKeyEvent(ev);
			} catch (NumberFormatException nfe) {
			}
			if (tf.getText().equals("")) {
				Info.setText("Time: " + time_calc(0));
			} else {
				time = Integer.parseInt(tf.getText());
				Info.setText("Time: " + time_calc(time));
			}
		}
	};

	Frame sub_frame = new Frame();
	Panel p1 = new Panel();
	Panel p2 = new Panel();
	Panel p3 = new Panel();
	Label Set_time = new Label();
	Label Info = new Label("                                                                             ");

	public Sub_Frame(String str) {
		super(str);
		init();
		start();
		create_sub();
	}

	private String time_calc(int time) {
		hour = time / 3600;
		time %= 3600;
		min = time / 60;
		sec = time % 60;
		return "Time: " + hour + "h " + min + "m " + sec + "s";
	}

	private void create_sub() {
		this.setLocation(100, 100);
		super.setVisible(true);
		super.setSize(300, 170);
		super.setResizable(false);
		super.setFocusable(true);

		Set_time.setText("Repeat Cycle Settings (sec): ");
		Save.setLabel("Save");
		tf.setColumns(7);
		tf.setText(AutoLauncher.time_set / 1000 + "");
		p1.add(Set_time);
		p1.add(tf);
		p2.add(Info);
		Set_time.setAlignment(1);
		p3.add(Save);
		super.add(p1, BorderLayout.NORTH);
		super.add(p2, BorderLayout.CENTER);
		super.add(p3, BorderLayout.SOUTH);
		time_calc(Integer.parseInt(tf.getText()));
		Info.setText("Time: " + time_calc(Integer.parseInt(tf.getText())));
		Save.addActionListener(this);

	}

	private void start() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
	}

	private void init() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = e.getActionCommand();

		if (name.equals("Save")) { // 시간 설정만 만들면 끝. 새창띄워서 시간, 경로 설정 할 수 있도록
			if (tf.getText().equals("")) {
				Info.setText("Put in the numbers.");
			} else {
				AutoLauncher.time_set = (Integer.parseInt(tf.getText()) * 1000);
				AutoLauncher.now = time_calc(AutoLauncher.time_set / 1000);
				AutoLauncher.ta.append(AutoLauncher.gettime + " / Setting: Repeat cycle set to "
						+ (AutoLauncher.time_set / 1000) + " seconds. Relaunch now.\n");
				AutoLauncher.time_now.setText(AutoLauncher.now);
				try {
					AutoLauncher.Write_log(AutoLauncher.gettime + " / Setting: Repeat cycle set to "
							+ (AutoLauncher.time_set / 1000) + " seconds. Relaunch now.\n");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				super.setVisible(false);
			}

//			for(int i = 0; i<10; i++) {
//				Integer.parseInt(tf.getText())
//			}
		}
	}
}