package profe.testing.httpunit.empleados;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class HttpUnitDemo {

	public static void main(String[] args) throws Exception {
		WebConversation wc;

		wc = new WebConversation();
		WebRequest request = new GetMethodWebRequest("http://localhost:5555/empleados");
		WebResponse response = wc.getResponse(request);
		System.out.println("La respuesta es :" + response.getText());

	}

}
