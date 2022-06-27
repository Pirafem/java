package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ClientMain {

public static void main(String[] args) {
		
		Socket client = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		Scanner sc = new Scanner(System.in);
		try {
			client = new Socket("127.0.0.1", 5000);
			
			pw = new PrintWriter(client.getOutputStream());
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			System.out.print("구매할 로또번호 개수 입력 : ");
			int lotto = sc.nextInt();
			
			pw.println(lotto);
			pw.flush();
			while(true) {
				String str = br.readLine(); // 서버로부터 결과값을 반환받음
				if(str == null) break; // 결과값을 전부 출력했다면 반복문을 빠져나옴
				System.out.println(str); // 결과값 출력
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(br != null)br.close();
				if(pw != null)pw.close();
				if(client != null)client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
