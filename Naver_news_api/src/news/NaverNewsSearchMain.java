package news;

import java.util.ArrayList;
import java.util.Scanner;

public class NaverNewsSearchMain {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.print("검색어 입력 : ");
		String tag = sc.nextLine();
		
		NaverNewsRun.getInstance().Write(tag);
		System.out.println("파일 생성 완료");
	}
}
