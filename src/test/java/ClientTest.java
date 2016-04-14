import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.drafts.Draft_18;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;


public class ClientTest extends WebSocketClient{
	
	public ClientTest(URI serverUri, Draft draft) {
		super(serverUri, draft);
		// TODO Auto-generated constructor stub
	}

	public static void main( String[] args ) {
		URI server_uri = URI.create("ws://192.168.1.104:8080/WebSocketTest/websocket");
		Draft draft = new Draft_18();
		ClientTest client = new ClientTest(server_uri, draft);
			
		try{
            boolean succeed = client.connectBlocking();

            if(succeed){
                
                WebSocketImpl.RCVBUF = 40*1024;
                client.bind("123456");
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
	}
	
	public void bind(String token){
        //Bind
        JSONObject jsonObj = new JSONObject();
        
        jsonObj.put("type","Bind");//0 denote register information;

        JSONObject data = new JSONObject();
        data.put("token", token);
        data.put("MAC", "18:59:36:70:1b:37");
        data.put("role", "TABLET");

        jsonObj.put("data", data);
        

        if(this.isOpen()){
            this.send(jsonObj.toString());
        }

    }
	
	
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		// TODO Auto-generated method stub
		System.out.println("OPENING!!!!");
	}

	@Override
	public void onMessage(String message) {
		// TODO Auto-generated method stub
		System.out.println(message.length() + " : " + message);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		System.out.println("CLOSING!!!");
	}

	@Override
	public void onError(Exception ex) {
		// TODO Auto-generated method stub
		
	}
	
	private StringBuilder buffer = new StringBuilder();
	@Override
	public void onFragment(Framedata data){
		ByteBuffer bytes = data.getPayloadData();
		String str = Charset.forName("UTF-8").decode(bytes).toString();
		System.out.println(str);
		buffer.append(str);
		
		if(data.isFin()){
			onMessage(buffer.toString());
			System.out.println(buffer.toString());
			buffer.setLength(0);
		}
	}
	

}