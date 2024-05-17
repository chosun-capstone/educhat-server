package kr.chosun.educhatserver.authentication.security.handler;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.chosun.educhatserver.authentication.member.domain.PrincipalDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class SuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();

		Map<String, Object> responseMap = principal.getMemberInfo();

		Gson gson = new Gson();
		String json = gson.toJson(responseMap);

		response.setContentType("application/json; charset=UTF-8");

		PrintWriter writer = response.getWriter();
		writer.println(json);
		writer.flush();
	}
}
