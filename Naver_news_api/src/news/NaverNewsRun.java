package news;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NaverNewsRun {

	private static NaverNewsRun instance = new NaverNewsRun();
	
	public static NaverNewsRun getInstance() {
		if (instance == null)
			instance = new NaverNewsRun();
		return instance;
	}

	public NaverNewsRun() {
		
	}

	public static ArrayList<String> Search(String text) {
		String clientId = "UunOP0nbqxhkMkNRMYQg";
        String clientSecret = "32q6j32o_M";
        String apiURL = "https://openapi.naver.com/v1/search/news";
        DataOutputStream dos = null;
        BufferedReader br = null;
        HttpURLConnection con = null;
        ArrayList<String> result = new ArrayList<String>();
        String msg = new String();
        try {
			text = URLEncoder.encode(text,"UTF-8");
			
			String postParams = "?query=" + text+"&display=20"; //display = 검색 결과 출력 건수 = 20개.
			URL url = new URL(apiURL + postParams);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			
			con.setDoOutput(true);
			
			int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
            	 br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  
            	br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
			while(true) {
				String str = br.readLine();
				if(str==null) break;
				msg += str;
			}
			JSONObject json = new JSONObject(msg);
			JSONArray arr = json.getJSONArray("items");
			for(int i=0;i<arr.length();i++) {
				JSONObject obj = arr.getJSONObject(i);
				result.add(obj.getString("title") + "\t" + obj.getString("description")+ "\t" + obj.getString("link"));
			}
		} catch (UnsupportedEncodingException e) {
			result.add("오류"); //Exception이 발생했다는 신호
			result.add(msg); // 오류메세지에 대한 정보
			result.add(e.getMessage()); //Exception 메시지
		} catch (MalformedURLException e) {
			result.add("오류");
			result.add(msg);
			result.add(e.getMessage());
		} catch (IOException e) {
			result.add("오류");
			result.add(msg);
			result.add(e.getMessage());
		} catch(JSONException e) {
			result.add("오류");
			result.add(msg);
			result.add(e.getMessage());
		}
			return result;
	}
	
	
	public static void Write(String fileName) {
		byte[] encode;
		ArrayList<String> list = Search(fileName); //Search 메서드 호출, 검색결과값 list를 받아옴.
		FileWriter fw = null;
		try {
			encode = Files.readAllBytes(Paths.get("template.html"));
			String tag = new String(encode,"UTF-8");
			String table = "<table><tr><th>기사 제목</th><th>요약</th><th>링크</th></tr>";
			for(String row : list) {
				String cell[] = row.split("\t");
				table += "<tr>";
				for(int i=0;i<cell.length-1;i++) {
					table += "<td>"+cell[i]+"</td>";		
				}
				table += "<td><a href='"+cell[cell.length-1]+"'>"+cell[cell.length-1]+"</a></td></tr>";
			}
			table += "</table>";
			
			if(list.contains("오류")) { //오류에 대한 정보를 받아왔을때 처리. 
				tag = ("오류 발생\n오류 내용 : "+list.get(1)+"\nException 내용 : "+list.get(2));
				fw = new FileWriter("exception.txt"); //exception.txt 파일 생성
			}
			else { //정상적으로 기사를 받아왔을때 처리.
			tag = tag.replace("{result}", table); //결과값을 body로
			fw = new FileWriter("news_"+fileName+".html"); //검색결과 html 파일 생성
			}
			fw.write(tag);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
