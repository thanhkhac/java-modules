/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Form;

@WebServlet(urlPatterns = { "/LoginGoogleHandler" })
public class LoginGoogleHandler extends HttpServlet {
        //Tài liệu về Google Auth 2.0: https://developers.google.com/identity/protocols/oauth2/native-app?hl=vi#exchange-authorization-code
        //Luồng:
        //Người dùng click vào nút đăng nhập trên trang login.jsp
        //Trang login.jsp chuyển hướng tới trang Sign in của Google
        //Người dùng xác nhận đăng nhập 
        //Trang Sign in google chuyển hướng tới đướng dẫn LoginGoogleHandler (đã được cấu hình trong url ở trang login.jsp) kèm với authorization code
        //Xử lý bên trong LoginGoogleHandler: xem trong các hàm
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
                //Nhận authorization code từ Google
		String code = request.getParameter("code");
                //Sử dụng authorization code để lấy access token
		String accessToken = getToken(code);
                //Sử dụng access token để lấy thông tin người dùng gmail
		UserGoogleDto user = getUserInfo(accessToken);
                try (PrintWriter out = response.getWriter()) {
                    out.println("Email: " + user.getEmail());
                    out.println("Picture URL: " + user.getPicture());
                    out.println("Verified_email: " + user.isVerified_email());
                }
	}
        
        

	public static String getToken(String code) throws ClientProtocolException, IOException {
                //Tạo một POST request với các param => gửi đi => nhận về response
		String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
				.bodyForm(Form.form().add("client_id", Constants.GOOGLE_CLIENT_ID)
						.add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
						.add("redirect_uri", Constants.GOOGLE_REDIRECT_URI).add("code", code)
						.add("grant_type", Constants.GOOGLE_GRANT_TYPE).build())
				.execute().returnContent().asString();
                
                // sau khi call api, biến response sẽ nhận được token ở định dạng JSON như sau
                /*
                {
                    "access_token": "ya29.a0AfB_byB_mtZBKkeCx7BUoTk57i6haq6oIrcVURCyhKjZGLBFEIR1ZGudUdCy2p72ncXQmLOCUjYcEh8h2Qu8wsWFUDQlChT28QcsnStkIRNwPycTQ2sT-0j9dvCDK_T6HWUb1kIYb3yTWunzzrvoPQktDEtsqZf9mSNlaCgYKAfYSARESFQHGX2MiGmNol6hf5HDjRxUl_sBdDA0171",
                    "expires_in": 3599,
                    "scope": "https://www.googleapis.com/auth/userinfo.email openid",
                    "token_type": "Bearer",
                    "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFmNDBmMGE4ZWYzZDg4MDk3OGRjODJmMjVjM2VjMzE3YzZhNWI3ODEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiMzM2NjMzNjY4Mjc0LWo1c2FvZWlpMDc0NW1ha2k0Y2VqZGxraDVvMmdsOWpwLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMzM2NjMzNjY4Mjc0LWo1c2FvZWlpMDc0NW1ha2k0Y2VqZGxraDVvMmdsOWpwLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTExNDE2NjQ1Nzk1MTQyODgxMjI3IiwiZW1haWwiOiJ0aGFuaGNxYjIwNDhAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJ5REFiLUJVT3c4UE9SVU5iRUtpT3FBIiwiaWF0IjoxNzA1MTE2NjIyLCJleHAiOjE3MDUxMjAyMjJ9.PixCjZkF_7gJ8uXdsHQuvgn450QVDU72MeKQ3mjxHCAwwkUH6W4D5IusGQib0bl10gimgj2gK1Cko_WcnDzDZVj3LOJbzy5Jh7RPKFNGt8CpkXROmS3cVCuKPFOYjQFXnm0LusMcKW4tCeAlrM-6csFmHMN20QP3UvrLp_bX8p8ALJraobCBOPY4YwDDLXe4PRiv6sPVJfsCBc7uVvvnWVQHGNaAS6V82VMEl2nlGgH0wyemzsZwhvgTk4XP6hZl14vxO7Y9Ao-lZbfFGO5Fba9GEYJlcVkUUZZmOUQX4RkJh1AeDHtFDXO6PS71F54fIweHMbxWgaRczrx9GgMGvA"
                }
                */
                //Sử dụng thư viện GSON để chuyển đổi chuỗi JSON thành đối tượng JsonObject, 
                //đối tượng này sẽ có các thuộc tính và giá trị của thuộc tính như đoạn JSON trên
		JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
                //Lấy thuộc tính access_token của jobj và trả về
		String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
		return accessToken;
	}

	public static UserGoogleDto getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
		//Đường dẫn tới trang lấy access token
                //Định dạng: https://www.googleapis.com/oauth2/v1/userinfo?access_token= ewewewewewewewewewwewewew...
                String link = Constants.GOOGLE_LINK_GET_USER_INFO + accessToken;
                //Gửi request tới đường link và nhận về response
		String response = Request.Get(link).execute().returnContent().asString();
                //Giá trị response được trả về là định dạng JSON:
                /*
                {
                    "id": "111416645795142881227",
                    "email": "thanhcqb2048@gmail.com",
                    "verified_email": true,
                    "picture": "https://lh3.googleusercontent.com/a-/ALV-UjX77QvpZT7hi085tXimSy6sFNeljvyE5M8WRsVqywv1L8Y=s96-c"
                }
                */
                //Sử dụng thư viện GSON để chuyển đổi chuỗi JSON thành đối tượng GoogleDTO,
                UserGoogleDto googlePojo = new Gson().fromJson(response, UserGoogleDto.class);
		return googlePojo;
	}

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
