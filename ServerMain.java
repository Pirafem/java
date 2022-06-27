package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerMain {

	public static void main(String[] args) {
		
		ServerSocket server = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		try {
			server = new ServerSocket(5000);
			Socket client = server.accept();
			
			pw = new PrintWriter(client.getOutputStream());
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			String num = br.readLine(); // 클라이언트에게 로또 개수를 입력받음
			
			
			for(int i = 0; i < Integer.parseInt(num); i ++) {
				ArrayList<Integer> lotto = new ArrayList<Integer>(); //로또 번호 받을 배열 선언
				while(true) {
					int lottonum = ((int)(Math.random()*45+1)); // 랜덤 수 생성
					if(lotto.contains(lottonum))continue; // 중복값일 경우 배열에 누적하지 않고 넘어감
					lotto.add(lottonum); // 중복값이 아닐경우 누적
					if(lotto.size() == 6) break; // 6개의 데이터 입력시 반복문을 빠져나오고 출력
				}
				pw.println(Integer.parseInt(num)-i+"set : "+lotto); // 클라이언트에게 결과값 전송
				pw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null)br.close();
				if(pw != null)pw.close();
				if(server != null)server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
