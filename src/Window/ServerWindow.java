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
//	//读取输入的IP地址
//	InetSocketAddress so_addr = (InetSocketAddress) socket.getInetAddress() ;
//	so_addr.getHostString ;
	public static final int PORT = 9999 ;
	
	public ServerWindow() {
		//---------------------------------添加控件操作------------------------------------
		Font font = new Font( "微软雅黑" , 1 , 12 ) ;
		mainPanel = new JPanel() ;
		mainPanel.setOpaque( false );
		
		backgroundImage = new ImageIcon( "images/background.jpg" ) ;
		backLabel = new JLabel( backgroundImage ) ;
		backLabel.setBounds( 0 , 0 , backgroundImage.getIconWidth() , backgroundImage.getIconHeight() );
		this.getLayeredPane().add( backLabel , new Integer( Integer.MIN_VALUE ) ) ;
		
		JPanel jP = (JPanel)this.getContentPane() ;
		jP.setOpaque( false );
		
		startButton = new JqueryButton() ;
		startButton.setText("开始");
		startButton.setFont(font) ;
		startButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(startButton.getText().equals("启动")) {
					startServer() ;					
				}else {
					stopServer() ;
				}
			}
		});
		
		sendButton = new JqueryButton() ;
		sendButton.setText("发送");
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
		contentInput.setText("欢迎使用史上最简陋的聊天工具Beta V1.0") ;
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
		
		//---------------------------------窗口布局操作------------------------------------
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
		this.setTitle( "史上最简陋的聊天服务器Beta V1.0" ) ;
		this.setSize( 400 , 600 ) ;
		this.setLocationRelativeTo( null ) ;
		this.setVisible( true ) ;
	}
	//---------------------------------业务操作------------------------------------
	/**
	 * @Description: 自动完成窗体布局
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015年3月12日
	 */
	public void autoLayout() {
		scrollPane.setBounds( 15 , 15 , mainPanel.getWidth()-30 , mainPanel.getHeight()-95 ) ;
		startButton.setBounds( scrollPane.getX() , scrollPane.getHeight()+scrollPane.getY()+27 , 60 , 30) ;
		contentInput.setBounds( startButton.getX()+startButton.getWidth()+10 , startButton.getY() - 7 , 216 , 43 ) ;
		sendButton.setBounds( contentInput.getX()+contentInput.getWidth()+10 , startButton.getY() , 60 , 30 ) ;
		
		if( server == null || server.isClosed() ) {
			startButton.setText( "启动" ) ;
			sendButton.setEnabled( false ) ;
			contentInput.setEnabled( false );
		}else {
			startButton.setText( "停止" ) ;
			sendButton.setEnabled( true ) ;
			contentInput.setEnabled( true ) ;
		}
	}
	/**
	 * @Description: 显示窗体警告
	 * @param @param message   
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015年3月12日
	 */
	public void showMessage ( String message ) {
		JOptionPane.showMessageDialog( this , message ) ;
	}
	
	/**
	 * @Description: 记录日志事件
	 * @param @param info   
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015年3月12日
	 */
	@SuppressWarnings("deprecation")
	public void setLog ( String info ) {
		String text = this.logArea.getText() ;
		this.logArea.setText( text + new Date().toLocaleString() + "：" + info + "\r\n" ) ;
	}
	
	/**
	 * @Description: 服务器发送聊天信息
	 * @param @param info   
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015年3月12日
	 */
	public void setContent( String info ) {
		String text = this.logArea.getText() ;
		this.logArea.setText( text+info + "\r\n" ) ;
	}
	
	/**
	 * @Description: 启动聊天服务器
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015年3月12日
	 */
	public void startServer() {
		if( server == null || server.isClosed() ) {
			try {
				server = new ServerSocket( PORT ) ;
				setLog( "服务器启动成功！" ) ;
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
				setLog( "启动服务器失败！\n" + IOe.getMessage() );
			}
			autoLayout();
		}
	}
	
	/**
	 * @Description: 停止聊天服务器
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015年3月12日
	 */
	public void stopServer() {
		socket = null ;
		if( server != null && socket != null ) {
			try {
				server.close() ;
			} catch (IOException IOe) {
				IOe.printStackTrace() ;
				setLog( "服务器已经停止！\n" + IOe.getMessage() ) ;
			}
			try {
				socket.close() ;
			} catch (IOException e) {
				e.printStackTrace() ;
				setLog( "客户端已经离开！\n" + e.getMessage() ) ;
			}
			server = null ;
			socket = null ;
			setLog( "停止服务器成功！" ) ;
		}else {
			try {
				server.close() ;
			} catch (IOException e) {
				e.printStackTrace() ;
				setLog( "服务器已经停止！\n" + e.getMessage() ) ;
			}
			server = null ;
			setLog( "停止服务器成功！" ) ;
		}
		autoLayout() ;
	}
	
	/**
	 * @Description: 接受用户发送的请求
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015年3月12日
	 */
	public void acceptUserInfo() {
		setLog( "等待客户请求……" ) ;
		try {
			socket = server.accept() ;
			in = socket.getInputStream() ;
			out = socket.getOutputStream() ;
			setLog( "有一个新用户登录！" ) ;
			new Thread( new Runnable() {
				
				@Override
				public void run() {
					while ( true ){
						try {
							service() ;
						} catch (IOException IOe) {
							IOe.printStackTrace() ;
							setLog( "网络连接异常！\n" + IOe.getMessage() ) ;
						}
					}
				}
			}).start() ;
		} catch (Exception e) {
			e.printStackTrace() ;
			setLog( "网络连接异常！\n" + e.getMessage() );
		}
	}
	
	/**
	 * @Description: 处理用户请求
	 * @param @throws IOException   
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015年3月12日
	 */
	public void service() throws IOException {
		if ( socket != null && !socket.isClosed() ) {
			int command = in.read() ;
			//---------------------------------处理客户离开------------------------------------
			if ( command == 0 ) {
				byte data[] = new byte[ in.read() ] ;
				in.read(data) ;
				String name = new String(data).trim() ;
				setLog( name + "已经离开……" ) ;
				socket = null ;
				acceptUserInfo() ;
			}
			//---------------------------------接受客户发送的消息------------------------------------
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
	 * @Description: 服务器发送消息
	 * @param    
	 * @return void  
	 * @throws
	 * @author Gavin
	 * @date 2015年3月12日
	 */
	public void sendContent() {
		if (contentInput.getText().equals("")) {
			showMessage("请输入消息内容再点击发送！\n        "
					+ "-我是小尾巴，据说15字有惊喜哦！");
		}else {
			if( socket != null && !socket.isClosed() ){
				try {
					String content = contentInput.getText().trim() ;
					String allInfo = "服务器" + "-" + ipAddr +
							"（" + new Date().toLocaleString() + "）：\n" + content ;
					setContent( allInfo );
					byte data[] = allInfo.getBytes() ;
					out.write( data.length ) ;
					out.write( data ) ;
					out.flush() ;
					contentInput.setText("") ;
				} catch (Exception e) {
					e.printStackTrace();
					showMessage( "消息发送失败！\n" + e.getMessage() );
				}
			}
		}
	}
}
