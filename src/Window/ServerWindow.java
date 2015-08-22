package Window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.activation.ActivationGroupDesc.CommandEnvironment;
import java.security.PublicKey;
import java.security.Provider.Service;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.omg.CORBA.INTERNAL;

import JqueryUI.JqueryButton;

@SuppressWarnings("serial")
public class ServerWindow extends JFrame{

	JPanel mainPanel ;
	JTextArea logArea ;
	JqueryButton startButton ;
	JTextArea contentInput ;
	JqueryButton sendButton ;
	JScrollPane scrollPane ;
	ImageIcon backgroundImage ;
	JLabel backLabel ;
	String ipAddr ;
	ServerSocket server ;
	Socket socket ;
	InputStream in ;
	OutputStream out ;
//	//��ȡ�����IP��ַ
//	InetSocketAddress so_addr = (InetSocketAddress) socket.getInetAddress() ;
//	so_addr.getHostString ;
	public static final int PORT = 9999 ;
	
	public ServerWindow() {
		//---------------------------------��ӿؼ�����------------------------------------
		Font font = new Font( "΢���ź�" , 1 , 12 ) ;
		mainPanel = new JPanel() ;
		mainPanel.setOpaque( false );
		
		backgroundImage = new ImageIcon( "images/background.jpg" ) ;
		backLabel = new JLabel( backgroundImage ) ;
		backLabel.setBounds( 0 , 0 , backgroundImage.getIconWidth() , backgroundImage.getIconHeight() );
		this.getLayeredPane().add( backLabel , new Integer( Integer.MIN_VALUE ) ) ;
		
		JPanel jP = (JPanel)this.getContentPane() ;
		jP.setOpaque( false );
		
		startButton = new JqueryButton() ;
		startButton.setText("��ʼ");
		startButton.setFont(font) ;
		startButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(startButton.getText().equals("����")) {
					startServer() ;					
				}else {
					stopServer() ;
				}
			}
		});
		
		sendButton = new JqueryButton() ;
		sendButton.setText("����");
		sendButton.setFont(font) ;
		sendButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendContent() ;
			}
		});
		
		Color color = new Color( 225 , 225 , 225 ) ;
		
		logArea = new JTextArea() ;
		logArea.setFont(font) ;
		logArea.setEditable( false );
		logArea.setLineWrap( true );
		scrollPane = new JScrollPane( logArea ) ;
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		scrollPane.transferFocusDownCycle() ;
		scrollPane.setBackground( color ) ;
		logArea.setBackground( color );
		
		contentInput = new JTextArea("") ;
		contentInput.setBackground( color );
		contentInput.setFont(font) ;
		contentInput.setLineWrap( true );
		contentInput.setText("��ӭʹ��ʷ�����ª�����칤��Beta V1.0") ;
		contentInput.addKeyListener( new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				int code = e.getKeyCode() ;
				if( code == 13 ) {
					sendContent() ;					
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mainPanel.setLayout(null) ;
		mainPanel.add( scrollPane ) ;
		mainPanel.add( startButton ) ;
		mainPanel.add( contentInput ) ;
		mainPanel.add( sendButton ) ;
		
		//---------------------------------���ڲ��ֲ���------------------------------------
		this.add( mainPanel ) ;
		this.addComponentListener( new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				autoLayout() ;
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		}) ;
		
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
		this.setFont(font) ;
		this.setTitle( "ʷ�����ª�����������Beta V1.0" ) ;
		this.setSize( 400 , 600 ) ;
		this.setLocationRelativeTo( null ) ;
		this.setVisible( true ) ;
	}
	//---------------------------------ҵ�����------------------------------------
	/**
	 * @Description: �Զ���ɴ��岼��
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015��3��12��
	 */
	public void autoLayout() {
		scrollPane.setBounds( 15 , 15 , mainPanel.getWidth()-30 , mainPanel.getHeight()-95 ) ;
		startButton.setBounds( scrollPane.getX() , scrollPane.getHeight()+scrollPane.getY()+27 , 60 , 30) ;
		contentInput.setBounds( startButton.getX()+startButton.getWidth()+10 , startButton.getY() - 7 , 216 , 43 ) ;
		sendButton.setBounds( contentInput.getX()+contentInput.getWidth()+10 , startButton.getY() , 60 , 30 ) ;
		
		if( server == null || server.isClosed() ) {
			startButton.setText( "����" ) ;
			sendButton.setEnabled( false ) ;
			contentInput.setEnabled( false );
		}else {
			startButton.setText( "ֹͣ" ) ;
			sendButton.setEnabled( true ) ;
			contentInput.setEnabled( true ) ;
		}
	}
	/**
	 * @Description: ��ʾ���徯��
	 * @param @param message   
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015��3��12��
	 */
	public void showMessage ( String message ) {
		JOptionPane.showMessageDialog( this , message ) ;
	}
	
	/**
	 * @Description: ��¼��־�¼�
	 * @param @param info   
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015��3��12��
	 */
	@SuppressWarnings("deprecation")
	public void setLog ( String info ) {
		String text = this.logArea.getText() ;
		this.logArea.setText( text + new Date().toLocaleString() + "��" + info + "\r\n" ) ;
	}
	
	/**
	 * @Description: ����������������Ϣ
	 * @param @param info   
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015��3��12��
	 */
	public void setContent( String info ) {
		String text = this.logArea.getText() ;
		this.logArea.setText( text+info + "\r\n" ) ;
	}
	
	/**
	 * @Description: �������������
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015��3��12��
	 */
	public void startServer() {
		if( server == null || server.isClosed() ) {
			try {
				server = new ServerSocket( PORT ) ;
				setLog( "�����������ɹ���" ) ;
				InetAddress addr = InetAddress.getLocalHost() ;
				ipAddr = addr.getHostAddress().toString() ;
				new Thread( new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						acceptUserInfo() ;
					}
				}).start(); ;
			} catch (IOException IOe) {
				IOe.printStackTrace() ;
				setLog( "����������ʧ�ܣ�\n" + IOe.getMessage() );
			}
			autoLayout();
		}
	}
	
	/**
	 * @Description: ֹͣ���������
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015��3��12��
	 */
	public void stopServer() {
		socket = null ;
		if( server != null && socket != null ) {
			try {
				server.close() ;
			} catch (IOException IOe) {
				IOe.printStackTrace() ;
				setLog( "�������Ѿ�ֹͣ��\n" + IOe.getMessage() ) ;
			}
			try {
				socket.close() ;
			} catch (IOException e) {
				e.printStackTrace() ;
				setLog( "�ͻ����Ѿ��뿪��\n" + e.getMessage() ) ;
			}
			server = null ;
			socket = null ;
			setLog( "ֹͣ�������ɹ���" ) ;
		}else {
			try {
				server.close() ;
			} catch (IOException e) {
				e.printStackTrace() ;
				setLog( "�������Ѿ�ֹͣ��\n" + e.getMessage() ) ;
			}
			server = null ;
			setLog( "ֹͣ�������ɹ���" ) ;
		}
		autoLayout() ;
	}
	
	/**
	 * @Description: �����û����͵�����
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015��3��12��
	 */
	public void acceptUserInfo() {
		setLog( "�ȴ��ͻ����󡭡�" ) ;
		try {
			socket = server.accept() ;
			in = socket.getInputStream() ;
			out = socket.getOutputStream() ;
			setLog( "��һ�����û���¼��" ) ;
			new Thread( new Runnable() {
				
				@Override
				public void run() {
					while ( true ){
						try {
							service() ;
						} catch (IOException IOe) {
							IOe.printStackTrace() ;
							setLog( "���������쳣��\n" + IOe.getMessage() ) ;
						}
					}
				}
			}).start() ;
		} catch (Exception e) {
			e.printStackTrace() ;
			setLog( "���������쳣��\n" + e.getMessage() );
		}
	}
	
	/**
	 * @Description: �����û�����
	 * @param @throws IOException   
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015��3��12��
	 */
	public void service() throws IOException {
		if ( socket != null && !socket.isClosed() ) {
			int command = in.read() ;
			//---------------------------------����ͻ��뿪------------------------------------
			if ( command == 0 ) {
				byte data[] = new byte[ in.read() ] ;
				in.read(data) ;
				String name = new String(data).trim() ;
				setLog( name + "�Ѿ��뿪����" ) ;
				socket = null ;
				acceptUserInfo() ;
			}
			//---------------------------------���ܿͻ����͵���Ϣ------------------------------------
			else if ( command == 1 ) {
				byte data[] = new byte[ in.read() ] ;
				in.read(data) ;
				String content = new String(data).trim() ;
				setContent( content );
				byte[] data2 = content.getBytes();
				out.write(data2.length);
				out.write(data2);
				out.flush() ;
			}
		}
	}
	
	/**
	 * @Description: ������������Ϣ
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015��3��12��
	 */
	public void sendContent() {
		if (contentInput.getText().equals("")) {
			showMessage("��������Ϣ�����ٵ�����ͣ�\n        "
					+ "-����Сβ�ͣ���˵15���о�ϲŶ��");
		}else {
			if( socket != null && !socket.isClosed() ){
				try {
					String content = contentInput.getText().trim() ;
					String allInfo = "������" + "-" + ipAddr +
							"��" + new Date().toLocaleString() + "����\n" + content ;
					setContent( allInfo );
					byte data[] = allInfo.getBytes() ;
					out.write( data.length ) ;
					out.write( data ) ;
					out.flush() ;
					contentInput.setText("") ;
				} catch (Exception e) {
					e.printStackTrace();
					showMessage( "��Ϣ����ʧ�ܣ�\n" + e.getMessage() );
				}
			}
		}
	}
}
