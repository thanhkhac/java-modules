/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Constants {
    
    
        //ID của ứng dụng được đăng ký trên https://console.cloud.google.com/
	public static String GOOGLE_CLIENT_ID = "336633668274-j5saoeii0745maki4cejdlkh5o2gl9jp.apps.googleusercontent.com";
        //Secretkey của ứng dụng được đăng ký trên https://console.cloud.google.com/
	public static String GOOGLE_CLIENT_SECRET = "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";
        //Đường dẫn dùng để chuyển hướng sau khi người dùng đã đăng nhập ở trang login của Google (Không phải trang login.jsp)
	public static String GOOGLE_REDIRECT_URI = "http://localhost:8080/LoginByGoogle/LoginGoogleHandler";
        
	public static String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";
        
	public static String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

	public static String GOOGLE_GRANT_TYPE = "authorization_code";

}
